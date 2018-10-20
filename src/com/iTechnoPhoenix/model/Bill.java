/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.layout.HBox;

public class Bill extends RecursiveTreeObject<Bill> {

    private int billno, uid, st, customernumber, rid, billref;
    long perunit, curunit, useunit;
    private double balance, interested, curamount, scharges, total, paidamt, adjustment;
    private String period, year, status, meternumber, customername, address, bankname, cheque;
    private String bdate, pdate;
    private Meter meter;
    private Customer cust;
    private HBox actionBox;
    private String periodyear;
    private String remark;
    private int pmode;
    private String numberInWord;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.billref;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bill other = (Bill) obj;
        if (this.billref != other.billref) {
            return false;
        }
        return true;
    }

    public double getPaidamt() {
        return paidamt;
    }

    public void setPaidamt(double paidamt) {
        this.paidamt = paidamt;
    }

    public double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(double adjustment) {
        this.adjustment = adjustment;
    }

    public int getBillref() {
        return billref;
    }

    public void setBillref(int billref) {
        this.billref = billref;
    }

    public String getNumberInWord() {
        return numberInWord;
    }

    public void setNumberInWord(String numberInWord) {
        this.numberInWord = numberInWord;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public int getPmode() {
        return pmode;
    }

    public void setPmode(int pmode) {
        this.pmode = pmode;
    }

    public Bill() {
    }

    public Bill(int billno, int customernumber, long perunit, long curunit, long useunit, double balance, double interested, double curamount, double scharges, double total, String period, String year, String meternumber, String customername, String address, String bdate, String pdate, String periodyear, String remark) {
        this.billno = billno;
        this.customernumber = customernumber;
        this.perunit = perunit;
        this.curunit = curunit;
        this.useunit = useunit;
        this.balance = balance;
        this.interested = interested;
        this.curamount = curamount;
        this.scharges = scharges;
        this.total = total;
        this.period = period;
        this.year = year;
        this.meternumber = meternumber;
        this.customername = customername;
        this.address = address;
        this.bdate = bdate;
        this.pdate = pdate;
        this.periodyear = periodyear;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCustomernumber() {
        return customernumber;
    }

    public void setCustomernumber(int customernumber) {
        this.customernumber = customernumber;
    }

    public String getMeternumber() {
        return meternumber;
    }

    public void setMeternumber(String meternumber) {
        this.meternumber = meternumber;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getMeternumner() {
        return meternumber;
    }

    public void setMeternumner(String meternumner) {
        this.meternumber = meternumner;
    }

    public String getPeriodyear() {
        return periodyear;
    }

    public void setPeriodyear(String periodyear) {
        this.periodyear = periodyear;
    }

    public String getString() {
        return periodyear;
    }

    public void setString(String String) {
        this.periodyear = String;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
        switch (st) {
            case 3:
                setStatus("होय");
                break;
            case 2:
                setStatus("थकबाकी");
                break;
            default:
                setStatus("नाही");
                break;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HBox getActionBox() {
        return actionBox;
    }

    public void setActionBox(HBox actionBox) {
        this.actionBox = actionBox;
    }

    public Meter getMeter() {
        return meter;
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
        setMeternumner(meter.getMetor_num());
    }

    public Customer getCust() {
        return cust;
    }

    public void setCust(Customer cust) {
        this.cust = cust;
    }

    public int getBillno() {
        return billno;
    }

    public void setBillno(int billno) {
        this.billno = billno;
    }

    public long getPerunit() {
        return perunit;
    }

    public void setPerunit(long perunit) {
        this.perunit = perunit;
    }

    public long getCurunit() {
        return curunit;
    }

    public void setCurunit(long curunit) {
        this.curunit = curunit;
    }

    public long getUseunit() {
        return useunit;
    }

    public void setUseunit(long useunit) {
        this.useunit = useunit;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterested() {
        return interested;
    }

    public void setInterested(double interested) {
        this.interested = interested;
    }

    public double getCuramount() {
        return curamount;
    }

    public void setCuramount(double curamount) {
        this.curamount = curamount;
    }

    public double getScharges() {
        return scharges;
    }

    public void setScharges(double scharges) {
        this.scharges = scharges;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        setString(getPeriod() + "-" + year);
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

}
