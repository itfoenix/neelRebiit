package com.iTechnoPhoenix.model;

public class Receipt {

    private int receipt_no, billno, bankid, uid, paymode;
    private String pdate;
    private String cheq_no;
    private double amount, delay_amount;

    @Override
    public String toString() {
        return "Receipt{" + "receipt_no=" + receipt_no + ", billno=" + billno + ", bankid=" + bankid + ", uid=" + uid + ", paymode=" + paymode + ", pdate=" + pdate + ", cheq_no=" + cheq_no + ", amount=" + amount + ", delay_amount=" + delay_amount + '}';
    }

    public int getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(int receipt_no) {
        this.receipt_no = receipt_no;
    }

    public int getBillno() {
        return billno;
    }

    public void setBillno(int billno) {
        this.billno = billno;
    }

    public int getBankid() {
        return bankid;
    }

    public void setBankid(int bankid) {
        this.bankid = bankid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPaymode() {
        return paymode;
    }

    public void setPaymode(int paymode) {
        this.paymode = paymode;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getCheq_no() {
        return cheq_no;
    }

    public void setCheq_no(String cheq_no) {
        this.cheq_no = cheq_no;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDelay_amount() {
        return delay_amount;
    }

    public void setDelay_amount(double delay_amount) {
        this.delay_amount = delay_amount;
    }

}
