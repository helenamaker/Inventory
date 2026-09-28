
package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    /*
     * Pasta onde ficarão os dados permanentes
     *
     * No Railway, vamos montar um Volume
     * em /app/data
     *
     * No seu computador, será usado:
     * C:\GameBoxdUploads\data
     */

    private static final String PASTA_DADOS;

    static {

        String sistema =
                System.getProperty("os.name")
                .toLowerCase();

        if (sistema.contains("win")) {

            PASTA_DADOS =
                    "C:\\GameBoxdUploads\\data";

        } else {

            PASTA_DADOS =
                    "/app/data";
        }

        File diretorio =
                new File(PASTA_DADOS);

        if (!diretorio.exists()) {

            diretorio.mkdirs();
        }
    }

    /*
     * Banco SQLite permanente
     */

    private static final String URL =
            "jdbc:sqlite:"
            + PASTA_DADOS
            + File.separator
            + "gameboxd.db";

    public static Connection conectar() {

        try {

            Class.forName(
                    "org.sqlite.JDBC"
            );

            Connection conexao =
                    DriverManager.getConnection(
                            URL
                    );

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "CONEXAO COM SQLITE OK!"
            );

            System.out.println(
                    "BANCO: " + URL
            );

            System.out.println(
                    "================================="
            );

            return conexao;

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO CONECTAR COM SQLITE:"
            );

            System.out.println(
                    "================================="
            );

            e.printStackTrace();

            return null;
        }
    }

    public static String getPastaDados() {

        return PASTA_DADOS;
    }
}

