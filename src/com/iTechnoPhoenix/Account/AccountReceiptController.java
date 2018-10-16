/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Account;

import com.iTechnoPhoenix.database.AccountOperation;
import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.model.AccountReceipt;
import com.iTechnoPhoenix.model.Reason;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author choudhary
 */
public class AccountReceiptController implements Initializable {

    @FXML
    private StackPane window;
    @FXML
    private JFXTextField txt_bill_number;
    @FXML
    private Label txt_customer;
    @FXML
    private Label txt_lastdate;
    @FXML
    private JFXTextField txt_delay_payment;
    @FXML
    private Label lbl_total;
    @FXML
    private JFXTextField txt_paid_amount;
    @FXML
    private Label txt_remaining_amt;
    @FXML
    private JFXTreeTableView<Reason> tbl_receipt;

    private JFXTreeTableColumn<Reason, String> tc_reason;
    private JFXTreeTableColumn<Reason, Double> tc_amount;
    private JFXTreeTableColumn<Reason, Integer> tc_serial;

    private ObservableList<Reason> reasonList;
    private JFXDialog dialog;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        reasonList = FXCollections.observableArrayList();
        initTable();
        txt_bill_number.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txt_bill_number.getText().isEmpty()) {
                    AccountOperation ao = new AccountOperation();
                    reasonList = ao.getAccountFromBillNumber(PhoenixSupport.getInteger(txt_bill_number.getText()));
                    if (!reasonList.isEmpty()) {
                        refreshTable();
                        txt_customer.setText(reasonList.get(0).getName());
                        txt_lastdate.setText(reasonList.get(0).getAccount().getDate());
                        lbl_total.setText(String.valueOf(reasonList.get(0).getAccount().getTotalAmt()));
                        txt_remaining_amt.setText(lbl_total.getText());
                    } else {
                        PhoenixSupport.Error("खर्च बिल क्रमांक चुकीचा आहे.", window);
                    }
                }
            }
        });
        txt_delay_payment.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                lbl_total.setText(String.valueOf(reasonList.get(0).getAccount().getTotalAmt() + PhoenixSupport.getDouble(txt_delay_payment.getText())));
                txt_remaining_amt.setText(String.valueOf(PhoenixSupport.getDouble(lbl_total.getText()) - PhoenixSupport.getDouble(txt_paid_amount.getText())));
            }
        });
        txt_paid_amount.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txt_paid_amount.getText().isEmpty()) {
                    txt_remaining_amt.setText(String.valueOf(PhoenixSupport.getDouble(lbl_total.getText()) - PhoenixSupport.getDouble(txt_paid_amount.getText())));
                } else {
                    txt_paid_amount.setText("0");
                }
            }
        });
    }

    @FXML
    private void btn_cash_paid_key(KeyEvent event) {

    }

    @FXML
    private void btn_cash_paid(ActionEvent event) {
    }

    @FXML
    private void btn_cheque_paid_key(KeyEvent event) {
    }

    @FXML
    private void btn_cheque_paid(ActionEvent event) {
    }

    @FXML
    private void btn_cancel_key(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) {
            cancel();
        }
    }

    @FXML
    private void btn_cancel(ActionEvent event) {
        cancel();
    }

    private void cash_paid() {
        AccountReceipt receipt = new AccountReceipt();
        receipt.setPaid_amt(PhoenixSupport.getDouble(txt_paid_amount.getText()));
        receipt.setPaymode(1);
        receipt.setAccount(reasonList.get(0).getAccount());
        AccountOperation ao = new AccountOperation();
        int i = ao.insertReceiptDetails(receipt, window);
        if (i < 0) {
            JFXButton btnDPrint = new JFXButton("प्रिंट करा");
            btnDPrint.getStyleClass().add("btn-search");
            btnDPrint.setOnAction(e -> {
                cancel();
                dialog.close();
            });
            btnDPrint.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    cancel();
                    dialog.close();
                }
            });
            JFXButton btnDCancel = new JFXButton("रद्द करा");
            btnDCancel.getStyleClass().add("btn-cancel");
            btnDCancel.setOnAction(e -> {
                cancel();
                dialog.close();
            });
            btnDCancel.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    cancel();
                    dialog.close();
                }
            });
            dialog = Support.getDialog(window, new Label("पावती व्यवहार"), new Label("पावती जतन झाली आहे."), btnDPrint, btnDCancel);
            dialog.show();
            dialog.setOnDialogOpened(e -> {
                btnDCancel.requestFocus();
            });
        }
    }

    private void cancel() {
        txt_bill_number.clear();
        txt_delay_payment.clear();
        txt_paid_amount.clear();
        lbl_total.setText("");
        txt_customer.setText("");
        txt_lastdate.setText("");
        txt_remaining_amt.setText("");
        reasonList.clear();
        refreshTable();
    }

    private void initTable() {
        tc_serial = new JFXTreeTableColumn<>("क्रमांक");
        tc_serial.setCellValueFactory(param -> new SimpleIntegerProperty(reasonList.indexOf(param.getValue().getValue())).asObject());
        tc_reason = new JFXTreeTableColumn<>("कारण");
        tc_reason.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getReason()));
        tc_reason.setMinWidth(150);
        tc_amount = new JFXTreeTableColumn<>("रक्कम");
        tc_amount.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getAmount()).asObject());
        tc_amount.setMinWidth(150);
        tbl_receipt.getColumns().addAll(tc_serial, tc_reason, tc_amount);
    }

    private void refreshTable() {
        TreeItem<Reason> treeItem = new RecursiveTreeItem<>(reasonList, RecursiveTreeObject::getChildren);
        tbl_receipt.setRoot(treeItem);
        tbl_receipt.setShowRoot(false);
    }

}
