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

@WebServlet("/capa")
public class CapaServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        String capa = null;

        try {

            int idJogo =
                    Integer.parseInt(idTexto);

            try (
                    Connection conexao =
                            Conexao.conectar();

                    PreparedStatement stmt =
                            conexao.prepareStatement(
                                    "SELECT capa " +
                                    "FROM jogo " +
                                    "WHERE id = ?"
                            )
            ) {

                stmt.setInt(1, idJogo);

                try (
                        ResultSet rs =
                                stmt.executeQuery()
                ) {

                    if (rs.next()) {
                        capa = rs.getString("capa");
                    }
                }
            }

            if (capa == null ||
                    capa.trim().isEmpty()) {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                return;
            }

            capa = capa.trim();

            // =================================================
            // CASO SEJA APENAS O APP ID
            // =================================================

            if (capa.matches("\\d+")) {

                capa =
                        "https://cdn.akamai.steamstatic.com/" +
                        "steam/apps/" +
                        capa +
                        "/library_600x900_2x.jpg";
            }

            // =================================================
            // PEGAR IMAGEM
            // =================================================

            URL url =
                    new URL(capa);

            HttpURLConnection conexaoHttp =
                    (HttpURLConnection) url.openConnection();

            conexaoHttp.setRequestMethod("GET");

            conexaoHttp.setConnectTimeout(10000);

            conexaoHttp.setReadTimeout(15000);

            conexaoHttp.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
            );

            conexaoHttp.setRequestProperty(
                    "Accept",
                    "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8"
            );

            int codigo =
                    conexaoHttp.getResponseCode();

            if (codigo < 200 ||
                    codigo >= 300) {

                conexaoHttp.disconnect();

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                return;
            }

            String tipo =
                    conexaoHttp.getContentType();

            if (tipo == null ||
                    !tipo.startsWith("image/")) {

                conexaoHttp.disconnect();

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                return;
            }

            response.setContentType(tipo);

            response.setHeader(
                    "Cache-Control",
                    "public, max-age=86400"
            );

            try (
                    InputStream entrada =
                            conexaoHttp.getInputStream()
            ) {

                OutputStream saida =
                        response.getOutputStream();

                byte[] buffer =
                        new byte[8192];

                int quantidade;

                while (
                        (quantidade =
                                entrada.read(buffer)) != -1
                ) {

                    saida.write(
                            buffer,
                            0,
                            quantidade
                    );
                }

                saida.flush();

            } finally {

                conexaoHttp.disconnect();
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );
        }
    }
}