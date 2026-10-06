package controller;

import dao.Conexao;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/foto-perfil")
public class FotoPerfilServlet extends HttpServlet {

    private static final String PASTA_FOTOS = Conexao.getPastaFotos();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String urlRemota = request.getParameter("url");

        if (urlRemota != null && !urlRemota.trim().isEmpty()) {
            if (!urlGooglePermitida(urlRemota.trim())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if (enviarUrlRemota(urlRemota.trim(), response)) {
                return;
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String arquivo =
                request.getParameter("arquivo");

        if (arquivo == null ||
                arquivo.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        /*
         * Pega apenas o nome do arquivo.
         * Impede que alguém passe um caminho externo.
         */

        arquivo =
                new File(arquivo)
                        .getName();

        String nomeMinusculo =
                arquivo.toLowerCase();

        /*
         * Aceitar somente imagens.
         */

        if (!nomeMinusculo.endsWith(".jpg")
                && !nomeMinusculo.endsWith(".jpeg")
                && !nomeMinusculo.endsWith(".png")
                && !nomeMinusculo.endsWith(".webp")) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }

        File arquivoFoto =
                new File(
                        PASTA_FOTOS,
                        arquivo
                );

        /*
         * Verificar se existe.
         */

        if (!arquivoFoto.exists() ||
                !arquivoFoto.isFile()) {

            System.out.println(
                    "FOTO NÃO ENCONTRADA:"
            );

            System.out.println(
                    arquivoFoto.getAbsolutePath()
            );

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        /*
         * MIME.
         */

        String tipo =
                getServletContext()
                        .getMimeType(
                                arquivoFoto.getName()
                        );

        if (tipo == null) {

            tipo =
                    "application/octet-stream";
        }

        response.setContentType(tipo);

        response.setContentLengthLong(
                arquivoFoto.length()
        );

        /*
         * Enviar imagem.
         */

        try (
                OutputStream saida =
                        response.getOutputStream()
        ) {

            Files.copy(
                    arquivoFoto.toPath(),
                    saida
            );
        }
    }
    private boolean urlGooglePermitida(String endereco) {
        try {
            URI uri = new URI(endereco);
            String esquema = uri.getScheme();
            String host = uri.getHost();
            if (esquema == null || host == null ||
                    !("https".equalsIgnoreCase(esquema) || "http".equalsIgnoreCase(esquema))) {
                return false;
            }
            host = host.toLowerCase();
            return host.equals("googleusercontent.com") ||
                    host.endsWith(".googleusercontent.com") ||
                    host.equals("ggpht.com") ||
                    host.endsWith(".ggpht.com");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean enviarUrlRemota(String endereco, HttpServletResponse response) {
        HttpURLConnection conexao = null;
        try {
            URL url = new URL(endereco);
            conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(8000);
            conexao.setReadTimeout(12000);
            conexao.setInstanceFollowRedirects(true);
            conexao.setRequestProperty("User-Agent", "Mozilla/5.0");
            conexao.setRequestProperty("Accept", "image/avif,image/webp,image/jpeg,image/png,image/*,*/*;q=0.8");
            int codigo = conexao.getResponseCode();
            if (codigo < 200 || codigo >= 300) return false;
            String tipo = conexao.getContentType();
            if (tipo == null || !tipo.toLowerCase().startsWith("image/")) return false;
            response.setContentType(tipo);
            response.setHeader("Cache-Control", "public, max-age=86400");
            try (InputStream entrada = conexao.getInputStream();
                 OutputStream saida = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int quantidade;
                while ((quantidade = entrada.read(buffer)) != -1) saida.write(buffer, 0, quantidade);
                saida.flush();
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (conexao != null) conexao.disconnect();
        }
    }

}