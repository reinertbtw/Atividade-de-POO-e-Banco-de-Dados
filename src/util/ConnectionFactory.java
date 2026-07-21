package util;

import java.sql.*;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/loja_poo?useTimezone=true&serverTimezone=UTC";

    // Usuário do banco de dados
    private static final String USER = "root";

    // Senha do usuário do banco de dados
    private static final String PASS = "@1@senac2021";

    public static Connection getConnection() {
        try {
            // Carrega o driver JDBC do MySQL.
            // 'com.mysql.cj.jdbc.Driver' é o nome da classe do driver para versões mais recentes do MySQL Connector/J.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Retorna a conexão usando a URL, usuário e senha definidos.
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            // Captura exceção se o driver não for encontrado (verifique se o JAR está no classpath)
            System.err.println("Erro: Driver JDBC do MySQL não encontrado. Verifique o JAR no classpath.");
            throw new RuntimeException("Erro ao carregar o driver JDBC.", e);
        } catch (SQLException e) {
            // Captura exceção se houver erro na conexão com o banco de dados
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public static void closeConnection(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o Statement: " + e.getMessage());
            }
        }
    }

    public static void closeConnection(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
            }
        }
    }
}