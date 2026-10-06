package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AtualizarJogos {

    public static void colocarCapas() {

        try {

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "UPDATE jogo SET capa = ? "
                    + "WHERE id = ?";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            // Resident Evil 4
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r7f.jpg");
            stmt.setInt(2, 1);
            stmt.executeUpdate();

            // The Last of Us
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co5s5x.jpg");
            stmt.setInt(2, 2);
            stmt.executeUpdate();

            // God of War
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co5vmg.jpg");
            stmt.setInt(2, 3);
            stmt.executeUpdate();

            // Minecraft
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co49x5.jpg");
            stmt.setInt(2, 4);
            stmt.executeUpdate();

            // Red Dead Redemption 2
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co1q1f.jpg");
            stmt.setInt(2, 5);
            stmt.executeUpdate();

            // GTA V
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co2lbd.jpg");
            stmt.setInt(2, 6);
            stmt.executeUpdate();

            // Silent Hill 2
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co7v9g.jpg");
            stmt.setInt(2, 7);
            stmt.executeUpdate();

            // Elden Ring
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co4jni.jpg");
            stmt.setInt(2, 8);
            stmt.executeUpdate();

            // Resident Evil Village
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co2l9z.jpg");
            stmt.setInt(2, 9);
            stmt.executeUpdate();

            // The Witcher 3
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co1wyy.jpg");
            stmt.setInt(2, 10);
            stmt.executeUpdate();

            // Cyberpunk 2077
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co2rzc.jpg");
            stmt.setInt(2, 11);
            stmt.executeUpdate();

            // Spider-Man 2
            stmt.setString(1,
                    "https://images.igdb.com/igdb/image/upload/t_cover_big/co6v1s.jpg");
            stmt.setInt(2, 12);
            stmt.executeUpdate();

            stmt.close();
            conexao.close();

            System.out.println(
                    "Capas atualizadas!"
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}