/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Account.Print;

import com.iTechnoPhoenix.database.AccountOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Reason;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author choudhary
 */
public class AccBillPrintingController implements Initializable {

    @FXML
    private StackPane window;

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private JFXTreeTableView<Account> tbl_bill;

    @FXML
    private JFXDatePicker dp_sdate;

    @FXML
    private JFXDatePicker dp_edate;

    private JFXTreeTableColumn<Account, String> tcbilldate;
    private JFXTreeTableColumn<Account, Integer> tcbillnumber;
    private JFXTreeTableColumn<Account, String> tcstatus;
    private JFXTreeTableColumn<Account, Double> tcamount;
    private JFXTreeTableColumn<Account, Integer> tcaction;
    private JFXTreeTableColumn<Account, String> tccustomer;

    private ObservableSet<String> custList;
    private ObservableList<Account> accountList;
    private StringConverter<Double> strConvert;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        custList = FXCollections.observableSet();
        CustomerOperation co = new CustomerOperation();
        for (Customer c : co.getCustomerName()) {
            custList.add(c.getName());
        }
        AccountOperation ao = new AccountOperation();
        for (Reason a : ao.getAllAccount()) {
            custList.add(String.valueOf(a.getAccount().getAccount_id()));
        }

        TextFields.bindAutoCompletion(txt_meter_customer, custList);
        initTable();
    }

    @FXML
    private void btn_print_all(ActionEvent event) {
        printAllAccountBills();
    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            printAllAccountBills();
        }
    }

    @FXML
    private void btn_print_list(ActionEvent event) {
        printAllList();
    }

    @FXML
    private void btn_print_list_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            printAllList();
        }
    }

    @FXML
    private void btn_search(ActionEvent event) {
        search();
    }

    @FXML
    private void btn_search_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            search();
        }
    }

    private void refreshTable() {
        TreeItem<Account> treeItem = new RecursiveTreeItem<>(accountList, RecursiveTreeObject::getChildren);
        tbl_bill.setRoot(treeItem);
        tbl_bill.setShowRoot(false);
    }

    private void printAllList() {
        if (!accountList.isEmpty()) {
            List<Account> accList = new ArrayList<>();
            ObservableList<Account> billListPrint = FXCollections.observableArrayList();
            AccountOperation ao = new AccountOperation();
            for (TreeItem<Account> treeItem : tbl_bill.getRoot().getChildren()) {
                billListPrint.add(treeItem.getValue());
            }
            accList = billListPrint.subList(0, billListPrint.size());
            PhoenixSupport ps = new PhoenixSupport();
            ps.printAllAccountBillList(accList);
        } else {
            PhoenixSupport.Error("प्रिंट करण्यासाठी कुठलीच माहिती नाही आहे.", window);
        }
    }

    private void printAllAccountBills() {
        if (!accountList.isEmpty()) {
            ObservableList<Reason> billListPrint = FXCollections.observableArrayList();
            AccountOperation ao = new AccountOperation();
            for (TreeItem<Account> treeItem : tbl_bill.getRoot().getChildren()) {
                billListPrint = ao.getAccountFromBillNumber(treeItem.getValue().getAccount_id());
                ArrayList<Reason> billList = new ArrayList<>();
                for (Reason r : billListPrint) {
                    billList.add(r);
                }
                PhoenixSupport ps = new PhoenixSupport();
                ps.printAllAccountBill(billList);
            }
        } else {
            PhoenixSupport.Error("प्रिंट करण्यासाठी कुठलीच माहिती नाही आहे.", window);
        }
    }

    private void search() {
        accountList = FXCollections.observableArrayList();
        AccountOperation ao = new AccountOperation();
        if (!txt_meter_customer.getText().isEmpty() && dp_sdate.getValue() != null && dp_edate.getValue() != null) {
            accountList = ao.searchBills(1, txt_meter_customer.getText(), dp_sdate.getValue().toString(), dp_edate.getValue().toString());
        } else if (txt_meter_customer.getText().isEmpty() && dp_sdate.getValue() != null && dp_edate.getValue() != null) {
            accountList = ao.searchBills(2, null, dp_sdate.getValue().toString(), dp_edate.getValue().toString());
        } else if (!txt_meter_customer.getText().isEmpty() && dp_sdate.getValue() == null && dp_edate.getValue() == null) {
            accountList = ao.searchBills(3, txt_meter_customer.getText(), null, null);
        }
        if (!accountList.isEmpty()) {
            refreshTable();
        }
    }

    private void initTable() {
        tcaction = new JFXTreeTableColumn<>();
        tcaction.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAccount_id()).asObject());
        tcaction.setCellFactory(param -> new ActionCell(tbl_bill));
        tcbillnumber = new JFXTreeTableColumn<>("खर्च बिल क्र.");
        tcbillnumber.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAccount_id()).asObject());
        tccustomer = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
        tccustomer.setMinWidth(200);
        tccustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomer().getName()));
        tcbilldate = new JFXTreeTableColumn<>("खर्च बिल दिनांक");
        tcbilldate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDate()));
        tcamount = new JFXTreeTableColumn<>("रक्कम");
        tcamount.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotalAmt()).asObject());
        tcstatus = new JFXTreeTableColumn<>("स्तिथी");
        tcstatus.setCellValueFactory(param -> {
            String stat = null;
            if (param.getValue().getValue().getStatus() == 0) {
                stat = "होय";
            } else if (param.getValue().getValue().getStatus() == 1) {
                stat = "नाही";
            }
            return new SimpleStringProperty(stat);
        });
        tbl_bill.getColumns().addAll(tcaction, tcbillnumber, tccustomer, tcbilldate, tcamount, tcstatus);
    }

    public class ActionCell extends JFXTreeTableCell<Account, Integer> {

        final JFXButton print = new JFXButton("Print");
        final JFXButton edit = new JFXButton("Edit");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<Account> table) {
            actiongroup.setAlignment(Pos.CENTER);
            actiongroup.getChildren().addAll(print, edit);
            print.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Print_24px.png"));
            edit.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Edit Row_48px.png"));
            print.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            edit.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            print.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ObservableList<Reason> reasonListPrint;
                    tbl_bill.getSelectionModel().select(getTreeTableRow().getIndex());
                    Account b = tbl_bill.getSelectionModel().getSelectedItem().getValue();
                    AccountOperation ao = new AccountOperation();
                    reasonListPrint = ao.getAccountFromBillNumber(b.getAccount_id());
                    ArrayList<Reason> billList = new ArrayList<>();
                    for (Reason r : reasonListPrint) {
                        billList.add(r);
                    }
                    PhoenixSupport.printAccountBill(billList);
                }
            });
            edit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                JFXTreeTableView<Reason> tbl_account;

                @Override
                public void handle(MouseEvent event) {
                    ObservableList<Reason> reasonListPrint;
                    tbl_bill.getSelectionModel().select(getTreeTableRow().getIndex());
                    Account b = tbl_bill.getSelectionModel().getSelectedItem().getValue();
                    AccountOperation ao = new AccountOperation();
                    reasonListPrint = ao.getAccountFromBillNumber(b.getAccount_id());

                    try {
                        PhoenixSupport.accountBill = b.getAccount_id();
                        StackPane viewBox = FXMLLoader.load(getClass().getResource("/com/iTechnoPhoenix/Account/AccountingBill.fxml"));
                        JFXDialog dialog = Support.getDialog(window, viewBox, JFXDialog.DialogTransition.TOP);
                        dialog.show();
                        dialog.setOnDialogClosed(e -> {
                            PhoenixSupport.accountBill = 0;
                            accountList.clear();
                            search();
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(AccBillPrintingController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            );
        }

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(actiongroup);
            } else {
                setGraphic(null);
            }
        }
    }
}
