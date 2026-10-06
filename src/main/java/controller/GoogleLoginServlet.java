package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/google-login")
public class GoogleLoginServlet extends HttpServlet {

    private static final String CLIENT_ID =
            System.getenv("GOOGLE_CLIENT_ID");

    private static final String REDIRECT_URI =
            System.getenv("GOOGLE_REDIRECT_URI");

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("==============================");
        System.out.println("GOOGLE LOGIN FOI CHAMADO");
        System.out.println("==============================");

        // =====================================================
        // VERIFICAR CONFIGURAÇÃO
        // =====================================================

        if (CLIENT_ID == null ||
                CLIENT_ID.trim().isEmpty()) {

            System.out.println(
                    "ERRO: GOOGLE_CLIENT_ID não configurado."
            );

            response.sendRedirect(
                    "login.html?erro=config_google"
            );

            return;
        }

        if (REDIRECT_URI == null ||
                REDIRECT_URI.trim().isEmpty()) {

            System.out.println(
                    "ERRO: GOOGLE_REDIRECT_URI não configurado."
            );

            response.sendRedirect(
                    "login.html?erro=config_google"
            );

            return;
        }

        // =====================================================
        // SESSÃO
        // =====================================================

        HttpSession sessao =
                request.getSession(true);

        // =====================================================
        // CRIAR STATE DE SEGURANÇA
        // =====================================================

        SecureRandom random =
                new SecureRandom();

        byte[] bytes =
                new byte[32];

        random.nextBytes(bytes);

        String state =
                java.util.Base64
                        .getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);

        sessao.setAttribute(
                "google_oauth_state",
                state
        );

        // =====================================================
        // MONTAR URL DO GOOGLE
        // =====================================================

        String url =
                "https://accounts.google.com/o/oauth2/v2/auth"

                + "?client_id="
                + URLEncoder.encode(
                        CLIENT_ID,
                        StandardCharsets.UTF_8.name()
                )

                + "&redirect_uri="
                + URLEncoder.encode(
                        REDIRECT_URI,
                        StandardCharsets.UTF_8.name()
                )

                + "&response_type=code"

                + "&scope="
                + URLEncoder.encode(
                        "openid email profile",
                        StandardCharsets.UTF_8.name()
                )

                + "&state="
                + URLEncoder.encode(
                        state,
                        StandardCharsets.UTF_8.name()
                )

                + "&access_type=online";

        System.out.println(
                "REDIRECIONANDO PARA GOOGLE..."
        );

        response.sendRedirect(url);
    }
}