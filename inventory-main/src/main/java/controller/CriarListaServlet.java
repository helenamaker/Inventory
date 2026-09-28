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

@WebServlet("/criar-lista")
public class CriarListaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        Usuario usuario =
                (Usuario) sessao.getAttribute(
                        "usuario"
                );

        int idUsuario =
                usuario.getId();

        String nome =
                request.getParameter("nome");

        if (nome == null ||
                nome.trim().isEmpty()) {

            response.sendRedirect("listas");
            return;
        }

        nome =
                nome.trim();

        if (nome.length() > 80) {

            nome =
                    nome.substring(
                            0,
                            80
                    );
        }

        Connection conexao = null;
        PreparedStatement tabela = null;
        PreparedStatement stmt = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                response.sendRedirect("listas");
                return;
            }

            // =================================================
            // GARANTIR TABELA
            // =================================================

            String sqlTabela =
                    "CREATE TABLE IF NOT EXISTS lista ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_usuario INTEGER NOT NULL,"
                    + "nome TEXT NOT NULL,"
                    + "data_criacao TEXT "
                    + "DEFAULT CURRENT_TIMESTAMP"
                    + ")";

            tabela =
                    conexao.prepareStatement(
                            sqlTabela
                    );

            tabela.executeUpdate();

            tabela.close();
            tabela = null;

            // =================================================
            // INSERIR LISTA
            // =================================================

            String sql =
                    "INSERT INTO lista "
                    + "(id_usuario, nome) "
                    + "VALUES (?, ?)";

            stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setInt(
                    1,
                    idUsuario
            );

            stmt.setString(
                    2,
                    nome
            );

            stmt.executeUpdate();

            System.out.println(
                    "LISTA CRIADA: "
                    + nome
                    + " | USUARIO: "
                    + idUsuario
            );

            response.sendRedirect(
                    "listas"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "listas"
            );

        } finally {

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (tabela != null) {
                    tabela.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}