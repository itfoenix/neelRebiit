package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Cheque;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FailureOperation {

    private PreparedStatement stm;

    public void saveFailureBanktransaction(Cheque fr) {

        try {
            CallableStatement stm = Connector.getConnection().prepareCall("{ call bankChequeReturn(?,?,?,?,?,?,?)}");
            stm.setInt(1, fr.getReceiptno());
            stm.setInt(2, fr.getBillno());
            stm.setString(3, fr.getBdate());
            stm.setString(4, fr.getChequenumber());
            stm.setDouble(5, fr.getExtrachages());
            stm.setDouble(6, fr.getAmount());
            stm.setString(7, LocalDate.now().toString());
            stm.executeUpdate();
            Connector.commit();
            PhoenixSupport.Info("चेक माहित जतन झाली", "चेक माहित ");
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixSupport.Info("save", "failure operation");
            PhoenixConfiguration.loggedRecored("failure operation", ex.getMessage());
        }

    }

//    public boolean checkFailed(String chqNum) {
//        boolean checked = false;
//        try {
//            stm = Connector.getConnection().prepareStatement("Select * from neel.cheque where cheqno = ?");
//            stm.setString(1, chqNum);
//            ResultSet rs = stm.executeQuery();
//        } catch (SQLException ex) {
//            Logger.getLogger(FailureOperation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return checked;
//    }
    public ObservableList<Cheque> getAllFailedReceipts() {

        ObservableList<Cheque> flist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("SELECT * FROM neel.cheque");

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Cheque fr = new Cheque();
                fr.setId(rs.getInt(1));
                fr.setReceiptno(rs.getInt(2));
                fr.setBillno(rs.getInt(3));
                fr.setBdate(rs.getString(4));
                fr.setChequenumber(rs.getString(5));
                fr.setExtrachages(rs.getDouble(6));
                fr.setAmount(rs.getDouble(7));
                fr.setDate(rs.getString(8));
                flist.add(fr);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FailureOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flist;

    }

    public void updateFailureBanktransaction(Cheque fr) {

        try {
            stm = Connector.getConnection().prepareStatement("update cheque set extracharges=?,total=?,rdate=? where id=?");
            stm.setDouble(1, fr.getExtrachages());
            stm.setDouble(2, fr.getAmount());
            stm.setString(3, LocalDate.now().toString());
            stm.setInt(4, fr.getId());

            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("चेक माहित जतन झाली आहे", "चेक माहित ");

            } else {
                Connector.rollbackresult();
                PhoenixSupport.Info("चेक माहिती जतन नाहीत झाली ", "चेक माहित ");
            }

        } catch (SQLException ex) {

            PhoenixConfiguration.loggedRecored("failure operation", ex.getMessage());
        }

    }

    public void deleteFailure(int id) {
        try {
            stm = Connector.getConnection().prepareStatement("delete from cheque where id=?");
            stm.setInt(1, id);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("चेक माहित हटवली गेली आहे ", "चेक माहिती");
            } else {
                PhoenixSupport.Error("चेक माहित हटवली गेली नाही");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Failure deletion ", ex.getMessage());
        }
    }

}
