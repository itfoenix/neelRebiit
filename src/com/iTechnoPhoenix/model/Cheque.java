/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Objects;

/**
 *
 * @author choudhary
 */
public class Cheque extends RecursiveTreeObject<Cheque> {

    private int id, billno, receiptno;
    private double amount, extrachages;
    private String date, chequenumber, bdate, status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillno() {
        return billno;
    }

    public void setBillno(int billno) {
        this.billno = billno;
    }

    public int getReceiptno() {
        return receiptno;
    }

    public void setReceiptno(int receiptno) {
        this.receiptno = receiptno;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getExtrachages() {
        return extrachages;
    }

    public void setExtrachages(double extrachages) {
        this.extrachages = extrachages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChequenumber() {
        return chequenumber;
    }

    public void setChequenumber(String chequenumber) {
        this.chequenumber = chequenumber;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.chequenumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cheque other = (Cheque) obj;
        if (!Objects.equals(this.chequenumber, other.chequenumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return chequenumber;
    }

}
