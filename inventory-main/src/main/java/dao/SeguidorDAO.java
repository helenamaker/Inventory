package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SeguidorDAO {

    // =========================
    // SEGUIR USUARIO
    // =========================

    public boolean seguir(int idSeguidor, int idSeguido) {

        if (idSeguidor == idSeguido) {
            return false;
        }

        String sql =
                "INSERT INTO seguidor "
                + "(id_seguidor, id_seguido) "
                + "VALUES (?, ?)";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idSeguidor);
            stmt.setInt(2, idSeguido);

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO SEGUIR USUARIO:"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // VERIFICAR SE JA SEGUE
    // =========================

    public boolean seguindo(
            int idSeguidor,
            int idSeguido) {

        String sql =
                "SELECT * FROM seguidor "
                + "WHERE id_seguidor = ? "
                + "AND id_seguido = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idSeguidor);
            stmt.setInt(2, idSeguido);

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
    // DEIXAR DE SEGUIR
    // =========================

    public boolean deixarDeSeguir(
            int idSeguidor,
            int idSeguido) {

        String sql =
                "DELETE FROM seguidor "
                + "WHERE id_seguidor = ? "
                + "AND id_seguido = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idSeguidor);
            stmt.setInt(2, idSeguido);

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
    // CONTAR SEGUIDORES
    // =========================

    public int contarSeguidores(int idUsuario) {

        String sql =
                "SELECT COUNT(*) FROM seguidor "
                + "WHERE id_seguido = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet resultado =
                    stmt.executeQuery();

            int quantidade = 0;

            if (resultado.next()) {
                quantidade =
                        resultado.getInt(1);
            }

            resultado.close();
            stmt.close();
            conexao.close();

            return quantidade;

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }
}