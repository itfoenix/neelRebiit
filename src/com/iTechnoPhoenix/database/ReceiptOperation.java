package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Receipt;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceiptOperation {

    private PreparedStatement stm;

    public int saveReceipt(Receipt r) {
        try {
            CallableStatement stm = Connector.getConnection().prepareCall("{ call generatReceipt (?,?,?,?,?,?,?,?,?)}");
            stm.setString(1, r.getPdate());
            stm.setInt(2, r.getBillno());
            stm.setDouble(3, r.getAmount());
            stm.setDouble(4, r.getDelay_amount());
            stm.setInt(5, r.getPaymode());
            stm.setString(6, r.getCheq_no());
            stm.setInt(7, r.getBankid());
            stm.setInt(8, r.getUid());
            stm.registerOutParameter(9, Types.INTEGER);
            stm.executeUpdate();
            if (stm.getInt(9) > 0) {
                Connector.commit();

                return stm.getInt(9);

            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("ReceiptOperation", ex.getMessage());
        }
        return 0;
    }

    public int updateStatus(int billref) {
        int i = 0;
        try {
            stm = Connector.getConnection().prepareStatement("update billing set status = 3 where billref=?");
            stm.setInt(1, billref);
            i = stm.executeUpdate();
            Connector.commit();
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Reciept Status Changing", ex.getMessage());
        }
        return i;
    }

    public Receipt getReceiptByID(int id) {
        Receipt re = null;
        try {
            stm = Connector.getConnection().prepareStatement("select * from receipt where receip_no=?");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                re = new Receipt();

                re.setReceipt_no(rs.getInt(1));
                re.setPdate(rs.getString(2));
                re.setBillno(rs.getInt(3));
                re.setAmount(rs.getDouble(4));
                re.setDelay_amount(rs.getDouble(5));
                re.setPaymode(rs.getInt(6));
                re.setCheq_no(rs.getString(7));
                re.setBankid(rs.getInt(8));
                re.setUid(rs.getInt(9));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return re;
    }

    public void deleteReceipt(int rid) {

        try {
            stm = Connector.getConnection().prepareStatement("delete from receipt where receipt_no=?");
            stm.setInt(1, rid);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("Delete Receipt", "Receipt Operation");
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Info("Not Delete Receipt", "Receipt Operation");
            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Receipt Operation", ex.getMessage());
        }

    }

    public void updateBillStatus(int billno, int status) {
        try {
            stm = Connector.getConnection().prepareStatement("update billing set status=? where bill_no=?");
            stm.setInt(1, status);
            stm.setInt(2, billno);

            if (stm.executeUpdate() > 0) {
                Connector.commit();
            } else {
                Connector.rollbackresult();
            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("billing outstanding updation", ex.getMessage());
        }

    }

//    public FaildReceipt getReceiptByReceiptNum(int rid) {
//
//        FaildReceipt fr = null;
//        try {
//            stm = Connector.getConnection().prepareStatement("SELECT * FROM receipt where receipt_no=? and  paymode=2");
//            stm.setInt(1, rid);
//            ResultSet rs = stm.executeQuery();
//
//            while (rs.next()) {
//                fr = new FaildReceipt();
//                fr.setReceiptno(rs.getInt(1));
//                fr.setBillno(rs.getInt(3));
//                fr.setBdate(rs.getString(2));
//                fr.setChequenumber(rs.getString(7));
//                fr.setAmount(rs.getDouble(4));
//            }
//
//        } catch (SQLException ex) {
//            Logger.getLogger(FailureOperation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return fr;
//    }
    public int checkBill(int billId) {
        int i = 0;
        try {
            stm = Connector.getConnection().prepareStatement("SELECT receipt_no from receipt where bill_no=?");
            stm.setInt(1, billId);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                i = 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public ObservableList<Bill> getAllReceipt(String period, String year) {

        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select c.name, c.cust_num, c.address, r.bill_no, r.receipt_no, r.pdate, r.amount, r.paymode from customer c join meter m on m.cust_id=c.cust_num join billing b on b.meterno=m.id join receipt r on r.bill_no=b.bill_no where b.period=? and b.year=?");
            stm.setString(1, period);
            stm.setString(2, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(2));
                c.setName(rs.getString(1));
                c.setAddress(rs.getString(3));
                b.setBillref(rs.getInt(4));
                b.setRid(rs.getInt(5));
                b.setPdate(rs.getString(6));
                b.setTotal(rs.getDouble(7));
                b.setPmode(rs.getInt(8));
                b.setCust(c);
                b.setCustomername(rs.getString(1));
                b.setCustomernumber(rs.getInt(2));
                b.setAddress(rs.getString(3));

                billlist.add(b);

            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }
}
