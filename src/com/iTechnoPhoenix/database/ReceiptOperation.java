package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Cheque;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.database.FailureOperation;
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
import javafx.scene.layout.StackPane;

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

    public void deleteReceipt(int rid, StackPane window) {

        try {
            stm = Connector.getConnection().prepareStatement("delete from receipt where receipt_no=?");
            stm.setInt(1, rid);
            if (stm.executeUpdate() > 0) {
                Connector.commit();
                PhoenixSupport.Info("Delete Receipt", "Receipt Operation", window);
            } else {
                Connector.rollbackresult();
                PhoenixSupport.Info("Not Delete Receipt", "Receipt Operation", window);
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

    public Cheque getReceiptByChequeNum(String chequeNumber) {
        Cheque fr = null;
        try {
            stm = Connector.getConnection().prepareStatement("Select r.*, b.bdate from receipt r join billing b on r.bill_no = b.bill_no join cheque c on r.cheq_no = c.cheqno where r.cheq_no = ? and r.paymode = 2 and c.visible = 0");
            stm.setString(1, chequeNumber);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                fr = new Cheque();
                fr.setReceiptno(rs.getInt(1));
                fr.setDate(rs.getString(2));
                fr.setBillno(rs.getInt(3));
                fr.setBdate(rs.getString(10));
                fr.setChequenumber(rs.getString(7));
                fr.setAmount(rs.getDouble(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FailureOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fr;
    }

    public ObservableList<Receipt> checkBill(int billId) {
        ObservableList<Receipt> receiptList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("SELECT * from receipt where bill_no=?");
            stm.setInt(1, billId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Receipt receipt = new Receipt();
                receipt = new Receipt();
                receipt.setReceipt_no(rs.getInt(1));
                receipt.setPdate(rs.getString(2));
                receipt.setBillno(rs.getInt(3));
                receipt.setAmount(rs.getDouble(4));
                receipt.setPaymode(rs.getInt(6));
                if (rs.getString(7) != null) {
                    receipt.setCheq_no(rs.getString(7));
                }
                receipt.setBankid(rs.getInt(8));
                receipt.setUid(rs.getInt(9));
                receiptList.add(receipt);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return receiptList;
    }

    public ObservableList<Bill> getAllReceipt(String period, String year) {

        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select c.name, c.cust_num, c.address, r.bill_no, r.receipt_no, r.pdate, r.amount, r.paymode, r.cheq_no, k.name from customer c join meter m on m.cust_id=c.cust_num join billing b on b.meterno=m.id join receipt r on r.bill_no=b.bill_no left join bank k on k.bid = r.bank_id where b.period=? and b.year=?");
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
                if (b.getPmode() == 2) {
                    b.setCheque(rs.getString(9));
                    b.setBankname(rs.getString(10));
                }
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

    public ObservableList<Bill> getAllReceipts(String period, String name, String year) {

        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select distinct r.receipt_no, c.name, c.cust_num, c.address, r.bill_no, r.pdate, r.amount, r.paymode, r.cheq_no, k.name from customer c join meter m on c.cust_num = m.cust_id join billing b on m.id = b.meterno join receipt r on b.billref = r.bill_no left join bank k on r.bank_id = k.bid where b.period = ? and b.year = ? and (c.name=? or m.meter_num=? or r.receipt_no =?)");
            stm.setString(1, period);
            stm.setString(2, year);
            stm.setString(3, name);
            stm.setString(4, name);
            stm.setString(5, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(3));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(4));
                b.setBillref(rs.getInt(5));
                b.setRid(rs.getInt(1));
                b.setPdate(rs.getString(6));
                b.setTotal(rs.getDouble(7));
                b.setPmode(rs.getInt(8));
                if (b.getPmode() == 2) {
                    b.setCheque(rs.getString(9));
                    b.setBankname(rs.getString(10));
                }
                b.setCust(c);
                b.setCustomername(rs.getString(2));
                b.setCustomernumber(rs.getInt(3));
                b.setAddress(rs.getString(4));

                billlist.add(b);

            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }

    public ObservableList<Bill> getAllReceiptName(String name) {

        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select distinct r.receipt_no, c.name, c.cust_num, c.address, r.bill_no, r.pdate, r.amount, r.paymode, r.cheq_no, k.name from customer c join meter m on c.cust_num = m.cust_id join billing b on m.id = b.meterno join receipt r on b.billref = r.bill_no left join bank k on r.bank_id = k.bid where c.name=? or m.meter_num=? or r.receipt_no =?");
            stm.setString(1, name);
            stm.setString(2, name);
            stm.setString(3, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(3));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(4));
                b.setBillref(rs.getInt(5));
                b.setRid(rs.getInt(1));
                b.setPdate(rs.getString(6));
                b.setTotal(rs.getDouble(7));
                b.setPmode(rs.getInt(8));
                if (b.getPmode() == 2) {
                    b.setCheque(rs.getString(9));
                    b.setBankname(rs.getString(10));
                }
                b.setCust(c);
                b.setCustomername(rs.getString(2));
                b.setCustomernumber(rs.getInt(3));
                b.setAddress(rs.getString(4));

                billlist.add(b);

            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }

    public ObservableList<String> getReceiptNo() {
        ObservableList<String> receiptNo = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select receipt_no from receipt");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                receiptNo.add(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return receiptNo;
    }

    public ObservableList<Cheque> getAllCheckPayment() {
        ObservableList<Cheque> chequeList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("Select r.*, b.bdate from receipt r join billing b on r.bill_no = b.bill_no left join cheque c on r.cheq_no = c.cheqno where r.paymode = 2");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Cheque chq = new Cheque();
                chq.setReceiptno(rs.getInt(1));
                chq.setBillno(rs.getInt(3));
                chq.setBdate(rs.getString(10));
                chq.setAmount(rs.getDouble(4));
                chq.setChequenumber(rs.getString(7));
                chequeList.add(chq);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiptOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return chequeList;
    }

}
