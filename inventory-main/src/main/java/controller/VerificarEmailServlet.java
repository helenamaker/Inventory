package controller;

import dao.CriarBanco;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/verificar-email")
public class VerificarEmailServlet extends HttpServlet {

    // =====================================================
    // POST - VERIFICAR CÓDIGO
    // =====================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        System.out.println(
                "================================="
        );

        System.out.println(
                "VERIFICACAO DE EMAIL FOI CHAMADA"
        );

        System.out.println(
                "================================="
        );

        // =================================================
        // DADOS
        // =================================================

        String email =
                request.getParameter("email");

        String codigo =
                request.getParameter("codigo");

        if (email == null) {
            email = "";
        }

        if (codigo == null) {
            codigo = "";
        }

        email =
                email.trim()
                       .toLowerCase();

        codigo =
                codigo.trim();

        System.out.println(
                "Email: " + email
        );

        // =================================================
        // VALIDAR
        // =================================================

        if (email.isEmpty() ||
                codigo.isEmpty()) {

            mostrarErro(
                    response,
                    "Informe o e-mail e o código."
            );

            return;
        }

        if (!codigo.matches("\\d{6}")) {

            mostrarErro(
                    response,
                    "O código deve ter 6 números."
            );

            return;
        }

        try {

            // =================================================
            // GARANTIR BANCO
            // =================================================

            CriarBanco.criarTabela();

            // =================================================
            // DAO
            // =================================================

            UsuarioDAO usuarioDAO =
                    new UsuarioDAO();

            // =================================================
            // CONFIRMAR
            // =================================================

            Usuario usuario =
                    usuarioDAO.confirmarEmail(
                            email,
                            codigo
                    );

            // =================================================
            // CÓDIGO INVÁLIDO / EXPIRADO
            // =================================================

            if (usuario == null) {

                mostrarErro(
                        response,
                        "Código inválido ou expirado."
                );

                return;
            }

            // =================================================
            // SUCESSO
            // =================================================

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "EMAIL VERIFICADO COM SUCESSO!"
            );

            System.out.println(
                    "USUARIO CRIADO:"
                            + usuario.getUsername()
            );

            System.out.println(
                    "================================="
            );

            // =================================================
            // LIMPAR SESSÃO ANTERIOR
            // =================================================

            HttpSession sessao =
                    request.getSession(true);

            sessao.removeAttribute(
                    "usuario"
            );

            // =================================================
            // TELA DE SUCESSO
            // =================================================

            mostrarSucesso(
                    response
            );

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO VERIFICAR EMAIL"
            );

            System.out.println(
                    e.getMessage()
            );

            System.out.println(
                    "================================="
            );

            e.printStackTrace();

            mostrarErro(
                    response,
                    "Ocorreu um erro ao verificar "
                    + "seu e-mail."
            );
        }
    }

    // =====================================================
    // GET
    // =====================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                "cadastro.html"
        );
    }

    // =====================================================
    // TELA DE SUCESSO
    // =====================================================

    private void mostrarSucesso(
            HttpServletResponse response)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<!DOCTYPE html>"
        );

        html.append(
                "<html lang='pt-BR'>"
        );

        html.append(
                "<head>"
        );

        html.append(
                "<meta charset='UTF-8'>"
        );

        html.append(
                "<meta name='viewport' "
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
        );

        html.append(
                "<title>E-mail verificado - Inventory</title>"
        );

        html.append(
                "<link href='https://fonts.googleapis.com/css2?"
                + "family=Rajdhani:wght@400;500;600;700&display=swap' "
                + "rel='stylesheet'>"
        );

        html.append(
                "<style>"
                + "body{"
                + "margin:0;"
                + "min-height:100vh;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "font-family:'Rajdhani',sans-serif;"
                + "background:"
                + "linear-gradient("
                + "135deg,"
                + "#0d0714,"
                + "#1d0b2d,"
                + "#0d0714"
                + ");"
                + "color:white;"
                + "}"

                + ".box{"
                + "width:420px;"
                + "max-width:90%;"
                + "background:#181020;"
                + "border:1px solid #4b2370;"
                + "border-radius:18px;"
                + "padding:40px;"
                + "text-align:center;"
                + "box-shadow:"
                + "0 20px 60px rgba(0,0,0,.45);"
                + "}"

                + "h1{"
                + "color:#c084fc;"
                + "font-size:30px;"
                + "margin-bottom:10px;"
                + "font-family:'Rajdhani',sans-serif;"
                + "}"

                + "p{"
                + "color:#aaa;"
                + "line-height:1.6;"
                + "}"

                + ".botao{"
                + "display:inline-block;"
                + "margin-top:20px;"
                + "padding:13px 25px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#a855f7"
                + ");"
                + "color:white;"
                + "text-decoration:none;"
                + "border-radius:9px;"
                + "font-weight:bold;"
                + "}"

                + "</style>"
        );

        html.append(
                "</head>"
        );

        html.append(
                "<body>"
        );

        html.append(
                "<div class='box'>"
        );

        html.append(
                "<h1>E-mail verificado!</h1>"
        );

        html.append(
                "<p>"
                + "Sua conta foi criada com sucesso."
                + "</p>"
        );

        html.append(
                "<p>"
                + "Agora você já pode entrar no Inventory."
                + "</p>"
        );

        html.append(
                "<a "
                + "class='botao' "
                + "href='login.html'>"
                + "Ir para o login"
                + "</a>"
        );

        html.append(
                "</div>"
        );

        html.append(
                "</body>"
        );

        html.append(
                "</html>"
        );

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // TELA DE ERRO
    // =====================================================

    private void mostrarErro(
            HttpServletResponse response,
            String mensagem)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        String mensagemSegura =
                escapar(mensagem);

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<!DOCTYPE html>"
        );

        html.append(
                "<html lang='pt-BR'>"
        );

        html.append(
                "<head>"
        );

        html.append(
                "<meta charset='UTF-8'>"
        );

        html.append(
                "<meta name='viewport' "
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
        );

        html.append(
                "<title>Erro - Inventory</title>"
        );

        html.append(
                "<link href='https://fonts.googleapis.com/css2?"
                + "family=Rajdhani:wght@400;500;600;700&display=swap' "
                + "rel='stylesheet'>"
        );

        html.append(
                "<style>"
                + "body{"
                + "margin:0;"
                + "min-height:100vh;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "font-family:'Rajdhani',sans-serif;"
                + "background:#100814;"
                + "color:white;"
                + "}"

                + ".box{"
                + "width:420px;"
                + "max-width:90%;"
                + "background:#1d1226;"
                + "border:1px solid #5a2a70;"
                + "border-radius:18px;"
                + "padding:35px;"
                + "text-align:center;"
                + "}"

                + "h1{"
                + "color:#c084fc;"
                + "font-family:'Rajdhani',sans-serif;"
                + "}"

                + "p{"
                + "color:#aaa;"
                + "line-height:1.6;"
                + "}"

                + "a{"
                + "display:inline-block;"
                + "margin-top:18px;"
                + "color:#c084fc;"
                + "text-decoration:none;"
                + "}"

                + "</style>"
        );

        html.append(
                "</head>"
        );

        html.append(
                "<body>"
        );

        html.append(
                "<div class='box'>"
        );

        html.append(
                "<h1>Não foi possível verificar</h1>"
        );

        html.append(
                "<p>"
                + mensagemSegura
                + "</p>"
        );

        html.append(
                "<a href='cadastro.html'>"
                + "Voltar para o cadastro"
                + "</a>"
        );

        html.append(
                "</div>"
        );

        html.append(
                "</body>"
        );

        html.append(
                "</html>"
        );

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // ESCAPAR HTML
    // =====================================================

    private String escapar(
            String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}