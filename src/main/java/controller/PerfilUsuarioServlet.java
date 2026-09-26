
package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/perfil-usuario")
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // VERIFICAR LOGIN
        // =====================================================

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        // =====================================================
        // ID DO PERFIL
        // =====================================================

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.sendRedirect("buscar-usuarios");
            return;
        }

        int idPerfil;

        try {

            idPerfil =
                    Integer.parseInt(
                            idTexto.trim()
                    );

        } catch (Exception e) {

            response.sendRedirect("buscar-usuarios");
            return;
        }

        // =====================================================
        // USUARIO LOGADO
        // =====================================================

        Usuario usuarioLogado =
                (Usuario) sessao.getAttribute("usuario");

        int idLogado =
                usuarioLogado.getId();

        // =====================================================
        // BUSCAR PERFIL
        // =====================================================

        UsuarioDAO dao =
                new UsuarioDAO();

        Usuario perfil =
                dao.buscarPorId(idPerfil);

        if (perfil == null) {

            response.sendRedirect("buscar-usuarios");
            return;
        }

        // =====================================================
        // EMBLEMA MY LOVE
        // =====================================================

        boolean jogadorEspecial =
                perfil.getEmail() != null
                &&
                perfil.getEmail()
                        .trim()
                        .equalsIgnoreCase(
                                "rebecarodriguesduarte2@gmail.com"
                        );

        // =====================================================
        // MESMO USUARIO
        // =====================================================

        boolean mesmoUsuario =
                idLogado == idPerfil;

        // =====================================================
        // SEGUINDO
        // =====================================================

        boolean seguindo = false;

        if (!mesmoUsuario) {

            seguindo =
                    dao.seguindo(
                            idLogado,
                            idPerfil
                    );
        }

        // =====================================================
        // CONTADORES
        // =====================================================

        int totalSeguidores =
                dao.contarSeguidores(
                        idPerfil
                );

        int totalSeguindo =
                dao.contarSeguindo(
                        idPerfil
                );

        // =====================================================
        // LISTAS DE USUARIOS
        // =====================================================

        ArrayList<Usuario> seguidores =
                dao.listarSeguidores(
                        idPerfil
                );

        ArrayList<Usuario> seguindoLista =
                dao.listarSeguindo(
                        idPerfil
                );

        // =====================================================
        // FAVORITOS - 5
        // =====================================================

        ArrayList<String[]> favoritos =
                carregarFavoritos(
                        idPerfil
                );

        // =====================================================
        // LISTAS
        // =====================================================

        ArrayList<String[]> listas =
                carregarListas(
                        idPerfil
                );

        // =====================================================
        // AVALIACOES DO PERFIL PESQUISADO
        // =====================================================

        ArrayList<String[]> avaliacoes =
                carregarAvaliacoes(
                        idPerfil
                );

        // =====================================================
        // HTML
        // =====================================================

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        StringBuilder html =
                new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");

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
                "<title>" +
                escapar(perfil.getNome()) +
                " - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' " +
                "href='style.css'>"
        );

        // =====================================================
        // CSS
        // =====================================================

        html.append("<style>");

        html.append(
                "*{" +
                "box-sizing:border-box;" +
                "}"
        );

        html.append(
                "body{" +
                "margin:0;" +
                "min-height:100vh;" +
                "background:" +
                "radial-gradient(" +
                "circle at 15% 10%," +
                "rgba(139,92,246,.18)," +
                "transparent 30%" +
                ")," +
                "radial-gradient(" +
                "circle at 85% 20%," +
                "rgba(168,85,247,.12)," +
                "transparent 28%" +
                ")," +
                "linear-gradient(" +
                "135deg,#0a0710,#100a18 45%,#08060c" +
                ");" +
                "color:#fff;" +
                "}"
        );

        // =====================================================
        // CONTAINER
        // =====================================================

        html.append(
                ".perfil-container{" +
                "width:100%;" +
                "max-width:1250px;" +
                "margin:0 auto;" +
                "padding:40px 25px 70px;" +
                "}"
        );

        // =====================================================
        // HERO
        // =====================================================

        html.append(
                ".perfil-hero{" +
                "position:relative;" +
                "overflow:hidden;" +
                "background:" +
                "linear-gradient(" +
                "135deg," +
                "rgba(39,22,59,.95)," +
                "rgba(18,12,27,.96)" +
                ");" +
                "border:1px solid rgba(168,85,247,.25);" +
                "border-radius:28px;" +
                "padding:42px 35px;" +
                "box-shadow:" +
                "0 30px 80px rgba(0,0,0,.5)," +
                "inset 0 1px rgba(255,255,255,.03);" +
                "}"
        );

        html.append(
                ".perfil-hero:before{" +
                "content:'';" +
                "position:absolute;" +
                "width:320px;" +
                "height:320px;" +
                "background:#8b5cf6;" +
                "filter:blur(120px);" +
                "opacity:.12;" +
                "top:-170px;" +
                "right:-90px;" +
                "pointer-events:none;" +
                "}"
        );

        html.append(
                ".perfil-hero:after{" +
                "content:'';" +
                "position:absolute;" +
                "width:250px;" +
                "height:250px;" +
                "background:#a855f7;" +
                "filter:blur(110px);" +
                "opacity:.08;" +
                "bottom:-150px;" +
                "left:-80px;" +
                "pointer-events:none;" +
                "}"
        );

        // =====================================================
        // TOPO
        // =====================================================

        html.append(
                ".perfil-topo{" +
                "position:relative;" +
                "z-index:1;" +
                "text-align:center;" +
                "}"
        );

        // =====================================================
        // FOTO
        // =====================================================

        html.append(
                ".foto-perfil{" +
                "width:160px;" +
                "height:160px;" +
                "object-fit:cover;" +
                "border-radius:50%;" +
                "border:5px solid #8b5cf6;" +
                "box-shadow:" +
                "0 0 0 6px rgba(139,92,246,.10)," +
                "0 0 35px rgba(139,92,246,.35);" +
                "}"
        );

        html.append(
                ".sem-foto{" +
                "width:160px;" +
                "height:160px;" +
                "margin:0 auto;" +
                "border-radius:50%;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "background:linear-gradient(135deg,#251532,#160d20);" +
                "border:5px solid #8b5cf6;" +
                "color:#8f8496;" +
                "font-size:15px;" +
                "}"
        );

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                ".nome-area{" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "gap:14px;" +
                "flex-wrap:wrap;" +
                "margin-top:22px;" +
                "}"
        );

        html.append(
                ".nome-perfil{" +
                "font-size:38px;" +
                "font-weight:800;" +
                "margin:0;" +
                "letter-spacing:-1px;" +
                "}"
        );

        html.append(
                ".username-perfil{" +
                "color:#b98be8;" +
                "font-size:15px;" +
                "margin-top:8px;" +
                "}"
        );

        html.append(
                ".bio-perfil{" +
                "max-width:700px;" +
                "margin:18px auto 0;" +
                "font-size:15px;" +
                "line-height:1.7;" +
                "color:#bcb3c6;" +
                "}"
        );

        // =====================================================
        // MY LOVE
        // =====================================================

        html.append(
                ".emblema-my-love{" +
                "display:inline-flex;" +
                "align-items:center;" +
                "gap:7px;" +
                "padding:9px 16px;" +
                "border-radius:999px;" +
                "background:linear-gradient(135deg,#6d2b8c,#9333ea);" +
                "border:1px solid rgba(255,255,255,.3);" +
                "color:#fff;" +
                "font-size:13px;" +
                "font-weight:800;" +
                "box-shadow:0 5px 20px rgba(147,51,234,.25);" +
                "}"
        );

        html.append(
                ".coracao-my-love{" +
                "font-size:23px;" +
                "line-height:1;" +
                "}"
        );

        // =====================================================
        // ESTATISTICAS
        // =====================================================

        html.append(
                ".estatisticas{" +
                "display:flex;" +
                "justify-content:center;" +
                "gap:12px;" +
                "flex-wrap:wrap;" +
                "margin-top:28px;" +
                "}"
        );

        html.append(
                ".estatistica{" +
                "min-width:150px;" +
                "padding:18px 24px;" +
                "background:rgba(11,7,16,.55);" +
                "border:1px solid rgba(168,85,247,.16);" +
                "border-radius:16px;" +
                "backdrop-filter:blur(10px);" +
                "}"
        );

        html.append(
                ".estatistica strong{" +
                "display:block;" +
                "font-size:28px;" +
                "color:#c084fc;" +
                "margin-bottom:4px;" +
                "}"
        );

        html.append(
                ".estatistica span{" +
                "font-size:12px;" +
                "color:#8f8496;" +
                "text-transform:uppercase;" +
                "letter-spacing:.5px;" +
                "}"
        );

        // =====================================================
        // BOTAO SEGUIR
        // =====================================================

        html.append(
                ".botao-seguir{" +
                "margin-top:25px;" +
                "padding:13px 36px;" +
                "border:none;" +
                "border-radius:12px;" +
                "background:linear-gradient(135deg,#7c3aed,#a855f7);" +
                "color:#fff;" +
                "font-size:14px;" +
                "font-weight:800;" +
                "cursor:pointer;" +
                "box-shadow:0 10px 25px rgba(124,58,237,.25);" +
                "transition:.25s;" +
                "}"
        );

        html.append(
                ".botao-seguir:hover{" +
                "transform:translateY(-2px);" +
                "box-shadow:0 15px 30px rgba(124,58,237,.35);" +
                "}"
        );

        html.append(
                ".botao-seguindo{" +
                "background:rgba(124,58,237,.12);" +
                "border:1px solid #8b5cf6;" +
                "box-shadow:none;" +
                "}"
        );

        // =====================================================
        // GRID
        // =====================================================

        html.append(
                ".perfil-grid{" +
                "display:grid;" +
                "grid-template-columns:minmax(0,2fr) minmax(280px,.9fr);" +
                "gap:22px;" +
                "margin-top:22px;" +
                "align-items:start;" +
                "}"
        );

        // =====================================================
        // PAINEL
        // =====================================================

        html.append(
                ".painel{" +
                "background:rgba(18,12,27,.82);" +
                "border:1px solid rgba(168,85,247,.14);" +
                "border-radius:22px;" +
                "padding:22px;" +
                "box-shadow:0 18px 45px rgba(0,0,0,.25);" +
                "}"
        );

        // =====================================================
        // SECOES
        // =====================================================

        html.append(
                ".secao{" +
                "margin-top:22px;" +
                "}"
        );

        html.append(
                ".secao:first-child{" +
                "margin-top:0;" +
                "}"
        );

        html.append(
                ".titulo-secao{" +
                "display:flex;" +
                "align-items:center;" +
                "gap:10px;" +
                "font-size:24px;" +
                "margin:0;" +
                "font-weight:800;" +
                "}"
        );

        html.append(
                ".linha-roxa{" +
                "width:48px;" +
                "height:3px;" +
                "margin:10px 0 22px;" +
                "border-radius:999px;" +
                "background:linear-gradient(90deg,#7c3aed,#c084fc);" +
                "}"
        );

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append(
                ".favoritos-grid{" +
                "display:grid;" +
                "grid-template-columns:repeat(5,minmax(0,1fr));" +
                "gap:12px;" +
                "}"
        );

        html.append(
                ".favorito-card{" +
                "position:relative;" +
                "overflow:hidden;" +
                "background:#120d18;" +
                "border:1px solid rgba(168,85,247,.16);" +
                "border-radius:15px;" +
                "padding:7px;" +
                "transition:.25s;" +
                "}"
        );

        html.append(
                ".favorito-card:hover{" +
                "transform:translateY(-5px);" +
                "border-color:#8b5cf6;" +
                "box-shadow:0 12px 28px rgba(124,58,237,.22);" +
                "}"
        );

        html.append(
                ".favorito-capa{" +
                "width:100%;" +
                "height:225px;" +
                "object-fit:cover;" +
                "display:block;" +
                "border-radius:10px;" +
                "background:#24152f;" +
                "}"
        );

        html.append(
                ".favorito-titulo{" +
                "padding:10px 4px 5px;" +
                "font-size:13px;" +
                "font-weight:800;" +
                "line-height:1.3;" +
                "color:#f4eff7;" +
                "}"
        );

        // =====================================================
        // LISTAS
        // =====================================================

        html.append(
                ".lista-perfil{" +
                "background:#120d18;" +
                "border:1px solid rgba(168,85,247,.14);" +
                "border-radius:17px;" +
                "padding:18px;" +
                "margin-bottom:15px;" +
                "}"
        );

        html.append(
                ".lista-perfil:hover{" +
                "border-color:rgba(139,92,246,.45);" +
                "}"
        );

        html.append(
                ".nome-lista-perfil{" +
                "font-size:19px;" +
                "font-weight:800;" +
                "margin-bottom:15px;" +
                "color:#f5eff9;" +
                "}"
        );

        html.append(
                ".jogos-lista-perfil{" +
                "display:grid;" +
                "grid-template-columns:repeat(auto-fill,minmax(120px,1fr));" +
                "gap:12px;" +
                "}"
        );

        html.append(
                ".jogo-lista-perfil{" +
                "background:#18101f;" +
                "border:1px solid rgba(168,85,247,.10);" +
                "border-radius:12px;" +
                "padding:7px;" +
                "transition:.2s;" +
                "}"
        );

        html.append(
                ".jogo-lista-perfil:hover{" +
                "transform:translateY(-3px);" +
                "border-color:#7c3aed;" +
                "}"
        );

        html.append(
                ".capa-lista-perfil{" +
                "width:100%;" +
                "height:165px;" +
                "object-fit:cover;" +
                "display:block;" +
                "border-radius:8px;" +
                "background:#24152f;" +
                "}"
        );

        html.append(
                ".nome-jogo-lista{" +
                "font-size:12px;" +
                "font-weight:700;" +
                "line-height:1.35;" +
                "margin-top:8px;" +
                "color:#eee7f2;" +
                "}"
        );

        // =====================================================
        // USUARIOS
        // =====================================================

        html.append(
                ".lista-usuarios{" +
                "display:grid;" +
                "grid-template-columns:1fr;" +
                "gap:10px;" +
                "}"
        );

        html.append(
                ".card-usuario{" +
                "display:flex;" +
                "align-items:center;" +
                "gap:13px;" +
                "padding:12px;" +
                "background:#120d18;" +
                "border:1px solid rgba(168,85,247,.12);" +
                "border-radius:14px;" +
                "text-decoration:none;" +
                "color:#fff;" +
                "transition:.2s;" +
                "}"
        );

        html.append(
                ".card-usuario:hover{" +
                "transform:translateX(3px);" +
                "border-color:#8b5cf6;" +
                "background:#181020;" +
                "}"
        );

        html.append(
                ".mini-foto{" +
                "width:50px;" +
                "height:50px;" +
                "border-radius:50%;" +
                "object-fit:cover;" +
                "border:2px solid #7c3aed;" +
                "}"
        );

        html.append(
                ".mini-sem-foto{" +
                "width:50px;" +
                "height:50px;" +
                "border-radius:50%;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "background:#261630;" +
                "color:#7e7085;" +
                "font-size:11px;" +
                "}"
        );

        html.append(
                ".info-usuario strong{" +
                "display:block;" +
                "font-size:14px;" +
                "margin-bottom:4px;" +
                "}"
        );

        html.append(
                ".info-usuario span{" +
                "font-size:12px;" +
                "color:#b98be8;" +
                "}"
        );

        // =====================================================
        // AVALIACOES
        // =====================================================

        html.append(
                ".avaliacoes-perfil-grid{" +
                "display:grid;" +
                "grid-template-columns:1fr;" +
                "gap:15px;" +
                "}"
        );

        html.append(
                ".avaliacao-perfil-card{" +
                "display:flex;" +
                "gap:16px;" +
                "padding:15px;" +
                "background:#120d18;" +
                "border:1px solid rgba(168,85,247,.14);" +
                "border-radius:16px;" +
                "transition:.2s;" +
                "}"
        );

        html.append(
                ".avaliacao-perfil-card:hover{" +
                "border-color:#8b5cf6;" +
                "transform:translateY(-2px);" +
                "}"
        );

        html.append(
                ".avaliacao-perfil-capa{" +
                "width:85px;" +
                "height:120px;" +
                "object-fit:cover;" +
                "border-radius:8px;" +
                "display:block;" +
                "flex-shrink:0;" +
                "background:#24152f;" +
                "}"
        );

        html.append(
                ".avaliacao-perfil-texto{" +
                "flex:1;" +
                "min-width:0;" +
                "}"
        );

        html.append(
                ".avaliacao-perfil-texto h3{" +
                "margin:0 0 8px;" +
                "font-size:17px;" +
                "color:#fff;" +
                "}"
        );

        html.append(
                ".estrelas-avaliacao{" +
                "color:#ffd700;" +
                "font-size:20px;" +
                "letter-spacing:2px;" +
                "margin-bottom:6px;" +
                "}"
        );

        html.append(
                ".nota-avaliacao{" +
                "color:#c084fc;" +
                "font-size:13px;" +
                "font-weight:bold;" +
                "}"
        );

        html.append(
                ".horas-avaliacao{" +
                "color:#a99cad;" +
                "font-size:12px;" +
                "margin:7px 0;" +
                "}"
        );

        html.append(
                ".resenha-avaliacao{" +
                "color:#c8bfce;" +
                "font-size:14px;" +
                "line-height:1.5;" +
                "}"
        );

        // =====================================================
        // VAZIO
        // =====================================================

        html.append(
                ".vazio{" +
                "padding:28px 18px;" +
                "background:#120d18;" +
                "border:1px dashed rgba(168,85,247,.2);" +
                "border-radius:14px;" +
                "color:#857b8b;" +
                "text-align:center;" +
                "font-size:14px;" +
                "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:1000px){" +
                ".perfil-grid{" +
                "grid-template-columns:1fr;" +
                "}" +
                "}"
        );

        html.append(
                "@media(max-width:800px){" +
                ".favoritos-grid{" +
                "grid-template-columns:repeat(3,minmax(0,1fr));" +
                "}" +
                ".favorito-capa{" +
                "height:220px;" +
                "}" +
                "}"
        );

        html.append(
                "@media(max-width:600px){" +
                ".perfil-container{" +
                "padding:20px 12px 50px;" +
                "}" +
                ".perfil-hero{" +
                "padding:30px 18px;" +
                "border-radius:20px;" +
                "}" +
                ".foto-perfil,.sem-foto{" +
                "width:125px;" +
                "height:125px;" +
                "}" +
                ".nome-perfil{" +
                "font-size:28px;" +
                "}" +
                ".painel{" +
                "padding:16px;" +
                "border-radius:18px;" +
                "}" +
                ".favoritos-grid{" +
                "grid-template-columns:repeat(2,minmax(0,1fr));" +
                "}" +
                ".favorito-capa{" +
                "height:210px;" +
                "}" +
                ".jogos-lista-perfil{" +
                "grid-template-columns:repeat(2,minmax(0,1fr));" +
                "}" +
                ".capa-lista-perfil{" +
                "height:170px;" +
                "}" +
                ".estatistica{" +
                "min-width:120px;" +
                "padding:14px 18px;" +
                "}" +
                "}"
        );

        html.append("</style>");
        
        html.append("<script>");
        html.append("function tentarOutraCapa(img){" +
                "var src=img.getAttribute('src')||'';" +
                "var match=src.match(/steam\\/apps\\/(\\d+)/);" +
                "if(!match){img.style.display='none';return;}" +
                "var id=match[1];" +
                "var tentativas=parseInt(img.getAttribute('data-tentativas')||'0',10);" +
                "var urls=[" +
                "'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/'+id+'/library_600x900_2x.jpg'," +
                "'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/'+id+'/library_600x900.jpg'," +
                "'https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/'+id+'/header.jpg'," +
                "'https://cdn.akamai.steamstatic.com/steam/apps/'+id+'/library_600x900_2x.jpg'," +
                "'https://cdn.akamai.steamstatic.com/steam/apps/'+id+'/library_600x900.jpg'," +
                "'https://cdn.akamai.steamstatic.com/steam/apps/'+id+'/header.jpg'" +
                "];" +
                "if(tentativas<urls.length){img.setAttribute('data-tentativas',tentativas+1);img.src=urls[tentativas];}else{img.style.display='none';}" +
                "}");
        html.append("</script>");

        html.append("</head>");
        html.append("<body>");

        // =====================================================
        // HEADER
        // =====================================================

        html.append("<header>");

        html.append(
                "<h1>Inventory</h1>"
        );

        html.append("<nav>");

        html.append(
                "<a href='index.html'>Início</a>"
        );

        html.append(
                "<a href='buscar-usuarios'>" +
                "Buscar usuários" +
                "</a>"
        );

        html.append(
                "<a href='jogos'>Jogos</a>"
        );

        html.append(
                "<a href='perfil'>Meu Perfil</a>"
        );

        html.append(
                "<a href='biblioteca'>Biblioteca</a>"
        );

        html.append(
                "<a href='listas'>Listas</a>"
        );

        html.append(
                "<a href='logout'>Sair</a>"
        );

        html.append("</nav>");
        html.append("</header>");

        // =====================================================
        // MAIN
        // =====================================================

        html.append(
                "<main class='perfil-container'>"
        );

        // =====================================================
        // HERO
        // =====================================================

        html.append(
                "<section class='perfil-hero'>"
        );

        html.append(
                "<div class='perfil-topo'>"
        );

        // =====================================================
        // FOTO
        // =====================================================

        String foto =
                prepararFoto(
                        request,
                        perfil.getFoto()
                );

        if (foto != null) {

            html.append(
                    "<img " +
                    "class='foto-perfil' " +
                    "src='" +
                    escapar(foto) +
                    "' " +
                    "alt='Foto de " +
                    escapar(perfil.getNome()) +
                    "'>"
            );

        } else {

            html.append(
                    "<div class='sem-foto'>" +
                    "Sem foto" +
                    "</div>"
            );
        }

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                "<div class='nome-area'>"
        );

        html.append(
                "<h2 class='nome-perfil'>" +
                escapar(perfil.getNome()) +
                "</h2>"
        );

        if (jogadorEspecial) {

            html.append(
                    "<span class='emblema-my-love'>" +
                    "<span class='coracao-my-love'>♡</span>" +
                    " My Love" +
                    "</span>"
            );
        }

        html.append("</div>");

        // =====================================================
        // USERNAME
        // =====================================================

        String username =
                perfil.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            username =
                    "usuario" +
                    perfil.getId();
        }

        html.append(
                "<div class='username-perfil'>" +
                "@" +
                escapar(username) +
                "</div>"
        );

        // =====================================================
        // BIO
        // =====================================================

        if (perfil.getBio() != null &&
                !perfil.getBio().trim().isEmpty()) {

            html.append(
                    "<div class='bio-perfil'>" +
                    escapar(perfil.getBio()) +
                    "</div>"
            );
        }

        // =====================================================
        // ESTATISTICAS
        // =====================================================

        html.append(
                "<div class='estatisticas'>"
        );

        html.append(
                "<div class='estatistica'>" +
                "<strong>" +
                totalSeguidores +
                "</strong>" +
                "<span>Seguidores</span>" +
                "</div>"
        );

        html.append(
                "<div class='estatistica'>" +
                "<strong>" +
                totalSeguindo +
                "</strong>" +
                "<span>Seguindo</span>" +
                "</div>"
        );

        html.append("</div>");

        // =====================================================
        // BOTAO SEGUIR
        // =====================================================

        if (!mesmoUsuario) {

            html.append(
                    "<form " +
                    "method='POST' " +
                    "action='/seguir'>"
            );

            html.append(
                    "<input " +
                    "type='hidden' " +
                    "name='idUsuario' " +
                    "value='" +
                    idPerfil +
                    "'>"
            );

            if (seguindo) {

                html.append(
                        "<input " +
                        "type='hidden' " +
                        "name='acao' " +
                        "value='deixar'>"
                );

                html.append(
                        "<button " +
                        "class='botao-seguir botao-seguindo' " +
                        "type='submit'>" +
                        "✓ Seguindo" +
                        "</button>"
                );

            } else {

                html.append(
                        "<input " +
                        "type='hidden' " +
                        "name='acao' " +
                        "value='seguir'>"
                );

                html.append(
                        "<button " +
                        "class='botao-seguir' " +
                        "type='submit'>" +
                        "+ Seguir" +
                        "</button>"
                );
            }

            html.append("</form>");
        }

        html.append("</div>");
        html.append("</section>");

        // =====================================================
        // GRID
        // =====================================================

        html.append(
                "<div class='perfil-grid'>"
        );

        // =====================================================
        // ESQUERDA
        // =====================================================

        html.append(
                "<div class='painel'>"
        );

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>" +
                "❤️ Favoritos" +
                "</h2>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        if (favoritos.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Este usuário ainda não selecionou favoritos." +
                    "</div>"
            );

        } else {

            html.append(
                    "<div class='favoritos-grid'>"
            );

            for (String[] favorito :
                    favoritos) {

                String titulo =
                        favorito[1];

                String capa =
                        favorito[3];

                String caminhoCapa =
                        prepararCapa(
                                request,
                                capa
                        );

                html.append(
                        "<div class='favorito-card'>"
                );

                if (caminhoCapa != null) {

                    html.append(
                            "<img " +
                            "class='favorito-capa' " +
                            "src='" +
                            escapar(caminhoCapa) +
                            "' " +
                            "alt='Capa de " +
                            escapar(titulo) +
                            "' " +
                            "onerror=\"this.style.display='none';" +
                            "this.nextElementSibling.style.display='flex';\">"
                    );

                    html.append(
                            "<div " +
                            "class='vazio' " +
                            "style='display:none;" +
                            "min-height:225px;" +
                            "align-items:center;" +
                            "justify-content:center;" +
                            "padding:12px;'>" +
                            escapar(titulo) +
                            "</div>"
                    );

                } else {

                    html.append(
                            "<div " +
                            "class='vazio' " +
                            "style='min-height:225px;" +
                            "display:flex;" +
                            "align-items:center;" +
                            "justify-content:center;" +
                            "padding:12px;'>" +
                            escapar(titulo) +
                            "</div>"
                    );
                }

                html.append(
                        "<div class='favorito-titulo'>" +
                        escapar(titulo) +
                        "</div>"
                );

                html.append("</div>");
            }

            html.append("</div>");
        }

        html.append("</section>");

        // =====================================================
        // AVALIACOES DO USUARIO PESQUISADO
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>" +
                "⭐ Avaliações" +
                "</h2>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        if (avaliacoes.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Este usuário ainda não fez nenhuma avaliação." +
                    "</div>"
            );

        } else {

            html.append(
                    "<div class='avaliacoes-perfil-grid'>"
            );

            for (String[] avaliacao :
                    avaliacoes) {

                String titulo =
                        avaliacao[1];

                String capa =
                        avaliacao[2];

                String notaTexto =
                        avaliacao[3];

                String comentario =
                        avaliacao[4];

                String horas =
                        avaliacao[5];

                String caminhoCapa =
                        prepararCapa(
                                request,
                                capa
                        );

                double nota = 0;

                try {

                    nota =
                            Double.parseDouble(
                                    notaTexto
                            );

                } catch (Exception e) {

                    nota = 0;
                }

                int estrelas =
                        (int) Math.round(nota);

                if (estrelas < 0) {
                    estrelas = 0;
                }

                if (estrelas > 5) {
                    estrelas = 5;
                }

                html.append(
                        "<div class='avaliacao-perfil-card'>"
                );

                // =================================================
                // CAPA
                // =================================================

                if (caminhoCapa != null &&
                        !caminhoCapa.trim().isEmpty()) {

                    html.append(
                            "<img " +
                            "class='avaliacao-perfil-capa' " +
                            "src='" +
                            escapar(caminhoCapa) +
                            "' " +
                            "alt='Capa de " +
                            escapar(titulo) +
                            "' " +
                            "onerror=\"tentarOutraCapa(this);\"" +
                            ">"
                    );

                } else {

                    html.append(
                            "<div class='avaliacao-perfil-capa' " +
                            "style='display:flex;" +
                            "align-items:center;" +
                            "justify-content:center;" +
                            "color:#83758c;" +
                            "text-align:center;" +
                            "padding:8px;'>" +
                            escapar(titulo) +
                            "</div>"
                    );
                }

                // =================================================
                // INFORMACOES
                // =================================================

                html.append(
                        "<div class='avaliacao-perfil-texto'>"
                );

                html.append(
                        "<h3>" +
                        escapar(titulo) +
                        "</h3>"
                );

                html.append(
                        "<div class='estrelas-avaliacao'>"
                );

                for (int i = 1; i <= 5; i++) {

                    if (i <= estrelas) {

                        html.append("★");

                    } else {

                        html.append("☆");
                    }
                }

                html.append(
                        "</div>"
                );

                html.append(
                        "<div class='nota-avaliacao'>" +
                        "Nota: " +
                        nota +
                        "/5" +
                        "</div>"
                );

                if (horas != null &&
                        !horas.trim().isEmpty() &&
                        !horas.equals("0.0")) {

                    html.append(
                            "<div class='horas-avaliacao'>" +
                            "⏱️ " +
                            escapar(horas) +
                            " horas jogadas" +
                            "</div>"
                    );
                }

                if (comentario != null &&
                        !comentario.trim().isEmpty()) {

                    html.append(
                            "<div class='resenha-avaliacao'>" +
                            escapar(comentario) +
                            "</div>"
                    );
                }

                if (mesmoUsuario) {

                    html.append(
                            "<div style='margin-top:12px;'>" +
                            "<a class='botao-avaliar' " +
                            "href='avaliar?id=" +
                            avaliacao[0] +
                            "'>" +
                            "✏️ Editar avaliação" +
                            "</a>" +
                            "</div>"
                    );
                }

                html.append(
                        "</div>"
                );

                html.append(
                        "</div>"
                );
            }

            html.append(
                    "</div>"
            );
        }

        html.append(
                "</section>"
        );

        // =====================================================
        // LISTAS
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>" +
                "📚 Listas" +
                "</h2>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        if (listas.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Este usuário ainda não criou nenhuma lista." +
                    "</div>"
            );

        } else {

            for (String[] lista :
                    listas) {

                int idLista =
                        Integer.parseInt(
                                lista[0]
                        );

                String nomeLista =
                        lista[1];

                ArrayList<String[]> jogos =
                        carregarJogosLista(
                                idLista
                        );

                html.append(
                        "<div class='lista-perfil'>"
                );

                html.append(
                        "<div class='nome-lista-perfil'>" +
                        "📋 " +
                        escapar(nomeLista) +
                        "</div>"
                );

                if (jogos.isEmpty()) {

                    html.append(
                            "<div class='vazio'>" +
                            "Esta lista ainda não possui jogos." +
                            "</div>"
                    );

                } else {

                    html.append(
                            "<div class='jogos-lista-perfil'>"
                    );

                    for (String[] jogo :
                            jogos) {

                        String titulo =
                                jogo[1];

                        String capa =
                                jogo[2];

                        String caminhoCapa =
                                prepararCapa(
                                        request,
                                        capa
                                );

                        html.append(
                                "<div class='jogo-lista-perfil'>"
                        );

                        if (caminhoCapa != null) {

                            html.append(
                                    "<img " +
                                    "class='capa-lista-perfil' " +
                                    "src='" +
                                    escapar(caminhoCapa) +
                                    "' " +
                                    "alt='Capa de " +
                                    escapar(titulo) +
                                    "' onerror=\"tentarOutraCapa(this);\">"
                            );

                        } else {

                            html.append(
                                    "<div " +
                                    "class='capa-lista-perfil' " +
                                    "style='display:flex;" +
                                    "align-items:center;" +
                                    "justify-content:center;" +
                                    "color:#777;" +
                                    "text-align:center;" +
                                    "padding:10px;'>" +
                                    escapar(titulo) +
                                    "</div>"
                            );
                        }

                        html.append(
                                "<div class='nome-jogo-lista'>" +
                                escapar(titulo) +
                                "</div>"
                        );

                        html.append("</div>");
                    }

                    html.append("</div>");
                }

                html.append("</div>");
            }
        }

        html.append("</section>");

        html.append("</div>");

        // =====================================================
        // DIREITA
        // =====================================================

        html.append("<div>");

        // =====================================================
        // SEGUIDORES
        // =====================================================

        html.append(
                "<div class='painel'>"
        );

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>" +
                "👥 Seguidores" +
                "</h2>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        if (seguidores.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Nenhum seguidor ainda." +
                    "</div>"
            );

        } else {

            html.append(
                    "<div class='lista-usuarios'>"
            );

            for (Usuario u :
                    seguidores) {

                html.append(
                        criarCardUsuario(
                                request,
                                u
                        )
                );
            }

            html.append("</div>");
        }

        html.append("</section>");
        html.append("</div>");

        // =====================================================
        // SEGUINDO
        // =====================================================

        html.append(
                "<div class='painel' " +
                "style='margin-top:22px;'>"
        );

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>" +
                "➕ Seguindo" +
                "</h2>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        if (seguindoLista.isEmpty()) {

            html.append(
                    "<div class='vazio'>" +
                    "Não segue ninguém ainda." +
                    "</div>"
            );

        } else {

            html.append(
                    "<div class='lista-usuarios'>"
            );

            for (Usuario u :
                    seguindoLista) {

                html.append(
                        criarCardUsuario(
                                request,
                                u
                        )
                );
            }

            html.append("</div>");
        }

        html.append("</section>");
        html.append("</div>");

        html.append("</div>");

        // =====================================================
        // FECHAR GRID
        // =====================================================

        html.append("</div>");

        // =====================================================
        // FECHAR MAIN
        // =====================================================

        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // CARREGAR AVALIACOES DO PERFIL
    // =====================================================

    private ArrayList<String[]> carregarAvaliacoes(
            int idUsuario) {

        ArrayList<String[]> avaliacoes =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT " +
                    "j.id, " +
                    "j.titulo, " +
                    "j.capa, " +
                    "a.nota, " +
                    "a.comentario, " +
                    "a.horas_jogadas, " +
                    "a.data_avaliacao " +
                    "FROM avaliacao a " +
                    "INNER JOIN jogo j " +
                    "ON j.id = a.id_jogo " +
                    "WHERE a.id_usuario = ? " +
                    "ORDER BY a.data_avaliacao DESC";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                avaliacoes.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("titulo"),
                            rs.getString("capa"),
                            String.valueOf(
                                    rs.getDouble("nota")
                            ),
                            rs.getString("comentario"),
                            String.valueOf(
                                    rs.getDouble("horas_jogadas")
                            ),
                            rs.getString("data_avaliacao")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
            }

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {
            }
        }

        return avaliacoes;
    }

    // =====================================================
    // CARREGAR FAVORITOS
    // =====================================================

    private ArrayList<String[]> carregarFavoritos(
            int idUsuario) {

        ArrayList<String[]> favoritos =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT " +
                    "j.id, " +
                    "j.titulo, " +
                    "j.genero, " +
                    "j.capa " +
                    "FROM favorito f " +
                    "INNER JOIN jogo j " +
                    "ON j.id = f.id_jogo " +
                    "WHERE f.id_usuario = ? " +
                    "ORDER BY f.data_adicionado ASC " +
                    "LIMIT 5";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                favoritos.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("titulo"),
                            rs.getString("genero"),
                            rs.getString("capa")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception e) {
            }
        }

        return favoritos;
    }

    // =====================================================
    // CARREGAR LISTAS
    // =====================================================

    private ArrayList<String[]> carregarListas(
            int idUsuario) {

        ArrayList<String[]> listas =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT " +
                    "id, " +
                    "nome " +
                    "FROM lista " +
                    "WHERE id_usuario = ? " +
                    "ORDER BY id DESC";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                listas.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("nome")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception e) {
            }
        }

        return listas;
    }

    // =====================================================
    // CARREGAR JOGOS DA LISTA
    // =====================================================

    private ArrayList<String[]> carregarJogosLista(
            int idLista) {

        ArrayList<String[]> jogos =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT " +
                    "j.id, " +
                    "j.titulo, " +
                    "j.capa " +
                    "FROM lista_jogo lj " +
                    "INNER JOIN jogo j " +
                    "ON j.id = lj.id_jogo " +
                    "WHERE lj.id_lista = ? " +
                    "ORDER BY lj.id ASC";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    idLista
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                jogos.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("titulo"),
                            rs.getString("capa")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception e) {
            }
        }

        return jogos;
    }

    // =====================================================
    // PREPARAR FOTO
    // =====================================================

    private String prepararFoto(
            HttpServletRequest request,
            String foto)
            throws IOException {

        if (foto == null ||
                foto.trim().isEmpty()) {

            return null;
        }

        String caminho =
                foto.trim();

        if (caminho.startsWith("http://") ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        while (caminho.startsWith("/")) {

            caminho =
                    caminho.substring(1);
        }

        return
                request.getContextPath() +
                "/foto-perfil?arquivo=" +
                URLEncoder.encode(
                        caminho,
                        "UTF-8"
                );
    }

    // =====================================================
    // PREPARAR CAPA
    // =====================================================

    private String prepararCapa(
            HttpServletRequest request,
            String capa) {

        if (capa == null ||
                capa.trim().isEmpty()) {

            return null;
        }

        String caminho =
                capa.trim();

        // =================================================
        // MARKDOWN
        // =================================================

        if (caminho.startsWith("[") &&
                caminho.contains("](") &&
                caminho.endsWith(")")) {

            int inicio =
                    caminho.indexOf("](");

            caminho =
                    caminho.substring(
                            inicio + 2,
                            caminho.length() - 1
                    ).trim();
        }

        // =================================================
        // APP ID DIRETO
        // =================================================

        if (caminho.matches("\\d+")) {

            return
                    "https://shared.cloudflare.steamstatic.com/" + "store_item_assets/steam/apps/" +
                    caminho +
                    "/library_600x900_2x.jpg";
        }

        // =================================================
        // APP ID DENTRO DA URL
        // =================================================

        Pattern pattern =
                Pattern.compile(
                        "/apps/(\\d+)"
                );

        Matcher matcher =
                pattern.matcher(caminho);

        if (matcher.find()) {

            String appId =
                    matcher.group(1);

            return
                    "https://shared.cloudflare.steamstatic.com/" + "store_item_assets/steam/apps/" +
                    appId +
                    "/library_600x900_2x.jpg";
        }

        // =================================================
        // URL COMPLETA
        // =================================================

        if (caminho.startsWith("http://") ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        // =================================================
        // CAMINHO LOCAL
        // =================================================

        while (caminho.startsWith("/")) {

            caminho =
                    caminho.substring(1);
        }

        return
                request.getContextPath() +
                "/" +
                caminho;
    }

    // =====================================================
    // CARD DE USUARIO
    // =====================================================

    private String criarCardUsuario(
            HttpServletRequest request,
            Usuario usuario)
            throws IOException {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<a " +
                "class='card-usuario' " +
                "href='perfil-usuario?id=" +
                usuario.getId() +
                "'>"
        );

        String foto =
                prepararFoto(
                        request,
                        usuario.getFoto()
                );

        if (foto != null) {

            html.append(
                    "<img " +
                    "class='mini-foto' " +
                    "src='" +
                    escapar(foto) +
                    "' " +
                    "alt='Foto de " +
                    escapar(usuario.getNome()) +
                    "'>"
            );

        } else {

            html.append(
                    "<div class='mini-sem-foto'>" +
                    "👤" +
                    "</div>"
            );
        }

        String username =
                usuario.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            username =
                    "usuario" +
                    usuario.getId();
        }

        html.append(
                "<div class='info-usuario'>"
        );

        html.append(
                "<strong>" +
                escapar(usuario.getNome()) +
                "</strong>"
        );

        html.append(
                "<span>@" +
                escapar(username) +
                "</span>"
        );
        html.append(
        "<link rel='icon' " +
        "type='image/png' " +
        "href='favicon.png'>"
);

        html.append(
                "</div>"
        );

        html.append(
                "</a>"
        );

        return html.toString();
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