// br.com.dinheiroconsciente.util.PasswordUtil
package br.com.dinheiroconsciente.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Gera o hash (cadeia de caracteres segura) da senha em texto puro.
     */
    public static String hashPassword(String plainTextPassword) {
        // BCrypt.gensalt() gera um 'salt' aleatório para aumentar a segurança do hash
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Verifica se a senha em texto puro fornecida corresponde ao hash armazenado.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}