package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {

    private static Connection connection=null;

    private DatabaseConnection(){

    }

    public static Connection getConnection() throws SQLException {
        if(connection ==null || connection.isClosed()) {
            try {
                Properties props = new Properties();
                InputStream input = DatabaseConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");

                if (input == null) {
                    System.out.println("Sorry, unable to find db.properties");
                    return null;
                }

                props.load(input);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connected successfully!");

            } catch (IOException e) {
                System.out.println("Error loading properties file: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}


