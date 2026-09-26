package controller;

import dao.Conexao;
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

@WebServlet("/biblioteca")
public class BibliotecaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static class Jogo {
        int steamAppId;
        String nome;
        String capa;
        String status;
        double horas;

        Jogo(int steamAppId, String nome, String capa,
             String status, double horas) {

            this.steamAppId = steamAppId;
            this.nome = nome;
            this.capa = capa;
            this.status = status;
            this.horas = horas;
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession sessao = request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario =
                (Usuario) sessao.getAttribute("usuario");

        int idUsuario = usuario.getId();

        List<Jogo> queroJogar =
                buscarJogos(idUsuario, "quero_jogar");

        List<Jogo> jogando =
                buscarJogos(idUsuario, "jogando");

        List<Jogo> zerados =
                buscarJogos(idUsuario, "zerado");

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");
        html.append("<head>");

        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");

        html.append("<title>Minha Biblioteca - Inventory</title>");

        html.append("<link rel='preconnect' href='https://fonts.googleapis.com'>");
        html.append("<link rel='preconnect' href='https://fonts.gstatic.com'>");
        html.append("<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap' rel='stylesheet'>");

        html.append("<style>");

        html.append("*{box-sizing:border-box;margin:0;padding:0}");

        html.append("body{");
        html.append("font-family:'Poppins',Arial,sans-serif;");
        html.append("background:#0d0912;");
        html.append("color:#fff;");
        html.append("min-height:100vh;");
        html.append("}");

        html.append("header{");
        html.append("height:70px;");
        html.append("background:#120d19;");
        html.append("border-bottom:1px solid #2b2035;");
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

        html.append("nav{display:flex;gap:25px}");

        html.append("nav a{");
        html.append("color:#b9afc4;");
        html.append("text-decoration:none;");
        html.append("font-size:14px;");
        html.append("transition:.2s;");
        html.append("}");

        html.append("nav a:hover{color:#c084fc}");

        html.append("main{");
        html.append("width:88%;");
        html.append("max-width:1400px;");
        html.append("margin:40px auto;");
        html.append("}");

        html.append(".titulo{");
        html.append("margin-bottom:35px;");
        html.append("}");

        html.append(".titulo h1{");
        html.append("font-size:30px;");
        html.append("margin-bottom:7px;");
        html.append("}");

        html.append(".titulo p{");
        html.append("color:#92869d;");
        html.append("font-size:14px;");
        html.append("}");

        html.append(".secao{margin-bottom:45px}");

        html.append(".cabecalho-secao{");
        html.append("display:flex;");
        html.append("align-items:center;");
        html.append("justify-content:space-between;");
        html.append("margin-bottom:18px;");
        html.append("}");

        html.append(".cabecalho-secao h2{");
        html.append("font-size:20px;");
        html.append("}");

        html.append(".contador{");
        html.append("background:#21152c;");
        html.append("border:1px solid #382448;");
        html.append("color:#c084fc;");
        html.append("border-radius:20px;");
        html.append("padding:5px 12px;");
        html.append("font-size:12px;");
        html.append("}");

        html.append(".grid{");
        html.append("display:grid;");
        html.append("grid-template-columns:repeat(auto-fill,minmax(180px,1fr));");
        html.append("gap:20px;");
        html.append("}");

        html.append(".card{");
        html.append("background:#16101c;");
        html.append("border:1px solid #2a2032;");
        html.append("border-radius:14px;");
        html.append("overflow:hidden;");
        html.append("transition:.2s;");
        html.append("}");

        html.append(".card:hover{");
        html.append("transform:translateY(-4px);");
        html.append("border-color:#6d3d91;");
        html.append("}");

        html.append(".capa{");
        html.append("width:100%;");
        html.append("height:250px;");
        html.append("object-fit:cover;");
        html.append("background:#201726;");
        html.append("}");

        html.append(".info{padding:14px}");

        html.append(".nome{");
        html.append("font-size:14px;");
        html.append("font-weight:600;");
        html.append("min-height:42px;");
        html.append("margin-bottom:10px;");
        html.append("}");

        html.append(".horas{");
        html.append("font-size:11px;");
        html.append("color:#81758b;");
        html.append("margin-bottom:12px;");
        html.append("}");

        html.append(".acoes{");
        html.append("display:flex;");
        html.append("flex-direction:column;");
        html.append("gap:7px;");
        html.append("}");

        html.append(".botao{");
        html.append("width:100%;");
        html.append("border:0;");
        html.append("border-radius:8px;");
        html.append("padding:9px;");
        html.append("font-family:'Poppins',Arial,sans-serif;");
        html.append("font-size:11px;");
        html.append("font-weight:600;");
        html.append("cursor:pointer;");
        html.append("text-decoration:none;");
        html.append("text-align:center;");
        html.append("display:block;");
        html.append("}");

        html.append(".jogar{");
        html.append("background:#7c3aed;");
        html.append("color:#fff;");
        html.append("}");

        html.append(".jogar:hover{background:#8b5cf6}");

        html.append(".avaliar{");
        html.append("background:#2a1738;");
        html.append("color:#d8b4fe;");
        html.append("border:1px solid #63358a;");
        html.append("}");

        html.append(".avaliar:hover{");
        html.append("background:#3a2050;");
        html.append("}");

        html.append(".zerar{");
        html.append("background:#173523;");
        html.append("color:#86efac;");
        html.append("border:1px solid #27613b;");
        html.append("}");

        html.append(".vazio{");
        html.append("background:#120e16;");
        html.append("border:1px dashed #35283d;");
        html.append("border-radius:12px;");
        html.append("padding:30px;");
        html.append("text-align:center;");
        html.append("color:#716579;");
        html.append("font-size:13px;");
        html.append("}");

        html.append("@media(max-width:700px){");
        html.append("header{padding:0 4%}");
        html.append("nav{gap:10px}");
        html.append("nav a{font-size:11px}");
        html.append("main{width:92%}");
        html.append("}");

        html.append("</style>");
        html.append("</head>");

        html.append("<body>");

        html.append("<header>");

        html.append("<a class='logo' href='jogos'>INVENT<span>O</span>RY</a>");

        html.append("<nav>");
        html.append("<a href='jogos'>Jogos</a>");
        html.append("<a href='biblioteca'>Biblioteca</a>");
        html.append("<a href='buscar-usuarios'>Usuários</a>");
        html.append("<a href='perfil'>Perfil</a>");
        html.append("</nav>");

        html.append("</header>");

        html.append("<main>");

        html.append("<div class='titulo'>");
        html.append("<h1>Minha biblioteca</h1>");
        html.append("<p>Organize seus jogos e acompanhe seu progresso.</p>");
        html.append("</div>");

        criarSecao(
                html,
                "🎮 Quero jogar",
                "Adicionados à biblioteca",
                queroJogar,
                "quero_jogar"
        );

        criarSecao(
                html,
                "🟡 Jogando",
                "Jogos que você está jogando",
                jogando,
                "jogando"
        );

        criarSecao(
                html,
                "✓ Zerados",
                "Jogos que você terminou",
                zerados,
                "zerado"
        );

        html.append("</main>");
        html.append("</body>");
        html.append("</html>");

        response.getWriter().write(html.toString());
    }

    private void criarSecao(
            StringBuilder html,
            String titulo,
            String descricao,
            List<Jogo> jogos,
            String status) {

        html.append("<section class='secao'>");

        html.append("<div class='cabecalho-secao'>");

        html.append("<div>");
        html.append("<h2>").append(titulo).append("</h2>");
        html.append("<p style='color:#756b7d;font-size:12px;margin-top:3px'>")
                .append(descricao)
                .append("</p>");
        html.append("</div>");

        html.append("<span class='contador'>")
                .append(jogos.size())
                .append("</span>");

        html.append("</div>");

        if (jogos.isEmpty()) {

            html.append("<div class='vazio'>");
            html.append("Nenhum jogo nesta seção ainda.");
            html.append("</div>");

        } else {

            html.append("<div class='grid'>");

            for (Jogo jogo : jogos) {

                html.append("<article class='card'>");

                html.append("<img class='capa' ");
                html.append("src='")
                        .append(jogo.capa)
                        .append("' ");
                html.append("onerror=\"this.src='https://cdn.cloudflare.steamstatic.com/steam/apps/")
                        .append(jogo.steamAppId)
                        .append("/header.jpg'\">");

                html.append("<div class='info'>");

                html.append("<div class='nome'>")
                        .append(escapar(jogo.nome))
                        .append("</div>");

                html.append("<div class='horas'>")
                        .append(String.format("%.1f", jogo.horas))
                        .append(" horas jogadas</div>");

                html.append("<div class='acoes'>");

                if ("quero_jogar".equals(status)) {

                    html.append("<form method='POST' action='biblioteca-status'>");

                    html.append("<input type='hidden' name='steamAppId' value='")
                            .append(jogo.steamAppId)
                            .append("'>");

                    html.append("<input type='hidden' name='status' value='jogando'>");

                    html.append("<button class='botao jogar' type='submit'>");
                    html.append("▶ Começar a jogar");
                    html.append("</button>");

                    html.append("</form>");

                } else if ("jogando".equals(status)) {

                    html.append("<a class='botao avaliar' href='avaliar?id=")
                            .append(jogo.steamAppId)
                            .append("'>");
                    html.append("⭐ Avaliar jogo");
                    html.append("</a>");

                    html.append("<form method='POST' action='biblioteca-status'>");

                    html.append("<input type='hidden' name='steamAppId' value='")
                            .append(jogo.steamAppId)
                            .append("'>");

                    html.append("<input type='hidden' name='status' value='zerado'>");

                    html.append("<button class='botao zerar' type='submit'>");
                    html.append("✓ Marcar como zerado");
                    html.append("</button>");

                    html.append("</form>");

                } else {

                    html.append("<a class='botao avaliar' href='avaliar?id=")
                            .append(jogo.steamAppId)
                            .append("'>");
                    html.append("⭐ Ver / editar avaliação");
                    html.append("</a>");
                }

                html.append("</div>");
                html.append("</div>");
                html.append("</article>");
            }

            html.append("</div>");
        }

        html.append("</section>");
    }

    private List<Jogo> buscarJogos(
            int idUsuario,
            String status) {

        List<Jogo> jogos = new ArrayList<Jogo>();

        String sql =
                "SELECT steam_app_id, status, horas_jogadas " +
                "FROM biblioteca " +
                "WHERE id_usuario = ? " +
                "AND status = ? " +
                "ORDER BY id DESC";

        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ) {

            ps.setInt(1, idUsuario);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int appId =
                        rs.getInt("steam_app_id");

                double horas =
                        rs.getDouble("horas_jogadas");

                String nome =
                        buscarNomeJogo(conexao, appId);

                if (nome == null ||
                        nome.trim().isEmpty()) {

                    nome =
                            buscarNomeSteam(appId);
                }

                String capa =
                        "https://cdn.cloudflare.steamstatic.com/steam/apps/"
                        + appId
                        + "/library_600x900.jpg";

                jogos.add(
                        new Jogo(
                                appId,
                                nome,
                                capa,
                                status,
                                horas
                        )
                );
            }

            rs.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jogos;
    }

    private String buscarNomeJogo(
            Connection conexao,
            int steamAppId) {

        String sql =
                "SELECT titulo FROM jogo " +
                "WHERE steam_app_id = ? " +
                "LIMIT 1";

        try (
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ) {

            ps.setInt(1, steamAppId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return rs.getString("titulo");
            }

        } catch (Exception e) {

            // Se a tabela ainda não possuir steam_app_id,
            // usa a Steam como fallback.
        }

        return null;
    }

    private String buscarNomeSteam(
            int steamAppId) {

        HttpURLConnection conexao = null;

        try {

            URL url =
                    new URL(
                            "https://store.steampowered.com/api/appdetails?appids="
                                    + steamAppId
                    );

            conexao =
                    (HttpURLConnection) url.openConnection();

            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(5000);
            conexao.setReadTimeout(5000);

            BufferedReader leitor =
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

            leitor.close();

            String json =
                    resposta.toString();

            Pattern pattern =
                    Pattern.compile(
                            "\"name\"\\s*:\\s*\"([^\"]+)\""
                    );

            Matcher matcher =
                    pattern.matcher(json);

            if (matcher.find()) {

                return matcher.group(1)
                        .replace("\\u0026", "&")
                        .replace("\\\"", "\"");
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (conexao != null) {
                conexao.disconnect();
            }
        }

        return "Jogo " + steamAppId;
    }

    private String escapar(String texto) {

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