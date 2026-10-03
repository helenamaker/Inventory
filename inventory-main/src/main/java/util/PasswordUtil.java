package util;

import org.mindrot.jbcrypt.BCrypt;

/** Utilitário central para proteção de senhas. */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String senha) {
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    public static boolean matches(String senha, String hash) {
        if (senha == null || hash == null || !isHash(hash)) {
            return false;
        }
        return BCrypt.checkpw(senha, hash);
    }

    public static boolean isHash(String valor) {
        return valor != null &&
                valor.matches("^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
    }
}
