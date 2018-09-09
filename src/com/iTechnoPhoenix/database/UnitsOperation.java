package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Unit;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

public class UnitsOperation {

    private PreparedStatement stm;

    public void addUnits(Unit u, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("insert into units (min_unit,max_unit,unit_price,rdate) values(?,?,?,now())");
            stm.setInt(1, u.getMin());
            stm.setInt(2, u.getMax());
            stm.setDouble(3, u.getUnitprice());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("युनिट माहित जतन झाली आहे ", "युनिट माहिती", window);
            } else {
                PhoenixSupport.Error("युनिट माहित जनता नाही झाली", window);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Units Insertion ", ex.getMessage());
        }
    }

    public void updateUnits(Unit u, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("update units set min_unit=?,max_unit=?,unit_price=?,rdate=now() where uid=?");
            stm.setInt(1, u.getMin());
            stm.setInt(2, u.getMax());
            stm.setDouble(3, u.getUnitprice());
            stm.setInt(4, u.getId());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("युनिट माहित जतन झाली आहे ", "युनिट माहिती", window);
            } else {
                PhoenixSupport.Error("युनिट माहित जनता नाही झाली", window);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Units updation ", ex.getMessage());
        }
    }

    public void deleteUnits(int id, StackPane window) {
        try {
            stm = Connector.getConnection().prepareStatement("delete from units where uid=?");
            stm.setInt(1, id);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("युनिट हटवले गेले आहे.", "युनिट माहिती", window);
            } else {
                PhoenixSupport.Error("युनिट हटवले गेले नाही.", window);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Units deletion ", ex.getMessage());
        }
    }

    public ObservableList<Unit> getAllUnits() {
        ObservableList<Unit> Unitslist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from units");

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Unit u = new Unit();
                u.setId(rs.getInt(1));
                u.setMin(rs.getInt(2));
                u.setMax(rs.getInt(3));
                u.setUnitprice(rs.getDouble(4));
                Unitslist.add(u);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Units Selection ", ex.getMessage());
        }
        return Unitslist;
    }

//    public boolean checkUnits(Unit unit) {
//        boolean check = false;
//        try {
//            stm = Connector.getConnection().prepareStatement("select * from units where min_unit = ? and max_unit = ?");
//            stm.setInt(1, unit.getMin());
//            stm.setInt(2, unit.getMax());
//            ResultSet rs = stm.executeQuery();
//            if (rs.next()) {
//
//            }
//        } catch (SQLException ex) {
//            Connector.rollbackresult();
//            PhoenixConfiguration.loggedRecored("Units deletion ", ex.getMessage());
//        }
//    }
}
