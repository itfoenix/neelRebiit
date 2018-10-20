/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 *
 * @author choudhary
 */
public class AccountReceipt extends RecursiveTreeObject<AccountReceipt> {

    private int areceipt_id, account_id, bank_id;
    private double paid_amt, total_amt, delay_amt;
    private String cheque_no, bankname, numberInWord;
    private int paymode;
    private String paydate;
    private Account account;
    private String name, address;

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public double getDelay_amt() {
        return delay_amt;
    }

    public void setDelay_amt(double delay_amt) {
        this.delay_amt = delay_amt;
    }

    public AccountReceipt() {
    }

    public int getAreceipt_id() {
        return areceipt_id;
    }

    public int getBank_id() {
        return bank_id;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public String getBankname() {
        return bankname;
    }

    public String getNumberInWord() {
        return numberInWord;
    }

    public void setNumberInWord(String numberInWord) {
        this.numberInWord = numberInWord;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
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

    public double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(double total_amt) {
        this.total_amt = total_amt;
    }

}
