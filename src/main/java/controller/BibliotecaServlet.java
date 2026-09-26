package controller;

import dao.Conexao;
import model.Usuario;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/biblioteca")
public class BibliotecaServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            List<Jogo> jogando =
                    carregarJogos(idUsuario, "jogando");

            List<Jogo> zerados =
                    carregarJogos(idUsuario, "zerado");

            List<Jogo> queroJogar =
                    carregarJogos(idUsuario, "quero jogar");

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            StringBuilder html =
                    new StringBuilder();

            html.append("<!DOCTYPE html>");
            html.append("<html lang='pt-BR'>");

            html.append("<head>");

            html.append("<meta charset='UTF-8'>");

            html.append(
                    "<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>"
            );

            html.append(
                    "<title>Biblioteca - Inventory</title>"
            );

            html.append(
                    "<link rel='icon' " +
                    "type='image/png' " +
                    "href='icon.png'>"
            );

            html.append(
                    "<style>" +

                    "*{box-sizing:border-box;}" +

                    "body{" +
                    "margin:0;" +
                    "background:linear-gradient(135deg,#0d0714,#160b24,#0d0714);" +
                    "min-height:100vh;" +
                    "color:#fff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}" +

                    "header{" +
                    "width:100%;" +
                    "min-height:80px;" +
                    "padding:14px 35px;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:space-between;" +
                    "gap:25px;" +
                    "background:rgba(10,6,15,.96);" +
                    "border-bottom:1px solid #322044;" +
                    "}" +

                    ".logo-area{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:9px;" +
                    "}" +

                    ".logo-header{" +
                    "width:40px;" +
                    "height:40px;" +
                    "object-fit:contain;" +
                    "}" +

                    ".logo-area h1{" +
                    "margin:0;" +
                    "font-size:30px;" +
                    "}" +

                    "nav{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:25px;" +
                    "flex-wrap:wrap;" +
                    "}" +

                    "nav a{" +
                    "color:#aaa1b5;" +
                    "text-decoration:none;" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "}" +

                    "nav a:hover{" +
                    "color:#b66cff;" +
                    "}" +

                    ".biblioteca-page{" +
                    "max-width:1200px;" +
                    "margin:0 auto;" +
                    "padding:30px 20px 60px;" +
                    "}" +

                    ".biblioteca-topo{" +
                    "background:linear-gradient(135deg,#24102f,#1b1820);" +
                    "border:1px solid #40244f;" +
                    "border-radius:18px;" +
                    "padding:28px;" +
                    "margin-bottom:25px;" +
                    "}" +

                    ".biblioteca-topo h2{" +
                    "margin:0 0 8px;" +
                    "font-size:32px;" +
                    "}" +

                    ".biblioteca-topo p{" +
                    "margin:0;" +
                    "color:#98919f;" +
                    "}" +

                    ".biblioteca-secao{" +
                    "padding:15px 0;" +
                    "margin-bottom:25px;" +
                    "}" +

                    ".secao-header{" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "margin-bottom:20px;" +
                    "}" +

                    ".secao-titulo{" +
                    "margin:0;" +
                    "font-size:23px;" +
                    "}" +

                    ".contador{" +
                    "background:#171b20;" +
                    "border:1px solid #363e46;" +
                    "padding:6px 11px;" +
                    "border-radius:20px;" +
                    "font-size:13px;" +
                    "color:#aaa;" +
                    "}" +

                    ".jogos-grid{" +
                    "display:grid;" +
                    "grid-template-columns:repeat(auto-fill,minmax(165px,1fr));" +
                    "gap:18px;" +
                    "}" +

                    ".jogo-card{" +
                    "background:transparent;" +
                    "border:1px solid #303840;" +
                    "border-radius:11px;" +
                    "overflow:hidden;" +
                    "transition:.2s;" +
                    "}" +

                    ".jogo-card:hover{" +
                    "transform:translateY(-4px);" +
                    "border-color:#7300d1;" +
                    "}" +

                    ".capa-container{" +
                    "width:100%;" +
                    "height:245px;" +
                    "overflow:hidden;" +
                    "background:#17111e;" +
                    "}" +

                    ".jogo-capa{" +
                    "width:100%;" +
                    "height:245px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "}" +

                    ".jogo-info{" +
                    "padding:12px;" +
                    "}" +

                    ".jogo-titulo{" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "line-height:1.35;" +
                    "min-height:38px;" +
                    "}" +

                    ".botao-avaliar{" +
                    "display:block;" +
                    "margin-top:11px;" +
                    "padding:9px;" +
                    "background:linear-gradient(135deg,#7c3aed,#9333ea);" +
                    "color:#fff;" +
                    "text-decoration:none;" +
                    "text-align:center;" +
                    "border-radius:7px;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "}" +

                    ".vazio{" +
                    "text-align:center;" +
                    "color:#7f8790;" +
                    "padding:30px;" +
                    "}" +

                    "@media(max-width:600px){" +
                    "header{" +
                    "padding:14px 20px;" +
                    "flex-direction:column;" +
                    "align-items:flex-start;" +
                    "}" +
                    "nav{gap:15px;}" +
                    ".jogos-grid{" +
                    "grid-template-columns:repeat(2,1fr);" +
                    "gap:12px;" +
                    "}" +
                    ".capa-container,.jogo-capa{" +
                    "height:210px;" +
                    "}" +
                    "}" +

                    "</style>"
            );

            html.append("</head>");
            html.append("<body>");

            html.append("<header>");

            html.append(
                    "<div class='logo-area'>" +
                    "<img src='icon.png' class='logo-header'>" +
                    "<h1>Inventory</h1>" +
                    "</div>"
            );

            html.append("<nav>");
            html.append("<a href='index.html'>Início</a>");
            html.append("<a href='jogos'>Jogos</a>");
            html.append("<a href='biblioteca'>Biblioteca</a>");
            html.append("<a href='buscar-usuarios'>Buscar usuários</a>");
            html.append("<a href='listas'>Listas</a>");
            html.append("<a href='perfil'>Meu Perfil</a>");
            html.append("<a href='logout'>Sair</a>");
            html.append("</nav>");

            html.append("</header>");

            html.append(
                    "<main class='biblioteca-page'>"
            );

            html.append(
                    "<section class='biblioteca-topo'>" +
                    "<h2>Minha Biblioteca</h2>" +
                    "<p>Seus jogos organizados por status.</p>" +
                    "</section>"
            );

            html.append(
                    montarSecao(
                            "🎮 Jogando",
                            "Nenhum jogo sendo jogado.",
                            jogando
                    )
            );

            html.append(
                    montarSecao(
                            "✅ Zerados",
                            "Nenhum jogo zerado ainda.",
                            zerados
                    )
            );

            html.append(
                    montarSecao(
                            "🎯 Quero jogar",
                            "Nenhum jogo na sua lista.",
                            queroJogar
                    )
            );

            html.append("</main>");
            html.append("</body>");
            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }

    private List<Jogo> carregarJogos(
            int idUsuario,
            String status)
            throws Exception {

        List<Jogo> jogos =
                new ArrayList<Jogo>();

        Connection conexao =
                Conexao.conectar();

        if (conexao == null) {
            throw new Exception("Banco não conectado.");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql =
                    "SELECT " +
                    "steam_app_id, " +
                    "status, " +
                    "horas_jogadas " +
                    "FROM biblioteca " +
                    "WHERE id_usuario = ? " +
                    "AND status = ? " +
                    "ORDER BY id DESC";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);
            stmt.setString(2, status);

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                int steamAppId =
                        rs.getInt("steam_app_id");

                Jogo jogo =
                        new Jogo();

                jogo.id =
                        steamAppId;

                jogo.titulo =
                        buscarNomeJogo(
                                conexao,
                                steamAppId
                        );

                jogo.capa =
                        "https://cdn.cloudflare.steamstatic.com/" +
                        "steam/apps/" +
                        steamAppId +
                        "/library_600x900.jpg";

                jogo.status =
                        rs.getString("status");

                jogo.horasJogadas =
                        rs.getDouble("horas_jogadas");

                jogos.add(jogo);
            }

        } finally {

            try {
                if (rs != null) rs.close();
            } catch (Exception ignored) {
            }

            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) {
            }

            try {
                conexao.close();
            } catch (Exception ignored) {
            }
        }

        return jogos;
    }

    private String buscarNomeJogo(
            Connection conexao,
            int steamAppId) {

        String nome =
                "Jogo Steam #" + steamAppId;

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            /*
             * IMPORTANTE:
             * NÃO procurar em jogo.id.
             *
             * Agora procuramos pelo steam_app_id.
             */

            stmt =
                    conexao.prepareStatement(
                            "SELECT titulo " +
                            "FROM jogo " +
                            "WHERE steam_app_id = ? " +
                            "LIMIT 1"
                    );

            stmt.setInt(
                    1,
                    steamAppId
            );

            rs =
                    stmt.executeQuery();

            if (rs.next()) {

                String titulo =
                        rs.getString("titulo");

                if (titulo != null &&
                        !titulo.trim().isEmpty()) {

                    nome =
                            titulo;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Nome não encontrado: " +
                    steamAppId
            );

        } finally {

            try {
                if (rs != null) rs.close();
            } catch (Exception ignored) {
            }

            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) {
            }
        }

        /*
         * Se o jogo ainda não estiver na tabela jogo,
         * pelo menos pegamos o nome diretamente da Steam.
         */

        if (nome.equals("Jogo Steam #" + steamAppId)) {

            try {

                nome =
                        buscarNomeSteam(
                                steamAppId
                        );

            } catch (Exception ignored) {
            }
        }

        return nome;
    }

    private String buscarNomeSteam(
            int steamAppId) {

        String nome =
                "Jogo Steam #" + steamAppId;

        HttpURLConnection conexao = null;
        BufferedReader leitor = null;

        try {

            URL url =
                    new URL(
                            "https://store.steampowered.com/api/appdetails" +
                            "?appids=" +
                            steamAppId +
                            "&l=portuguese"
                    );

            conexao =
                    (HttpURLConnection)
                    url.openConnection();

            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(5000);
            conexao.setReadTimeout(5000);
            conexao.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
            );

            if (conexao.getResponseCode() != 200) {
                return nome;
            }

            leitor =
                    new BufferedReader(
                            new InputStreamReader(
                                    conexao.getInputStream(),
                                    "UTF-8"
                            )
                    );

            StringBuilder json =
                    new StringBuilder();

            String linha;

            while ((linha = leitor.readLine()) != null) {
                json.append(linha);
            }

            Pattern pattern =
                    Pattern.compile(
                            "\"name\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\""
                    );

            java.util.regex.Matcher matcher =
                    pattern.matcher(
                            json.toString()
                    );

            if (matcher.find()) {

                nome =
                        matcher.group(1)
                                .replace("\\/", "/")
                                .replace("\\\"", "\"")
                                .replace("\\\\", "\\");
            }

        } catch (Exception ignored) {

        } finally {

            try {
                if (leitor != null) leitor.close();
            } catch (Exception ignored) {
            }

            if (conexao != null) {
                conexao.disconnect();
            }
        }

        return nome;
    }

    private String montarSecao(
            String titulo,
            String mensagemVazia,
            List<Jogo> jogos) {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<section class='biblioteca-secao'>"
        );

        html.append(
                "<div class='secao-header'>" +
                "<h2 class='secao-titulo'>" +
                titulo +
                "</h2>" +
                "<span class='contador'>" +
                jogos.size() +
                "</span>" +
                "</div>"
        );

        if (jogos.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    mensagemVazia +
                    "</div>"
            );

        } else {

            html.append(
                    "<div class='jogos-grid'>"
            );

            for (Jogo jogo : jogos) {

                html.append(
                        montarCard(jogo)
                );
            }

            html.append("</div>");
        }

        html.append("</section>");

        return html.toString();
    }

    private String montarCard(
            Jogo jogo) {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<div class='jogo-card'>"
        );

        html.append(
                "<div class='capa-container'>"
        );

        html.append(
                "<img class='jogo-capa' " +
                "src='" +
                escaparHtml(jogo.capa) +
                "' " +
                "alt='Capa de " +
                escaparHtml(jogo.titulo) +
                "' " +
                "onerror=\"this.style.display='none'\">"
        );

        html.append("</div>");

        html.append(
                "<div class='jogo-info'>"
        );

        html.append(
                "<div class='jogo-titulo'>" +
                escaparHtml(jogo.titulo) +
                "</div>"
        );

        html.append(
                "<a class='botao-avaliar' " +
                "href='avaliar?id=" +
                jogo.id +
                "'>" +
                "⭐ Avaliar jogo" +
                "</a>"
        );

        html.append("</div>");
        html.append("</div>");

        return html.toString();
    }

    private String escaparHtml(
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

    private static class Jogo {

        int id;
        String titulo;
        String capa;
        String status;
        double horasJogadas;
    }
}