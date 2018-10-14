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
public class Account extends RecursiveTreeObject<Account> {

    private int account_id;
    private Customer customer;
    private String reason;
    private double amount;
    private String date;

    public Account() {
    }

    public Account(int account_id, Customer customer, String reason, double amount, String date) {
        this.account_id = account_id;
        this.customer = customer;
        this.reason = reason;
        this.amount = amount;
        this.date = date;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Account{" + "account_id=" + account_id + ", customer=" + customer + ", reason=" + reason + ", amount=" + amount + ", date=" + date + '}';
    }

}
