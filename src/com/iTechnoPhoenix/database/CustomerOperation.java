package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerOperation {

    private PreparedStatement stm;

    public int addCustomer(Customer cust) {
        try {
            stm = Connector.getConnection().prepareStatement("insert into customer (name,address,phone,email,rdate) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, cust.getName());
            stm.setString(2, cust.getAddress());
            stm.setString(3, cust.getPhone());
            stm.setString(4, cust.getEmail());
            stm.setDate(5, Date.valueOf(LocalDate.now()));

            if (stm.executeUpdate() > 0) {
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

            } else {
                PhoenixSupport.Error("माहिती जतन नाही झाली. पुन प्रयत्न करा");
                return 0;
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Customer Insertion ", ex.getMessage());
        }
        return 0;
    }

    public int updateCustomer(Customer cust) {
        try {
            stm = Connector.getConnection().prepareStatement("update customer set name=?,address=?,phone=?,email=?,rdate=? where cust_num=?");
            stm.setString(1, cust.getName());
            stm.setString(2, cust.getAddress());
            stm.setString(3, cust.getPhone());
            stm.setString(4, cust.getEmail());
            stm.setDate(5, Date.valueOf(LocalDate.now()));
            stm.setInt(6, cust.getCust_num());
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                return 1;
            } else {
                PhoenixSupport.Error("माहिती जतन नाही झाली. पुन प्रयत्न करा");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Customer updation ", ex.getMessage());
        }
        return 0;

    }

    public int deleteCustomer(int cust_num) {
        try {
            stm = Connector.getConnection().prepareStatement("delete from customer where cust_num=?");
            stm.setInt(1, cust_num);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                return 1;
            } else {
                PhoenixSupport.Error("माहिती जतन नाही झाली. पुन प्रयत्न करा");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Customer deletion ", ex.getMessage());
        }
        return 0;
    }

    public ObservableList<Meter> getCustomerByName() {
        ObservableList<Meter> customerlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select * from customer c join meter m on c.cust_num = m.cust_id");

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCust_num(rs.getInt(1));
                customer.setName(rs.getString(2));
                customer.setAddress(rs.getString(3));
                customer.setPhone(rs.getString(4));
                customer.setEmail(rs.getString(5));
                Meter meter = new Meter();
                meter.setId(rs.getInt(7));
                meter.setMetor_num(rs.getString(8));
                meter.setCon_date(rs.getString(9));
                meter.setCurr_reading(rs.getLong(10));
                meter.setOutstanding(rs.getDouble(11));
                meter.setDeposit(rs.getDouble(12));
                meter.setCustomeObject(customer);
                customerlist.add(meter);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Customer deletion ", ex.getMessage());
        }
        return customerlist;
    }

    public ObservableList<Customer> getCustomerName() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select cust_num, name from customer");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCust_num(rs.getInt(1));
                customer.setName(rs.getString(2));
                customerList.add(customer);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerList;
    }

    public ObservableList<Meter> getCustomerDetails(String name) {
        ObservableList<Meter> meterList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select * from customer c join meter m on c.cust_num = m.cust_id where cust_num in (Select cust_num from customer c1 join meter m1 on c1.cust_num = m1.cust_id where c1.name = ? or m1.meter_num = ?)");
            stm.setString(1, name);
            stm.setString(2, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCust_num(rs.getInt(1));
                customer.setName(rs.getString(2));
                customer.setAddress(rs.getString(3));
                customer.setPhone(rs.getString(4));
                customer.setEmail(rs.getString(5));
                Meter meter = new Meter();
                meter.setId(rs.getInt(7));
                meter.setMetor_num(rs.getString(8));
                meter.setCon_date(rs.getString(9));
                meter.setCurr_reading(rs.getLong(10));
                meter.setOutstanding(rs.getDouble(11));
                meter.setDeposit(rs.getDouble(12));
                meter.setCustomeObject(customer);
                meterList.add(meter);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meterList;
    }

    public Customer getCustomerNameonID(int id) {
        Customer customer = new Customer();
        try {
            stm = Connector.getConnection().prepareStatement("Select * from customer where cust_num = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                customer.setCust_num(rs.getInt(1));
                customer.setName(rs.getString(2));
                customer.setAddress(rs.getString(3));
                customer.setPhone(rs.getString(4));
                customer.setEmail(rs.getString(5));
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customer;
    }
}
