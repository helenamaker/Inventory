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

        HttpSession sessao =
                request.getSession(false);

        // ==========================================
        // VERIFICAR LOGIN
        // ==========================================

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        // ==========================================
        // PEGAR STEAM APP ID
        // ==========================================

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.sendRedirect("jogos");
            return;
        }

        Connection conexao = null;
        PreparedStatement stmtVerificar = null;
        PreparedStatement stmtInserir = null;
        ResultSet resultado = null;

        try {

            int steamAppId =
                    Integer.parseInt(idTexto);

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            // ==========================================
            // CONECTAR AO BANCO
            // ==========================================

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                throw new Exception(
                        "Não foi possível conectar ao banco."
                );
            }

            // ==========================================
            // VERIFICAR SE JÁ EXISTE
            // ==========================================

            String verificar =
                    "SELECT id " +
                    "FROM biblioteca " +
                    "WHERE id_usuario = ? " +
                    "AND steam_app_id = ?";

            stmtVerificar =
                    conexao.prepareStatement(verificar);

            stmtVerificar.setInt(
                    1,
                    idUsuario
            );

            stmtVerificar.setInt(
                    2,
                    steamAppId
            );

            resultado =
                    stmtVerificar.executeQuery();

            boolean existe =
                    resultado.next();

            resultado.close();
            resultado = null;

            stmtVerificar.close();
            stmtVerificar = null;

            // ==========================================
            // ADICIONAR
            // ==========================================

            if (!existe) {

                String inserir =
                        "INSERT INTO biblioteca " +
                        "(id_usuario, steam_app_id, status) " +
                        "VALUES (?, ?, ?)";

                stmtInserir =
                        conexao.prepareStatement(inserir);

                stmtInserir.setInt(
                        1,
                        idUsuario
                );

                stmtInserir.setInt(
                        2,
                        steamAppId
                );

                stmtInserir.setString(
                        3,
                        "quero jogar"
                );

                stmtInserir.executeUpdate();

                System.out.println(
                        "================================="
                );

                System.out.println(
                        "JOGO ADICIONADO À BIBLIOTECA!"
                );

                System.out.println(
                        "USUARIO: " + idUsuario
                );

                System.out.println(
                        "STEAM APP ID: " + steamAppId
                );

                System.out.println(
                        "STATUS: quero jogar"
                );

                System.out.println(
                        "================================="
                );

            } else {

                System.out.println(
                        "JOGO JÁ ESTÁ NA BIBLIOTECA!"
                );
            }

            // ==========================================
            // FECHAR
            // ==========================================

            if (stmtInserir != null) {
                stmtInserir.close();
            }

            conexao.close();

            // ==========================================
            // VOLTAR PARA JOGOS
            // ==========================================

            response.sendRedirect("jogos");

        } catch (NumberFormatException e) {

            System.out.println(
                    "Steam AppID inválido: " + idTexto
            );

            response.sendRedirect("jogos");

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO ADICIONAR À BIBLIOTECA:"
            );

            e.printStackTrace();

            System.out.println(
                    "================================="
            );

            response.sendRedirect("jogos");

        } finally {

            try {

                if (resultado != null) {
                    resultado.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (stmtVerificar != null) {
                    stmtVerificar.close();
                }

            } catch (Exception ignored) {
            }

            try {

                if (stmtInserir != null) {
                    stmtInserir.close();
                }

            } catch (Exception ignored) {
            }
        }
    }
}