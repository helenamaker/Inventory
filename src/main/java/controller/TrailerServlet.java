package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/trailer")
public class TrailerServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, java.io.IOException {

        String appId = request.getParameter("appId");
        String nome = request.getParameter("nome");

        if (appId == null || !appId.matches("\\d+")) {
            enviarErro(response, "Jogo inválido.");
            return;
        }

        String video = buscarTrailer(appId);

        response.setContentType("text/html;charset=UTF-8");

        if (video == null || video.isEmpty()) {
            String busca = (nome == null || nome.trim().isEmpty())
                    ? "trailer oficial jogo " + appId
                    : nome.trim() + " official trailer";

            String youtube = "https://www.youtube.com/results?search_query="
                    + URLEncoder.encode(busca, "UTF-8");

            response.getWriter().print(
                "<!DOCTYPE html><html><head><meta charset='UTF-8'>" +
                "<style>html,body{margin:0;width:100%;height:100%;background:#0b0910;color:white;font-family:Arial}" +
                ".box{height:100%;display:flex;align-items:center;justify-content:center;text-align:center}" +
                "a{display:inline-block;margin-top:16px;padding:12px 20px;border-radius:10px;background:#7c3aed;color:white;text-decoration:none;font-weight:bold}</style>" +
                "</head><body><div class='box'><div><h2>Abrindo o trailer...</h2>" +
                "<p>O trailer deste jogo será aberto no YouTube.</p>" +
                "<a href='" + escaparHtml(youtube) + "' target='_top'>▶ Abrir trailer</a>" +
                "</div></div></body></html>"
            );
            return;
        }

        String videoHtml = escaparHtml(video);

        response.getWriter().print(
            "<!DOCTYPE html>" +
            "<html><head><meta charset='UTF-8'>" +
            "<style>" +
            "html,body{margin:0;width:100%;height:100%;background:#000;overflow:hidden}" +
            "video{width:100%;height:100%;display:block;background:#000;object-fit:contain}" +
            "</style></head><body>" +
            "<video controls autoplay playsinline src='" + videoHtml + "'></video>" +
            "</body></html>"
        );
    }

    private String buscarTrailer(String appId) {

        HttpURLConnection conexao = null;
        BufferedReader leitor = null;

        try {

            String urlApi =
                "https://store.steampowered.com/api/appdetails?appids="
                + URLEncoder.encode(appId, "UTF-8")
                + "&l=english";

            URL url = new URL(urlApi);

            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(10000);
            conexao.setReadTimeout(15000);
            conexao.setRequestProperty("User-Agent", "Inventory/1.0");

            if (conexao.getResponseCode() != 200) {
                return null;
            }

            leitor = new BufferedReader(
                new InputStreamReader(
                    conexao.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            StringBuilder json = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                json.append(linha);
            }

            String dados = json.toString();

            // Preferimos o MP4 de maior qualidade.
            String video = extrair(dados, "mp4_max");

            if (video == null) {
                video = extrair(dados, "mp4");
            }

            if (video == null) {
                video = extrair(dados, "webm_max");
            }

            if (video == null) {
                video = extrair(dados, "webm");
            }

            return video;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            try {
                if (leitor != null) leitor.close();
            } catch (Exception ignored) {
            }

            if (conexao != null) {
                conexao.disconnect();
            }
        }
    }

    private String extrair(String json, String campo) {

        Pattern pattern = Pattern.compile(
            "\\\"" + Pattern.quote(campo) +
            "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"\\\\])*)\\\""
        );

        Matcher matcher = pattern.matcher(json);

        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1)
            .replace("\\/", "/")
            .replace("\\u0026", "&")
            .replace("\\\"", "\"")
            .replace("\\\\", "\\");
    }

    private void enviarErro(
            HttpServletResponse response,
            String mensagem
    ) throws java.io.IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(
            "<!DOCTYPE html><html><body style=\"background:#0b0910;color:white;font-family:Arial;text-align:center;padding:40px\">" +
            escaparHtml(mensagem) +
            "</body></html>"
        );
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
}
