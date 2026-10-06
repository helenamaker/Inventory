package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/** Centraliza a localização e a conexão com o SQLite. */
public final class Conexao {

    private static final String PASTA_DADOS = definirPastaDados();

    private static final String URL =
            "jdbc:sqlite:" + PASTA_DADOS + File.separator + "inventory.db";

    static {
        File diretorio = new File(PASTA_DADOS);
        if (!diretorio.exists() && !diretorio.mkdirs()) {
            System.err.println("Não foi possível criar a pasta de dados: " + PASTA_DADOS);
        }
    }

    private Conexao() {
    }

    private static String definirPastaDados() {
        String configurada = System.getenv("INVENTORY_DATA_PATH");
        if (configurada != null && !configurada.trim().isEmpty()) {
            return configurada.trim();
        }

        String uploads = System.getenv("UPLOADS_PATH");
        if (uploads != null && !uploads.trim().isEmpty()) {
            return uploads.trim();
        }

        String sistema = System.getProperty("os.name", "").toLowerCase();
        return sistema.contains("win")
                ? "C:\\Inventory\\data"
                : "/app/data";
    }

    public static Connection conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao SQLite: " + e.getMessage());
            return null;
        }
    }

    public static String getPastaDados() {
        return PASTA_DADOS;
    }

    public static String getPastaFotos() {
        return PASTA_DADOS + File.separator + "perfil";
    }
}
