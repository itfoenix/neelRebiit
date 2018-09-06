package com.iTechnoPhoenix.database;

import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class BillOperation {

    private PreparedStatement stm;

    public ObservableList<Bill> getAllBills(String period) {
        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer c join meter m on  m.cust_id=c.cust_num join billing b on b.meterno=m.id where b.period=?");
            stm.setString(1, period);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                Meter m = new Meter();
                m.setId(rs.getInt(7));
                m.setMetor_num(rs.getString(8));
                m.setCurr_reading(rs.getLong(10));
                m.setOutstanding(rs.getDouble(11));
                b.setBillno(rs.getInt(16));
                b.setBdate(rs.getString(17));
                b.setPeriod(rs.getString(19));
                b.setYear(rs.getString(20));
                b.setPdate(rs.getString(21));
                b.setBalance(rs.getDouble(22));
                b.setInterested(rs.getDouble(23));
                b.setCuramount(rs.getDouble(24));
                b.setScharges(rs.getDouble(25));
                b.setTotal(rs.getDouble(26));
                b.setPerunit(rs.getInt(27));
                b.setCurunit(rs.getInt(28));
                b.setUseunit(rs.getInt(29));
                b.setUid(rs.getInt(30));
                b.setRemark(rs.getString(32));
                b.setMeternumner(rs.getString(8));
                b.setCust(c);
                b.setMeter(m);
                b.setCustomername(rs.getString(2));
                b.setCustomernumber(rs.getInt(1));
                b.setAddress(rs.getString(3));
                b.setBillref(rs.getInt(33));
                billlist.add(b);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }

    public ObservableList<Bill> getAllBillsadd(String period, String name) {
        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer c join meter m on  m.cust_id=c.cust_num join billing b on b.meterno=m.id where b.period=? and (c.name=? or m.meter_num = ? or b.billref = ?)");
            stm.setString(1, period);
            stm.setString(2, name);
            stm.setString(3, name);
            stm.setString(4, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                Meter m = new Meter();
                m.setId(rs.getInt(7));
                m.setMetor_num(rs.getString(8));
                m.setCurr_reading(rs.getLong(10));
                m.setOutstanding(rs.getDouble(11));
                b.setBillno(rs.getInt(16));
                b.setBdate(rs.getString(17));
                b.setPeriod(rs.getString(19));
                b.setYear(rs.getString(20));
                b.setPdate(rs.getString(21));
                b.setBalance(rs.getDouble(22));
                b.setInterested(rs.getDouble(23));
                b.setCuramount(rs.getDouble(24));
                b.setScharges(rs.getDouble(25));
                b.setTotal(rs.getDouble(26));
                b.setPerunit(rs.getInt(27));
                b.setCurunit(rs.getInt(28));
                b.setUseunit(rs.getInt(29));
                b.setUid(rs.getInt(30));
                b.setRemark(rs.getString(32));
                b.setMeternumner(rs.getString(8));
                b.setCust(c);
                b.setMeter(m);
                b.setCustomername(rs.getString(2));
                b.setCustomernumber(rs.getInt(1));
                b.setAddress(rs.getString(3));
                b.setBillref(rs.getInt(33));
                billlist.add(b);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }

    public ObservableList<Bill> getAllBill(int billref) {
        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer c join meter m on  m.cust_id=c.cust_num join billing b on b.meterno=m.id where b.billref = ?");
            stm.setInt(1, billref);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                Meter m = new Meter();
                m.setId(rs.getInt(7));
                m.setMetor_num(rs.getString(8));
                m.setCurr_reading(rs.getLong(10));
                m.setOutstanding(rs.getDouble(11));
                b.setBillno(rs.getInt(16));
                b.setBdate(rs.getString(17));
                b.setPeriod(rs.getString(19));
                b.setYear(rs.getString(20));
                b.setPdate(rs.getString(21));
                b.setBalance(rs.getDouble(22));
                b.setInterested(rs.getDouble(23));
                b.setCuramount(rs.getDouble(24));
                b.setScharges(rs.getDouble(25));
                b.setTotal(rs.getDouble(26));
                b.setPerunit(rs.getInt(27));
                b.setCurunit(rs.getInt(28));
                b.setUseunit(rs.getInt(29));
                b.setUid(rs.getInt(30));
                b.setRemark(rs.getString(32));
                b.setMeternumner(rs.getString(8));
                b.setCust(c);
                b.setMeter(m);
                b.setCustomername(rs.getString(2));
                b.setCustomernumber(rs.getInt(1));
                b.setAddress(rs.getString(3));
                b.setBillref(rs.getInt(33));
                b.setStatus(rs.getString(31));
                billlist.add(b);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }

    public ObservableList<Bill> getAllBillByName(String name) {
        ObservableList<Bill> billlist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer c join meter m on  m.cust_id=c.cust_num join billing b on b.meterno=m.id where c.name = ? or m.meter_num = ? or b.billref = ?");
            stm.setString(1, name);
            stm.setString(2, name);
            stm.setString(3, name);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                Meter m = new Meter();
                m.setId(rs.getInt(7));
                m.setMetor_num(rs.getString(8));
                m.setCurr_reading(rs.getLong(10));
                m.setOutstanding(rs.getDouble(11));
                b.setBillno(rs.getInt(16));
                b.setBdate(rs.getString(17));
                b.setPeriod(rs.getString(19));
                b.setYear(rs.getString(20));
                b.setPdate(rs.getString(21));
                b.setBalance(rs.getDouble(22));
                b.setInterested(rs.getDouble(23));
                b.setCuramount(rs.getDouble(24));
                b.setScharges(rs.getDouble(25));
                b.setTotal(rs.getDouble(26));
                b.setPerunit(rs.getInt(27));
                b.setCurunit(rs.getInt(28));
                b.setUseunit(rs.getInt(29));
                b.setUid(rs.getInt(30));
                b.setRemark(rs.getString(32));
                b.setMeternumner(rs.getString(8));
                b.setCust(c);
                b.setMeter(m);
                b.setCustomername(rs.getString(2));
                b.setCustomernumber(rs.getInt(1));
                b.setAddress(rs.getString(3));
                b.setBillref(rs.getInt(33));
                billlist.add(b);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billlist;
    }

    public Bill getMeterInfo(String meterNumber) {
        Bill b = null;
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer c join meter m on  m.cust_id=c.cust_num where m.meter_num=?");
            stm.setString(1, meterNumber);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                b = new Bill();
                Meter m = new Meter();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                c.setPhone(rs.getString(4));
                m.setId(rs.getInt(7));
                m.setMetor_num(rs.getString(8));

                m.setCon_date(rs.getDate(9).toString());
                m.setCurr_reading(rs.getLong(10));
                m.setOutstanding(rs.getDouble(11));
                m.setDeposit(rs.getDouble(12));
                m.setCustomeObject(c);
                b.setMeter(m);
                b.setCust(c);
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return b;
    }

    public ObservableList<Meter> getMeterInfolist(String meterNumber) {
        ObservableList<Meter> blist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("select * from customer c join meter m on  m.cust_id=c.cust_num where m.meter_num like ? OR c.name like ? ");
            stm.setString(1, meterNumber + "%");
            stm.setString(2, meterNumber + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Meter m = new Meter();
                Customer c = new Customer();
                c.setCust_num(rs.getInt(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                c.setPhone(rs.getString(4));
                c.setEmail(rs.getString(5));
                m.setId(rs.getInt(7));
                m.setMetor_num(rs.getString(8));

                m.setCon_date(rs.getString(9));
                m.setCurr_reading(rs.getLong(10));
                m.setOutstanding(rs.getDouble(11));
                m.setDeposit(rs.getDouble(12));
                m.setCustomeObject(c);
                blist.add(m);

            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return blist;
    }

    public ObservableList<Bill> getBillHistory(int meterNumber) {
        ObservableList<Bill> blist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("SELECT * FROM billing where meterno=? order by bill_no desc;");
            stm.setInt(1, meterNumber);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                b.setBillno(rs.getInt(1));
                b.setBdate(rs.getString(2));
                Meter m = new Meter();
                m.setId(rs.getInt(3));
                b.setMeter(m);
                b.setPeriod(rs.getString(4));
                b.setYear(rs.getString(5));
                b.setString(rs.getString(4) + "-" + rs.getString(5));
                b.setPdate(rs.getString(6));
                b.setBalance(rs.getDouble(7));
                b.setInterested(rs.getDouble(8));
                b.setCuramount(rs.getDouble(9));
                b.setScharges(rs.getDouble(10));
                b.setTotal(rs.getDouble(11));
                b.setPerunit(rs.getInt(12));
                b.setCurunit(rs.getInt(13));
                b.setUseunit(rs.getInt(14));
                b.setUid(rs.getInt(15));
                b.setSt(rs.getInt(16));
                b.setRemark(rs.getString(17));
                b.setBillref(rs.getInt(18));
                blist.add(b);
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return blist;
    }

    public int saveBill(Bill m) {
        int i = 0;
        try {
            CallableStatement stm = Connector.getConnection().prepareCall("{ call carryForwardBillss(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            stm.setString(1, m.getBdate());
            stm.setInt(2, m.getMeter().getId());
            stm.setString(3, m.getPeriod());
            stm.setString(4, m.getYear());
            stm.setString(5, m.getPdate());
            stm.setDouble(6, m.getBalance());
            stm.setDouble(7, m.getInterested());
            stm.setDouble(8, m.getCuramount());
            stm.setDouble(9, m.getScharges());
            stm.setDouble(10, m.getTotal());
            stm.setLong(11, m.getPerunit());
            stm.setLong(12, m.getCurunit());
            stm.setLong(13, m.getUseunit());
            stm.setInt(14, m.getUid());
            stm.setString(15, m.getRemark());
            stm.setInt(16, m.getBillref());
            stm.registerOutParameter(17, Types.INTEGER);
            stm.executeUpdate();
            if (stm.getInt(17) > 0) {
                System.out.println(stm.getInt(17));
                i = stm.getInt(17);
                Connector.commit();
            }

        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Bill Insertion ", ex.getMessage());
        }
        return i;
    }

    public Bill getBill(int billno) {
        Bill b = null;
        try {
            stm = Connector.getConnection().prepareStatement("SELECT  m.meter_num,c.cust_num,c.name,c.address,"
                    + "b.bill_no, b.bdate, concat(b.period,'-',b.year) as 'period' ,b.pdate,b.balance,b.interent,b.camount,b.schargers,b.total,b.preunit,b.curunit,b.useunit,b.remark, b.billref"
                    + " FROM neel.billing b join meter m  on m.id=b.meterno  join customer c on c.cust_num=m.cust_id where b.bill_no=?");
            stm.setInt(1, billno);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                b = new Bill();
                Meter m = new Meter();
                m.setMetor_num(rs.getString(1));
                Customer c = new Customer();
                c.setCust_num(rs.getInt(2));
                c.setName(rs.getString(3));
                c.setAddress(rs.getString(4));
                b.setBillno(rs.getInt(5));
                b.setBdate(rs.getString(6));
                b.setString(rs.getString(7));
                b.setPdate(rs.getString(8));
                b.setBalance(rs.getDouble(9));
                b.setInterested(rs.getDouble(10));
                b.setCuramount(rs.getDouble(11));
                b.setScharges(rs.getDouble(12));
                b.setTotal(rs.getDouble(13));
                b.setPerunit(rs.getInt(14));
                b.setCurunit(rs.getInt(15));
                b.setUseunit(rs.getInt(16));
                b.setRemark(rs.getString(17));
                b.setBillref(rs.getInt(18));
                b.setMeter(m);
                b.setCust(c);
                b.setCustomername(rs.getString(3));
                b.setCustomernumber(rs.getInt(2));
                b.setMeternumber(rs.getString(1));
                b.setAddress(rs.getString(4));
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At receipt window to get bill from number ", ex.getMessage());
        }
        return b;
    }

    public ObservableList<Bill> getBillRef(int billref) {
        ObservableList<Bill> billList = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("SELECT  m.meter_num,c.cust_num,c.name,c.address,"
                    + "b.bill_no, b.bdate, concat(b.period,'  ­  ',b.year) as 'period' ,b.pdate,b.balance,b.interent,b.camount,b.schargers,b.total,b.preunit,b.curunit,b.useunit,b.remark, b.billref, b.status"
                    + " FROM neel.billing b join meter m  on m.id=b.meterno  join customer c on c.cust_num=m.cust_id where b.billref=?");
            stm.setInt(1, billref);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                Meter m = new Meter();
                m.setMetor_num(rs.getString(1));
                Customer c = new Customer();
                c.setCust_num(rs.getInt(2));
                c.setName(rs.getString(3));
                c.setAddress(rs.getString(4));
                b.setBillno(rs.getInt(5));
                b.setBdate(rs.getString(6));
                b.setPeriod(rs.getString(7));
                b.setPdate(rs.getString(8));
                b.setBalance(rs.getDouble(9));
                b.setInterested(rs.getDouble(10));
                b.setCuramount(rs.getDouble(11));
                b.setScharges(rs.getDouble(12));
                b.setTotal(rs.getDouble(13));
                b.setPerunit(rs.getInt(14));
                b.setCurunit(rs.getInt(15));
                b.setUseunit(rs.getInt(16));
                b.setRemark(rs.getString(17));
                b.setBillref(rs.getInt(18));
                b.setSt(rs.getInt(19));
                b.setMeter(m);
                b.setCust(c);
                b.setCustomername(rs.getString(3));
                b.setCustomernumber(rs.getInt(2));
                b.setMeternumber(rs.getString(1));
                b.setAddress(rs.getString(4));
                billList.add(b);
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At receipt window to get bill from number ", ex.getMessage());
        }
        return billList;
    }

    public ObservableList<Bill> getPendingBills(String period, String year) {
        ObservableList<Bill> blist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("SELECT * FROM billing b join meter m on m.id=b.meterno left join receipt r on r.bill_no=b.bill_no where b.period=? and b.year=?");
            stm.setString(1, period);
            stm.setString(2, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill b = new Bill();
                b.setBillno(rs.getInt(1));
                b.setBdate(rs.getString(2));
                b.setPeriod(rs.getString(4));
                b.setYear(rs.getString(5));
                b.setString(rs.getString(4) + "-" + rs.getString(5));
                b.setPdate(rs.getString(6));
                b.setBalance(rs.getDouble(7));
                b.setInterested(rs.getDouble(8));
                b.setCuramount(rs.getDouble(9));
                b.setScharges(rs.getDouble(10));
                b.setTotal(rs.getDouble(11));
                b.setPerunit(rs.getInt(12));
                b.setCurunit(rs.getInt(13));
                b.setUseunit(rs.getInt(14));
                b.setUid(rs.getInt(15));
                b.setSt(rs.getInt(16));
                b.setRemark(rs.getString(17));
                b.setBillref(rs.getInt(18));
                b.setPmode(rs.getInt(33));
                Meter m = new Meter();
                m.setId(rs.getInt(3));
                m.setMetor_num(rs.getString(20));
                b.setMeter(m);
                blist.add(b);
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return blist;
    }

    public int[] getPendingBillscount(String period, String year) {
        int i[] = new int[2];
        try {
            stm = Connector.getConnection().prepareStatement("SELECT count(*),sum(total) FROM billing b where b.period like ? and b.year=? and b.status=1");
            stm.setString(1, "%" + period + "%");
            stm.setString(2, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                i[0] = rs.getInt(1);
                i[1] = rs.getInt(2);
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return i;
    }

    public int getPaiedBillscount(String period, String year) {
        int i = 0;
        try {
            stm = Connector.getConnection().prepareStatement("SELECT count(*) FROM billing b where b.period like ? and b.year=? and b.status=3");
            stm.setString(1, "%" + period + "%");
            stm.setString(2, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return i;
    }

    public Map<String, Integer> getGraph(String period, String year) {
        Map<String, Integer> mlist = new HashMap<>();
        try {
            stm = Connector.getConnection().prepareCall("{ call billstatuschart(?,?)}");
            stm.setString(1, period);
            stm.setString(2, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                mlist.put(rs.getString(2), rs.getInt(1));
            }
        } catch (SQLException ex) {
            PhoenixConfiguration.loggedRecored("At BillOperation Meter Info ", ex.getMessage());
        }
        return mlist;
    }

    public int checkBill(String meterNo, String period, String year) {
        int i = 0;
        MeterOperation mo = new MeterOperation();
        Meter meter = mo.getMeter(meterNo);
        if (meter != null) {
            try {
                stm = Connector.getConnection().prepareStatement("select * from billing where period=? and year=? and meterno=?");
                stm.setString(1, period);
                stm.setString(2, year);
                stm.setInt(3, meter.getId());
                ResultSet rs = stm.executeQuery();
                if (rs != null) {
                    if (rs.next()) {
                        rs.getInt(1);
                        i = 1;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return i;
    }

    public void insertNameWise(Bill bill) {
        try {
            stm = Connector.getConnection().prepareStatement("Insert into custwisebill (cust_id, meter_no, bill_no) values (?,?,?)");
            stm.setInt(1, bill.getCustomernumber());
            stm.setInt(2, bill.getMeter().getId());
            stm.setInt(3, bill.getBillno());
            int i = stm.executeUpdate();
            if (i == 1) {
                Connector.commit();
                PhoenixSupport.Info("बिल जतन झालं आहे ", "बिल व्यवहार");
            }
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Bill Insertion ", ex.getMessage());
        }
    }

    public Bill lastid(String period, String year) {
        Bill bill = null;
        try {
            stm = Connector.getConnection().prepareStatement("SELECT b.bill_no, b.billref, m.cust_id FROM billing b join meter m on b.meterno = m.id where b.period=? and b.year=? ORDER BY bill_no DESC LIMIT 1");
            stm.setString(1, period);
            stm.setString(2, year);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bill = new Bill();
                bill.setBillno(rs.getInt(1));
                bill.setBillref(rs.getInt(2));
                bill.setCustomernumber(rs.getInt(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bill;
    }

//    public void checkBillRef(int billno, String period, String year) {
//        Bill bill = null;
//        try {
//            stm = Connector.getConnection().prepareStatement("Select m.cust_id, b.billref from billing b join meter m on b.meterno = m.id where bill_no=? and year=? and period=?");
//            ResultSet rs = stm.executeQuery();
//            while(rs.next()) {
//                bill = new Bill();
//                bill.setCustomernumber(rs.getInt(1));
//                bill.set
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public ObservableList<Bill> anyGeneratedBill(String period, String year, Customer customer) {
        ObservableList<Bill> billist = FXCollections.observableArrayList();
        try {
            stm = Connector.getConnection().prepareStatement("SELECT b.billref, m.meter_num FROM billing b join meter m on b.meterno = m.id where b.period=? and b.year=? and m.cust_id =?");
            stm.setString(1, period);
            stm.setString(2, year);
            stm.setInt(3, customer.getCust_num());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillref(rs.getInt(1));
                bill.setMeternumber(rs.getString(2));
                billist.add(bill);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billist;
    }

    public int getref(int billno) {
        int i = 0;
        try {
            stm = Connector.getConnection().prepareStatement("SELECT billref from billing where bill_no =?");
            stm.setInt(1, billno);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public void updateBill(Bill m) {
        try {
            CallableStatement stm = Connector.getConnection().prepareCall("{ call updateBill(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            stm.setInt(1, m.getMeter().getId());
            stm.setString(2, m.getPeriod());
            stm.setString(3, m.getYear());
            stm.setDouble(4, m.getBalance());
            stm.setDouble(5, m.getInterested());
            stm.setDouble(6, m.getCuramount());
            stm.setDouble(7, m.getScharges());
            stm.setDouble(8, m.getTotal());
            stm.setLong(9, m.getPerunit());
            stm.setLong(10, m.getCurunit());
            stm.setLong(11, m.getUseunit());
            stm.setInt(13, m.getUid());
            stm.setInt(12, m.getBillref());
            stm.executeUpdate();
            Connector.commit();
        } catch (SQLException ex) {
            Connector.rollbackresult();
            PhoenixConfiguration.loggedRecored("Bill Updation ", ex.getMessage());
        }
    }

    public ObservableSet<String> getAllBillNum() {
        ObservableSet<String> billnoSet = FXCollections.observableSet();
        try {
            stm = Connector.getConnection().prepareStatement("Select billref from billing");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                billnoSet.add(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return billnoSet;
    }

//    public void getStatusDetails(int billno) {
//        try {
//            stm =Connector.getConnection().prepareStatement("select sum(b.total), b.status, r.amount from billing b join receipt r on b.billref = r.bill_no where b.billref = ?;");
//            stm.setInt(1, billno);
//            ResultSet rs = stm.executeQuery();
//            while(rs.next()) {
//                Bill bill = new Bill();
//                bill.setTotal(rs.getDouble(1));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(BillOperation.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
