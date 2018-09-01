package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    public static Connection con;

    public static Connection getConnection() {

        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(PhoenixConfiguration.getConnectionUrl(), PhoenixConfiguration.getConnectionUser(), PhoenixConfiguration.getConnectionPassword());
                con.setAutoCommit(false);
                System.out.println("connection");
            }
        } catch (ClassNotFoundException ex) {
            PhoenixConfiguration.loggedRecored("ClassNotFoundException", ex.getMessage());
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("SQLException", ex.getMessage());
        }
        return con;
    }

    public static void rollbackresult() {
        try {
            con.rollback();
            System.out.println("Rollback Transation");
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("While rollback error ", ex.getMessage());
        }
    }

    public static void commit() {
        try {
            con.commit();
            System.out.println("Commit Transation");
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("While commiting error ", ex.getMessage());
        }
    }

}
