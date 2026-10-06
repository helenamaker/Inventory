package controller;

import dao.Conexao;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/biblioteca-status")
public class BibliotecaStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
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

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            String steamAppIdTexto =
                    request.getParameter("steamAppId");

            String status =
                    request.getParameter("status");

            if (steamAppIdTexto == null ||
                    status == null) {

                response.sendRedirect("biblioteca");
                return;
            }

            int steamAppId =
                    Integer.parseInt(steamAppIdTexto);

            if (!status.equals("quero_jogar") &&
                    !status.equals("jogando") &&
                    !status.equals("zerado")) {

                response.sendRedirect("biblioteca");
                return;
            }

            String sql =
                    "UPDATE biblioteca " +
                    "SET status = ? " +
                    "WHERE id_usuario = ? " +
                    "AND steam_app_id = ?";

            try (
                    Connection conexao =
                            Conexao.conectar();

                    PreparedStatement ps =
                            conexao.prepareStatement(sql)
            ) {

                ps.setString(1, status);
                ps.setInt(2, idUsuario);
                ps.setInt(3, steamAppId);

                ps.executeUpdate();
            }

            response.sendRedirect("biblioteca");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("biblioteca");
        }
    }
}