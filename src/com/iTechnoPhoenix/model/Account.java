/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;

/**
 *
 * @author choudhary
 */
public class Account extends RecursiveTreeObject<Account> {

    private int account_id;
    private Customer customer;
    private ObservableList<Reason> reasonList;
    private String date;

    public Account() {
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

    public ObservableList<Reason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(ObservableList<Reason> reasonList) {
        this.reasonList = reasonList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Account{" + "account_id=" + account_id + ", customer=" + customer + ", reasonList=" + reasonList + ", date=" + date + '}';
    }

}
