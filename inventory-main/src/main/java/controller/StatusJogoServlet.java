package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/status-jogo")
public class StatusJogoServlet extends HttpServlet {

    @Override
    protected void doGet(
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

            int idJogo =
                    Integer.parseInt(
                            request.getParameter("id")
                    );

            String status =
                    request.getParameter("status");

            if (status == null) {
                response.sendRedirect("biblioteca");
                return;
            }

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

            try (Connection conexao = Conexao.conectar();
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setString(1, status);
                stmt.setInt(2, idUsuario);
                stmt.setInt(3, idJogo);

                stmt.executeUpdate();
            }

            response.sendRedirect("biblioteca");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("biblioteca");
        }
    }
}