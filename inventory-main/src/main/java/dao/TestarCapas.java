package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestarCapas {

    public static void main(String[] args) {

        try {

            Connection conexao = Conexao.conectar();

            String sql =
                    "SELECT id, titulo, capa FROM jogo ORDER BY id";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                System.out.println(
                        "ID: " + rs.getInt("id") +
                        " | JOGO: " + rs.getString("titulo") +
                        " | CAPA: [" + rs.getString("capa") + "]"
                );
            }

            rs.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}