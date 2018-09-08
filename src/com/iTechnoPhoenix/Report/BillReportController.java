/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Report;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author choudhary
 */
public class BillReportController implements Initializable {

    @FXML
    private JFXComboBox<String> cb_duration;

    @FXML
    private Label lbl_cash;

    @FXML
    private Label lbl_outstanding;

    @FXML
    private Label lbl_cheque;

    @FXML
    private Label lbl_total;

    @FXML
    private JFXTreeTableView<Bill> tbl_bill;

    @FXML
    private JFXComboBox<String> cb_year;

    @FXML
    private StackPane window;

    private JFXTreeTableColumn<Bill, Integer> tc_BillNumber;
    private JFXTreeTableColumn<Bill, String> tc_MeterNumber;
    private JFXTreeTableColumn<Bill, Double> tc_Total;
    private JFXTreeTableColumn<Bill, String> tc_PaidDate;
    private JFXTreeTableColumn<Bill, String> tc_Status;

    private ObservableList<Bill> bListCopy;
    private ObservableList<Bill> blist;
    private BillOperation billoperation;
    private double total, paid, remning, cheque;
    private String year, period;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cb_duration.setItems(PhoenixConfiguration.getMonth());
        cb_duration.getStyleClass().add("label-marathi");
        cb_year.setItems(PhoenixConfiguration.getYear());
        cb_year.getStyleClass().add("label-marathi");
        billoperation = new BillOperation();
        initTable();
    }

    @FXML
    private void btn_print_all(ActionEvent event) {
        print();
    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            print();
        }

    }

    @FXML
    private void btn_search(ActionEvent event) {
        getReport(cb_duration.getSelectionModel().getSelectedItem(), cb_year.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void btn_search_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            getReport(cb_duration.getSelectionModel().getSelectedItem(), cb_year.getSelectionModel().getSelectedItem());
        }
    }

    private void initTable() {
        tc_BillNumber = new JFXTreeTableColumn<>("बिल नं");
        tc_BillNumber.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillref()).asObject());
        tc_MeterNumber = new JFXTreeTableColumn<>("मीटर नं");
        tc_MeterNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMeter().getMetor_num()));
        tc_Total = new JFXTreeTableColumn<>("बिल रक्कम");
        tc_Total.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal()).asObject());
        tc_PaidDate = new JFXTreeTableColumn<>("देयक दिनक");
        tc_PaidDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPdate()));
        tc_Status = new JFXTreeTableColumn<>("स्थिती");
        tc_Status.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getStatus()));
        tbl_bill.getColumns().addAll(tc_BillNumber, tc_MeterNumber, tc_Total, tc_PaidDate, tc_Status);
    }

    public void getReport(String month, String year) {
        total = paid = remning = cheque = 0;
        blist = billoperation.getPendingBills(month, year);
        bListCopy = FXCollections.observableArrayList();
        if (!bListCopy.isEmpty()) {
            bListCopy.clear();
        }
        for (Bill e : blist) {
            if (!bListCopy.contains(e)) {
                bListCopy.add(e);
            } else {
                for (Bill bil : bListCopy) {
                    if (bil.getBillref() == e.getBillref()) {
                        if (!bil.getMeter().getMetor_num().equals(e.getMeter().getMetor_num())) {
                            bil.getMeter().setMetor_num(bil.getMeter().getMetor_num() + " , " + e.getMeter().getMetor_num());
                            bil.setTotal(bil.getTotal() + e.getTotal());
                            bil.setPaidamt(bil.getPaidamt() + e.getPaidamt());
                            bil.setBalance(bil.getBalance() + e.getBalance());
                        }
                    }
                }
            }
        }

        TreeItem treeItem = new RecursiveTreeItem<>(bListCopy, RecursiveTreeObject::getChildren);
        tbl_bill.setRoot(treeItem);
        tbl_bill.setShowRoot(false);

        bListCopy.forEach((e) -> {
            if (e.getSt() == 1 || e.getSt() == 2) {
                remning = remning + e.getPaidamt();
            }
            if (e.getSt() == 5) {
                remning = remning + e.getPaidamt();
                paid = paid + e.getTotal() - e.getPaidamt();
            }
            if (e.getSt() == 3) {
                paid = paid + e.getTotal();
            }
//            if (e.getSt() == 3 && e.getPmode() == 2) {
//                cheque = cheque + e.getTotal();
//            }
            total = total + e.getTotal();
        });

//        lbl_cheque.setText(String.valueOf(cheque));
        lbl_outstanding.setText(String.valueOf(remning));
        lbl_cash.setText(String.valueOf(paid));
        lbl_total.setText(String.valueOf(total));
        for (Bill b : bListCopy) {
            b.setPaidamt(paid);
            b.setBalance(remning);
            b.setCuramount(total);
            b.setMeternumber(b.getMeter().getMetor_num());
        }
    }

    public void print() {
        ArrayList<Bill> billList = new ArrayList<>();
        billList.addAll(bListCopy);
        PhoenixSupport.printBillReport(billList);
    }

}
