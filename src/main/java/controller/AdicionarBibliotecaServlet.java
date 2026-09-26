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

@WebServlet("/adicionar-biblioteca")
public class AdicionarBibliotecaServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);

        // Verificar login
        if (sessao == null ||
            sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        String idTexto = request.getParameter("id");

        if (idTexto == null ||
            idTexto.trim().isEmpty()) {

            response.sendRedirect("jogos");
            return;
        }

        try {

            int steamAppId = Integer.parseInt(idTexto);

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario = usuario.getId();

            Connection conexao =
                    Conexao.conectar();

            // Verificar se já está na biblioteca
            String verificar =
                    "SELECT id FROM biblioteca " +
                    "WHERE id_usuario = ? " +
                    "AND steam_app_id = ?";

            PreparedStatement stmtVerificar =
                    conexao.prepareStatement(verificar);

            stmtVerificar.setInt(1, idUsuario);
            stmtVerificar.setInt(2, steamAppId);

            ResultSet resultado =
                    stmtVerificar.executeQuery();

            boolean existe = resultado.next();

            resultado.close();
            stmtVerificar.close();

            // Se ainda não estiver, adicionar
            if (!existe) {

                String inserir =
                        "INSERT INTO biblioteca " +
                        "(id_usuario, steam_app_id, status) " +
                        "VALUES (?, ?, ?)";

                PreparedStatement stmtInserir =
                        conexao.prepareStatement(inserir);

                stmtInserir.setInt(1, idUsuario);
                stmtInserir.setInt(2, steamAppId);
                stmtInserir.setString(3, "quero jogar");

                stmtInserir.executeUpdate();

                stmtInserir.close();
            }

            conexao.close();

            response.sendRedirect("jogos");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("jogos");
        }
    }
}