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

@WebServlet("/excluir-lista")
public class ExcluirListaServlet extends HttpServlet {

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

        Connection conexao = null;
        PreparedStatement verificar = null;
        PreparedStatement apagarJogos = null;
        PreparedStatement apagarLista = null;

        ResultSet rs = null;

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

            int idUsuario =
                    usuario.getId();

            String idListaTexto =
                    request.getParameter(
                            "idLista"
                    );

            if (idListaTexto == null) {

                response.sendRedirect("listas");
                return;
            }

            int idLista =
                    Integer.parseInt(
                            idListaTexto
                    );

            conexao =
                    Conexao.conectar();

            // =================================================
            // VERIFICAR DONO
            // =================================================

            String sqlVerificar =
                    "SELECT id "
                    + "FROM lista "
                    + "WHERE id = ? "
                    + "AND id_usuario = ?";

            verificar =
                    conexao.prepareStatement(
                            sqlVerificar
                    );

            verificar.setInt(
                    1,
                    idLista
            );

            verificar.setInt(
                    2,
                    idUsuario
            );

            rs =
                    verificar.executeQuery();

            if (!rs.next()) {

                response.sendRedirect(
                        "listas"
                );

                return;
            }

            rs.close();
            rs = null;

            verificar.close();
            verificar = null;

            // =================================================
            // APAGAR JOGOS
            // =================================================

            String sqlApagarJogos =
                    "DELETE FROM lista_jogo "
                    + "WHERE id_lista = ?";

            apagarJogos =
                    conexao.prepareStatement(
                            sqlApagarJogos
                    );

            apagarJogos.setInt(
                    1,
                    idLista
            );

            apagarJogos.executeUpdate();

            apagarJogos.close();
            apagarJogos = null;

            // =================================================
            // APAGAR LISTA
            // =================================================

            String sqlApagarLista =
                    "DELETE FROM lista "
                    + "WHERE id = ? "
                    + "AND id_usuario = ?";

            apagarLista =
                    conexao.prepareStatement(
                            sqlApagarLista
                    );

            apagarLista.setInt(
                    1,
                    idLista
            );

            apagarLista.setInt(
                    2,
                    idUsuario
            );

            apagarLista.executeUpdate();

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
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (verificar != null) {
                    verificar.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (apagarJogos != null) {
                    apagarJogos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (apagarLista != null) {
                    apagarLista.close();
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