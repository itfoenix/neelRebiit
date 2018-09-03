/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.neelSupport;

import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.MeterBill;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author choudhary
 */
public class BillSupport {

    public ArrayList<MeterBill> assignBillValue(ObservableList<Bill> bList) {
        ArrayList<MeterBill> meterBillList = new ArrayList<>();
        MeterBill mb = new MeterBill();
        mb.setAddress(bList.get(0).getCust().getAddress());
        mb.setBillref(String.valueOf(bList.get(0).getBillref()));
        mb.setCustomername(String.valueOf(bList.get(0).getCust().getName()));
        mb.setCustomernumber(String.valueOf(bList.get(0).getCust().getCust_num()));
        mb.setBdate((bList.get(0).getBdate().split(" "))[0]);
        mb.setPdate((bList.get(0).getPdate().split(" "))[0]);
        mb.setPeriodyear(bList.get(0).getPeriod());
        mb.setRemark(bList.get(0).getRemark());

        if (bList.get(0) != null) {
            mb.setBalance(String.valueOf(bList.get(0).getBalance()));
            mb.setInterested(String.valueOf(bList.get(0).getInterested()));
            mb.setCuramount(String.valueOf(bList.get(0).getCuramount()));
            mb.setScharges(String.valueOf(bList.get(0).getScharges()));
            mb.setTotal(String.valueOf(bList.get(0).getTotal()));
            mb.setFinaltotal(mb.getTotal());
            mb.setMeternumber(bList.get(0).getMeternumber());
            mb.setPerunit(String.valueOf(bList.get(0).getPerunit()));
            if (bList.get(0).getPerunit() == bList.get(0).getCurunit()) {
                mb.setCurunit(null);
            } else {
                mb.setCurunit(String.valueOf(bList.get(0).getCurunit()));
            }
            mb.setUseunit(String.valueOf(bList.get(0).getUseunit()));
        }
        if (bList.size() > 1) {
            if (bList.get(1) != null) {
                mb.setBalance2(String.valueOf(bList.get(1).getBalance()));
                mb.setInterested2(String.valueOf(bList.get(1).getInterested()));
                mb.setCuramount2(String.valueOf(bList.get(1).getCuramount()));
                mb.setScharges2(String.valueOf(bList.get(1).getScharges()));
                mb.setTotal2(String.valueOf(bList.get(1).getTotal()));
                mb.setFinaltotal(String.valueOf(Double.parseDouble(mb.getFinaltotal()) + Double.parseDouble(mb.getTotal2())));
                mb.setMeternumber2(bList.get(1).getMeternumber());
                mb.setPerunit2(String.valueOf(bList.get(1).getPerunit()));
                if (bList.get(1).getPerunit() == bList.get(1).getCurunit()) {
                    mb.setCurunit2(null);
                } else {
                    mb.setCurunit2(String.valueOf(bList.get(1).getCurunit()));
                }
                mb.setUseunit2(String.valueOf(bList.get(1).getUseunit()));
            }
        }
        if (bList.size() > 2) {
            if (bList.get(2) != null) {
                mb.setBalance3(String.valueOf(bList.get(2).getBalance()));
                mb.setInterested3(String.valueOf(bList.get(2).getInterested()));
                mb.setCuramount3(String.valueOf(bList.get(2).getCuramount()));
                mb.setScharges3(String.valueOf(bList.get(2).getScharges()));
                mb.setTotal3(String.valueOf(bList.get(2).getTotal()));
                mb.setFinaltotal(String.valueOf(Double.parseDouble(mb.getFinaltotal()) + Double.parseDouble(mb.getTotal3())));
                mb.setMeternumber3(bList.get(2).getMeternumber());
                mb.setPerunit3(String.valueOf(bList.get(2).getPerunit()));
                if (bList.get(2).getPerunit() == bList.get(2).getCurunit()) {
                    mb.setCurunit3(null);
                } else {
                    mb.setCurunit3(String.valueOf(bList.get(2).getCurunit()));
                }
                mb.setUseunit3(String.valueOf(bList.get(2).getUseunit()));
            }
        }
        if (bList.size() > 3) {
            if (bList.get(3) != null) {
                mb.setBalance4(String.valueOf(bList.get(3).getBalance()));
                mb.setInterested4(String.valueOf(bList.get(3).getInterested()));
                mb.setCuramount4(String.valueOf(bList.get(3).getCuramount()));
                mb.setScharges4(String.valueOf(bList.get(3).getScharges()));
                mb.setTotal4(String.valueOf(bList.get(3).getTotal()));
                mb.setFinaltotal(String.valueOf(Double.parseDouble(mb.getFinaltotal()) + Double.parseDouble(mb.getTotal4())));
                mb.setMeternumber4(bList.get(3).getMeternumber());
                mb.setPerunit4(String.valueOf(bList.get(3).getPerunit()));
                if (bList.get(3).getPerunit() == bList.get(3).getCurunit()) {
                    mb.setCurunit4(null);
                } else {
                    mb.setCurunit4(String.valueOf(bList.get(3).getCurunit()));
                }
                mb.setUseunit4(String.valueOf(bList.get(3).getUseunit()));
            }
        }
        meterBillList.add(mb);
        return meterBillList;
    }

}
