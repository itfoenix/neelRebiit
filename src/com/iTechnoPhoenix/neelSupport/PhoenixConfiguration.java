package com.iTechnoPhoenix.neelSupport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PhoenixConfiguration {

    public static Properties prop = new Properties();

    public static String username;

    public static void setUsername(String name) {
        username = name;

    }

    public static String getUsername() {
        return username;
    }

    public static String getConnectionUrl() {

        String value = "";
        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            value = prop.getProperty("url");
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public static String getConnectionUser() {

        String value = "";
        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            value = prop.getProperty("username");
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public static String getConnectionPassword() {

        String value = "";
        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            value = prop.getProperty("password");
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public static ObservableList<String> getRoles() {

        ObservableList<String> roles = FXCollections.observableArrayList();

        try {
            String value = "";
            prop.load(new FileInputStream("Configuration.cfg"));
            value = prop.getProperty("Roles");
            String[] role = value.split(",");
            for (String s : role) {
                roles.add(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roles;
    }

    public static ObservableList<String> getMonth() {
        ObservableList<String> roles = FXCollections.observableArrayList();

        try {
            String value = "";
            prop.load(new FileInputStream("Configuration.cfg"));
            value = prop.getProperty("Month");
            String[] role = value.split(",");
            for (String s : role) {
                roles.add(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roles;
    }

    public static ObservableList<String> getYear() {
        ObservableList<String> roles = FXCollections.observableArrayList();

        try {
            String value = "";
            prop.load(new FileInputStream("Configuration.cfg"));
            value = prop.getProperty("Year");
            String[] role = value.split(",");
            for (String s : role) {
                roles.add(s);
            }

        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roles;
    }

    public static double getWidth() {

        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            String value = prop.getProperty("width");
            return Double.valueOf(value);
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            return 900.00;
        }
    }

    public static double getheight() {
        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            String value = prop.getProperty("height");
            return Double.valueOf(value);
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            return 600.00;
        }
    }

    public static String getBackUplocation() {

        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            String value = prop.getProperty("backup");
            return value;
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getSqlDump() {

        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            String value = prop.getProperty("Dumplocation");
            return value;
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getSql() {

        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            String value = prop.getProperty("mysqlpath");
            return value;
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getSoftwareRegistration() {

        try {
            prop.load(new FileInputStream("Configuration.cfg"));
            String value = prop.getProperty("owner");
            return value;
        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void loggedRecored(String title, String value) {

        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateobj = new Date();
            prop.setProperty(df.format(dateobj) + " =>> " + title, value);
            prop.store(new FileOutputStream("Logged.log"), null);

        } catch (IOException ex) {
            Logger.getLogger(PhoenixConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
