/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Account;

import com.iTechnoPhoenix.database.AccountOperation;
import com.iTechnoPhoenix.database.BankOpearation;
import com.iTechnoPhoenix.model.AccountReceipt;
import com.iTechnoPhoenix.model.Banks;
import com.iTechnoPhoenix.model.Reason;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import englishtomarathinumberconvertor.MarathiNumber;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    private ObservableList<Banks> bankList;
    private JFXDialog dialog;
    private JFXTextField txt_cheque;
    private JFXComboBox<Banks> cb_banklist;
    private Banks bank = new Banks();

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
                    if (!ao.isExist(PhoenixSupport.getInteger(txt_bill_number.getText()))) {
                        reasonList = ao.getAccountFromBillNumber(PhoenixSupport.getInteger(txt_bill_number.getText()));
                        if (!reasonList.isEmpty()) {
                            refreshTable();
                            txt_customer.setText(reasonList.get(0).getName());
                            txt_lastdate.setText(reasonList.get(0).getAccount().getDate());
                            lbl_total.setText(String.valueOf(reasonList.get(0).getAccount().getTotalAmt()));
                            txt_remaining_amt.setText(lbl_total.getText());
                        } else {
                            PhoenixSupport.Error("खर्च बिल क्रमांक चुकीचा आहे.", window);
                            cancel();
                            txt_bill_number.clear();
                        }
                    } else {
                        PhoenixSupport.Error("ह्या बिला मागील रक्कम आधीच मिळाली आहे.", window);
                        txt_bill_number.clear();
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
        if (event.getCode() == KeyCode.ENTER) {
            cash_paid(1);
        }
    }

    @FXML
    private void btn_cash_paid(ActionEvent event) {
        cash_paid(1);
    }

    @FXML
    private void btn_cheque_paid_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            chequeDialog();
        }
    }

    @FXML
    private void btn_cheque_paid(ActionEvent event) {
        chequeDialog();
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

    private void cash_paid(int mode) {
        AccountReceipt receipt = new AccountReceipt();
        receipt.setPaid_amt(PhoenixSupport.getDouble(txt_paid_amount.getText()));
        receipt.setPaymode(mode);
        if (mode == 2) {
            receipt.setBank_id(cb_banklist.getSelectionModel().getSelectedItem().getBid());
            receipt.setBankname(cb_banklist.getSelectionModel().getSelectedItem().getBankname());
            receipt.setCheque_no(txt_cheque.getText());
        }
        receipt.setDelay_amt(PhoenixSupport.getDouble(txt_delay_payment.getText()));
        receipt.setAccount(reasonList.get(0).getAccount());
        receipt.setAccount_id(PhoenixSupport.getInteger(txt_bill_number.getText()));
        receipt.setTotal_amt(receipt.getAccount().getTotalAmt());
        receipt.setName(receipt.getAccount().getCustomer().getName());
        receipt.setAddress(receipt.getAccount().getCustomer().getAddress());
        receipt.setPaydate(LocalDate.now().toString().split(" ")[0]);
        AccountOperation ao = new AccountOperation();
        int i = 0;
        if (receipt.getPaid_amt() == receipt.getTotal_amt() + receipt.getDelay_amt()) {
            i = ao.insertReceiptDetails(receipt, window);
        } else {
            PhoenixSupport.Error("भरलेली रक्कम हि कमी आहे.", window);
        }
        if (i > 0) {
            int j = i;
            JFXButton btnDPrint = new JFXButton("प्रिंट करा");
            btnDPrint.getStyleClass().add("btn-search");
            btnDPrint.setOnAction(e -> {
                receipt.setAreceipt_id(j);
                MarathiNumber mn = new MarathiNumber();
                receipt.setNumberInWord(mn.getMarathiNumber(receipt.getPaid_amt()));
                ArrayList<AccountReceipt> alist = new ArrayList<>();
                alist.add(receipt);
                PhoenixSupport.printAccountReceipt(alist);
                cancel();
                dialog.close();
            });
            btnDPrint.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    receipt.setAreceipt_id(j);
                    MarathiNumber mn = new MarathiNumber();
                    receipt.setNumberInWord(mn.getMarathiNumber(receipt.getPaid_amt()));
                    ArrayList<AccountReceipt> alist = new ArrayList<>();
                    alist.add(receipt);
                    PhoenixSupport.printAccountReceipt(alist);
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

    public void chequeDialog() {

        BankOpearation bankdb = new BankOpearation();

        bankList = FXCollections.observableArrayList();
        bankList = bankdb.getBanksByCode("");

        StackPane dialogWindow = new StackPane();
        dialogWindow.setPrefSize(640, 320);

        VBox outerbox = new VBox();
        outerbox.setAlignment(Pos.TOP_CENTER);

        // header
        Label header = new Label("बँकेची माहित");
        header.setStyle("-fx-background-color: #1F9CFF;-fx-text-fill:white;");
        VBox.setVgrow(header, Priority.ALWAYS);
        header.setMaxWidth(1.7976931348623157E308);
        VBox.setMargin(header, new Insets(8));
        header.setPadding(new Insets(8, 0, 8, 8));
        // new button
        JFXButton newBank = new JFXButton("नवीन बँक");
        newBank.setTextFill(Color.WHITE);
        newBank.setStyle("-fx-background-color:#4BB89B");
        VBox.setMargin(newBank, new Insets(8, 16, 0, 8));

        // innerStock
        StackPane innerStock = new StackPane();
        VBox.setMargin(innerStock, new Insets(8));
        innerStock.setStyle("-fx-border-color: #CECFD0; -fx-border-width: 1;");
        VBox newbox = new VBox();
        newbox.setAlignment(Pos.CENTER_RIGHT);
        newbox.setVisible(false);
        StackPane.setMargin(newbox, new Insets(8));
        newbox.setPadding(new Insets(8, 16, 8, 16));

        JFXTextField txt_bank = new JFXTextField();
        txt_bank.setPromptText("बँक नाव");
        VBox.setMargin(txt_bank, new Insets(8));
        txt_bank.setPadding(new Insets(8, 16, 8, 16));
        JFXTextField txt_code = new JFXTextField();
        txt_code.setPromptText("बँक IFSC Code");
        VBox.setMargin(txt_code, new Insets(8));
        txt_code.setPadding(new Insets(8, 16, 8, 16));
        JFXTextField txt_branch = new JFXTextField();
        txt_branch.setPromptText("बँक शाखा");
        VBox.setMargin(txt_branch, new Insets(8));
        txt_branch.setPadding(new Insets(8, 16, 8, 16));

        JFXButton btn_banksave = new JFXButton("जतन करा");
        btn_banksave.setStyle("-fx-background-color: #4BB89B; -fx-text-fill:white;");
        VBox.setMargin(btn_banksave, new Insets(8, 16, 8, 16));
        btn_banksave.setPadding(new Insets(8));
        JFXButton btn_bankclose = new JFXButton("रद्द करा");
        btn_bankclose.setStyle("-fx-background-color:#3E4A4F; -fx-text-fill:white;");
        VBox.setMargin(btn_bankclose, new Insets(8, 16, 8, 16));
        btn_bankclose.setPadding(new Insets(8));

        HBox buttonrow = new HBox();
        buttonrow.setSpacing(8);
        VBox.setMargin(buttonrow, new Insets(8, 16, 8, 16));
        buttonrow.setAlignment(Pos.CENTER_RIGHT);
        buttonrow.getChildren().addAll(btn_banksave, btn_bankclose);

        newbox.getChildren().addAll(txt_bank, txt_code, txt_branch, buttonrow);

        VBox billbank = new VBox();

        billbank.setAlignment(Pos.CENTER_RIGHT);

        cb_banklist = new JFXComboBox<>();
        cb_banklist.setMaxWidth(1.7976931348623157E308);
        cb_banklist.setPromptText("बँक नाव");
        cb_banklist.setItems(bankList);
        VBox.setMargin(cb_banklist, new Insets(8));

        txt_cheque = new JFXTextField();
        txt_cheque.setPromptText("चेक क्रमांक");
        VBox.setMargin(txt_cheque, new Insets(8));

        JFXButton btn_billsave = new JFXButton("जतन करा");
        btn_billsave.setStyle("-fx-background-color: #4BB89B;-fx-text-fill:white;");
        JFXButton btn_billcancl = new JFXButton("रद्ध करा");
        btn_billcancl.setStyle("-fx-background-color:#3E4A4F; -fx-text-fill:white;");
        VBox.setMargin(btn_billsave, new Insets(8, 8, 8, 8));

        StackPane.setMargin(billbank, new Insets(8));
        billbank.setPadding(new Insets(8, 16, 8, 16));
        HBox hb = new HBox();
        hb.setSpacing(16);
        hb.setAlignment(Pos.CENTER_RIGHT);
        hb.getChildren().addAll(btn_billsave, btn_billcancl);
        billbank.getChildren().addAll(cb_banklist, txt_cheque, hb);

        innerStock.getChildren().addAll(newbox, billbank);

        StackPane.setMargin(dialogWindow, new Insets(8));

        outerbox.getChildren().addAll(header, newBank, innerStock);
        dialogWindow.getChildren().add(outerbox);

        JFXDialog dialog = Support.getDialog(window, dialogWindow, JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(false);

        btn_billcancl.setOnMouseClicked(e -> {
            dialog.close();
        });

        btn_billcancl.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {

                dialog.close();
            }
        });

        dialog.show();

        btn_bankclose.setOnMouseClicked((e) -> {
            txt_bank.clear();
            txt_branch.clear();
            txt_code.clear();
            newbox.setVisible(false);
            billbank.setVisible(true);
        });

        btn_bankclose.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                txt_bank.clear();
                txt_branch.clear();
                txt_code.clear();
                newbox.setVisible(false);
                billbank.setVisible(true);
            }
        });

        btn_banksave.setOnMouseClicked((e) -> {
            if (PhoenixSupport.isValidate(txt_code, txt_bank, txt_branch)) {
                bank.setBankname(txt_bank.getText());
                bank.setBranch(txt_branch.getText());
                bank.setCode(txt_code.getText());
                bankdb.saveBank(bank, window);
                bankList.add(bank);
                newbox.setVisible(false);
                billbank.setVisible(true);
            } else {
                PhoenixSupport.Error("सर्व माहिती भरा", window);
            }
        });

        btn_banksave.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (PhoenixSupport.isValidate(txt_code, txt_bank, txt_branch)) {
                    bank.setBankname(txt_bank.getText());
                    bank.setBranch(txt_branch.getText());
                    bank.setCode(txt_code.getText());
                    bankdb.saveBank(bank, window);
                    bankList.add(bank);
                    newbox.setVisible(false);
                    billbank.setVisible(true);
                } else {
                    PhoenixSupport.Error("सर्व माहिती भरा", window);
                }
            }
        });

        newBank.setOnMouseClicked((e) -> {
            txt_bank.clear();
            txt_branch.clear();
            txt_code.clear();
            newbox.setVisible(true);
            billbank.setVisible(false);
        });

        newBank.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                txt_bank.clear();
                txt_branch.clear();
                txt_code.clear();
                newbox.setVisible(true);
                billbank.setVisible(false);
            }
        });

        btn_billsave.setOnMouseClicked(e -> {
            cash_paid(2);
            dialog.close();
        });

        btn_billsave.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                cash_paid(2);
                dialog.close();
            }
        });
    }

}
