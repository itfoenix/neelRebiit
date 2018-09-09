package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Banks;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

public class BankOpearation {

    private PreparedStatement stm;

    public void saveBank(Banks b, StackPane window) {

        try {
            stm = Connector.getConnection().prepareStatement("insert into bank (name,branch,code) values(?,?,?)");
            stm.setString(1, b.getBankname());
            stm.setString(2, b.getBranch());
            stm.setString(3, b.getCode());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("बँकेची माहिती जतन झाली आहे.", "बँक माहिती", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Info("बँकेची माहिती जतन नाही झाली. पुन प्रयत्न करा", "बँक माहिती", window);
            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Bank insertion", ex.getMessage());
        }
    }

    public ObservableList<Banks> getBanksByCode(String code) {
        ObservableList<Banks> bankslist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from bank where code like ? OR name like ?");
            stm.setString(1, code + "%");
            stm.setString(2, code + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Banks b = new Banks();
                b.setBid(rs.getInt(1));
                b.setBankname(rs.getString(2));
                b.setBranch(rs.getString(3));
                b.setCode(rs.getString(4));
                bankslist.add(b);
            }

        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("Bank selection", ex.getMessage());
        }
        return bankslist;
    }

    public void updateBank(Banks b, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("update bank set name=?,branch=?,code=? where bid=?");
            stm.setString(1, b.getBankname());
            stm.setString(2, b.getBranch());
            stm.setString(3, b.getCode());
            stm.setInt(4, b.getBid());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("बँकेची माहिती जतन झाली आहे.", "बँक माहिती", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Info("बँकेची माहिती जतन नाही झाली. पुन प्रयत्न करा", "बँक माहिती", window);
            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Bank updation", ex.getMessage());
        }
    }

    public void deleteBank(int bid, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("delete from bank where bid=?");
            stm.setInt(1, bid);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("बँकेची माहित हटवली आहे", "बँक माहिती", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Info("बँकेची माहिती जतन नाही झाली. पुन प्रयत्न करा", "बँक माहिती", window);
            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Bank deletion", ex.getMessage());
        }
    }

}
