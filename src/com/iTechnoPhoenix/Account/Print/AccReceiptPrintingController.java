package com.iTechnoPhoenix.Account.Print;

import com.iTechnoPhoenix.database.AccountOperation;
import com.iTechnoPhoenix.model.AccountReceipt;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class AccReceiptPrintingController implements Initializable {

    @FXML
    private StackPane window;

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private JFXDatePicker sdate;

    @FXML
    private JFXDatePicker edate;

    @FXML
    private JFXTreeTableView<AccountReceipt> tbl_receipt;

    private JFXTreeTableColumn<AccountReceipt, Integer> tcareceipt_id;
    private JFXTreeTableColumn<AccountReceipt, Integer> tcaccount_id;
    private JFXTreeTableColumn<AccountReceipt, String> tcdate;
    private JFXTreeTableColumn<AccountReceipt, Double> tcpaid_amt;
    private JFXTreeTableColumn<AccountReceipt, Double> tctotal_amt;
    private JFXTreeTableColumn<AccountReceipt, Double> tcdelay_amt;
    private JFXTreeTableColumn<AccountReceipt, Integer> tcaction;
    private JFXTreeTableColumn<AccountReceipt, String> tcpaymod;
    private JFXTreeTableColumn<AccountReceipt, String> tcchequeno;
    private JFXTreeTableColumn<AccountReceipt, String> tccustomer;
    private JFXTreeTableColumn<AccountReceipt, String> tcchequestatus;

    private ObservableList<AccountReceipt> receiptList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTable();
    }

    @FXML
    private void btn_print_all(ActionEvent event) {
        printAllReceipt(1);
    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            printAllReceipt(1);
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

    @FXML
    private void printallList(ActionEvent event) {
        printAllReceipt(2);
    }

    @FXML
    private void printallList_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            printAllReceipt(2);
        }
    }

    private void search() {
        receiptList = FXCollections.observableArrayList();
        AccountOperation ao = new AccountOperation();
        if (!txt_meter_customer.getText().isEmpty() && sdate.getValue() != null && edate.getValue() != null) {
            receiptList = ao.searchReceipts(1, txt_meter_customer.getText(), sdate.getValue().toString(), edate.getValue().toString());
        } else if (txt_meter_customer.getText().isEmpty() && sdate.getValue() != null && edate.getValue() != null) {
            receiptList = ao.searchReceipts(2, null, sdate.getValue().toString(), edate.getValue().toString());
        } else if (!txt_meter_customer.getText().isEmpty() && sdate.getValue() == null && edate.getValue() == null) {
            receiptList = ao.searchReceipts(3, txt_meter_customer.getText(), null, null);
        }
        if (!receiptList.isEmpty()) {
            refreshTable();
        }
    }

    private void printAllReceipt(int i) {
        ArrayList<AccountReceipt> meterBillList = new ArrayList<>();
        meterBillList.addAll(receiptList);
        PhoenixSupport ps = new PhoenixSupport();
        if (i == 1) {
            ps.printAllAccountReceipt(meterBillList);
        }
        if (i == 2) {
            ps.printAllAccountReceiptList(meterBillList);
        }
    }

    private void initTable() {
        tcaction = new JFXTreeTableColumn<>();
        tcaction.setCellFactory(param -> new ActionCell(tbl_receipt));
        tcaction.setMinWidth(200);
        tcaction.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAreceipt_id()).asObject());
        tcareceipt_id = new JFXTreeTableColumn<>("पावती क्र.");
        tcareceipt_id.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAreceipt_id()).asObject());
        tcaccount_id = new JFXTreeTableColumn<>("बिल क्र.");
        tcaccount_id.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getAccount_id()).asObject());
        tccustomer = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
        tccustomer.setMinWidth(200);
        tccustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));
        tctotal_amt = new JFXTreeTableColumn<>("रक्कम");
        tctotal_amt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal_amt()).asObject());
        tcdate = new JFXTreeTableColumn<>("भरलेली तारिक");
        tcdate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPaydate()));
        tcdelay_amt = new JFXTreeTableColumn<>("विलंब रक्कम");
        tcdelay_amt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getDelay_amt()).asObject());
        tcpaid_amt = new JFXTreeTableColumn<>("भरलेली रक्कम");
        tcpaid_amt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getPaid_amt()).asObject());
        tcpaymod = new JFXTreeTableColumn<>("देयक पद्धत");
        tcpaymod.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<AccountReceipt, String>, ObservableValue<String>>() {

            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<AccountReceipt, String> param) {
                String s = null;
                if (param.getValue().getValue().getPaymode() == 1) {
                    s = "Cash";
                }
                if (param.getValue().getValue().getPaymode() == 2) {
                    s = "Cheque";
                }
                return new SimpleStringProperty(s);
            }
        });
        tcchequeno = new JFXTreeTableColumn<>("चेक क्रमांक");
        tcchequeno.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCheque_no()));
        tcchequestatus = new JFXTreeTableColumn<>("चेक स्तिथी");
        tcchequestatus.setMinWidth(150);
        tcchequestatus.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<AccountReceipt, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<AccountReceipt, String> param) {
                String s = null;
                if (param.getValue().getValue().getPaymode() == 2) {
                    if (param.getValue().getValue().getCheque_status() == 0) {
                        s = "चेक भरण्यात आले आहे.";
                    }
                    if (param.getValue().getValue().getCheque_status() == 1) {
                        s = "चेक रद्ध झाला आहे.";
                    }
                }
                return new SimpleStringProperty(s);
            }
        });
        tbl_receipt.getColumns().addAll(tcaction, tcareceipt_id, tcaccount_id, tccustomer, tctotal_amt, tcdate, tcdelay_amt, tcpaid_amt, tcpaymod, tcchequeno, tcchequestatus);
    }

    public void refreshTable() {
        TreeItem<AccountReceipt> treeItem = new RecursiveTreeItem<>(receiptList, RecursiveTreeObject::getChildren);
        tbl_receipt.setRoot(treeItem);
        tbl_receipt.setShowRoot(false);
    }

    public class ActionCell extends JFXTreeTableCell<AccountReceipt, Integer> {

        final JFXButton enables = new JFXButton("Cancel Cheque");
        final JFXButton print = new JFXButton("Print");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<AccountReceipt> table) {
            actiongroup.setAlignment(Pos.CENTER);
            enables.getStyleClass().add("btn-cancel");
            actiongroup.getChildren().addAll(print, enables);
            print.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Print_24px.png"));
            print.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            print.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ObservableList<AccountReceipt> receiptListPrint;
                    tbl_receipt.getSelectionModel().select(getTreeTableRow().getIndex());
                    AccountReceipt b = tbl_receipt.getSelectionModel().getSelectedItem().getValue();
                    AccountOperation ao = new AccountOperation();
                    receiptListPrint = ao.getReceiptFromReceiptNumber(b.getAreceipt_id());
                    ArrayList<AccountReceipt> billList = new ArrayList<>();
                    for (AccountReceipt r : receiptListPrint) {
                        billList.add(r);
                    }
                    PhoenixSupport.printAccountReceipt(billList);
                }
            });
            enables.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    tbl_receipt.getSelectionModel().select(getTreeTableRow().getIndex());
                    AccountReceipt b = tbl_receipt.getSelectionModel().getSelectedItem().getValue();
                    if (b.getPaymode() == 1) {
                        PhoenixSupport.Error("ह्या पावतीवरील रक्कम रोख रक्कम भरण्यात आली आहे.", window);
                    }
                    if (b.getPaymode() == 2) {
                        if (b.getCheque_status() == 0) {
                            AccountOperation ao = new AccountOperation();
                            b.setCheque_status(1);
                            ao.updateChequeStatus(b, window);
                            refreshTable();
                        } else {
                            PhoenixSupport.Error("ह्या पावती वरील चेक आधीच राध केला आहे.", window);
                        }
                    }
                }
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
