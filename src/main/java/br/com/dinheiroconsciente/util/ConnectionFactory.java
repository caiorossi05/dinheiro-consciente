package br.com.dinheiroconsciente.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // URL do banco com UTF-8 configurado
    private static final String URL =
            "jdbc:mysql://localhost:3306/dinheiro_consciente" +
                    "?useSSL=false" +
                    "&allowPublicKeyRetrieval=true" +
                    "&useUnicode=true" +
                    "&characterEncoding=UTF-8" +
                    "&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "Skate2005";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
            throw new RuntimeException("Falha na conexão com o banco de dados.", e);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL não encontrado.");
            e.printStackTrace();
            throw new RuntimeException("Driver JDBC não encontrado.", e);
        }
    }
}
