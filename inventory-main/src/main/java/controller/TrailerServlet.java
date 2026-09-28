package controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

        if (appId == null || !appId.matches("\\d+")) {
            enviarErro(response, "Jogo inválido.");
            return;
        }

        /*
         * O trailer continua sendo buscado na Steam, mas agora o navegador
         * recebe o vídeo pelo próprio Inventory. Assim o botão não abre
         * YouTube nem manda o usuário para outro site.
         */
        if ("1".equals(request.getParameter("stream"))) {
            transmitirVideo(appId, request, response);
            return;
        }

        String video = buscarTrailer(appId);

        response.setContentType("text/html;charset=UTF-8");

        if (video == null || video.isEmpty()) {
            response.getWriter().print(
                "<!DOCTYPE html><html><body style=\"margin:0;background:#0b0910;color:white;font-family:Arial;display:flex;align-items:center;justify-content:center;height:100vh;text-align:center\">" +
                "<div><h2>Trailer não encontrado</h2><p>Não foi possível carregar o trailer deste jogo.</p></div>" +
                "</body></html>"
            );
            return;
        }

        response.getWriter().print(
            "<!DOCTYPE html>" +
            "<html><head><meta charset='UTF-8'>" +
            "<style>" +
            "html,body{margin:0;width:100%;height:100%;background:#000;overflow:hidden}" +
            "video{width:100%;height:100%;display:block;background:#000;object-fit:contain}" +
            "</style></head><body>" +
            "<video controls autoplay playsinline src='trailer?appId=" +
            escaparHtml(appId) +
            "&stream=1'></video>" +
            "</body></html>"
        );
    }

    private void transmitirVideo(
            String appId,
            javax.servlet.http.HttpServletRequest request,
            HttpServletResponse response
    ) throws java.io.IOException {

        String videoUrl = buscarTrailer(appId);

        if (videoUrl == null || videoUrl.isEmpty()) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Trailer não encontrado"
            );
            return;
        }

        HttpURLConnection conexao = null;
        InputStream entrada = null;
        OutputStream saida = null;

        try {

            URL url = new URL(videoUrl);
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(15000);
            conexao.setReadTimeout(30000);
            conexao.setRequestProperty("User-Agent", "Inventory/1.0");

            String range = request.getHeader("Range");
            if (range != null) {
                conexao.setRequestProperty("Range", range);
            }

            int codigo = conexao.getResponseCode();

            if (codigo != 200 && codigo != 206) {
                response.sendError(codigo);
                return;
            }

            String contentType = conexao.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "video/mp4";
            }

            response.setContentType(contentType);
            response.setHeader("Accept-Ranges", "bytes");

            long tamanho = conexao.getContentLengthLong();
            if (tamanho >= 0) {
                response.setContentLengthLong(tamanho);
            }

            if (codigo == 206) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                String contentRange = conexao.getHeaderField("Content-Range");
                if (contentRange != null) {
                    response.setHeader("Content-Range", contentRange);
                }
            }

            entrada = new BufferedInputStream(conexao.getInputStream());
            saida = response.getOutputStream();

            byte[] buffer = new byte[16384];
            int lidos;

            while ((lidos = entrada.read(buffer)) != -1) {
                saida.write(buffer, 0, lidos);
                saida.flush();
            }

        } finally {

            try {
                if (entrada != null) entrada.close();
            } catch (Exception ignored) {
            }

            try {
                if (saida != null) saida.close();
            } catch (Exception ignored) {
            }

            if (conexao != null) {
                conexao.disconnect();
            }
        }
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
