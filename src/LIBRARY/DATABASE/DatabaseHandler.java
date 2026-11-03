package LIBRARY.DATABASE;

import LIBRARY.HOME.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Creates and manages the application's connection to the Apache Derby
 * Network Server. The server must be running before the application starts.
 *
 * Database credentials are read from environment variables and are never
 * stored in the source code:
 *   KGL_DB_USER (defaults to KGL)
 *   KGL_DB_PASSWORD (required)
 */
public final class DatabaseHandler {

    private static DatabaseHandler handler;
    private static Connection conn;
    private static String DB_URL;

    private DatabaseHandler() {
        ConnectDb();
    }

    public static synchronized DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public static synchronized Connection ConnectDb() {
        try {
            config.openFile();
            config.readFile();
            config.closeFile();

            DB_URL = "jdbc:derby://" + config.textline[3]
                    + ":1527/" + config.textline[4] + ";";

            String dbUser = System.getenv("KGL_DB_USER");
            if (dbUser == null || dbUser.trim().isEmpty()) {
                dbUser = "KGL";
            }

            String dbPassword = System.getenv("KGL_DB_PASSWORD");
            if (dbPassword == null || dbPassword.isEmpty()) {
                throw new SQLException(
                        "KGL_DB_PASSWORD is not configured. Set it in your environment before starting the application.");
            }

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(DB_URL, dbUser, dbPassword);
            return conn;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to connect to the School Library database.\n"
                    + e.getMessage(),
                    "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
