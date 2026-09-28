package controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/foto-perfil")
public class FotoPerfilServlet extends HttpServlet {

    private static final String PASTA_FOTOS;

    static {

        String uploadsPath =
                System.getenv("UPLOADS_PATH");

        if (uploadsPath != null &&
                !uploadsPath.trim().isEmpty()) {

            PASTA_FOTOS =
                    uploadsPath
                    + File.separator
                    + "perfil";

        } else {

            String sistema =
                    System.getProperty("os.name")
                            .toLowerCase();

            if (sistema.contains("win")) {

                PASTA_FOTOS =
                        "C:\\GameBoxdUploads\\data\\perfil";

            } else {

                PASTA_FOTOS =
                        "/app/data/perfil";
            }
        }

        File pasta =
                new File(PASTA_FOTOS);

        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        System.out.println(
                "================================="
        );

        System.out.println(
                "PASTA DAS FOTOS:"
        );

        System.out.println(
                PASTA_FOTOS
        );

        System.out.println(
                "================================="
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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
}