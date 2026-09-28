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

@WebServlet("/adicionar-jogo-lista")
public class AdicionarJogoListaServlet extends HttpServlet {

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

        Connection conexao = null;
        PreparedStatement criarTabela = null;
        PreparedStatement verificarLista = null;
        PreparedStatement verificarJogo = null;
        PreparedStatement inserir = null;

        ResultSet rsLista = null;
        ResultSet rsJogo = null;

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

            String idJogoTexto =
                    request.getParameter(
                            "idJogo"
                    );

            if (idListaTexto == null ||
                    idJogoTexto == null) {

                response.sendRedirect("listas");
                return;
            }

            int idLista =
                    Integer.parseInt(
                            idListaTexto
                    );

            int idJogo =
                    Integer.parseInt(
                            idJogoTexto
                    );

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                response.sendRedirect("listas");
                return;
            }

            // =================================================
            // GARANTIR LISTA_JOGO
            // =================================================

            String sqlTabela =
                    "CREATE TABLE IF NOT EXISTS lista_jogo ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_lista INTEGER NOT NULL,"
                    + "id_jogo INTEGER NOT NULL,"
                    + "data_adicionado TEXT "
                    + "DEFAULT CURRENT_TIMESTAMP,"
                    + "UNIQUE(id_lista,id_jogo)"
                    + ")";

            criarTabela =
                    conexao.prepareStatement(
                            sqlTabela
                    );

            criarTabela.executeUpdate();

            criarTabela.close();
            criarTabela = null;

            // =================================================
            // VERIFICAR DONO DA LISTA
            // =================================================

            String sqlLista =
                    "SELECT id "
                    + "FROM lista "
                    + "WHERE id = ? "
                    + "AND id_usuario = ?";

            verificarLista =
                    conexao.prepareStatement(
                            sqlLista
                    );

            verificarLista.setInt(
                    1,
                    idLista
            );

            verificarLista.setInt(
                    2,
                    idUsuario
            );

            rsLista =
                    verificarLista.executeQuery();

            if (!rsLista.next()) {

                System.out.println(
                        "LISTA NAO PERTENCE AO USUARIO"
                );

                response.sendRedirect(
                        "listas"
                );

                return;
            }

            rsLista.close();
            rsLista = null;

            verificarLista.close();
            verificarLista = null;

            // =================================================
            // VERIFICAR SE JOGO EXISTE
            // =================================================

            String sqlJogo =
                    "SELECT id "
                    + "FROM jogo "
                    + "WHERE id = ?";

            verificarJogo =
                    conexao.prepareStatement(
                            sqlJogo
                    );

            verificarJogo.setInt(
                    1,
                    idJogo
            );

            rsJogo =
                    verificarJogo.executeQuery();

            if (!rsJogo.next()) {

                System.out.println(
                        "JOGO NAO EXISTE: "
                        + idJogo
                );

                response.sendRedirect(
                        "listas"
                );

                return;
            }

            rsJogo.close();
            rsJogo = null;

            verificarJogo.close();
            verificarJogo = null;

            // =================================================
            // ADICIONAR
            // =================================================

            String sqlInserir =
                    "INSERT OR IGNORE INTO lista_jogo "
                    + "(id_lista,id_jogo) "
                    + "VALUES (?,?)";

            inserir =
                    conexao.prepareStatement(
                            sqlInserir
                    );

            inserir.setInt(
                    1,
                    idLista
            );

            inserir.setInt(
                    2,
                    idJogo
            );

            int resultado =
                    inserir.executeUpdate();

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "ADICIONAR JOGO NA LISTA"
            );

            System.out.println(
                    "USUARIO: "
                    + idUsuario
            );

            System.out.println(
                    "LISTA: "
                    + idLista
            );

            System.out.println(
                    "JOGO: "
                    + idJogo
            );

            System.out.println(
                    "RESULTADO: "
                    + resultado
            );

            System.out.println(
                    "================================"
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
                if (rsLista != null) {
                    rsLista.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (rsJogo != null) {
                    rsJogo.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (criarTabela != null) {
                    criarTabela.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (verificarLista != null) {
                    verificarLista.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (verificarJogo != null) {
                    verificarJogo.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (inserir != null) {
                    inserir.close();
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