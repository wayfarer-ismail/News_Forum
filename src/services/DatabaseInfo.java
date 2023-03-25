package services;

import java.sql.*;

/**
 * This class contains all the information that is common to all the JDBC services and database access.
 */
public class DatabaseInfo {

    public static final String DB_URL = "jdbc:sqlite:src/resources/COMMUNITY_NEWS.db";

    public enum Tables {
        USERS ("USERS"),
        CURRENT_USER ("CURRENT_USER"),
        POSTS ("POSTS"),
        COMMENTS ("COMMENTS");

        public final String label;

        Tables(String label) {
            this.label = label;
        }
    }

    /*
    Method to test the drivers are found in the classpath
     */
    public static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("The driver was successfully loaded.");
            Thread.sleep(1000);
        } catch (ClassNotFoundException | InterruptedException e) {
            System.out.println("The driver class was not found in the program files at runtime.");
            System.out.println(e);
            System.exit(1);
        }
    }

    /*
    Method to test the connection to the database
     */
    public static void testDatabaseConnection() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            System.out.println("The connection to the SQLite database was successful!");
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("The connection to the database was unsuccessful!");
            System.out.println(e);
        }
    }
}
