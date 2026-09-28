package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BibliotecaDAO {

    // =========================
    // ADICIONAR JOGO
    // =========================

    public boolean adicionar(
            int idUsuario,
            int idJogo,
            String status) {

        String sql =
                "INSERT INTO biblioteca "
                + "(id_usuario, id_jogo, status) "
                + "VALUES (?, ?, ?)";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);
            stmt.setString(3, status);

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // VERIFICAR JOGO
    // =========================

    public boolean possuiJogo(
            int idUsuario,
            int idJogo) {

        String sql =
                "SELECT * FROM biblioteca "
                + "WHERE id_usuario = ? "
                + "AND id_jogo = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);

            ResultSet resultado =
                    stmt.executeQuery();

            boolean existe =
                    resultado.next();

            resultado.close();
            stmt.close();
            conexao.close();

            return existe;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // REMOVER JOGO
    // =========================

    public boolean remover(
            int idUsuario,
            int idJogo) {

        String sql =
                "DELETE FROM biblioteca "
                + "WHERE id_usuario = ? "
                + "AND id_jogo = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // LISTAR BIBLIOTECA
    // =========================

    public ArrayList<String[]> listar(
            int idUsuario) {

        ArrayList<String[]> jogos =
                new ArrayList<>();

        String sql =
                "SELECT "
                + "jogo.id, "
                + "jogo.titulo, "
                + "jogo.genero, "
                + "jogo.plataforma, "
                + "jogo.capa, "
                + "biblioteca.status "
                + "FROM biblioteca "
                + "INNER JOIN jogo "
                + "ON biblioteca.id_jogo = jogo.id "
                + "WHERE biblioteca.id_usuario = ? "
                + "ORDER BY jogo.titulo";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                String[] jogo = {

                    resultado.getString("id"),

                    resultado.getString("titulo"),

                    resultado.getString("genero"),

                    resultado.getString("plataforma"),

                    resultado.getString("capa"),

                    resultado.getString("status")
                };

                jogos.add(jogo);
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jogos;
    }
}