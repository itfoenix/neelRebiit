package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.User;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

public class UserOperation {

    private PreparedStatement stm;

    public void addUser(User u, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("insert into users (name,username,password,role,rdate) values(?,?,?,?,now())");
            stm.setString(1, u.getName());
            stm.setString(2, u.getUsername());
            stm.setString(3, u.getPassword());
            stm.setInt(4, u.getRole());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("जतन झालं", "user माहिती", window);
            } else {
                PhoenixSupport.Error("जतन नाही झालं", window);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("users Insertion ", ex.getMessage());
        }
    }

    public void updateUser(User u, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("update users set name=?,username=?,password=?,role=?,rdate=now() where uid=?");
            stm.setString(1, u.getName());
            stm.setString(2, u.getUsername());
            stm.setString(3, u.getPassword());
            stm.setInt(4, u.getRole());
            stm.setInt(5, u.getId());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("जतन झालं", "user माहिती", window);
            } else {
                PhoenixSupport.Error("जतन नाही झालं", window);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("users updation ", ex.getMessage());
        }
    }

    public void deleteUser(int id, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("delete from users where uid=?");
            stm.setInt(1, id);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("Userची माहिती काढून टाकली.", "User Master", window);
            } else {
                PhoenixSupport.Error("Userची माहिती नाही काढली आहे.", window);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("User deletion ", ex.getMessage());
        }

    }

    public ObservableList<User> getUserByName(String name) {
        ObservableList<User> Userlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from users where name like ?");
            stm.setString(1, name + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt(1));
                u.setName(rs.getString(2));
                u.setUsername(rs.getString(3));
                u.setPassword(rs.getString(4));
                u.setRole(rs.getInt(5));
                Userlist.add(u);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("users Selection ", ex.getMessage());
        }
        return Userlist;
    }

    public ObservableList<User> getAllUser() {
        ObservableList<User> Userlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from users");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt(1));
                u.setName(rs.getString(2));
                u.setUsername(rs.getString(3));
                u.setPassword(rs.getString(4));
                u.setRole(rs.getInt(5));
                Userlist.add(u);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("users Selection ", ex.getMessage());
        }
        return Userlist;
    }

    public User login(String username, String password) {

        User u = null;
        try {
            stm = Connector.getConnection().prepareStatement("select * from users where username=? and password=?");
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                u = new User();
                u.setId(rs.getInt(1));
                u.setName(rs.getString(2));
                u.setUsername(rs.getString(3));
                u.setRole(rs.getInt(5));
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("login ", ex.getMessage());
        }
        return u;
    }

}
