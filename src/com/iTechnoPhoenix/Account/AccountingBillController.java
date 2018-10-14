/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Account;

import com.iTechnoPhoenix.database.AccountOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private JFXTreeTableView<Account> tbl_account;

    private JFXTreeTableColumn<Account, Integer> tc_action;
    private JFXTreeTableColumn<Account, Integer> tc_billNo;
    private JFXTreeTableColumn<Account, String> tc_customer;
    private JFXTreeTableColumn<Account, String> tc_date;
    private JFXTreeTableColumn<Account, Double> tc_amt;
    private JFXTreeTableColumn<Account, String> tc_reason;

    @FXML
    private StackPane window;
    private CustomerOperation co;
    private JFXListView listView;
    private ObservableList<Meter> meterList;
    private boolean open = false;
    private Meter meter;
    private Customer customer;
    private ObservableList<Account> accountList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CustomerOperation co = new CustomerOperation();
        ObservableList<String> customerName = co.getAllCustomerName();
        TextFields.bindAutoCompletion(txt_name, customerName);
        accountList = FXCollections.observableArrayList();
        initTable();
        refreshTable();
        txt_name.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txt_name.getText().isEmpty()) {
                    meterList = co.getCustomerDetails(txt_name.getText());
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
                    listView.setItems(meterList);
                    vb.getChildren().add(listView);
                    JFXButton cancel = new JFXButton("राध करा");
                    cancel.getStyleClass().add("btn-cancel");
                    if (!open) {
                        JFXDialogLayout bodyContain = new JFXDialogLayout();
                        bodyContain.setHeading(new Label("ग्राहक निवडा"));
                        bodyContain.setBody(vb);
                        bodyContain.setActions(cancel);
                        JFXDialog dialogs = new JFXDialog(window, bodyContain, JFXDialog.DialogTransition.CENTER);
                        dialogs.setOverlayClose(false);
                        cancel.setOnAction(e -> {
                            dialogs.close();
                            open = false;
                            txt_name.clear();
                        });
                        cancel.setOnKeyPressed(e -> {
                            if (e.getCode() == KeyCode.ENTER) {
                                dialogs.close();
                                open = false;
                                txt_name.clear();
                            }
                        });
                        listView.setOnMouseClicked(e -> {
                            if (e.getClickCount() == 2) {
                                meter = (Meter) listView.getFocusModel().getFocusedItem();
                                customer = meter.getCustomeObject();
                                dialogs.close();
                                open = false;
                            }
                        });
                        listView.setOnKeyPressed(e -> {
                            if (e.getCode() == KeyCode.ENTER) {
                                meter = (Meter) listView.getFocusModel().getFocusedItem();
                                customer = meter.getCustomeObject();
                                dialogs.close();
                                open = false;
                            }
                        });
                        dialogs.show();
                    }
                }
            }
        });
    }

    @FXML
    private void btn_cancel(ActionEvent event) {
        cancel();
    }

    @FXML
    private void btn_cancel_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            cancel();
        }
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

    private void cancel() {
        txt_amount.clear();
        txt_name.clear();
        txt_reason.clear();
    }

    private void initTable() {
        tc_action = new JFXTreeTableColumn<>();
        tc_action.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAccount_id()).asObject());
        tc_action.setCellFactory(param -> new ActionCell(tbl_account));
        tc_billNo = new JFXTreeTableColumn<>("खर्च बिल");
        tc_billNo.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAccount_id()).asObject());
        tc_customer = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
        tc_customer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomer().getName()));
        tc_date = new JFXTreeTableColumn<>("बिलाची तारिक");
        tc_date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDate()));
        tc_amt = new JFXTreeTableColumn<>("एकूण रक्कम");
        tc_amt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getAmount()).asObject());
        tc_reason = new JFXTreeTableColumn<>("कारण");
        tc_reason.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getReason()));
        tbl_account.getColumns().addAll(tc_action, tc_customer, tc_billNo, tc_date, tc_amt, tc_reason);
    }

    public void refreshTable() {
        AccountOperation ao = new AccountOperation();
        accountList = ao.getAllAccount();
        TreeItem<Account> treeItem = new RecursiveTreeItem<>(accountList, RecursiveTreeObject::getChildren);
        tbl_account.setRoot(treeItem);
        tbl_account.setShowRoot(false);
    }

    public class ActionCell extends JFXTreeTableCell<Account, Integer> {

        final JFXButton edit = new JFXButton("Edit");
        final JFXButton delete = new JFXButton("Delete");
        final JFXButton print = new JFXButton("Print");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<Account> table) {
            actiongroup.getChildren().addAll(edit, print, delete);
            edit.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Edit Row_48px.png"));
            edit.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            print.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Print_24px.png"));
            print.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            delete.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Trash Can_48px.png"));
            delete.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            edit.setOnMouseClicked(event -> {
                table.getSelectionModel().select(getTreeTableRow().getIndex());
                Account account = table.getSelectionModel().getSelectedItem().getValue();
                VBox vb = new VBox();
                JFXTextField txtNum = new JFXTextField();
                txtNum.setPromptText("बिल क्रमांक");
                txtNum.setLabelFloat(true);
                txtNum.setMaxWidth(200);
                txtNum.setText(String.valueOf(account.getAccount_id()));
                txtNum.setEditable(false);
                JFXTextField txtCust = new JFXTextField();
                txtCust.setPromptText("ग्राहकाच नाव");
                txtCust.setLabelFloat(true);
                txtCust.setMaxWidth(200);
                txtCust.setText(String.valueOf(account.getCustomer().getCust_num()));
                txtCust.setEditable(false);
                JFXTextField txtAmt = new JFXTextField();
                PhoenixSupport.onlyNumber(txtAmt);
                txtAmt.setPromptText("रक्कम");
                txtAmt.setLabelFloat(true);
                txtAmt.setMaxWidth(200);
                txtAmt.setText(String.valueOf(account.getAmount()));
                JFXTextArea txtRes = new JFXTextArea();
                txtRes.setPromptText("कारण");
                txtRes.setLabelFloat(true);
                txtRes.setMaxWidth(200);
                txtRes.setText(account.getReason());
                txtRes.setPrefRowCount(3);
                vb.setSpacing(16);
                vb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(txtNum, txtCust, txtAmt, txtRes);
                JFXButton btnUpdate = new JFXButton("जतन");
                btnUpdate.getStyleClass().add("btn-search");
                JFXButton btnDelete = new JFXButton("राध");
                btnDelete.getStyleClass().add("btn-cancel");
                JFXDialog dialog = Support.getDialog(window, new Label("युनिट बदल करणे"), vb, btnUpdate, btnDelete);
                btnUpdate.setOnAction(e -> {
                    Account account1 = new Account();
                    account1.setAccount_id(account.getAccount_id());
                    account1.setCustomer(account.getCustomer());
                    account1.setAmount(Double.parseDouble(txtAmt.getText()));
                    account1.setDate(account.getDate());
                    account1.setReason(txtRes.getText());
                    AccountOperation ao = new AccountOperation();
                    ao.updateAccount(account, window);
                    dialog.close();
                });
                btnUpdate.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        Account account1 = new Account();
                        account1.setAccount_id(account.getAccount_id());
                        account1.setCustomer(account.getCustomer());
                        account1.setAmount(Double.parseDouble(txtAmt.getText()));
                        account1.setDate(account.getDate());
                        account1.setReason(txtRes.getText());
                        AccountOperation ao = new AccountOperation();
                        ao.updateAccount(account, window);
                        dialog.close();
                    }
                });
                btnDelete.setOnAction(e -> dialog.close());
                btnDelete.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        dialog.close();
                    }
                });
                dialog.setOnDialogOpened(e -> btnUpdate.requestFocus());
                dialog.show();
            });
            print.setOnMouseClicked(event -> {

            });
            delete.setOnMouseClicked((MouseEvent e) -> {
                table.getSelectionModel().select(getTreeTableRow().getIndex());
                Account accot = table.getSelectionModel().getSelectedItem().getValue();
                AccountOperation ao = new AccountOperation();
                ao.deleteAccount(accot.getAccount_id(), window);
                refreshTable();
            });
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
