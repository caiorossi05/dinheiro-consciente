package br.com.dinheiroconsciente.service;

import br.com.dinheiroconsciente.model.Transacao;
import br.com.dinheiroconsciente.model.Usuario;
import br.com.dinheiroconsciente.repository.TransacaoRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class TransacaoService {

    private final TransacaoRepository repository = new TransacaoRepository();


    public void registrarTransacao(Transacao transacao) {

        if (transacao.getValor() == null || transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor invalido. Informe um valor maior que zero.");
        }


        if (transacao.getCategoria() == null || transacao.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("A categoria e obrigatoria.");
        }


        repository.salvar(transacao);
    }

    public List<Transacao> buscarTodasPorUsuario(Usuario usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }

        return repository.findAllByUsuario(usuario);
    }

}