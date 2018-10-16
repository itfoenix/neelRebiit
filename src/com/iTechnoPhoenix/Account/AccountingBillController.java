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
import com.iTechnoPhoenix.model.Reason;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    private JFXTextField txt_reason;

    @FXML
    private JFXTreeTableView<Reason> tbl_account;

    private JFXTreeTableColumn<Reason, Integer> tc_action;
    private JFXTreeTableColumn<Reason, Integer> tc_billNo;
    private JFXTreeTableColumn<Reason, String> tc_customer;
    private JFXTreeTableColumn<Reason, String> tc_date;
    private JFXTreeTableColumn<Reason, Double> tc_amt;
    private JFXTreeTableColumn<Reason, String> tc_reason;

    @FXML
    private StackPane window;
    private CustomerOperation co;
    private JFXListView listView;
    private ObservableList<Meter> meterList;
    private ObservableList<Reason> reasonList;
    private boolean open = false;
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
        reasonList = FXCollections.observableArrayList();
        initTable();
//        refreshTable();
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
                    listView.setItems(tempList);
                    vb.getChildren().add(listView);
                    JFXButton cancel = new JFXButton("राध करा");
                    cancel.getStyleClass().add("btn-cancel");
                    if (!open) {
                        JFXDialog dialogs = Support.getDialog(window, new Label("ग्राहक निवडा"), vb, cancel);
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
                                txt_reason.requestFocus();
                            }
                        });
                        listView.setOnKeyPressed(e -> {
                            if (e.getCode() == KeyCode.ENTER) {
                                meter = (Meter) listView.getFocusModel().getFocusedItem();
                                customer = meter.getCustomeObject();
                                dialogs.close();
                                open = false;
                                txt_reason.requestFocus();
                            }
                        });
                        dialogs.show();
                        open = true;
                        dialogs.setOnDialogOpened(e -> listView.requestFocus());
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
        if (!reasonList.isEmpty()) {
            double sum = 0;
            AccountOperation ao = new AccountOperation();
            for (Reason r : reasonList) {
                sum = sum + r.getAmount();
            }
            reasonList.get(0).getAccount().setTotalAmt(sum);
            int id = ao.insertBill(reasonList.get(0), window);
            txt_name.setFocusTraversable(true);
            for (Reason r : reasonList) {
                r.getAccount().setAccount_id(id);
                ao.insertReason(r, window);
            }
            cancel();
            reasonList.clear();
            refreshTable();
        }
    }

    private void cancel() {
        txt_amount.clear();
        txt_reason.clear();
    }

    private void initTable() {
        tc_action = new JFXTreeTableColumn<>();
        tc_action.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getReason_id()).asObject());
        tc_action.setCellFactory(param -> new ActionCell(tbl_account));
        tc_billNo = new JFXTreeTableColumn<>("खर्च क्रमांक");
        tc_billNo.setCellValueFactory(param -> new SimpleIntegerProperty(reasonList.indexOf(param.getValue().getValue()) + 1).asObject());
//        tc_customer = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
//        tc_customer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomer().getName()));
//        tc_date = new JFXTreeTableColumn<>("बिलाची तारिक");
//        tc_date.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDate()));
        tc_amt = new JFXTreeTableColumn<>("एकूण रक्कम");
        tc_amt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getAmount()).asObject());
        tc_reason = new JFXTreeTableColumn<>("कारण");
        tc_reason.setMinWidth(200);
        tc_reason.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getReason()));
        tbl_account.getColumns().addAll(tc_action, tc_billNo, tc_amt, tc_reason);
    }

    public void refreshTable() {
        TreeItem<Reason> treeItem = new RecursiveTreeItem<>(reasonList, RecursiveTreeObject::getChildren);
        tbl_account.setRoot(treeItem);
        tbl_account.setShowRoot(false);
    }

    @FXML
    private void btn_add_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            addAccount();
        }
    }

    @FXML
    private void btn_add(ActionEvent event) {
        addAccount();
    }

    public void addAccount() {
        if (PhoenixSupport.isValidate(txt_name, txt_amount)) {
            Reason reason = new Reason();
            Account account = new Account();
            account.setCustomer(meter.getCustomeObject());
            reason.setAmount(PhoenixSupport.getDouble(txt_amount.getText()));
            reason.setReason(txt_reason.getText());
            reason.setAccount(account);
            reasonList.add(reason);
            refreshTable();
            cancel();
        }
    }

    public class ActionCell extends JFXTreeTableCell<Reason, Integer> {

        final JFXButton edit = new JFXButton("Edit");
        final JFXButton delete = new JFXButton("Delete");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<Reason> table) {
            actiongroup.getChildren().addAll(edit, delete);
            edit.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Edit Row_48px.png"));
            edit.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            delete.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Trash Can_48px.png"));
            delete.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            edit.setOnMouseClicked(event -> {
                table.getSelectionModel().select(getTreeTableRow().getIndex());
                Reason reason = table.getSelectionModel().getSelectedItem().getValue();
                VBox vb = new VBox();
                JFXTextField txtAmt = new JFXTextField();
                PhoenixSupport.onlyNumber(txtAmt);
                txtAmt.setPromptText("रक्कम");
                txtAmt.setLabelFloat(true);
                txtAmt.setMaxWidth(200);
                txtAmt.setText(String.valueOf(reason.getAmount()));
                JFXTextField txtRes = new JFXTextField();
                txtRes.setPromptText("कारण");
                txtRes.setLabelFloat(true);
                txtRes.setMaxWidth(200);
                txtRes.setText(reason.getReason());
                vb.setSpacing(16);
                vb.setAlignment(Pos.CENTER);
                vb.getChildren().addAll(txtAmt, txtRes);
                JFXButton btnUpdate = new JFXButton("जतन");
                btnUpdate.getStyleClass().add("btn-search");
                JFXButton btnDelete = new JFXButton("रद्ध");
                btnDelete.getStyleClass().add("btn-cancel");
                JFXDialog dialog = Support.getDialog(window, new Label("युनिट बदल करणे"), vb, btnUpdate, btnDelete);
                btnUpdate.setOnAction(e -> {
                    reason.setAmount(Double.parseDouble(txtAmt.getText()));
                    reason.setReason(txtRes.getText());
//                    AccountOperation ao = new AccountOperation();
//                    ao.updateAccount(reason, window);
                    dialog.close();
                    refreshTable();
                });
                btnUpdate.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        reason.setAmount(Double.parseDouble(txtAmt.getText()));
                        reason.setReason(txtRes.getText());
//                        AccountOperation ao = new AccountOperation();
//                        ao.updateAccount(reason, window);
                        dialog.close();
                        refreshTable();
                    }
                });
                btnDelete.setOnAction(e -> dialog.close());
                btnDelete.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        dialog.close();
                    }
                });
                dialog.setOnDialogOpened(e -> btnUpdate.requestFocus());
                dialog.setOnDialogClosed(e -> txt_reason.requestFocus());
                dialog.show();
            });
            delete.setOnMouseClicked((MouseEvent e) -> {
                table.getSelectionModel().select(getTreeTableRow().getIndex());
                Reason accot = table.getSelectionModel().getSelectedItem().getValue();
                reasonList.remove(accot);
                refreshTable();
                txt_reason.requestFocus();
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
