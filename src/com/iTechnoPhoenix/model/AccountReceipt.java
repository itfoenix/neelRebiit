/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.model;

/**
 *
 * @author choudhary
 */
public class AccountReceipt {

    private int areceipt_id;
    private double paid_amt;
    private String cheque_no;
    private int paymode;
    private String paydate, paymodes;
    private Account account;
    private String name, address;

    public AccountReceipt() {
    }

    public int getAreceipt_id() {
        return areceipt_id;
    }

    public void setAreceipt_id(int areceipt_id) {
        this.areceipt_id = areceipt_id;
    }

    public double getPaid_amt() {
        return paid_amt;
    }

    public void setPaid_amt(double paid_amt) {
        this.paid_amt = paid_amt;
    }

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public int getPaymode() {
        return paymode;
    }

    public void setPaymode(int paymode) {
        this.paymode = paymode;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
