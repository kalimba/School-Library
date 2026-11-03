/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LIBRARY.DATABASE;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTransaction {

    private Connection conn;

    public DBTransaction(Connection conn){
        this.conn = conn;
    }

    public void begin() throws SQLException{
        conn.setAutoCommit(false);
    }

    public void commit() throws SQLException{
        conn.commit();
        conn.setAutoCommit(true);
    }

    public void rollback(){
        try{
            conn.rollback();
            conn.setAutoCommit(true);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}