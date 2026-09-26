package controller;

import dao.Conexao;
import model.Usuario;

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

        // ==========================================
        // VERIFICAR LOGIN
        // ==========================================

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

            // ==========================================
            // CARREGAR POR STATUS
            // ==========================================

            List<Jogo> jogando =
                    carregarJogos(
                            idUsuario,
                            "jogando"
                    );

            List<Jogo> zerados =
                    carregarJogos(
                            idUsuario,
                            "zerado"
                    );

            List<Jogo> queroJogar =
                    carregarJogos(
                            idUsuario,
                            "quero jogar"
                    );

            // ==========================================
            // HTML
            // ==========================================

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            StringBuilder html =
                    new StringBuilder();

            html.append("<!DOCTYPE html>");
            html.append("<html lang='pt-BR'>");

            // ==========================================
            // HEAD
            // ==========================================

            html.append("<head>");

            html.append(
                    "<meta charset='UTF-8'>"
            );

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
                    "<link rel='stylesheet' " +
                    "href='style.css'>"
            );

            // ==========================================
            // CSS
            // ==========================================

            html.append("<style>");

            html.append(
                    "* {" +
                    "box-sizing:border-box;" +
                    "}"
            );

            html.append(
                    "body {" +
                    "margin:0;" +
                    "background:linear-gradient(135deg,#0d0714,#160b24,#0d0714);" +
                    "min-height:100vh;" +
                    "color:#ffffff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}"
            );

            html.append(
                    ".biblioteca-page {" +
                    "max-width:1200px;" +
                    "margin:0 auto;" +
                    "padding:30px 20px 60px;" +
                    "}"
            );

            // ==========================================
            // TOPO
            // ==========================================

            html.append(
                    ".biblioteca-topo {" +
                    "background:linear-gradient(135deg,#24102f,#1b1820);" +
                    "border:1px solid #40244f;" +
                    "border-radius:18px;" +
                    "padding:28px;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-topo h2 {" +
                    "margin:0 0 8px;" +
                    "font-size:32px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-topo p {" +
                    "margin:0;" +
                    "color:#98919f;" +
                    "}"
            );

            // ==========================================
            // SEÇÃO
            // ==========================================

            html.append(
                    ".biblioteca-secao {" +
                    "padding:15px 0;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            html.append(
                    ".secao-header {" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            html.append(
                    ".secao-titulo {" +
                    "margin:0;" +
                    "font-size:23px;" +
                    "}"
            );

            html.append(
                    ".contador {" +
                    "background:#171b20;" +
                    "border:1px solid #363e46;" +
                    "padding:6px 11px;" +
                    "border-radius:20px;" +
                    "font-size:13px;" +
                    "color:#aaa;" +
                    "}"
            );

            // ==========================================
            // GRID
            // ==========================================

            html.append(
                    ".jogos-grid {" +
                    "display:grid;" +
                    "grid-template-columns:repeat(auto-fill,minmax(165px,1fr));" +
                    "gap:18px;" +
                    "}"
            );

            // ==========================================
            // CARD
            // ==========================================

            html.append(
                    ".jogo-card {" +
                    "background:transparent;" +
                    "border:1px solid #303840;" +
                    "border-radius:11px;" +
                    "overflow:hidden;" +
                    "transition:0.2s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover {" +
                    "transform:translateY(-4px);" +
                    "border-color:#7300d1;" +
                    "}"
            );

            // ==========================================
            // CAPA
            // ==========================================

            html.append(
                    ".capa-container {" +
                    "width:100%;" +
                    "height:245px;" +
                    "overflow:hidden;" +
                    "background:#17111e;" +
                    "}"
            );

            html.append(
                    ".jogo-capa {" +
                    "width:100%;" +
                    "height:245px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "border-radius:10px 10px 0 0;" +
                    "}"
            );

            // ==========================================
            // INFO
            // ==========================================

            html.append(
                    ".jogo-info {" +
                    "padding:12px;" +
                    "background:transparent;" +
                    "}"
            );

            html.append(
                    ".jogo-titulo {" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "line-height:1.35;" +
                    "min-height:38px;" +
                    "}"
            );

            // ==========================================
            // BOTÃO
            // ==========================================

            html.append(
                    ".botao-avaliar {" +
                    "display:block;" +
                    "margin-top:11px;" +
                    "padding:9px;" +
                    "background:linear-gradient(135deg,#7c3aed,#9333ea);" +
                    "color:#ffffff;" +
                    "text-decoration:none;" +
                    "text-align:center;" +
                    "border-radius:7px;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".botao-avaliar:hover {" +
                    "background:#8300ed;" +
                    "}"
            );

            // ==========================================
            // VAZIO
            // ==========================================

            html.append(
                    ".vazio {" +
                    "text-align:center;" +
                    "color:#7f8790;" +
                    "padding:30px;" +
                    "}"
            );

            // ==========================================
            // RESPONSIVO
            // ==========================================

            html.append(
                    "@media(max-width:600px) {" +

                    ".biblioteca-page {" +
                    "padding:20px 12px 40px;" +
                    "}" +

                    ".biblioteca-topo h2 {" +
                    "font-size:26px;" +
                    "}" +

                    ".jogos-grid {" +
                    "grid-template-columns:repeat(2,1fr);" +
                    "gap:12px;" +
                    "}" +

                    ".capa-container," +
                    ".jogo-capa {" +
                    "height:210px;" +
                    "}" +

                    "}"
            );

            html.append("</style>");

            // ==========================================
            // SCRIPT DAS CAPAS
            // ==========================================

            html.append("<script>");

            html.append(
                    "function tentarOutraCapa(img){" +

                    "var src=img.getAttribute('src')||'';" +

                    "var match=src.match(/steam\\/apps\\/(\\d+)/);" +

                    "if(!match){" +
                    "img.style.display='none';" +
                    "return;" +
                    "}" +

                    "var id=match[1];" +

                    "var tentativas=parseInt(" +
                    "img.getAttribute('data-tentativas')||'0'," +
                    "10);" +

                    "var urls=[" +

                    "'https://shared.cloudflare.steamstatic.com/" +
                    "store_item_assets/steam/apps/'+id+" +
                    "'/library_600x900_2x.jpg'," +

                    "'https://shared.cloudflare.steamstatic.com/" +
                    "store_item_assets/steam/apps/'+id+" +
                    "'/library_600x900.jpg'," +

                    "'https://shared.cloudflare.steamstatic.com/" +
                    "store_item_assets/steam/apps/'+id+" +
                    "'/header.jpg'," +

                    "'https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/'+id+'/library_600x900_2x.jpg'," +

                    "'https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/'+id+'/library_600x900.jpg'," +

                    "'https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/'+id+'/header.jpg'" +

                    "];" +

                    "if(tentativas<urls.length){" +

                    "img.setAttribute(" +
                    "'data-tentativas'," +
                    "tentativas+1" +
                    ");" +

                    "img.src=urls[tentativas];" +

                    "}else{" +

                    "img.style.display='none';" +

                    "}" +

                    "}"
            );

            html.append("</script>");

            html.append("</head>");

            // ==========================================
            // BODY
            // ==========================================

            html.append("<body>");

            // ==========================================
            // HEADER
            // ==========================================

            html.append("<header>");

            html.append(
                    "<div class='logo-area'>"
            );

            html.append(
                    "<img " +
                    "src='icon.png' " +
                    "alt='Logo Inventory' " +
                    "class='logo-header' " +
                    "style='width:40px;height:40px;object-fit:contain;'>"
            );

            html.append(
                    "<h1>Inventory</h1>"
            );

            html.append("</div>");

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
                    "<a href='buscar-usuarios'>Buscar usuários</a>"
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

            // ==========================================
            // CONTEÚDO
            // ==========================================

            html.append(
                    "<main class='biblioteca-page'>"
            );

            html.append(
                    "<section class='biblioteca-topo'>"
            );

            html.append(
                    "<h2>Minha Biblioteca</h2>"
            );

            html.append(
                    "<p>Seus jogos organizados por status.</p>"
            );

            html.append("</section>");

            // ==========================================
            // JOGANDO
            // ==========================================

            html.append(
                    montarSecao(
                            "🎮 Jogando",
                            "Nenhum jogo sendo jogado.",
                            jogando
                    )
            );

            // ==========================================
            // ZERADOS
            // ==========================================

            html.append(
                    montarSecao(
                            "✅ Zerados",
                            "Nenhum jogo zerado ainda.",
                            zerados
                    )
            );

            // ==========================================
            // QUERO JOGAR
            // ==========================================

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

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO ABRIR BIBLIOTECA:"
            );

            e.printStackTrace();

            System.out.println(
                    "================================="
            );

            response.sendRedirect("index.html");
        }
    }

    // =====================================================
    // MONTAR SEÇÃO
    // =====================================================

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
                "<div class='secao-header'>"
        );

        html.append(
                "<h2 class='secao-titulo'>" +
                titulo +
                "</h2>"
        );

        html.append(
                "<span class='contador'>" +
                jogos.size() +
                "</span>"
        );

        html.append("</div>");

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

    // =====================================================
    // CARREGAR JOGOS
    // =====================================================

    private List<Jogo> carregarJogos(
            int idUsuario,
            String status)
            throws Exception {

        List<Jogo> jogos =
                new ArrayList<Jogo>();

        Connection conexao =
                Conexao.conectar();

        if (conexao == null) {

            throw new Exception(
                    "Não foi possível conectar ao banco."
            );
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String sql =
                    "SELECT " +
                    "steam_app_id, " +
                    "status " +
                    "FROM biblioteca " +
                    "WHERE id_usuario = ? " +
                    "AND status = ? " +
                    "ORDER BY id DESC";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            stmt.setString(
                    2,
                    status
            );

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
                                steamAppId
                        );

                jogo.capa =
                        "https://cdn.cloudflare.steamstatic.com/" +
                        "steam/apps/" +
                        steamAppId +
                        "/library_600x900.jpg";

                jogo.status =
                        rs.getString("status");

                jogos.add(jogo);
            }

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception ignored) {
            }

            try {
                conexao.close();
            } catch (Exception ignored) {
            }
        }

        return jogos;
    }

    // =====================================================
    // BUSCAR NOME
    // =====================================================

    private String buscarNomeJogo(
            int steamAppId) {

        String nome =
                "Jogo Steam #" + steamAppId;

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return nome;
            }

            stmt =
                    conexao.prepareStatement(
                            "SELECT titulo " +
                            "FROM jogo " +
                            "WHERE id = ? " +
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
                    "Nome não encontrado para AppID: "
                    + steamAppId
            );

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception ignored) {
            }

            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception ignored) {
            }
        }

        return nome;
    }

    // =====================================================
    // CARD
    // =====================================================

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
                "<img " +
                "class='jogo-capa' " +
                "src='" +
                escaparHtml(jogo.capa) +
                "' " +
                "alt='Capa de " +
                escaparHtml(jogo.titulo) +
                "' " +
                "onerror=\"tentarOutraCapa(this)\">"
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

    // =====================================================
    // ESCAPAR HTML
    // =====================================================

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

    // =====================================================
    // CLASSE JOGO
    // =====================================================

    private static class Jogo {

        int id;

        String titulo;

        String capa;

        String status;

        double nota;

        double horasJogadas;

        String comentario;

        boolean avaliado;
    }
}