package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MeterOperation {

    private PreparedStatement stm;

    public int addMeter(Meter m) {
        int i = 0;
        try {
            stm = Connector.getConnection().prepareStatement("insert into meter (meter_num,con_date,curr_reading,outstanding,deposit,rdate,cust_id) values(?,?,?,?,?,?,?)");
            stm.setString(1, m.getMetor_num());
            stm.setString(2, m.getCon_date().toString());
            stm.setLong(3, m.getCurr_reading());
            stm.setDouble(4, m.getOutstanding());
            stm.setDouble(5, m.getDeposit());
            stm.setDate(6, Date.valueOf(LocalDate.now()));
            stm.setInt(7, m.getCustomeObject().getCust_num());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
            } else {
                PhoenixSupport.Error("माहिती जतन नाही झाली. पुन प्रयत्न करा");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Meter Insertion ", ex.getMessage());
        }
        return i;
    }

    public void updateMeter(Meter m) {
        try {
            stm = Connector.getConnection().prepareStatement("update meter set meter_num=?,con_date=?,curr_reading=?, outstanding=?,deposit=?,rdate=?,cust_id=? where id=?");
            stm.setString(1, m.getMetor_num());
            stm.setString(2, m.getCon_date().toString());
            stm.setLong(3, m.getCurr_reading());
            stm.setDouble(4, m.getOutstanding());
            stm.setDouble(5, m.getDeposit());
            stm.setDate(6, Date.valueOf(LocalDate.now()));
            stm.setInt(7, m.getCustomeObject().getCust_num());
            stm.setInt(8, m.getId());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("ग्राहक आणि मीटर माहिती जतन झाली आहे", "ग्राहक आणि मीटर माहित ");
            } else {
                PhoenixSupport.Error("माहिती जतन नाही झाली. पुन प्रयत्न करा");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Meter updation ", ex.getMessage());
        }
    }

    public void deleteMeter(int id) {
        try {
            stm = Connector.getConnection().prepareStatement("update meter set status = 1 where id=?");
            stm.setInt(1, id);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("मीटरची माहित हटवली आहे", "मीटर माहित");
            } else {
                PhoenixSupport.Error("माहिती हटवली नाही गेली. पुन प्रयत्न करा");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Meter deletion ", ex.getMessage());
        }
    }

    public ObservableList<Meter> getMeterByNumber(String name) {
        ObservableList<Meter> meterlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from meter where meter_num like ? and status = 0");
            stm.setString(1, name + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Meter m = new Meter();
                m.setId(rs.getInt(1));
                m.setMetor_num(rs.getString(2));
                m.setCon_date(rs.getString(3));
                m.setCurr_reading(rs.getLong(4));
                m.setOutstanding(rs.getDouble(5));
                m.setDeposit(rs.getDouble(6));
//                m.setCustomeObject().setCust_num(rs.getInt(8));

            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Meter selection ", ex.getMessage());
        }
        return meterlist;
    }

    public int updateOutstanding(Meter m) {
        try {
            stm = Connector.getConnection().prepareStatement("update meter set curr_reading=?, outstanding=?,rdate=? where id=?");
            stm.setLong(1, m.getCurr_reading());
            stm.setDouble(2, m.getOutstanding());
            stm.setDate(3, Date.valueOf(LocalDate.now()));
            stm.setInt(4, m.getId());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Meter updation ", ex.getMessage());
        }
        return 0;
    }

    public ArrayList<String> getMeterNumber() {
        ArrayList<String> mnumberList = new ArrayList<>();
        try {
            stm = Connector.getConnection().prepareStatement("Select meter_num from meter where status = 0");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                mnumberList.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mnumberList;
    }

    public Meter getMeter(String meterNo) {
        Meter meter = null;
        try {
            stm = Connector.getConnection().prepareStatement("Select * from meter where meter_num = ? and status = 0");
            stm.setString(1, meterNo);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                meter = new Meter();
                meter.setId(rs.getInt(1));
                meter.setMetor_num(rs.getString(2));
                meter.setCon_date(rs.getString(3));
                meter.setCurr_reading(rs.getLong(4));
                meter.setOutstanding(rs.getDouble(5));
                meter.setDeposit(rs.getDouble(6));
//                meter.setCustomer(rs.getInt(8));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meter;
    }

    public ObservableList<Meter> getMeterByCustomer(Customer customer) {
        ObservableList<Meter> meterList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select * from meter where cust_id = ? and status = 0");
            stm.setInt(1, customer.getCust_num());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Meter meter = new Meter();
                meter.setId(rs.getInt(1));
                meter.setMetor_num(rs.getString(2));
                meter.setCon_date(rs.getString(3));
                meter.setCurr_reading(rs.getLong(4));
                meter.setOutstanding(rs.getDouble(5));
                meter.setDeposit(rs.getDouble(6));
//                meter.setCustomer(rs.getInt(8));
                meterList.add(meter);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meterList;
    }

    public ObservableList<Meter> getMeterCustId(String meternumber) {
        ObservableList<Meter> meterList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select cust_id from meter where meter_num = ?");
            stm.setString(1, meternumber);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Meter meter = new Meter();
//                meter.setCustomer(rs.getInt(1));
                PreparedStatement pstm = Connector.getConnection().prepareStatement("Select * from meter where cust_id = ? and status = 0");
//                pstm.setInt(1, meter.getCustomer());
                ResultSet result = pstm.executeQuery();
                while (result.next()) {
                    Meter meters = new Meter();
                    meters.setId(result.getInt(1));
                    meters.setMetor_num(result.getString(2));
                    meters.setCon_date(result.getString(3));
                    meters.setCurr_reading(result.getLong(4));
                    meters.setOutstanding(result.getDouble(5));
                    meters.setDeposit(result.getDouble(6));
//                    meters.setCustomer(result.getInt(8));
                    meterList.add(meters);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meterList;
    }

}
