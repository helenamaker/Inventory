package controller;

import dao.Conexao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/capa")
public class CapaServlet extends HttpServlet {

    private static final Map<String, String> CAPAS_LOCAIS = new HashMap<String, String>();

    static {
        CAPAS_LOCAIS.put(normalizar("Resident Evil 4"), "resident-evil-4.jpg");
        CAPAS_LOCAIS.put(normalizar("The Last of Us Part I"), "the-last-of-us-part-1.jpg");
        CAPAS_LOCAIS.put(normalizar("God of War Ragnarök"), "god-of-war-ragnarok.jpg");
        CAPAS_LOCAIS.put(normalizar("Minecraft"), "minecraft.jpg");
        CAPAS_LOCAIS.put(normalizar("Red Dead Redemption 2"), "red-dead-redemption-2.jpg");
        CAPAS_LOCAIS.put(normalizar("Silent Hill 2"), "silent-hill-2.jpg");
        CAPAS_LOCAIS.put(normalizar("Elden Ring"), "elden-ring.jpg");
        CAPAS_LOCAIS.put(normalizar("ELDEN RING"), "elden-ring.jpg");
        CAPAS_LOCAIS.put(normalizar("Resident Evil Village"), "resident-evil-village.jpg");
        CAPAS_LOCAIS.put(normalizar("The Witcher 3: Wild Hunt"), "the-witcher-3.jpg");
        CAPAS_LOCAIS.put(normalizar("The Witcher 3"), "the-witcher-3.jpg");
        CAPAS_LOCAIS.put(normalizar("Cyberpunk 2077"), "cyberpunk-2077.jpg");
        CAPAS_LOCAIS.put(normalizar("Marvel's Spider-Man 2"), "marvel-spider-man-2.jpg");
        CAPAS_LOCAIS.put(normalizar("Grand Theft Auto V"), "gta-v.jpg");
        CAPAS_LOCAIS.put(normalizar("GTA V"), "gta-v.jpg");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {

        String idTexto = request.getParameter("id");
        String appIdTexto = request.getParameter("appId");
        String tituloTexto = request.getParameter("titulo");

        String capa = null;
        String titulo = null;

        try {
            if (idTexto != null && !idTexto.trim().isEmpty()) {
                int idJogo = Integer.parseInt(idTexto);

                try (Connection conexao = Conexao.conectar();
                     PreparedStatement stmt = conexao.prepareStatement(
                             "SELECT titulo, capa, steam_app_id FROM jogo WHERE id = ?")) {

                    stmt.setInt(1, idJogo);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            titulo = rs.getString("titulo");
                            capa = rs.getString("capa");
                            String dbAppId = rs.getString("steam_app_id");
                            if ((appIdTexto == null || appIdTexto.trim().isEmpty()) &&
                                    dbAppId != null && !dbAppId.trim().isEmpty()) {
                                appIdTexto = dbAppId;
                            }
                        }
                    }
                }
            } else if (tituloTexto != null && !tituloTexto.trim().isEmpty()) {
                try (Connection conexao = Conexao.conectar();
                     PreparedStatement stmt = conexao.prepareStatement(
                             "SELECT titulo, capa, steam_app_id FROM jogo WHERE titulo = ? COLLATE NOCASE LIMIT 1")) {
                    stmt.setString(1, tituloTexto.trim());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            titulo = rs.getString("titulo");
                            capa = rs.getString("capa");
                            appIdTexto = rs.getString("steam_app_id");
                        }
                    }
                }
            } else if (appIdTexto != null && !appIdTexto.trim().isEmpty()) {
                try {
                    int appId = Integer.parseInt(appIdTexto);
                    try (Connection conexao = Conexao.conectar();
                         PreparedStatement stmt = conexao.prepareStatement(
                                 "SELECT titulo, capa FROM jogo WHERE steam_app_id = ? LIMIT 1")) {
                        stmt.setInt(1, appId);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                titulo = rs.getString("titulo");
                                capa = rs.getString("capa");
                            }
                        }
                    }
                } catch (Exception ignored) {
                    // Continua usando o appId recebido.
                }
            }

            // Primeiro: capa empacotada no próprio Inventory.
            String arquivoLocal = titulo == null ? null : CAPAS_LOCAIS.get(normalizar(titulo));

            if (arquivoLocal == null && capa != null && !capa.trim().isEmpty()) {
                String nomeArquivo = new java.io.File(capa.trim()).getName();
                if (nomeArquivo.toLowerCase().endsWith(".jpg") ||
                        nomeArquivo.toLowerCase().endsWith(".jpeg") ||
                        nomeArquivo.toLowerCase().endsWith(".png") ||
                        nomeArquivo.toLowerCase().endsWith(".webp")) {
                    arquivoLocal = nomeArquivo;
                }
            }

            if (arquivoLocal != null && enviarRecursoLocal(request, response, arquivoLocal)) {
                return;
            }

            // Segundo: se a coluna capa já tiver uma URL, o servidor do Inventory faz o proxy.
            if (capa != null && !capa.trim().isEmpty() &&
                    (capa.startsWith("http://") || capa.startsWith("https://"))) {
                if (enviarUrl(capa, response)) {
                    return;
                }
            }

            // Terceiro: usa Steam pelo servidor, nunca diretamente pelo navegador.
            if (appIdTexto != null && appIdTexto.trim().matches("\\d+")) {
                String steamUrl =
                        "https://cdn.cloudflare.steamstatic.com/steam/apps/" +
                        appIdTexto.trim() +
                        "/library_600x900.jpg";

                if (enviarUrl(steamUrl, response)) {
                    return;
                }

                String headerUrl =
                        "https://cdn.cloudflare.steamstatic.com/steam/apps/" +
                        appIdTexto.trim() +
                        "/header.jpg";

                if (enviarUrl(headerUrl, response)) {
                    return;
                }
            }

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private boolean enviarRecursoLocal(
            HttpServletRequest request,
            HttpServletResponse response,
            String arquivo) throws Exception {

        InputStream entrada =
                request.getServletContext().getResourceAsStream("/" + arquivo);

        if (entrada == null) {
            return false;
        }

        String tipo = request.getServletContext().getMimeType(arquivo);
        if (tipo == null) {
            tipo = "image/jpeg";
        }

        response.setContentType(tipo);
        response.setHeader("Cache-Control", "public, max-age=604800");

        try (InputStream in = entrada;
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int quantidade;
            while ((quantidade = in.read(buffer)) != -1) {
                out.write(buffer, 0, quantidade);
            }
            out.flush();
        }

        return true;
    }

    private boolean enviarUrl(
            String endereco,
            HttpServletResponse response) {

        HttpURLConnection conexaoHttp = null;

        try {
            URL url = new URL(endereco);
            conexaoHttp = (HttpURLConnection) url.openConnection();
            conexaoHttp.setRequestMethod("GET");
            conexaoHttp.setConnectTimeout(8000);
            conexaoHttp.setReadTimeout(12000);
            conexaoHttp.setInstanceFollowRedirects(true);
            conexaoHttp.setRequestProperty("User-Agent", "Mozilla/5.0");
            conexaoHttp.setRequestProperty("Accept", "image/avif,image/webp,image/jpeg,image/png,image/*,*/*;q=0.8");

            int codigo = conexaoHttp.getResponseCode();
            if (codigo < 200 || codigo >= 300) {
                return false;
            }

            String tipo = conexaoHttp.getContentType();
            if (tipo == null || !tipo.toLowerCase().startsWith("image/")) {
                return false;
            }

            response.setContentType(tipo);
            response.setHeader("Cache-Control", "public, max-age=86400");

            try (InputStream entrada = conexaoHttp.getInputStream();
                 OutputStream saida = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int quantidade;
                while ((quantidade = entrada.read(buffer)) != -1) {
                    saida.write(buffer, 0, quantidade);
                }
                saida.flush();
            }

            return true;

        } catch (Exception e) {
            return false;
        } finally {
            if (conexaoHttp != null) {
                conexaoHttp.disconnect();
            }
        }
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        String s = java.text.Normalizer.normalize(
                texto.trim().toLowerCase(),
                java.text.Normalizer.Form.NFD
        );

        return s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-z0-9]+", "")
                .trim();
    }
}
