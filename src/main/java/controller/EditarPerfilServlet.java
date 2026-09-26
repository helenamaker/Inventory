package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/editar-perfil")
public class EditarPerfilServlet extends HttpServlet {

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

            Usuario usuarioSessao =
                    (Usuario) sessao.getAttribute("usuario");

            UsuarioDAO dao =
                    new UsuarioDAO();

            Usuario usuario =
                    dao.buscarPorId(
                            usuarioSessao.getId()
                    );

            if (usuario == null) {

                response.sendRedirect("login.html");
                return;
            }

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
                    "content='width=device-width, initial-scale=1.0'>"
            );

            html.append(
                    "<link rel='icon' " +
                    "type='image/png' " +
                    "href='icon.png'>"
            );

            html.append(
                    "<title>Editar Perfil - Inventory</title>"
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
                    "background:#14101b;" +
                    "color:#fff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}"
            );

            html.append(
                    ".editar-page{" +
                    "max-width:700px;" +
                    "margin:0 auto;" +
                    "padding:45px 20px 70px;" +
                    "}"
            );

            html.append(
                    ".editar-card{" +
                    "background:linear-gradient(135deg,#24102f,#14101b);" +
                    "border:1px solid #3e2849;" +
                    "border-radius:18px;" +
                    "padding:30px;" +
                    "box-shadow:0 12px 35px rgba(0,0,0,.35);" +
                    "}"
            );

            html.append(
                    ".editar-titulo{" +
                    "font-size:28px;" +
                    "margin:0;" +
                    "}"
            );

            html.append(
                    ".editar-subtitulo{" +
                    "color:#999;" +
                    "margin:8px 0 28px;" +
                    "}"
            );

            html.append(
                    ".campo{" +
                    "margin-bottom:19px;" +
                    "}"
            );

            html.append(
                    ".campo label{" +
                    "display:block;" +
                    "margin-bottom:7px;" +
                    "font-weight:bold;" +
                    "font-size:14px;" +
                    "}"
            );

            html.append(
                    ".campo input," +
                    ".campo textarea," +
                    ".campo select{" +
                    "width:100%;" +
                    "padding:12px 13px;" +
                    "border:1px solid #4b315d;" +
                    "border-radius:9px;" +
                    "background:#1c1424;" +
                    "color:#fff;" +
                    "font-size:14px;" +
                    "outline:none;" +
                    "}"
            );

            html.append(
                    ".campo input:focus," +
                    ".campo textarea:focus," +
                    ".campo select:focus{" +
                    "border-color:#8b35d6;" +
                    "box-shadow:0 0 0 2px rgba(139,53,214,.15);" +
                    "}"
            );

            html.append(
                    ".campo textarea{" +
                    "min-height:110px;" +
                    "resize:vertical;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}"
            );

            html.append(
                    ".foto-atual{" +
                    "width:100px;" +
                    "height:100px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "border:3px solid #7300d1;" +
                    "margin-bottom:12px;" +
                    "}"
            );

            html.append(
                    ".botoes{" +
                    "display:flex;" +
                    "gap:10px;" +
                    "margin-top:25px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    ".btn-salvar{" +
                    "border:none;" +
                    "background:#6300c0;" +
                    "color:#fff;" +
                    "padding:12px 20px;" +
                    "border-radius:9px;" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "cursor:pointer;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".btn-salvar:hover{" +
                    "background:#8300ed;" +
                    "transform:translateY(-1px);" +
                    "}"
            );

            html.append(
                    ".btn-voltar{" +
                    "display:inline-block;" +
                    "padding:12px 20px;" +
                    "border-radius:9px;" +
                    "border:1px solid #59366d;" +
                    "color:#ddd;" +
                    "text-decoration:none;" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".btn-voltar:hover{" +
                    "background:#24152f;" +
                    "}"
            );

            html.append(
                    ".mensagem{" +
                    "background:#301a3d;" +
                    "border:1px solid #713a91;" +
                    "color:#e8cfff;" +
                    "padding:11px 14px;" +
                    "border-radius:8px;" +
                    "margin-bottom:20px;" +
                    "font-size:13px;" +
                    "}"
            );

            html.append(
                    "@media(max-width:600px){" +
                    ".editar-page{" +
                    "padding:25px 12px;" +
                    "}" +
                    ".editar-card{" +
                    "padding:22px;" +
                    "}" +
                    ".botoes{" +
                    "flex-direction:column;" +
                    "}" +
                    ".btn-salvar," +
                    ".btn-voltar{" +
                    "width:100%;" +
                    "text-align:center;" +
                    "}" +
                    "}"
            );

            html.append("</style>");

            html.append("</head>");

            // =====================================================
            // BODY
            // =====================================================

            html.append("<body>");

            html.append(
                    "<main class='editar-page'>"
            );

            html.append(
                    "<section class='editar-card'>"
            );

            html.append(
                    "<h1 class='editar-titulo'>" +
                    "✏️ Editar perfil" +
                    "</h1>"
            );

            html.append(
                    "<p class='editar-subtitulo'>" +
                    "Altere as informações do seu perfil." +
                    "</p>"
            );

            // =====================================================
            // MENSAGEM
            // =====================================================

            String mensagem =
                    request.getParameter("mensagem");

            if ("sucesso".equals(mensagem)) {

                html.append(
                        "<div class='mensagem'>" +
                        "Perfil atualizado com sucesso!" +
                        "</div>"
                );
            }

            // =====================================================
            // FORMULÁRIO
            // =====================================================

            html.append(
                    "<form method='POST' " +
                    "action='editar-perfil'>"
            );

            // =====================================================
            // NOME
            // =====================================================

            html.append(
                    "<div class='campo'>"
            );

            html.append(
                    "<label for='nome'>Nome</label>"
            );

            html.append(
                    "<input type='text' " +
                    "id='nome' " +
                    "name='nome' " +
                    "value='" +
                    escaparHtml(usuario.getNome()) +
                    "' " +
                    "required>"
            );

            html.append("</div>");

            // =====================================================
            // USERNAME
            // =====================================================

            html.append(
                    "<div class='campo'>"
            );

            html.append(
                    "<label for='username'>Username</label>"
            );

            html.append(
                    "<input type='text' " +
                    "id='username' " +
                    "name='username' " +
                    "value='" +
                    escaparHtml(usuario.getUsername()) +
                    "' " +
                    "required>"
            );

            html.append("</div>");

            // =====================================================
            // BIO
            // =====================================================

            html.append(
                    "<div class='campo'>"
            );

            html.append(
                    "<label for='bio'>Bio</label>"
            );

            html.append(
                    "<textarea " +
                    "id='bio' " +
                    "name='bio' " +
                    "maxlength='300'>" +
                    escaparHtml(
                            usuario.getBio()
                    ) +
                    "</textarea>"
            );

            html.append("</div>");

            // =====================================================
            // PAÍS
            // =====================================================

            html.append(
                    "<div class='campo'>"
            );

            html.append(
                    "<label for='pais'>País</label>"
            );

            html.append(
                    "<input type='text' " +
                    "id='pais' " +
                    "name='pais' " +
                    "value='" +
                    escaparHtml(usuario.getPais()) +
                    "' " +
                    "placeholder='Ex.: Brasil'>"
            );

            html.append("</div>");

            // =====================================================
            // PLATAFORMA FAVORITA
            // =====================================================

            html.append(
                    "<div class='campo'>"
            );

            html.append(
                    "<label for='plataformaFavorita'>" +
                    "Plataforma favorita" +
                    "</label>"
            );

            html.append(
                    "<select " +
                    "id='plataformaFavorita' " +
                    "name='plataformaFavorita'>"
            );

            String plataforma =
                    usuario.getPlataformaFavorita();

            html.append(
                    "<option value=''>Selecione</option>"
            );

            html.append(
                    "<option value='PC' " +
                    selecionar(plataforma, "PC") +
                    ">PC</option>"
            );

            html.append(
                    "<option value='PlayStation' " +
                    selecionar(
                            plataforma,
                            "PlayStation"
                    ) +
                    ">PlayStation</option>"
            );

            html.append(
                    "<option value='Xbox' " +
                    selecionar(plataforma, "Xbox") +
                    ">Xbox</option>"
            );

            html.append(
                    "<option value='Nintendo Switch' " +
                    selecionar(
                            plataforma,
                            "Nintendo Switch"
                    ) +
                    ">Nintendo Switch</option>"
            );

            html.append(
                    "<option value='Mobile' " +
                    selecionar(
                            plataforma,
                            "Mobile"
                    ) +
                    ">Mobile</option>"
            );

            html.append("</select>");

            html.append("</div>");

            // =====================================================
            // FOTO
            // =====================================================

            html.append(
                    "<div class='campo'>"
            );

            html.append(
                    "<label for='foto'>" +
                    "Foto de perfil" +
                    "</label>"
            );

            String foto =
                    usuario.getFoto();

            if (foto != null &&
                    !foto.trim().isEmpty()) {

                String fotoUrl =
                        prepararFoto(
                                foto,
                                request
                        );

                if (!fotoUrl.isEmpty()) {

                    html.append(
                            "<img class='foto-atual' " +
                            "src='" +
                            escaparHtml(fotoUrl) +
                            "' " +
                            "alt='Foto atual'>"
                    );
                }
            }

            html.append(
                    "<input type='text' " +
                    "id='foto' " +
                    "name='foto' " +
                    "value='" +
                    escaparHtml(foto) +
                    "' " +
                    "placeholder='URL da imagem'>"
            );

            html.append("</div>");

            // =====================================================
            // BOTÕES
            // =====================================================

            html.append(
                    "<div class='botoes'>"
            );

            html.append(
                    "<button type='submit' " +
                    "class='btn-salvar'>" +
                    "💾 Salvar alterações" +
                    "</button>"
            );

            html.append(
                    "<a href='perfil' " +
                    "class='btn-voltar'>" +
                    "Cancelar" +
                    "</a>"
            );

            html.append("</div>");

            html.append("</form>");

            html.append("</section>");

            html.append("</main>");

            html.append("</body>");

            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "perfil"
            );
        }
    }

    // =========================================================
    // SALVAR
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        try {

            Usuario usuarioSessao =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuarioSessao.getId();

            String nome =
                    limpar(
                            request.getParameter("nome")
                    );

            String username =
                    limpar(
                            request.getParameter("username")
                    );

            String bio =
                    limpar(
                            request.getParameter("bio")
                    );

            String pais =
                    limpar(
                            request.getParameter("pais")
                    );

            String plataformaFavorita =
                    limpar(
                            request.getParameter(
                                    "plataformaFavorita"
                            )
                    );

            String foto =
                    limpar(
                            request.getParameter("foto")
                    );

            if (nome.isEmpty() ||
                    username.isEmpty()) {

                response.sendRedirect(
                        "editar-perfil?mensagem=erro"
                );

                return;
            }

            Connection conexao =
                    Conexao.conectar();

            if (conexao == null) {

                throw new Exception(
                        "Não foi possível conectar ao banco."
                );
            }

            // =====================================================
            // VERIFICAR USERNAME
            // =====================================================

            String sqlUsername =
                    "SELECT id " +
                    "FROM usuario " +
                    "WHERE username = ? " +
                    "AND id != ?";

            PreparedStatement stmtUsername =
                    conexao.prepareStatement(
                            sqlUsername
                    );

            stmtUsername.setString(
                    1,
                    username
            );

            stmtUsername.setInt(
                    2,
                    idUsuario
            );

            ResultSet rsUsername =
                    stmtUsername.executeQuery();

            if (rsUsername.next()) {

                rsUsername.close();
                stmtUsername.close();
                conexao.close();

                response.sendRedirect(
                        "editar-perfil?mensagem=username_existente"
                );

                return;
            }

            rsUsername.close();
            stmtUsername.close();

            // =====================================================
            // ATUALIZAR
            // =====================================================

            String sql =
                    "UPDATE usuario SET " +
                    "nome = ?, " +
                    "username = ?, " +
                    "bio = ?, " +
                    "pais = ?, " +
                    "plataforma_favorita = ?, " +
                    "foto = ? " +
                    "WHERE id = ?";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    nome
            );

            stmt.setString(
                    2,
                    username
            );

            stmt.setString(
                    3,
                    bio
            );

            stmt.setString(
                    4,
                    pais
            );

            stmt.setString(
                    5,
                    plataformaFavorita
            );

            stmt.setString(
                    6,
                    foto
            );

            stmt.setInt(
                    7,
                    idUsuario
            );

            stmt.executeUpdate();

            stmt.close();
            conexao.close();

            // =====================================================
            // ATUALIZAR USUÁRIO DA SESSÃO
            // =====================================================

            Usuario usuarioAtualizado =
                    new Usuario();

            usuarioAtualizado.setId(
                    idUsuario
            );

            usuarioAtualizado.setNome(
                    nome
            );

            usuarioAtualizado.setUsername(
                    username
            );

            usuarioAtualizado.setEmail(
                    usuarioSessao.getEmail()
            );

            usuarioAtualizado.setSenha(
                    usuarioSessao.getSenha()
            );

            usuarioAtualizado.setBio(
                    bio
            );

            usuarioAtualizado.setPais(
                    pais
            );

            usuarioAtualizado.setPlataformaFavorita(
                    plataformaFavorita
            );

            usuarioAtualizado.setFoto(
                    foto
            );

            sessao.setAttribute(
                    "usuario",
                    usuarioAtualizado
            );

            response.sendRedirect(
                    "editar-perfil?mensagem=sucesso"
            );

        } catch (Exception e) {

            System.out.println(
                    "=============================="
            );

            System.out.println(
                    "ERRO AO EDITAR PERFIL:"
            );

            e.printStackTrace();

            System.out.println(
                    "=============================="
            );

            response.sendRedirect(
                    "editar-perfil?mensagem=erro"
            );
        }
    }

    // =========================================================
    // SELECIONAR OPTION
    // =========================================================

    private String selecionar(
            String atual,
            String valor) {

        if (atual != null &&
                atual.equals(valor)) {

            return "selected";
        }

        return "";
    }

    // =========================================================
    // LIMPAR TEXTO
    // =========================================================

    private String limpar(
            String texto) {

        if (texto == null) {

            return "";
        }

        return texto.trim();
    }

    // =========================================================
    // FOTO
    // =========================================================

    private String prepararFoto(
            String foto,
            HttpServletRequest request) {

        if (foto == null ||
                foto.trim().isEmpty()) {

            return "";
        }

        foto =
                foto.trim();

        if (foto.startsWith("http://") ||
                foto.startsWith("https://")) {

            return foto;
        }

        if (foto.startsWith("/")) {

            return
                    request.getContextPath()
                    + foto;
        }

        return
                request.getContextPath()
                + "/foto-perfil?arquivo="
                + foto;
    }

    // =========================================================
    // ESCAPAR HTML
    // =========================================================

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
}