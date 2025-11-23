package br.com.dinheiroconsciente.repository;

import br.com.dinheiroconsciente.model.TipoTransacao;
import br.com.dinheiroconsciente.model.Transacao;
import br.com.dinheiroconsciente.model.Usuario;
import br.com.dinheiroconsciente.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoRepository {

    public void salvar(Transacao transacao) {
        String sql = transacao.getId() == null
                ? "INSERT INTO transacao (usuario_id, tipo, valor, data, categoria, descricao) VALUES (?, ?, ?, ?, ?, ?)"
                : "UPDATE transacao SET tipo = ?, valor = ?, data = ?, categoria = ?, descricao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (transacao.getId() == null) {
                ps.setLong(1, transacao.getUsuario().getId());
                ps.setString(2, transacao.getTipo().toString());
                ps.setBigDecimal(3, transacao.getValor());
                ps.setDate(4, Date.valueOf(transacao.getData()));
                ps.setString(5, transacao.getCategoria());
                ps.setString(6, transacao.getDescricao());
            } else {
                // UPDATE (5 parâmetros no SET + ID no WHERE)
                ps.setString(1, transacao.getTipo().toString());
                ps.setBigDecimal(2, transacao.getValor());
                ps.setDate(3, Date.valueOf(transacao.getData()));
                ps.setString(4, transacao.getCategoria());
                ps.setString(5, transacao.getDescricao());
                ps.setLong(6, transacao.getId());
            }

            ps.executeUpdate();

            if (transacao.getId() == null) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        transacao.setId(rs.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar transacao via JDBC.", e);
        }
    }

    public List<Transacao> findAllByUsuario(Usuario usuario) {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT id, tipo, valor, data, categoria, descricao FROM transacao WHERE usuario_id = ? ORDER BY data DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, usuario.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transacao t = new Transacao();

                    t.setId(rs.getLong("id"));
                    t.setTipo(TipoTransacao.valueOf(rs.getString("tipo")));
                    t.setValor(rs.getBigDecimal("valor"));
                    t.setData(rs.getDate("data").toLocalDate());
                    t.setCategoria(rs.getString("categoria"));
                    t.setDescricao(rs.getString("descricao"));
                    t.setUsuario(usuario);

                    transacoes.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar transacoes via JDBC.", e);
        }
        return transacoes;
    }
}