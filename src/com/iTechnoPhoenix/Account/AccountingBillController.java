/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Account;

import com.iTechnoPhoenix.database.AccountOperation;
import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author choudhary
 */
public class AccountingBillController implements Initializable {

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXTextField txt_amount;

    @FXML
    private JFXTextArea txt_reason;

    @FXML
    private JFXTreeTableView<?> tbl_account;

    @FXML
    private StackPane window;
    private CustomerOperation co;
    private JFXListView listView;
    private ObservableList<Meter> meterList;
    private boolean open = false;
    private JFXDialog dialog;
    private Meter meter;
    private Customer customer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CustomerOperation co = new CustomerOperation();
        ObservableList<String> customerName = co.getAllCustomerName();
        TextFields.bindAutoCompletion(txt_name, customerName);
        txt_name.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                meterList = co.getCustomerDetails(txt_name.getText());
                getPopUp();
            }
        });
    }

    @FXML
    private void btn_cancel(ActionEvent event) {

    }

    @FXML
    private void btn_cancel_key(KeyEvent event) {

    }

    @FXML
    private void btn_save(ActionEvent event) {
        save();
    }

    @FXML
    private void btn_save_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            save();
        }
    }

    private void save() {

        Account account = new Account();
        account.setCustomer(customer);
        account.setAmount(Double.parseDouble(txt_amount.getText()));
        account.setReason(txt_reason.getText());
        AccountOperation ao = new AccountOperation();
        ao.insertBill(account, window);
    }

    private void getPopUp() {
        VBox vb = new VBox();
        vb.setSpacing(16);
        listView = new JFXListView();
        ObservableList<Meter> tempList = FXCollections.observableArrayList();
        for (Meter meter1 : meterList) {
            if (tempList.contains(meter1)) {
                tempList.get(tempList.indexOf(meter1)).setMetor_num(tempList.get(tempList.indexOf(meter1)).getMetor_num() + " , " + meter1.getMetor_num());
            } else {
                tempList.add(meter1);
            }
        }
        listView.setItems(tempList);
        vb.getChildren().add(listView);
        JFXButton btnCancel = new JFXButton("राध करा");
        btnCancel.getStyleClass().add("btn-cancel");
        if (!open) {
            dialog = Support.getDialog(window, new Label("ग्राहक नवडा"), vb, btnCancel);
            btnCancel.setOnAction(e -> {
                dialog.close();
                open = false;
                txt_name.clear();
            });
            btnCancel.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    dialog.close();
                    open = false;
                    txt_name.clear();
                }
            });
            listView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    meter = (Meter) listView.getFocusModel().getFocusedItem();
                    customer = meter.getCustomeObject();
                    meter.getCustomeObject();
                    dialog.close();
                    open = false;
                }
            });
            listView.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    meter = (Meter) listView.getFocusModel().getFocusedItem();
                    customer = meter.getCustomeObject();
                    meter.getCustomeObject();
                    dialog.close();
                    open = false;
                }
            });
            dialog.show();
            open = true;
            dialog.setOnDialogOpened(e -> listView.requestFocus());
        }
    }
}
