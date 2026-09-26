package controller;

import dao.Conexao;
import model.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/avaliar")
public class AvaliacaoServlet extends HttpServlet {

    // =========================================================
    // GET - ABRIR PÁGINA DE AVALIAÇÃO
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao =
                request.getSession(false);

        // =====================================================
        // VERIFICAR LOGIN
        // =====================================================

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        // =====================================================
        // PEGAR STEAM APP ID
        // =====================================================

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.sendRedirect("biblioteca");
            return;
        }

        try {

            int steamAppId =
                    Integer.parseInt(idTexto);

            // =================================================
            // BUSCAR NOME DO JOGO
            // =================================================

            String titulo =
                    buscarNomeSteam(steamAppId);

            // =================================================
            // CAPA STEAM
            // =================================================

            String capa =
                    "https://cdn.cloudflare.steamstatic.com/" +
                    "steam/apps/" +
                    steamAppId +
                    "/library_600x900.jpg";

            // =================================================
            // HTML
            // =================================================

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            StringBuilder html =
                    new StringBuilder();

            html.append("<!DOCTYPE html>");
            html.append("<html lang='pt-BR'>");

            // =================================================
            // HEAD
            // =================================================

            html.append("<head>");

            html.append(
                    "<meta charset='UTF-8'>"
            );

            html.append(
                    "<meta name='viewport' " +
                    "content='width=device-width, " +
                    "initial-scale=1.0'>"
            );

            html.append(
                    "<title>Avaliar " +
                    escapar(titulo) +
                    " - Inventory</title>"
            );

            html.append(
                    "<link rel='icon' " +
                    "type='image/png' " +
                    "href='icon.png'>"
            );

            html.append(
                    "<link rel='stylesheet' " +
                    "href='style.css'>"
            );

            // =================================================
            // CSS
            // =================================================

            html.append("<style>");

            html.append(
                    "* {" +
                    "box-sizing:border-box;" +
                    "}"
            );

            html.append(
                    "body {" +
                    "margin:0;" +
                    "min-height:100vh;" +
                    "background:" +
                    "radial-gradient(" +
                    "circle at top," +
                    "#35105f 0%," +
                    "#160b22 45%," +
                    "#09060d 100%" +
                    ");" +
                    "color:#fff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}"
            );

            // =================================================
            // HEADER
            // =================================================

            html.append(
                    "header {" +
                    "width:100%;" +
                    "min-height:80px;" +
                    "padding:14px 35px;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:space-between;" +
                    "gap:25px;" +
                    "background:rgba(10,6,15,0.96);" +
                    "border-bottom:1px solid #322044;" +
                    "}"
            );

            // =================================================
            // LOGO
            // =================================================

            html.append(
                    ".logo-area {" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:9px;" +
                    "flex-shrink:0;" +
                    "}"
            );

            html.append(
                    ".logo-header {" +
                    "width:40px !important;" +
                    "height:40px !important;" +
                    "max-width:40px !important;" +
                    "max-height:40px !important;" +
                    "object-fit:contain !important;" +
                    "display:block !important;" +
                    "}"
            );

            html.append(
                    ".logo-area h1 {" +
                    "margin:0;" +
                    "padding:0;" +
                    "font-size:30px;" +
                    "font-weight:bold;" +
                    "line-height:1;" +
                    "color:#fff;" +
                    "}"
            );

            // =================================================
            // NAV
            // =================================================

            html.append(
                    "nav {" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:flex-end;" +
                    "gap:28px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    "nav a {" +
                    "color:#aaa1b5;" +
                    "text-decoration:none;" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "transition:0.2s;" +
                    "}"
            );

            html.append(
                    "nav a:hover {" +
                    "color:#b66cff;" +
                    "}"
            );

            // =================================================
            // CONTAINER
            // =================================================

            html.append(
                    ".avaliacao-container {" +
                    "max-width:600px;" +
                    "margin:50px auto;" +
                    "padding:35px;" +
                    "background:" +
                    "linear-gradient(" +
                    "135deg,#24102f,#140b1b" +
                    ");" +
                    "border:1px solid #4b2464;" +
                    "border-radius:16px;" +
                    "text-align:center;" +
                    "box-shadow:" +
                    "0 15px 45px rgba(0,0,0,0.35);" +
                    "}"
            );

            // =================================================
            // CAPA
            // =================================================

            html.append(
                    ".capa-avaliacao {" +
                    "width:180px !important;" +
                    "height:250px !important;" +
                    "max-width:180px !important;" +
                    "max-height:250px !important;" +
                    "object-fit:cover !important;" +
                    "border-radius:8px;" +
                    "display:block;" +
                    "margin:0 auto 20px;" +
                    "}"
            );

            // =================================================
            // TITULO
            // =================================================

            html.append(
                    ".avaliacao-container h2 {" +
                    "margin:10px 0;" +
                    "font-size:26px;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".avaliacao-container p {" +
                    "color:#bbb0c2;" +
                    "}"
            );

            // =================================================
            // ESTRELAS
            // =================================================

            html.append(
                    ".estrelas {" +
                    "display:flex;" +
                    "flex-direction:row-reverse;" +
                    "justify-content:center;" +
                    "gap:5px;" +
                    "margin:22px 0;" +
                    "}"
            );

            html.append(
                    ".estrelas input {" +
                    "display:none;" +
                    "}"
            );

            html.append(
                    ".estrelas label {" +
                    "font-size:42px;" +
                    "line-height:1;" +
                    "color:#666;" +
                    "cursor:pointer;" +
                    "transition:0.2s;" +
                    "}"
            );

            html.append(
                    ".estrelas label:hover," +
                    ".estrelas label:hover ~ label," +
                    ".estrelas input:checked ~ label {" +
                    "color:#ffd700;" +
                    "text-shadow:" +
                    "0 0 8px rgba(255,215,0,0.35);" +
                    "}"
            );

            // =================================================
            // HORAS
            // =================================================

            html.append(
                    ".horas-container {" +
                    "margin-top:20px;" +
                    "text-align:left;" +
                    "}"
            );

            html.append(
                    ".horas-container label {" +
                    "display:block;" +
                    "margin-bottom:8px;" +
                    "color:#ddd;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".campo-horas {" +
                    "width:100%;" +
                    "padding:12px;" +
                    "background:#14101a;" +
                    "color:white;" +
                    "border:1px solid #493252;" +
                    "border-radius:8px;" +
                    "font-size:16px;" +
                    "outline:none;" +
                    "}"
            );

            html.append(
                    ".campo-horas:focus {" +
                    "border-color:#8b35d6;" +
                    "}"
            );

            // =================================================
            // RESENHA
            // =================================================

            html.append(
                    ".campo-resenha {" +
                    "width:100%;" +
                    "height:150px;" +
                    "padding:15px;" +
                    "background:#14101a;" +
                    "color:white;" +
                    "border:1px solid #493252;" +
                    "border-radius:8px;" +
                    "resize:vertical;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "font-size:15px;" +
                    "margin-top:20px;" +
                    "outline:none;" +
                    "}"
            );

            html.append(
                    ".campo-resenha:focus {" +
                    "border-color:#8b35d6;" +
                    "}"
            );

            html.append(
                    ".campo-resenha::placeholder," +
                    ".campo-horas::placeholder {" +
                    "color:#71677a;" +
                    "}"
            );

            // =================================================
            // BOTAO
            // =================================================

            html.append(
                    ".botao-postar {" +
                    "margin-top:20px;" +
                    "padding:12px 30px;" +
                    "border:none;" +
                    "border-radius:7px;" +
                    "background:" +
                    "linear-gradient(135deg,#7c3aed,#9333ea);" +
                    "color:white;" +
                    "font-weight:bold;" +
                    "cursor:pointer;" +
                    "font-size:16px;" +
                    "transition:0.2s;" +
                    "}"
            );

            html.append(
                    ".botao-postar:hover {" +
                    "background:#a33cff;" +
                    "transform:translateY(-1px);" +
                    "}"
            );

            // =================================================
            // RESPONSIVO
            // =================================================

            html.append(
                    "@media(max-width:800px) {" +

                    "header {" +
                    "padding:14px 20px;" +
                    "flex-direction:column;" +
                    "align-items:flex-start;" +
                    "}" +

                    "nav {" +
                    "justify-content:flex-start;" +
                    "gap:16px;" +
                    "}" +

                    "}"
            );

            html.append(
                    "@media(max-width:600px) {" +

                    ".avaliacao-container {" +
                    "margin:25px 12px;" +
                    "padding:25px 18px;" +
                    "}" +

                    ".logo-header {" +
                    "width:36px !important;" +
                    "height:36px !important;" +
                    "}" +

                    ".logo-area h1 {" +
                    "font-size:26px;" +
                    "}" +

                    ".capa-avaliacao {" +
                    "width:160px !important;" +
                    "height:225px !important;" +
                    "}" +

                    ".estrelas label {" +
                    "font-size:36px;" +
                    "}" +

                    "}"
            );

            html.append("</style>");

            html.append("</head>");

            // =================================================
            // BODY
            // =================================================

            html.append("<body>");

            // =================================================
            // HEADER
            // =================================================

            html.append("<header>");

            html.append(
                    "<div class='logo-area'>" +
                    "<img " +
                    "src='icon.png' " +
                    "alt='Logo Inventory' " +
                    "class='logo-header'>" +
                    "<h1>Inventory</h1>" +
                    "</div>"
            );

            html.append("<nav>");

            html.append(
                    "<a href='index.html'>Início</a>"
            );

            html.append(
                    "<a href='jogos'>Jogos</a>"
            );

            html.append(
                    "<a href='biblioteca'>Biblioteca</a>"
            );

            html.append(
                    "<a href='buscar-usuarios'>" +
                    "Buscar usuários" +
                    "</a>"
            );

            html.append(
                    "<a href='listas'>Listas</a>"
            );

            html.append(
                    "<a href='perfil'>Meu Perfil</a>"
            );

            html.append(
                    "<a href='logout'>Sair</a>"
            );

            html.append("</nav>");

            html.append("</header>");

            // =================================================
            // CONTEUDO
            // =================================================

            html.append(
                    "<main class='avaliacao-container'>"
            );

            // =================================================
            // CAPA
            // =================================================

            html.append(
                    "<img " +
                    "class='capa-avaliacao' " +
                    "src='" +
                    escapar(capa) +
                    "' " +
                    "alt='Capa de " +
                    escapar(titulo) +
                    "' " +
                    "onerror=\"this.onerror=null;" +
                    "this.src='https://cdn.cloudflare.steamstatic.com/" +
                    "steam/apps/" +
                    steamAppId +
                    "/header.jpg';\">"
            );

            // =================================================
            // TITULO
            // =================================================

            html.append("<h2>");

            html.append(
                    escapar(titulo)
            );

            html.append("</h2>");

            html.append(
                    "<p>O que você achou desse jogo?</p>"
            );

            // =================================================
            // FORMULARIO
            // =================================================

            html.append(
                    "<form method='POST' action='avaliar'>"
            );

            // IMPORTANTE:
            // Agora enviamos o STEAM APP ID.

            html.append(
                    "<input " +
                    "type='hidden' " +
                    "name='steamAppId' " +
                    "value='" +
                    steamAppId +
                    "'>"
            );

            // =================================================
            // ESTRELAS
            // =================================================

            html.append(
                    "<p><strong>Sua nota:</strong></p>"
            );

            html.append(
                    "<div class='estrelas'>"
            );

            for (int i = 5; i >= 1; i--) {

                html.append(
                        "<input " +
                        "type='radio' " +
                        "id='estrela" +
                        i +
                        "' " +
                        "name='nota' " +
                        "value='" +
                        i +
                        "' " +
                        "required>"
                );

                html.append(
                        "<label " +
                        "for='estrela" +
                        i +
                        "'>" +
                        "★" +
                        "</label>"
                );
            }

            html.append("</div>");

            // =================================================
            // HORAS
            // =================================================

            html.append(
                    "<div class='horas-container'>"
            );

            html.append(
                    "<label for='horasJogadas'>" +
                    "Horas jogadas" +
                    "</label>"
            );

            html.append(
                    "<input " +
                    "class='campo-horas' " +
                    "type='number' " +
                    "id='horasJogadas' " +
                    "name='horasJogadas' " +
                    "min='0' " +
                    "step='0.1' " +
                    "placeholder='Ex: 25.5' " +
                    "required>"
            );

            html.append("</div>");

            // =================================================
            // RESENHA
            // =================================================

            html.append(
                    "<textarea " +
                    "class='campo-resenha' " +
                    "name='comentario' " +
                    "placeholder='Escreva sua resenha...' " +
                    "required></textarea>"
            );

            html.append("<br>");

            // =================================================
            // BOTAO
            // =================================================

            html.append(
                    "<button " +
                    "class='botao-postar' " +
                    "type='submit'>" +
                    "Postar avaliação" +
                    "</button>"
            );

            html.append("</form>");

            html.append("</main>");

            html.append("</body>");

            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "biblioteca"
            );
        }
    }

    // =========================================================
    // POST - SALVAR AVALIAÇÃO
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao =
                request.getSession(false);

        // =====================================================
        // LOGIN
        // =====================================================

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        Connection conexao = null;
        PreparedStatement stmtAvaliacao = null;
        PreparedStatement stmtBiblioteca = null;

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

            int idUsuario =
                    usuario.getId();

            // =================================================
            // PEGAR STEAM APP ID
            // =================================================

            String appIdTexto =
                    request.getParameter(
                            "steamAppId"
                    );

            if (appIdTexto == null ||
                    appIdTexto.trim().isEmpty()) {

                response.sendRedirect("biblioteca");
                return;
            }

            int steamAppId =
                    Integer.parseInt(
                            appIdTexto
                    );

            // =================================================
            // NOTA
            // =================================================

            double nota =
                    Double.parseDouble(
                            request.getParameter("nota")
                    );

            if (nota < 1 || nota > 5) {

                response.sendRedirect("biblioteca");
                return;
            }

            // =================================================
            // HORAS
            // =================================================

            double horasJogadas =
                    Double.parseDouble(
                            request.getParameter(
                                    "horasJogadas"
                            )
                    );

            if (horasJogadas < 0) {
                horasJogadas = 0;
            }

            // =================================================
            // COMENTARIO
            // =================================================

            String comentario =
                    request.getParameter(
                            "comentario"
                    );

            if (comentario == null) {
                comentario = "";
            }

            // =================================================
            // CONECTAR
            // =================================================

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                throw new Exception(
                        "Não foi possível conectar ao banco."
                );
            }

            // =================================================
            // SALVAR AVALIAÇÃO
            // =================================================
            //
            // IMPORTANTE:
            // Aqui mantemos sua tabela avaliacao atual.
            // O Steam AppID é salvo em id_jogo.
            //
            // =================================================

            String sqlAvaliacao =
                    "INSERT INTO avaliacao " +
                    "(id_usuario, id_jogo, nota, " +
                    "comentario, horas_jogadas) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT(id_usuario, id_jogo) " +
                    "DO UPDATE SET " +
                    "nota = excluded.nota, " +
                    "comentario = excluded.comentario, " +
                    "horas_jogadas = excluded.horas_jogadas, " +
                    "data_avaliacao = CURRENT_TIMESTAMP";

            stmtAvaliacao =
                    conexao.prepareStatement(
                            sqlAvaliacao
                    );

            stmtAvaliacao.setInt(
                    1,
                    idUsuario
            );

            stmtAvaliacao.setInt(
                    2,
                    steamAppId
            );

            stmtAvaliacao.setDouble(
                    3,
                    nota
            );

            stmtAvaliacao.setString(
                    4,
                    comentario
            );

            stmtAvaliacao.setDouble(
                    5,
                    horasJogadas
            );

            stmtAvaliacao.executeUpdate();

            // =================================================
            // MUDAR BIBLIOTECA PARA ZERADO
            // =================================================

            String sqlBiblioteca =
                    "UPDATE biblioteca " +
                    "SET status = 'zerado', " +
                    "horas_jogadas = ? " +
                    "WHERE id_usuario = ? " +
                    "AND steam_app_id = ?";

            stmtBiblioteca =
                    conexao.prepareStatement(
                            sqlBiblioteca
                    );

            stmtBiblioteca.setDouble(
                    1,
                    horasJogadas
            );

            stmtBiblioteca.setInt(
                    2,
                    idUsuario
            );

            stmtBiblioteca.setInt(
                    3,
                    steamAppId
            );

            int linhasAtualizadas =
                    stmtBiblioteca.executeUpdate();

            // =================================================
            // LOG
            // =================================================

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "AVALIAÇÃO SALVA!"
            );

            System.out.println(
                    "USUARIO: " +
                    idUsuario
            );

            System.out.println(
                    "STEAM APP ID: " +
                    steamAppId
            );

            System.out.println(
                    "NOTA: " +
                    nota
            );

            System.out.println(
                    "HORAS: " +
                    horasJogadas
            );

            System.out.println(
                    "STATUS: zerado"
            );

            System.out.println(
                    "LINHAS BIBLIOTECA ATUALIZADAS: " +
                    linhasAtualizadas
            );

            System.out.println(
                    "================================="
            );

            // =================================================
            // FECHAR
            // =================================================

            if (stmtBiblioteca != null) {
                stmtBiblioteca.close();
            }

            if (stmtAvaliacao != null) {
                stmtAvaliacao.close();
            }

            conexao.close();

            // =================================================
            // VOLTAR PARA BIBLIOTECA
            // =================================================

            response.sendRedirect(
                    "biblioteca"
            );

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO SALVAR AVALIAÇÃO:"
            );

            e.printStackTrace();

            System.out.println(
                    "================================="
            );

            response.sendRedirect(
                    "biblioteca"
            );

        } finally {

            try {

                if (stmtBiblioteca != null) {
                    stmtBiblioteca.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (stmtAvaliacao != null) {
                    stmtAvaliacao.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (conexao != null &&
                        !conexao.isClosed()) {

                    conexao.close();
                }

            } catch (Exception ignored) {
            }
        }
    }

    // =========================================================
    // BUSCAR NOME DO JOGO NA STEAM
    // =========================================================

    private String buscarNomeSteam(
            int steamAppId) {

        String nome =
                "Jogo Steam #" + steamAppId;

        HttpURLConnection conexao =
                null;

        BufferedReader leitor =
                null;

        try {

            String endereco =
                    "https://store.steampowered.com/api/appdetails" +
                    "?appids=" +
                    steamAppId +
                    "&l=portuguese";

            URL url =
                    new URL(endereco);

            conexao =
                    (HttpURLConnection)
                    url.openConnection();

            conexao.setRequestMethod("GET");

            conexao.setConnectTimeout(
                    5000
            );

            conexao.setReadTimeout(
                    5000
            );

            conexao.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
            );

            int codigo =
                    conexao.getResponseCode();

            if (codigo != 200) {
                return nome;
            }

            leitor =
                    new BufferedReader(
                            new InputStreamReader(
                                    conexao.getInputStream(),
                                    "UTF-8"
                            )
                    );

            StringBuilder resposta =
                    new StringBuilder();

            String linha;

            while ((linha = leitor.readLine()) != null) {

                resposta.append(linha);
            }

            String json =
                    resposta.toString();

            // =================================================
            // PEGAR "name"
            // =================================================

            Pattern pattern =
                    Pattern.compile(
                            "\"name\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\""
                    );

            Matcher matcher =
                    pattern.matcher(json);

            if (matcher.find()) {

                nome =
                        matcher.group(1);

                nome =
                        nome.replace(
                                "\\/",
                                "/"
                        );

                nome =
                        nome.replace(
                                "\\\"",
                                "\""
                        );

                nome =
                        nome.replace(
                                "\\\\",
                                "\\"
                        );
            }

        } catch (Exception e) {

            System.out.println(
                    "Não foi possível buscar nome Steam: " +
                    steamAppId
            );

        } finally {

            try {

                if (leitor != null) {
                    leitor.close();
                }

            } catch (Exception ignored) {
            }

            if (conexao != null) {
                conexao.disconnect();
            }
        }

        return nome;
    }

    // =========================================================
    // ESCAPAR HTML
    // =========================================================

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