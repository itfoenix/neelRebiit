/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 *
 * @author NARENDRA JADHAV
 */
public class Meter extends RecursiveTreeObject<Meter> {

    private int id;
    private String metor_num;
    private String con_date;
    private long curr_reading;
    private double outstanding;
    private double deposit;
    private Customer customeObject;

    public Customer getCustomeObject() {
        return customeObject;
    }

    public void setCustomeObject(Customer customeObject) {
        this.customeObject = customeObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetor_num() {
        return metor_num;
    }

    public void setMetor_num(String metor_num) {
        this.metor_num = metor_num;
    }

    public String getCon_date() {
        return con_date;
    }

    public void setCon_date(String con_date) {
        this.con_date = con_date;
    }

    public long getCurr_reading() {
        return curr_reading;
    }

    public void setCurr_reading(long curr_reading) {
        this.curr_reading = curr_reading;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

}
