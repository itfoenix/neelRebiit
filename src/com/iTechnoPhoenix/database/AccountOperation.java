/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.StackPane;

/**
 *
 * @author choudhary
 */
public class AccountOperation {

    private PreparedStatement stmt;

    public void insertBill(Account account, StackPane window) {
        try {
            stmt = Connector.getConnection().prepareStatement("insert into accountbill (customer_id,amount,account_date,reason,status) values (?,?,now(),?,0)");
            stmt.setInt(1, account.getCustomer().getCust_num());
            stmt.setDouble(2, account.getAmount());
            stmt.setString(3, account.getReason());
            int i = stmt.executeUpdate();
            if (i > 0) {
                Connector.commit();
                PhoenixSupport.Info("बिलाची माहिती जतन झाली आहे.", "खर्च बिल बनवणे", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Error("बिलाची माहिती जतन नाही झाली आहे.", window);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
