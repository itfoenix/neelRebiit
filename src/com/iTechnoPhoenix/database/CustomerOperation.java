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
                //Connector.commit();

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

    public ObservableList<Customer> getCustomerByName(String name) {
        ObservableList<Customer> customerlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer where name like ?");
            stm.setString(1, name + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                c.setPhone(rs.getString(4));
                c.setEmail(rs.getString(5));
                customerlist.add(c);
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

    public ObservableList<Customer> getCustomerDetails(String name) {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select * from customer where name = ?");
            stm.setString(1, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCust_num(rs.getInt(1));
                customer.setName(rs.getString(2));
                customer.setAddress(rs.getString(3));
                customer.setPhone(rs.getString(4));
                customer.setEmail(rs.getString(5));
                PreparedStatement pstm = Connector.getConnection().prepareStatement("Select * from meter where cust_id = ?");
                pstm.setInt(1, customer.getCust_num());
                ResultSet result = pstm.executeQuery();
                Meter meter = new Meter();
                while (result.next()) {
                    if (meter.getMetor_num() != null) {
                        meter.setMetor_num(meter.getMetor_num() + "," + result.getString(2));
                    } else {
                        meter.setMetor_num(result.getString(2));
                    }
                }
//                customer.setMeter(meter);
                customerList.add(customer);
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            Logger.getLogger(MeterOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerList;
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
