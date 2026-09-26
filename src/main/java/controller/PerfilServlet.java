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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.html");
            return;
        }

        Usuario usuarioSessao = (Usuario) session.getAttribute("usuario");

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario = usuarioDAO.buscarPorId(usuarioSessao.getId());

        if (usuario == null) {
            response.sendRedirect("login.html");
            return;
        }

        int seguidores = usuarioDAO.contarSeguidores(usuario.getId());
        int seguindo = usuarioDAO.contarSeguindo(usuario.getId());

        List<JogoInfo> favoritos = buscarFavoritos(usuario.getId());
        List<JogoInfo> avaliacoes = buscarAvaliacoes(usuario.getId());
        List<ListaInfo> listas = buscarListas(usuario.getId());

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Meu Perfil - Inventory</title>");

        html.append("<link rel='preconnect' href='https://fonts.googleapis.com'>");
        html.append("<link rel='preconnect' href='https://fonts.gstatic.com' crossorigin>");
        html.append("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap' rel='stylesheet'>");

        html.append("<style>");

        html.append("*{box-sizing:border-box;margin:0;padding:0}");

        html.append("body{");
        html.append("font-family:'Poppins',Arial,sans-serif;");
        html.append("background:#100b18;");
        html.append("color:#fff;");
        html.append("min-height:100vh;");
        html.append("}");

        html.append("header{");
        html.append("height:70px;");
        html.append("background:#181020;");
        html.append("border-bottom:1px solid #2d2038;");
        html.append("display:flex;");
        html.append("align-items:center;");
        html.append("justify-content:space-between;");
        html.append("padding:0 6%;");
        html.append("}");

        html.append(".logo{");
        html.append("font-size:25px;");
        html.append("font-weight:700;");
        html.append("color:#fff;");
        html.append("text-decoration:none;");
        html.append("}");

        html.append(".logo span{color:#a855f7}");

        html.append("nav{display:flex;gap:25px;align-items:center}");

        html.append("nav a{");
        html.append("color:#cfc5d9;");
        html.append("text-decoration:none;");
        html.append("font-size:14px;");
        html.append("font-weight:500;");
        html.append("}");

        html.append("nav a:hover{color:#c084fc}");

        html.append(".container{");
        html.append("width:90%;");
        html.append("max-width:1150px;");
        html.append("margin:40px auto;");
        html.append("}");

        html.append(".perfil-topo{");
        html.append("background:#181020;");
        html.append("border:1px solid #2d2038;");
        html.append("border-radius:18px;");
        html.append("padding:30px;");
        html.append("display:flex;");
        html.append("align-items:center;");
        html.append("gap:25px;");
        html.append("}");

        html.append(".foto-perfil{");
        html.append("width:110px;");
        html.append("height:110px;");
        html.append("border-radius:50%;");
        html.append("object-fit:cover;");
        html.append("border:3px solid #8b5cf6;");
        html.append("background:#251630;");
        html.append("}");

        html.append(".sem-foto{");
        html.append("display:flex;");
        html.append("align-items:center;");
        html.append("justify-content:center;");
        html.append("font-size:40px;");
        html.append("font-weight:700;");
        html.append("color:#c084fc;");
        html.append("}");

        html.append(".dados{flex:1}");

        html.append(".dados h1{");
        html.append("font-size:28px;");
        html.append("margin-bottom:3px;");
        html.append("}");

        html.append(".username{");
        html.append("color:#a78bfa;");
        html.append("font-size:14px;");
        html.append("margin-bottom:12px;");
        html.append("}");

        html.append(".bio{");
        html.append("color:#b9adbf;");
        html.append("font-size:14px;");
        html.append("max-width:700px;");
        html.append("line-height:1.6;");
        html.append("}");

        html.append(".estatisticas{");
        html.append("display:flex;");
        html.append("gap:25px;");
        html.append("margin-top:18px;");
        html.append("}");

        html.append(".estatistica strong{");
        html.append("display:block;");
        html.append("font-size:20px;");
        html.append("color:#fff;");
        html.append("}");

        html.append(".estatistica span{");
        html.append("font-size:12px;");
        html.append("color:#9f91a7;");
        html.append("}");

        html.append(".botoes{");
        html.append("display:flex;");
        html.append("flex-direction:column;");
        html.append("gap:10px;");
        html.append("}");

        html.append(".btn{");
        html.append("display:inline-block;");
        html.append("padding:10px 18px;");
        html.append("border-radius:9px;");
        html.append("text-decoration:none;");
        html.append("font-size:13px;");
        html.append("font-weight:600;");
        html.append("text-align:center;");
        html.append("white-space:nowrap;");
        html.append("}");

        html.append(".btn-editar{");
        html.append("background:#7c3aed;");
        html.append("color:white;");
        html.append("}");

        html.append(".btn-editar:hover{background:#8b5cf6}");

        html.append(".btn-sair{");
        html.append("background:#25152f;");
        html.append("color:#d8b4fe;");
        html.append("border:1px solid #4c2c5c;");
        html.append("}");

        html.append(".secao{margin-top:35px}");

        html.append(".secao h2{");
        html.append("font-size:21px;");
        html.append("margin-bottom:18px;");
        html.append("}");

        html.append(".grade{");
        html.append("display:grid;");
        html.append("grid-template-columns:repeat(auto-fill,minmax(170px,1fr));");
        html.append("gap:18px;");
        html.append("}");

        html.append(".card{");
        html.append("background:#181020;");
        html.append("border:1px solid #2d2038;");
        html.append("border-radius:12px;");
        html.append("overflow:hidden;");
        html.append("}");

        html.append(".card img{");
        html.append("width:100%;");
        html.append("height:230px;");
        html.append("object-fit:cover;");
        html.append("display:block;");
        html.append("}");

        html.append(".card-info{padding:13px}");

        html.append(".card-info h3{");
        html.append("font-size:14px;");
        html.append("margin-bottom:5px;");
        html.append("}");

        html.append(".card-info p{");
        html.append("font-size:11px;");
        html.append("color:#9f91a7;");
        html.append("}");

        html.append(".nota{");
        html.append("color:#c084fc!important;");
        html.append("margin-top:5px;");
        html.append("}");

        html.append(".lista{");
        html.append("background:#181020;");
        html.append("border:1px solid #2d2038;");
        html.append("border-radius:12px;");
        html.append("padding:18px;");
        html.append("margin-bottom:12px;");
        html.append("}");

        html.append(".lista h3{");
        html.append("font-size:16px;");
        html.append("margin-bottom:5px;");
        html.append("}");

        html.append(".lista p{");
        html.append("font-size:12px;");
        html.append("color:#9f91a7;");
        html.append("}");

        html.append(".vazio{");
        html.append("background:#181020;");
        html.append("border:1px dashed #3b2948;");
        html.append("border-radius:12px;");
        html.append("padding:25px;");
        html.append("color:#8f8298;");
        html.append("text-align:center;");
        html.append("font-size:13px;");
        html.append("}");

        html.append("@media(max-width:700px){");

        html.append(".perfil-topo{");
        html.append("flex-direction:column;");
        html.append("text-align:center;");
        html.append("}");

        html.append(".estatisticas{justify-content:center}");

        html.append(".botoes{");
        html.append("width:100%;");
        html.append("}");

        html.append("nav{gap:10px}");

        html.append("}");

        html.append("</style>");
        html.append("</head>");

        html.append("<body>");

        html.append("<header>");

        html.append("<a href='jogos' class='logo'>INVENT<span>O</span>RY</a>");

        html.append("<nav>");
        html.append("<a href='jogos'>Jogos</a>");
        html.append("<a href='buscar-usuarios'>Usuários</a>");
        html.append("<a href='perfil'>Perfil</a>");
        html.append("</nav>");

        html.append("</header>");

        html.append("<main class='container'>");

        html.append("<section class='perfil-topo'>");

        String foto = usuario.getFoto();

        if (foto != null && !foto.trim().isEmpty()) {
            html.append("<img class='foto-perfil' src='")
                .append(escaparHtml(foto))
                .append("' alt='Foto de perfil'>");
        } else {
            html.append("<div class='foto-perfil sem-foto'>")
                .append(primeiraLetra(usuario.getNome()))
                .append("</div>");
        }

        html.append("<div class='dados'>");

        html.append("<h1>")
            .append(escaparHtml(usuario.getNome()))
            .append("</h1>");

        String username = usuario.getUsername();

        if (username != null && !username.trim().isEmpty()) {
            if (!username.startsWith("@")) {
                username = "@" + username;
            }

            html.append("<div class='username'>")
                .append(escaparHtml(username))
                .append("</div>");
        }

        if (usuario.getBio() != null && !usuario.getBio().trim().isEmpty()) {
            html.append("<div class='bio'>")
                .append(escaparHtml(usuario.getBio()))
                .append("</div>");
        }

        html.append("<div class='estatisticas'>");

        html.append("<div class='estatistica'>");
        html.append("<strong>").append(seguidores).append("</strong>");
        html.append("<span>Seguidores</span>");
        html.append("</div>");

        html.append("<div class='estatistica'>");
        html.append("<strong>").append(seguindo).append("</strong>");
        html.append("<span>Seguindo</span>");
        html.append("</div>");

        html.append("<div class='estatistica'>");
        html.append("<strong>").append(favoritos.size()).append("</strong>");
        html.append("<span>Favoritos</span>");
        html.append("</div>");

        html.append("</div>");
        html.append("</div>");

        html.append("<div class='botoes'>");

        html.append("<a class='btn btn-editar' href='editar-perfil'>Editar perfil</a>");

        html.append("<a class='btn btn-sair' href='logout'>Sair</a>");

        html.append("</div>");

        html.append("</section>");

        // FAVORITOS
        html.append("<section class='secao'>");
        html.append("<h2>Meus favoritos</h2>");

        if (favoritos.isEmpty()) {

            html.append("<div class='vazio'>");
            html.append("Você ainda não adicionou nenhum jogo aos favoritos.");
            html.append("</div>");

        } else {

            html.append("<div class='grade'>");

            for (JogoInfo jogo : favoritos) {

                html.append("<article class='card'>");

                html.append("<img src='")
                    .append(capaSteam(jogo.appId))
                    .append("' alt='")
                    .append(escaparHtml(jogo.titulo))
                    .append("'>");

                html.append("<div class='card-info'>");

                html.append("<h3>")
                    .append(escaparHtml(jogo.titulo))
                    .append("</h3>");

                html.append("<p>Favorito</p>");

                html.append("</div>");

                html.append("</article>");
            }

            html.append("</div>");
        }

        html.append("</section>");

        // AVALIAÇÕES
        html.append("<section class='secao'>");
        html.append("<h2>Minhas avaliações</h2>");

        if (avaliacoes.isEmpty()) {

            html.append("<div class='vazio'>");
            html.append("Você ainda não avaliou nenhum jogo.");
            html.append("</div>");

        } else {

            html.append("<div class='grade'>");

            for (JogoInfo jogo : avaliacoes) {

                html.append("<article class='card'>");

                html.append("<img src='")
                    .append(capaSteam(jogo.appId))
                    .append("' alt='")
                    .append(escaparHtml(jogo.titulo))
                    .append("'>");

                html.append("<div class='card-info'>");

                html.append("<h3>")
                    .append(escaparHtml(jogo.titulo))
                    .append("</h3>");

                html.append("<p class='nota'>Nota: ")
                    .append(jogo.nota)
                    .append("/10</p>");

                if (jogo.comentario != null &&
                    !jogo.comentario.trim().isEmpty()) {

                    html.append("<p>")
                        .append(escaparHtml(jogo.comentario))
                        .append("</p>");
                }

                html.append("</div>");

                html.append("</article>");
            }

            html.append("</div>");
        }

        html.append("</section>");

        // LISTAS
        html.append("<section class='secao'>");
        html.append("<h2>Minhas listas</h2>");

        if (listas.isEmpty()) {

            html.append("<div class='vazio'>");
            html.append("Você ainda não criou nenhuma lista.");
            html.append("</div>");

        } else {

            for (ListaInfo lista : listas) {

                html.append("<div class='lista'>");

                html.append("<h3>")
                    .append(escaparHtml(lista.nome))
                    .append("</h3>");

                html.append("<p>")
                    .append(lista.quantidade)
                    .append(" jogo(s)</p>");

                html.append("</div>");
            }
        }

        html.append("</section>");

        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().write(html.toString());
    }

    private List<JogoInfo> buscarFavoritos(int idUsuario) {

        List<JogoInfo> lista = new ArrayList<JogoInfo>();

        String sql =
            "SELECT f.id_jogo, " +
            "j.titulo, " +
            "j.capa " +
            "FROM favorito f " +
            "LEFT JOIN jogo j ON j.id = f.id_jogo " +
            "WHERE f.id_usuario = ? " +
            "ORDER BY f.data_adicionado DESC";

        try (
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    int appId = rs.getInt("id_jogo");

                    String titulo = rs.getString("titulo");

                    if (titulo == null || titulo.trim().isEmpty()) {
                        titulo = "Jogo " + appId;
                    }

                    lista.add(
                        new JogoInfo(
                            titulo,
                            appId,
                            0,
                            "",
                            rs.getString("capa")
                        )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    private List<JogoInfo> buscarAvaliacoes(int idUsuario) {

        List<JogoInfo> lista = new ArrayList<JogoInfo>();

        String sql =
            "SELECT a.id_jogo, " +
            "a.nota, " +
            "a.comentario, " +
            "j.titulo, " +
            "j.capa " +
            "FROM avaliacao a " +
            "LEFT JOIN jogo j ON j.id = a.id_jogo " +
            "WHERE a.id_usuario = ? " +
            "ORDER BY a.data_avaliacao DESC";

        try (
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    int appId = rs.getInt("id_jogo");

                    String titulo = rs.getString("titulo");

                    if (titulo == null || titulo.trim().isEmpty()) {
                        titulo = "Jogo " + appId;
                    }

                    lista.add(
                        new JogoInfo(
                            titulo,
                            appId,
                            rs.getDouble("nota"),
                            rs.getString("comentario"),
                            rs.getString("capa")
                        )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    private List<ListaInfo> buscarListas(int idUsuario) {

        List<ListaInfo> lista = new ArrayList<ListaInfo>();

        String sql =
            "SELECT l.id, l.nome, COUNT(lj.id) AS quantidade " +
            "FROM lista l " +
            "LEFT JOIN lista_jogo lj ON lj.id_lista = l.id " +
            "WHERE l.id_usuario = ? " +
            "GROUP BY l.id, l.nome " +
            "ORDER BY l.data_criacao DESC";

        try (
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                        new ListaInfo(
                            rs.getString("nome"),
                            rs.getInt("quantidade")
                        )
                    );
                }
            }

        } catch (Exception e) {
            // Se a tabela lista ainda não existir,
            // simplesmente não mostra listas.
            e.printStackTrace();
        }

        return lista;
    }

    private String capaSteam(int appId) {

        return "https://cdn.cloudflare.steamstatic.com/steam/apps/"
                + appId
                + "/library_600x900.jpg";
    }

    private String primeiraLetra(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            return "?";
        }

        return String.valueOf(
            nome.trim().charAt(0)
        ).toUpperCase();
    }

    private String escaparHtml(String texto) {

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
            String capa
        ) {
            this.titulo = titulo;
            this.appId = appId;
            this.nota = nota;
            this.comentario = comentario;
            this.capa = capa;
        }
    }

    private static class ListaInfo {

        String nome;
        int quantidade;

        ListaInfo(
            String nome,
            int quantidade
        ) {
            this.nome = nome;
            this.quantidade = quantidade;
        }
    }
}

