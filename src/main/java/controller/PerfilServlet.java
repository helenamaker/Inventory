package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        Usuario usuarioSessao =
                (Usuario) session.getAttribute("usuario");

        UsuarioDAO usuarioDAO =
                new UsuarioDAO();

        Usuario usuario =
                usuarioDAO.buscarPorId(
                        usuarioSessao.getId()
                );

        if (usuario == null) {

            response.sendRedirect("login.html");
            return;
        }

        int seguidores =
                usuarioDAO.contarSeguidores(
                        usuario.getId()
                );

        int seguindo =
                usuarioDAO.contarSeguindo(
                        usuario.getId()
                );

        List<JogoInfo> favoritos =
                buscarFavoritos(
                        usuario.getId()
                );

        List<JogoInfo> avaliacoes =
                buscarAvaliacoes(
                        usuario.getId()
                );

        List<ListaInfo> listas =
                buscarListas(
                        usuario.getId()
                );

        StringBuilder html =
                new StringBuilder();

        // =====================================================
        // HTML
        // =====================================================

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");

        html.append("<head>");

        html.append(
                "<meta charset='UTF-8'>"
        );

        html.append(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        html.append(
                "<title>Meu Perfil - Inventory</title>"
        );

        html.append(
                "<link rel='preconnect' " +
                "href='https://fonts.googleapis.com'>"
        );

        html.append(
                "<link rel='preconnect' " +
                "href='https://fonts.gstatic.com' " +
                "crossorigin>"
        );

        html.append(
                "<link href='https://fonts.googleapis.com/css2?" +
                "family=Poppins:wght@400;500;600;700;800&display=swap' " +
                "rel='stylesheet'>"
        );

        // =====================================================
        // CSS
        // =====================================================

        html.append("<style>");

        html.append(
                "*{" +
                "box-sizing:border-box;" +
                "margin:0;" +
                "padding:0;" +
                "}"
        );

        html.append(
                "body{" +
                "font-family:'Poppins',Arial,sans-serif;" +
                "background:" +
                "radial-gradient(circle at 50% -20%,#3b1760 0%,#170d22 42%,#0b0710 100%);" +
                "color:#fff;" +
                "min-height:100vh;" +
                "}"
        );

        // =====================================================
        // HEADER
        // =====================================================

        html.append(
                "header{" +
                "height:74px;" +
                "background:rgba(13,8,20,.92);" +
                "border-bottom:1px solid rgba(168,85,247,.16);" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:space-between;" +
                "padding:0 6%;" +
                "position:sticky;" +
                "top:0;" +
                "z-index:10;" +
                "backdrop-filter:blur(12px);" +
                "}"
        );

        html.append(
                ".logo{" +
                "font-size:25px;" +
                "font-weight:800;" +
                "letter-spacing:-1px;" +
                "color:#fff;" +
                "text-decoration:none;" +
                "}"
        );

        html.append(
                ".logo span{" +
                "color:#a855f7;" +
                "}"
        );

        html.append(
                "nav{" +
                "display:flex;" +
                "align-items:center;" +
                "gap:26px;" +
                "}"
        );

        html.append(
                "nav a{" +
                "color:#aaa0b4;" +
                "text-decoration:none;" +
                "font-size:13px;" +
                "font-weight:500;" +
                "transition:.2s;" +
                "}"
        );

        html.append(
                "nav a:hover{" +
                "color:#c084fc;" +
                "}"
        );

        // =====================================================
        // CONTAINER
        // =====================================================

        html.append(
                ".container{" +
                "width:90%;" +
                "max-width:1180px;" +
                "margin:38px auto 70px;" +
                "}"
        );

        // =====================================================
        // PERFIL
        // =====================================================

        html.append(
                ".perfil-box{" +
                "position:relative;" +
                "overflow:hidden;" +
                "background:linear-gradient(135deg,#21122d,#160d20 65%,#1d1029);" +
                "border:1px solid #392249;" +
                "border-radius:22px;" +
                "padding:35px;" +
                "box-shadow:0 20px 60px rgba(0,0,0,.35);" +
                "}"
        );

        html.append(
                ".perfil-box:before{" +
                "content:'';" +
                "position:absolute;" +
                "width:300px;" +
                "height:300px;" +
                "background:#8b5cf6;" +
                "filter:blur(130px);" +
                "opacity:.13;" +
                "right:-100px;" +
                "top:-150px;" +
                "}"
        );

        html.append(
                ".perfil-conteudo{" +
                "position:relative;" +
                "display:flex;" +
                "align-items:center;" +
                "gap:28px;" +
                "}"
        );

        html.append(
                ".foto-perfil{" +
                "width:125px;" +
                "height:125px;" +
                "border-radius:50%;" +
                "object-fit:cover;" +
                "border:3px solid #9b5cff;" +
                "background:#24152f;" +
                "box-shadow:0 0 35px rgba(139,92,246,.22);" +
                "}"
        );

        html.append(
                ".sem-foto{" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "font-size:43px;" +
                "font-weight:800;" +
                "color:#d8b4fe;" +
                "}"
        );

        html.append(
                ".dados{" +
                "flex:1;" +
                "min-width:0;" +
                "}"
        );

        html.append(
                ".dados h1{" +
                "font-size:30px;" +
                "font-weight:700;" +
                "margin-bottom:2px;" +
                "}"
        );

        html.append(
                ".username{" +
                "color:#a78bfa;" +
                "font-size:14px;" +
                "margin-bottom:10px;" +
                "}"
        );

        html.append(
                ".bio{" +
                "color:#b6aabc;" +
                "font-size:13px;" +
                "line-height:1.6;" +
                "max-width:650px;" +
                "}"
        );

        // =====================================================
        // ESTATÍSTICAS
        // =====================================================

        html.append(
                ".estatisticas{" +
                "display:flex;" +
                "gap:30px;" +
                "margin-top:20px;" +
                "}"
        );

        html.append(
                ".estatistica strong{" +
                "display:block;" +
                "font-size:20px;" +
                "font-weight:700;" +
                "}"
        );

        html.append(
                ".estatistica span{" +
                "display:block;" +
                "font-size:11px;" +
                "color:#8e8298;" +
                "margin-top:1px;" +
                "}"
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        html.append(
                ".botoes{" +
                "display:flex;" +
                "flex-direction:column;" +
                "gap:9px;" +
                "position:relative;" +
                "}"
        );

        html.append(
                ".btn{" +
                "display:block;" +
                "padding:10px 20px;" +
                "border-radius:9px;" +
                "text-decoration:none;" +
                "font-size:12px;" +
                "font-weight:600;" +
                "text-align:center;" +
                "white-space:nowrap;" +
                "transition:.2s;" +
                "}"
        );

        html.append(
                ".btn-editar{" +
                "background:linear-gradient(135deg,#7c3aed,#9333ea);" +
                "color:#fff;" +
                "box-shadow:0 5px 20px rgba(124,58,237,.2);" +
                "}"
        );

        html.append(
                ".btn-editar:hover{" +
                "transform:translateY(-1px);" +
                "background:linear-gradient(135deg,#8b5cf6,#a855f7);" +
                "}"
        );

        html.append(
                ".btn-sair{" +
                "background:#24152e;" +
                "border:1px solid #4a2c5a;" +
                "color:#d8b4fe;" +
                "}"
        );

        html.append(
                ".btn-sair:hover{" +
                "background:#301b3c;" +
                "}"
        );

        // =====================================================
        // SEÇÕES
        // =====================================================

        html.append(
                ".secao{" +
                "margin-top:42px;" +
                "}"
        );

        html.append(
                ".secao-topo{" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:space-between;" +
                "margin-bottom:18px;" +
                "}"
        );

        html.append(
                ".secao h2{" +
                "font-size:20px;" +
                "font-weight:600;" +
                "}"
        );

        html.append(
                ".contador{" +
                "background:#20142a;" +
                "border:1px solid #3b2649;" +
                "color:#bda5ca;" +
                "padding:5px 11px;" +
                "border-radius:20px;" +
                "font-size:11px;" +
                "}"
        );

        // =====================================================
        // GRID
        // =====================================================

        html.append(
                ".grade{" +
                "display:grid;" +
                "grid-template-columns:" +
                "repeat(auto-fill,minmax(170px,1fr));" +
                "gap:18px;" +
                "}"
        );

        // =====================================================
        // CARD
        // =====================================================

        html.append(
                ".card{" +
                "background:#17101e;" +
                "border:1px solid #2d2038;" +
                "border-radius:13px;" +
                "overflow:hidden;" +
                "transition:.25s;" +
                "}"
        );

        html.append(
                ".card:hover{" +
                "transform:translateY(-5px);" +
                "border-color:#7138a0;" +
                "box-shadow:0 12px 30px rgba(0,0,0,.35);" +
                "}"
        );

        html.append(
                ".card-capa{" +
                "position:relative;" +
                "width:100%;" +
                "height:245px;" +
                "background:#100b15;" +
                "overflow:hidden;" +
                "}"
        );

        html.append(
                ".card-capa img{" +
                "width:100%;" +
                "height:100%;" +
                "object-fit:cover;" +
                "display:block;" +
                "}"
        );

        html.append(
                ".card-capa:after{" +
                "content:'';" +
                "position:absolute;" +
                "left:0;" +
                "right:0;" +
                "bottom:0;" +
                "height:55%;" +
                "background:linear-gradient(transparent,rgba(10,5,15,.75));" +
                "pointer-events:none;" +
                "}"
        );

        html.append(
                ".card-info{" +
                "padding:13px;" +
                "}"
        );

        html.append(
                ".card-info h3{" +
                "font-size:13px;" +
                "font-weight:600;" +
                "line-height:1.4;" +
                "min-height:37px;" +
                "}"
        );

        html.append(
                ".tipo{" +
                "font-size:10px;" +
                "color:#83778c;" +
                "margin-top:5px;" +
                "}"
        );

        html.append(
                ".nota{" +
                "font-size:11px;" +
                "color:#c084fc!important;" +
                "font-weight:600;" +
                "margin-top:6px;" +
                "}"
        );

        html.append(
                ".comentario{" +
                "font-size:10px;" +
                "line-height:1.5;" +
                "color:#918496;" +
                "margin-top:7px;" +
                "display:-webkit-box;" +
                "-webkit-line-clamp:3;" +
                "-webkit-box-orient:vertical;" +
                "overflow:hidden;" +
                "}"
        );

        // =====================================================
        // LISTAS
        // =====================================================

        html.append(
                ".lista{" +
                "background:linear-gradient(135deg,#1b1224,#15101a);" +
                "border:1px solid #30213b;" +
                "border-radius:13px;" +
                "padding:18px;" +
                "margin-bottom:12px;" +
                "transition:.2s;" +
                "}"
        );

        html.append(
                ".lista:hover{" +
                "border-color:#583775;" +
                "transform:translateX(2px);" +
                "}"
        );

        html.append(
                ".lista h3{" +
                "font-size:15px;" +
                "font-weight:600;" +
                "margin-bottom:4px;" +
                "}"
        );

        html.append(
                ".lista p{" +
                "font-size:11px;" +
                "color:#827589;" +
                "}"
        );

        // =====================================================
        // VAZIO
        // =====================================================

        html.append(
                ".vazio{" +
                "background:#15101b;" +
                "border:1px dashed #3b2948;" +
                "border-radius:13px;" +
                "padding:28px;" +
                "color:#80738a;" +
                "text-align:center;" +
                "font-size:12px;" +
                "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:750px){"
        );

        html.append(
                "header{" +
                "padding:0 20px;" +
                "}"
        );

        html.append(
                "nav{" +
                "gap:13px;" +
                "}"
        );

        html.append(
                "nav a:nth-child(2)," +
                "nav a:nth-child(3){" +
                "display:none;" +
                "}"
        );

        html.append(
                ".container{" +
                "width:94%;" +
                "margin-top:25px;" +
                "}"
        );

        html.append(
                ".perfil-box{" +
                "padding:25px 20px;" +
                "}"
        );

        html.append(
                ".perfil-conteudo{" +
                "flex-direction:column;" +
                "text-align:center;" +
                "}"
        );

        html.append(
                ".dados{" +
                "width:100%;" +
                "}"
        );

        html.append(
                ".bio{" +
                "margin:auto;" +
                "}"
        );

        html.append(
                ".estatisticas{" +
                "justify-content:center;" +
                "}"
        );

        html.append(
                ".botoes{" +
                "width:100%;" +
                "}"
        );

        html.append(
                ".grade{" +
                "grid-template-columns:repeat(2,1fr);" +
                "gap:12px;" +
                "}"
        );

        html.append(
                ".card-capa{" +
                "height:210px;" +
                "}"
        );

        html.append("}");

        html.append("</style>");
        html.append("</head>");

        // =====================================================
        // BODY
        // =====================================================

        html.append("<body>");

        html.append("<header>");

        html.append(
                "<a href='jogos' class='logo'>" +
                "INVENT<span>O</span>RY" +
                "</a>"
        );

        html.append("<nav>");

        html.append(
                "<a href='jogos'>Jogos</a>"
        );

        html.append(
                "<a href='biblioteca'>Biblioteca</a>"
        );

        html.append(
                "<a href='buscar-usuarios'>Usuários</a>"
        );

        html.append(
                "<a href='perfil'>Perfil</a>"
        );

        html.append("</nav>");

        html.append("</header>");

        html.append("<main class='container'>");

        // =====================================================
        // CABEÇALHO DO PERFIL
        // =====================================================

        html.append(
                "<section class='perfil-box'>"
        );

        html.append(
                "<div class='perfil-conteudo'>"
        );

        String foto =
                usuario.getFoto();

        if (foto != null &&
                !foto.trim().isEmpty()) {

            html.append(
                    "<img class='foto-perfil' " +
                    "src='" +
                    escaparHtml(foto) +
                    "' " +
                    "alt='Foto de perfil'>"
            );

        } else {

            html.append(
                    "<div class='foto-perfil sem-foto'>" +
                    primeiraLetra(
                            usuario.getNome()
                    ) +
                    "</div>"
            );
        }

        html.append("<div class='dados'>");

        html.append(
                "<h1>" +
                escaparHtml(
                        usuario.getNome()
                ) +
                "</h1>"
        );

        String username =
                usuario.getUsername();

        if (username != null &&
                !username.trim().isEmpty()) {

            if (!username.startsWith("@")) {
                username =
                        "@" + username;
            }

            html.append(
                    "<div class='username'>" +
                    escaparHtml(username) +
                    "</div>"
            );
        }

        String bio =
                usuario.getBio();

        if (bio != null &&
                !bio.trim().isEmpty()) {

            html.append(
                    "<div class='bio'>" +
                    escaparHtml(bio) +
                    "</div>"
            );
        }

        html.append(
                "<div class='estatisticas'>"
        );

        html.append(
                "<div class='estatistica'>" +
                "<strong>" +
                seguidores +
                "</strong>" +
                "<span>Seguidores</span>" +
                "</div>"
        );

        html.append(
                "<div class='estatistica'>" +
                "<strong>" +
                seguindo +
                "</strong>" +
                "<span>Seguindo</span>" +
                "</div>"
        );

        html.append(
                "<div class='estatistica'>" +
                "<strong>" +
                favoritos.size() +
                "</strong>" +
                "<span>Favoritos</span>" +
                "</div>"
        );

        html.append(
                "<div class='estatistica'>" +
                "<strong>" +
                avaliacoes.size() +
                "</strong>" +
                "<span>Avaliações</span>" +
                "</div>"
        );

        html.append(
                "</div>"
        );

        html.append("</div>");

        html.append("<div class='botoes'>");

        html.append(
                "<a class='btn btn-editar' " +
                "href='editar-perfil'>" +
                "Editar perfil" +
                "</a>"
        );

        html.append(
                "<a class='btn btn-sair' " +
                "href='logout'>" +
                "Sair" +
                "</a>"
        );

        html.append("</div>");

        html.append("</div>");

        html.append("</section>");

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append("<section class='secao'>");

        html.append("<div class='secao-topo'>");

        html.append(
                "<h2>⭐ Meus favoritos</h2>"
        );

        html.append(
                "<span class='contador'>" +
                favoritos.size() +
                " jogos</span>"
        );

        html.append("</div>");

        if (favoritos.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Você ainda não adicionou nenhum jogo aos favoritos." +
                    "</div>"
            );

        } else {

            html.append("<div class='grade'>");

            for (JogoInfo jogo :
                    favoritos) {

                html.append(
                        montarCardFavorito(
                                jogo
                        )
                );
            }

            html.append("</div>");
        }

        html.append("</section>");

        // =====================================================
        // AVALIAÇÕES
        // =====================================================

        html.append("<section class='secao'>");

        html.append("<div class='secao-topo'>");

        html.append(
                "<h2>📝 Minhas avaliações</h2>"
        );

        html.append(
                "<span class='contador'>" +
                avaliacoes.size() +
                " avaliações</span>"
        );

        html.append("</div>");

        if (avaliacoes.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Você ainda não avaliou nenhum jogo." +
                    "</div>"
            );

        } else {

            html.append("<div class='grade'>");

            for (JogoInfo jogo :
                    avaliacoes) {

                html.append(
                        montarCardAvaliacao(
                                jogo
                        )
                );
            }

            html.append("</div>");
        }

        html.append("</section>");

        // =====================================================
        // LISTAS
        // =====================================================

        html.append("<section class='secao'>");

        html.append("<div class='secao-topo'>");

        html.append(
                "<h2>📚 Minhas listas</h2>"
        );

        html.append(
                "<span class='contador'>" +
                listas.size() +
                " listas</span>"
        );

        html.append("</div>");

        if (listas.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Você ainda não criou nenhuma lista." +
                    "</div>"
            );

        } else {

            for (ListaInfo lista :
                    listas) {

                html.append(
                        "<div class='lista'>"
                );

                html.append(
                        "<h3>" +
                        escaparHtml(
                                lista.nome
                        ) +
                        "</h3>"
                );

                html.append(
                        "<p>" +
                        lista.quantidade +
                        " jogo(s)</p>"
                );

                html.append("</div>");
            }
        }

        html.append("</section>");

        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().write(
                html.toString()
        );
    }

    // =====================================================
    // FAVORITOS
    // =====================================================

    private List<JogoInfo> buscarFavoritos(
            int idUsuario) {

        List<JogoInfo> lista =
                new ArrayList<JogoInfo>();

        String sql =
                "SELECT f.steam_app_id " +
                "FROM favorito f " +
                "WHERE f.id_usuario = ? " +
                "ORDER BY f.id DESC";

        try (
                Connection conn =
                        Conexao.conectar();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    idUsuario
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    int appId =
                            rs.getInt(
                                    "steam_app_id"
                            );

                    String titulo =
                            buscarNomeJogo(
                                    appId
                            );

                    lista.add(
                            new JogoInfo(
                                    titulo,
                                    appId,
                                    0,
                                    "",
                                    capaSteam(appId)
                            )
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR FAVORITOS"
            );

            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // AVALIAÇÕES
    // =====================================================

    private List<JogoInfo> buscarAvaliacoes(
            int idUsuario) {

        List<JogoInfo> lista =
                new ArrayList<JogoInfo>();

        String sql =
                "SELECT a.steam_app_id, " +
                "a.nota, " +
                "a.comentario " +
                "FROM avaliacao a " +
                "WHERE a.id_usuario = ? " +
                "ORDER BY a.data_avaliacao DESC";

        try (
                Connection conn =
                        Conexao.conectar();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    idUsuario
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    int appId =
                            rs.getInt(
                                    "steam_app_id"
                            );

                    String titulo =
                            buscarNomeJogo(
                                    appId
                            );

                    lista.add(
                            new JogoInfo(
                                    titulo,
                                    appId,
                                    rs.getDouble(
                                            "nota"
                                    ),
                                    rs.getString(
                                            "comentario"
                                    ),
                                    capaSteam(appId)
                            )
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR AVALIACOES"
            );

            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // NOME DO JOGO
    // =====================================================

    private String buscarNomeJogo(
            int steamAppId) {

        String nome =
                "Jogo " + steamAppId;

        String sql =
                "SELECT titulo " +
                "FROM jogo " +
                "WHERE steam_app_id = ? " +
                "LIMIT 1";

        try (
                Connection conn =
                        Conexao.conectar();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    steamAppId
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    String titulo =
                            rs.getString(
                                    "titulo"
                            );

                    if (titulo != null &&
                            !titulo.trim().isEmpty()) {

                        return titulo;
                    }
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Nome não encontrado no banco: " +
                    steamAppId
            );
        }

        // Fallback para a Steam
        String nomeSteam =
                buscarNomeSteam(
                        steamAppId
                );

        if (nomeSteam != null &&
                !nomeSteam.trim().isEmpty()) {

            return nomeSteam;
        }

        return nome;
    }

    // =====================================================
    // BUSCAR NOME NA STEAM
    // =====================================================

    private String buscarNomeSteam(
            int steamAppId) {

        String nome =
                "Jogo " + steamAppId;

        HttpURLConnection conexao =
                null;

        BufferedReader leitor =
                null;

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

            conexao.setRequestMethod(
                    "GET"
            );

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

            while (
                    (linha =
                            leitor.readLine()) != null
            ) {

                json.append(linha);
            }

            Pattern pattern =
                    Pattern.compile(
                            "\"name\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\""
                    );

            Matcher matcher =
                    pattern.matcher(
                            json.toString()
                    );

            if (matcher.find()) {

                nome =
                        matcher.group(1)
                                .replace(
                                        "\\/",
                                        "/"
                                )
                                .replace(
                                        "\\\"",
                                        "\""
                                )
                                .replace(
                                        "\\\\",
                                        "\\"
                                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro ao buscar nome na Steam: " +
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

    // =====================================================
    // CARD FAVORITO
    // =====================================================

    private String montarCardFavorito(
            JogoInfo jogo) {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<article class='card'>"
        );

        html.append(
                "<div class='card-capa'>"
        );

        html.append(
                "<img src='" +
                escaparHtml(
                        jogo.capa
                ) +
                "' " +
                "alt='" +
                escaparHtml(
                        jogo.titulo
                ) +
                "' " +
                "onerror=\"this.src='https://cdn.cloudflare.steamstatic.com/steam/apps/" +
                jogo.appId +
                "/header.jpg';\">"
        );

        html.append("</div>");

        html.append(
                "<div class='card-info'>"
        );

        html.append(
                "<h3>" +
                escaparHtml(
                        jogo.titulo
                ) +
                "</h3>"
        );

        html.append(
                "<p class='tipo'>⭐ Favorito</p>"
        );

        html.append("</div>");

        html.append("</article>");

        return html.toString();
    }

    // =====================================================
    // CARD AVALIAÇÃO
    // =====================================================

    private String montarCardAvaliacao(
            JogoInfo jogo) {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<article class='card'>"
        );

        html.append(
                "<div class='card-capa'>"
        );

        html.append(
                "<img src='" +
                escaparHtml(
                        jogo.capa
                ) +
                "' " +
                "alt='" +
                escaparHtml(
                        jogo.titulo
                ) +
                "' " +
                "onerror=\"this.src='https://cdn.cloudflare.steamstatic.com/steam/apps/" +
                jogo.appId +
                "/header.jpg';\">"
        );

        html.append("</div>");

        html.append(
                "<div class='card-info'>"
        );

        html.append(
                "<h3>" +
                escaparHtml(
                        jogo.titulo
                ) +
                "</h3>"
        );

        html.append(
                "<p class='nota'>" +
                "⭐ " +
                jogo.nota +
                "/5" +
                "</p>"
        );

        if (jogo.comentario != null &&
                !jogo.comentario.trim().isEmpty()) {

            html.append(
                    "<p class='comentario'>" +
                    escaparHtml(
                            jogo.comentario
                    ) +
                    "</p>"
            );
        }

        html.append("</div>");

        html.append("</article>");

        return html.toString();
    }

    // =====================================================
    // LISTAS
    // =====================================================

    private List<ListaInfo> buscarListas(
            int idUsuario) {

        List<ListaInfo> lista =
                new ArrayList<ListaInfo>();

        String sql =
                "SELECT l.id, " +
                "l.nome, " +
                "COUNT(lj.id) AS quantidade " +
                "FROM lista l " +
                "LEFT JOIN lista_jogo lj " +
                "ON lj.id_lista = l.id " +
                "WHERE l.id_usuario = ? " +
                "GROUP BY l.id, l.nome " +
                "ORDER BY l.data_criacao DESC";

        try (
                Connection conn =
                        Conexao.conectar();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    idUsuario
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            new ListaInfo(
                                    rs.getString(
                                            "nome"
                                    ),
                                    rs.getInt(
                                            "quantidade"
                                    )
                            )
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Não foi possível carregar listas."
            );
        }

        return lista;
    }

    // =====================================================
    // CAPA
    // =====================================================

    private String capaSteam(
            int appId) {

        return
                "https://cdn.cloudflare.steamstatic.com/" +
                "steam/apps/" +
                appId +
                "/library_600x900.jpg";
    }

    // =====================================================
    // PRIMEIRA LETRA
    // =====================================================

    private String primeiraLetra(
            String nome) {

        if (nome == null ||
                nome.trim().isEmpty()) {

            return "?";
        }

        return String.valueOf(
                nome.trim().charAt(0)
        ).toUpperCase();
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
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\"",
                        "&quot;"
                )
                .replace(
                        "'",
                        "&#39;"
                );
    }

    // =====================================================
    // CLASSE JOGO
    // =====================================================

    private static class JogoInfo {

        String titulo;

        int appId;

        double nota;

        String comentario;

        String capa;

        JogoInfo(
                String titulo,
                int appId,
                double nota,
                String comentario,
                String capa) {

            this.titulo =
                    titulo;

            this.appId =
                    appId;

            this.nota =
                    nota;

            this.comentario =
                    comentario;

            this.capa =
                    capa;
        }
    }

    // =====================================================
    // CLASSE LISTA
    // =====================================================

    private static class ListaInfo {

        String nome;

        int quantidade;

        ListaInfo(
                String nome,
                int quantidade) {

            this.nome =
                    nome;

            this.quantidade =
                    quantidade;
        }
    }
}