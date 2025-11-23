// br.com.dinheiroconsciente.util.PasswordUtil
package br.com.dinheiroconsciente.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        // BCrypt.gensalt() gera um 'salt' aleatório para aumentar a segurança do hash
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}