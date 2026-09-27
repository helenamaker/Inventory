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

@WebServlet("/excluir-conta")
public class ExcluirContaServlet extends HttpServlet {

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

        Connection conexao = null;

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                throw new Exception(
                        "Não foi possível conectar ao banco."
                );
            }

            conexao.setAutoCommit(false);

            // =====================================================
            // EXCLUIR JOGOS DAS LISTAS DO USUÁRIO
            // =====================================================

            PreparedStatement stmtListaJogos =
                    conexao.prepareStatement(
                            "DELETE FROM lista_jogo " +
                            "WHERE id_lista IN (" +
                            "SELECT id FROM lista " +
                            "WHERE id_usuario = ?" +
                            ")"
                    );

            stmtListaJogos.setInt(1, idUsuario);

            stmtListaJogos.executeUpdate();

            stmtListaJogos.close();

            // =====================================================
            // EXCLUIR LISTAS
            // =====================================================

            PreparedStatement stmtListas =
                    conexao.prepareStatement(
                            "DELETE FROM lista " +
                            "WHERE id_usuario = ?"
                    );

            stmtListas.setInt(1, idUsuario);

            stmtListas.executeUpdate();

            stmtListas.close();

            // =====================================================
            // EXCLUIR FAVORITOS
            // =====================================================

            PreparedStatement stmtFavoritos =
                    conexao.prepareStatement(
                            "DELETE FROM favorito " +
                            "WHERE id_usuario = ?"
                    );

            stmtFavoritos.setInt(1, idUsuario);

            stmtFavoritos.executeUpdate();

            stmtFavoritos.close();

            // =====================================================
            // EXCLUIR BIBLIOTECA
            // =====================================================

            PreparedStatement stmtBiblioteca =
                    conexao.prepareStatement(
                            "DELETE FROM biblioteca " +
                            "WHERE id_usuario = ?"
                    );

            stmtBiblioteca.setInt(1, idUsuario);

            stmtBiblioteca.executeUpdate();

            stmtBiblioteca.close();

            // =====================================================
            // EXCLUIR AVALIAÇÕES
            // =====================================================

            PreparedStatement stmtAvaliacao =
                    conexao.prepareStatement(
                            "DELETE FROM avaliacao " +
                            "WHERE id_usuario = ?"
                    );

            stmtAvaliacao.setInt(1, idUsuario);

            stmtAvaliacao.executeUpdate();

            stmtAvaliacao.close();

            // =====================================================
            // EXCLUIR SEGUIDORES
            // =====================================================

            PreparedStatement stmtSeguidor =
                    conexao.prepareStatement(
                            "DELETE FROM seguidor " +
                            "WHERE id_seguidor = ? " +
                            "OR id_seguido = ?"
                    );

            stmtSeguidor.setInt(1, idUsuario);
            stmtSeguidor.setInt(2, idUsuario);

            stmtSeguidor.executeUpdate();

            stmtSeguidor.close();

            // =====================================================
            // EXCLUIR USUÁRIO
            // =====================================================

            PreparedStatement stmtUsuario =
                    conexao.prepareStatement(
                            "DELETE FROM usuario " +
                            "WHERE id = ?"
                    );

            stmtUsuario.setInt(1, idUsuario);

            stmtUsuario.executeUpdate();

            stmtUsuario.close();

            // =====================================================
            // CONFIRMAR TRANSAÇÃO
            // =====================================================

            conexao.commit();

            // =====================================================
            // ENCERRAR SESSÃO
            // =====================================================

            sessao.invalidate();

            response.sendRedirect("index.html");

        } catch (Exception e) {

            e.printStackTrace();

            if (conexao != null) {

                try {
                    conexao.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            response.sendRedirect("perfil");

        } finally {

            if (conexao != null) {

                try {
                    conexao.close();
                } catch (Exception closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }
}