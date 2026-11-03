/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LIBRARY.DATABASE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static Connection conn;

    private static final String DRIVER =
            "org.apache.derby.jdbc.EmbeddedDriver";

    private static final String URL =
            "jdbc:derby:LibraryDB;create=true";

    private DBConnection(){}

    // ===== GET CONNECTION =====
    public static synchronized Connection getConnection(){

        try{

            if(conn == null || conn.isClosed()){

                Class.forName(DRIVER);

                conn = DriverManager.getConnection(URL);

                conn.setAutoCommit(true); // default mode
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return conn;
    }
}