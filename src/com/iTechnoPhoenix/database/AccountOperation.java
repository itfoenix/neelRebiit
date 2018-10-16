/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Reason;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

/**
 *
 * @author choudhary
 */
public class AccountOperation {

    private PreparedStatement stmt;

    public int insertBill(Reason reason, StackPane window) {
        int i = 0;
        try {
            stmt = Connector.getConnection().prepareStatement("insert into accountbill (customer_id,amount,account_date,status) values (?,?,now(),0)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, reason.getAccount().getCustomer().getCust_num());
            stmt.setDouble(2, reason.getAccount().getTotalAmt());
            i = stmt.executeUpdate();
            if (i > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                Connector.commit();
                PhoenixSupport.Info("बिलाची माहिती जतन झाली आहे.", "खर्च बिल बनवणे", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Error("बिलाची माहिती जतन नाही झाली आहे.", window);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public void insertReason(Reason reason, StackPane window) {
        try {
            stmt = Connector.getConnection().prepareStatement("insert into reason (reason, amount, account_id) values (?,?,?)");
            stmt.setString(1, reason.getReason());
            stmt.setDouble(2, reason.getAmount());
            stmt.setInt(3, reason.getAccount().getAccount_id());
            int j = stmt.executeUpdate();
            if (j > 0) {
                Connector.commit();
//                PhoenixSupport.Info("बिलाची माहिती जतन झाली आहे.", "खर्च बिल बनवणे", window);
                System.out.println("Account Saved");
            } else {
                Connector.rollbackresult();
//                PhoenixSupport.Error("बिलाची माहिती जतन नाही झाली आहे.", window);
                System.out.println("Not Saved Account");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<Reason> getAllAccount() {
        ObservableList<Reason> accountList = FXCollections.observableArrayList();
        try {
            stmt = Connector.getConnection().prepareStatement("select a.*, c.name from accountbill a join customer c on c.cust_num = a.customer_id where status != 1");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reason reason = new Reason();
                Account account = new Account();
                account.setAccount_id(rs.getInt(1));
                Customer customer = new Customer();
                customer.setCust_num(rs.getInt(2));
                customer.setName(rs.getString(7));
                account.setCustomer(customer);
                reason.setAmount(rs.getDouble(3));
                account.setDate(rs.getDate(4).toString());
                reason.setReason(rs.getString(5));
                reason.setAccount(account);
                accountList.add(reason);

            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountOperation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return accountList;
    }

    public void deleteAccount(int id, StackPane window) {
        try {
            stmt = Connector.getConnection().prepareStatement("update accountbill set status = 1 where account_id = ?");
            stmt.setInt(1, id);
            int i = stmt.executeUpdate();
            if (i > 0) {
                Connector.commit();
                System.out.println("deleted");
                PhoenixSupport.Info("बिल रद्ध करण्यात येत आहे.", "बिल राध करणे", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Error("बिल रद्ध नाही करण्यात आले आहे", window);

            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountOperation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAccount(Reason reason, StackPane window) {
        try {
            stmt = Connector.getConnection().prepareStatement("update accountbill set amount = ?, reason = ? where account_id = ?");
            stmt.setDouble(1, reason.getAmount());
            stmt.setString(2, reason.getReason());
            stmt.setInt(3, reason.getAccount().getAccount_id());
            int i = stmt.executeUpdate();
            if (i > 0) {
                Connector.commit();
                PhoenixSupport.Info("बिलात बदली करण्यात येत आहे.", "बिल बदली करणे", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Error("बिलात बदली नाही करण्यात आले आहे", window);

            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountOperation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

}
