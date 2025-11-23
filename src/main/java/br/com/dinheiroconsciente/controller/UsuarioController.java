package br.com.dinheiroconsciente.controller;

import br.com.dinheiroconsciente.model.Usuario;
import br.com.dinheiroconsciente.service.UsuarioService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UsuarioController", urlPatterns = "/usuario")
public class UsuarioController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String acao = request.getParameter("acao");
        String nome = request.getParameter("nome");
        String senha = request.getParameter("senha");

        try {
            if ("cadastro".equalsIgnoreCase(acao)) {

                usuarioService.cadastrarUsuario(nome, senha);

                response.sendRedirect("login.html?sucesso=" +
                        encode("Cadastro realizado com sucesso! Faca seu login."));
                return;
            }

            if ("login".equalsIgnoreCase(acao)) {

                Usuario usuarioAutenticado = usuarioService.autenticarUsuario(nome, senha);

                if (usuarioAutenticado != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuarioLogado", usuarioAutenticado);

                    String url = "dashboard.html?usuario=" + encode(usuarioAutenticado.getNome());
                    response.sendRedirect(url);
                    return;
                } else {
                    response.sendRedirect("login.html?erro=" +
                            encode("Nome de usuario ou senha invalidos."));
                    return;
                }
            }

            response.sendRedirect("login.html?erro=" + encode("Acao invalida."));

        } catch (IllegalArgumentException e) {
            response.sendRedirect("login.html?erro=" + encode(e.getMessage()));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?erro=" +
                    encode("Erro interno no servidor. Tente novamente mais tarde."));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        request.setCharacterEncoding("UTF-8");



        String acao = request.getParameter("acao");

        if ("sair".equalsIgnoreCase(acao)) {
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            response.sendRedirect("login.html?sucesso=" + encode("Logout realizado."));
            return;
        }
        
        response.sendRedirect("login.html");
    }

    private String encode(String mensagem) {
        return mensagem.replace(" ", "%20");
    }
}
