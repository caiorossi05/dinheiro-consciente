package br.com.dinheiroconsciente.service;

import br.com.dinheiroconsciente.model.Usuario;
import br.com.dinheiroconsciente.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    private final UsuarioRepository repository = new UsuarioRepository();

    public Usuario autenticarUsuario(String nome, String senha) {
        if (nome == null || nome.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e senha sao obrigatorios.");
        }

        Usuario usuarioDB = repository.buscarPorNome(nome);

        if (usuarioDB != null && BCrypt.checkpw(senha, usuarioDB.getSenha())) {
            return usuarioDB;
        }

        return null;
    }
    public Usuario cadastrarUsuario(String nome, String senha) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome invalido.");
        }
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }

        if (repository.buscarPorNome(nome) != null) {
            throw new IllegalArgumentException("Este nome de usuario ja esta cadastrado.");
        }

        String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setSenha(senhaCriptografada);

        repository.salvar(novoUsuario);

        return novoUsuario;
    }
}
