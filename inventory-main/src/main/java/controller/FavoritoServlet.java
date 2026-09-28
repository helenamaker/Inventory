package controller;

import dao.Conexao;
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

@WebServlet("/favorito")
public class FavoritoServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao =
                request.getSession(false);

        // Verifica se está logado
        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html?erro=login");
            return;
        }

        Usuario usuario =
                (Usuario) sessao.getAttribute("usuario");

        String steamAppIdTexto =
                request.getParameter("steamAppId");

        if (steamAppIdTexto == null ||
                steamAppIdTexto.trim().isEmpty()) {

            response.sendRedirect("jogos");
            return;
        }

        try {

            int steamAppId =
                    Integer.parseInt(
                            steamAppIdTexto.trim()
                    );

            int idUsuario =
                    usuario.getId();

            try (Connection conexao = Conexao.conectar()) {

                if (conexao == null) {

                    response.sendRedirect(
                            "jogos?erro=banco"
                    );

                    return;
                }

                // Verifica se já está favoritado
                String verificar =
                        "SELECT id FROM favorito " +
                        "WHERE id_usuario = ? " +
                        "AND steam_app_id = ?";

                try (PreparedStatement stmtVerificar =
                        conexao.prepareStatement(verificar)) {

                    stmtVerificar.setInt(1, idUsuario);
                    stmtVerificar.setInt(2, steamAppId);

                    try (ResultSet resultado =
                            stmtVerificar.executeQuery()) {

                        if (resultado.next()) {

                            response.sendRedirect(
                                    "jogos?erro=ja_favorito"
                            );

                            return;
                        }
                    }
                }

                // Limite de 5 favoritos
                String contar =
                        "SELECT COUNT(*) FROM favorito " +
                        "WHERE id_usuario = ?";

                int quantidade = 0;

                try (PreparedStatement stmtContar =
                        conexao.prepareStatement(contar)) {

                    stmtContar.setInt(1, idUsuario);

                    try (ResultSet resultadoContagem =
                            stmtContar.executeQuery()) {

                        if (resultadoContagem.next()) {
                            quantidade =
                                    resultadoContagem.getInt(1);
                        }
                    }
                }

                if (quantidade >= 5) {

                    response.sendRedirect(
                            "jogos?erro=limite_favoritos"
                    );

                    return;
                }

                // Salva o favorito
                String inserir =
                        "INSERT INTO favorito " +
                        "(id_usuario, steam_app_id) " +
                        "VALUES (?, ?)";

                try (PreparedStatement stmtInserir =
                        conexao.prepareStatement(inserir)) {

                    stmtInserir.setInt(1, idUsuario);
                    stmtInserir.setInt(2, steamAppId);

                    stmtInserir.executeUpdate();
                }
            }

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "FAVORITO ADICIONADO!"
            );

            System.out.println(
                    "USUARIO: " + idUsuario
            );

            System.out.println(
                    "STEAM APP ID: " + steamAppId
            );

            System.out.println(
                    "================================="
            );

            response.sendRedirect(
                    "jogos?favorito=sucesso"
            );

        } catch (NumberFormatException e) {

            e.printStackTrace();

            response.sendRedirect(
                    "jogos?erro=id_invalido"
            );

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO ADICIONAR FAVORITO"
            );

            System.out.println(
                    "================================="
            );

            e.printStackTrace();

            response.sendRedirect(
                    "jogos?erro=favorito"
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("jogos");
    }
}