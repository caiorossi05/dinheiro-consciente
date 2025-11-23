package br.com.dinheiroconsciente.controller;

import br.com.dinheiroconsciente.model.TipoTransacao;
import br.com.dinheiroconsciente.model.Transacao;
import br.com.dinheiroconsciente.model.Usuario;
import br.com.dinheiroconsciente.service.TransacaoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "TransacaoController", urlPatterns = "/transacao")
public class TransacaoController extends HttpServlet {

    private final TransacaoService transacaoService = new TransacaoService();

    // Gson configurado para serializar LocalDate como "yyyy-MM-dd"
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
            .create();

    // =========================================================
    // POST — REGISTRAR TRANSAÇÃO (RF03 / RF04)
    // =========================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");


        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (session != null)
                ? (Usuario) session.getAttribute("usuarioLogado")
                : null;

        if (usuarioLogado == null) {
            response.sendRedirect("login.html?erro=Voce%20precisa%20fazer%20login.");
            return;
        }

        String acao = request.getParameter("acao");

        if (!"registrar".equalsIgnoreCase(acao)) {
            response.sendRedirect("registrar_transacao.html?erro=Acao%20invalida.");
            return;
        }

        try {
            String tipoStr = request.getParameter("tipo");
            String valorStr = request.getParameter("valor");
            String dataStr = request.getParameter("data");
            String categoria = request.getParameter("categoria");
            String descricao = request.getParameter("descricao");

            if (valorStr == null || valorStr.isEmpty()
                    || dataStr == null || dataStr.isEmpty()
                    || categoria == null || categoria.isEmpty()) {
                throw new IllegalArgumentException("Valor, Data e Categoria sao obrigatorios.");
            }

            Transacao t = new Transacao();
            t.setUsuario(usuarioLogado);
            t.setCategoria(categoria);
            t.setDescricao(descricao);

            try {
                t.setTipo(TipoTransacao.valueOf(tipoStr));
                t.setValor(new BigDecimal(valorStr));
                t.setData(LocalDate.parse(dataStr));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Valor invalido.");
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data invalida.");
            }

            transacaoService.registrarTransacao(t);

            response.sendRedirect("registrar_transacao.html?sucesso=Transacao%20registrada!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("registrar_transacao.html?erro=" + encode(e.getMessage()));
        }
    }

    // =========================================================
    // GET — LISTAR TRANSACOES (JSON) (RF05)
    // =========================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");


        String action = request.getParameter("action");

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (session != null)
                ? (Usuario) session.getAttribute("usuarioLogado")
                : null;

        if (usuarioLogado == null) {
            response.sendRedirect("login.html?erro=Faca%20login%20primeiro.");
            return;
        }

        if ("list".equalsIgnoreCase(action)) {
            try {
                List<Transacao> transacoes = transacaoService.buscarTodasPorUsuario(usuarioLogado);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(transacoes));
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\":\"Falha ao carregar transacoes.\"}");
            }
            return;
        }

        response.sendRedirect("dashboard.html?erro=Acao%20invalida");
    }

    private String encode(String mensagem) {
        return mensagem.replace(" ", "%20");
    }
}
