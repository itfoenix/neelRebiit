/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Print;

import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.database.MeterOperation;
import com.iTechnoPhoenix.database.ReceiptOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import englishtomarathinumberconvertor.MarathiNumber;
import java.util.ArrayList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author NARENDRA JADHAV
 */
public class ReceiptPrintingController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private JFXComboBox<String> cb_duration;

    @FXML
    private JFXTreeTableView<Bill> tbl_receipt;

    private JFXTreeTableColumn<Bill, Integer> tclaction;
    private JFXTreeTableColumn<Bill, String> tclreceiptdate;
    private JFXTreeTableColumn<Bill, Integer> tclreceiptnumber;
    private JFXTreeTableColumn<Bill, String> tclcustomername;
    private JFXTreeTableColumn<Bill, Integer> tclbillno;
    private JFXTreeTableColumn<Bill, Double> tclamount;
    private JFXTreeTableColumn<Bill, Integer> tclmode;

    private ReceiptOperation recieptdb;
    private ObservableList<Bill> billList;
    private ObservableSet<String> meterReceiptCustList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb_duration.setItems(PhoenixConfiguration.getMonth());
        cb_duration.getStyleClass().add("label-marathi");

        meterReceiptCustList = FXCollections.observableSet();
        CustomerOperation co = new CustomerOperation();
        for (Customer customer : co.getCustomerName()) {
            meterReceiptCustList.add(customer.getName());
        }
        MeterOperation mo = new MeterOperation();
        meterReceiptCustList.addAll(mo.getMeterNumber());
        recieptdb = new ReceiptOperation();
        meterReceiptCustList.addAll(recieptdb.getReceiptNo());
        TextFields.bindAutoCompletion(txt_meter_customer, meterReceiptCustList);

        initTable();
    }

    @FXML
    private void btn_print_all(ActionEvent event) {
        if (!billList.isEmpty()) {
            printAllReceipt();
        }
    }

    @FXML
    private void btn_search(ActionEvent event) {
        if (PhoenixSupport.isValidate(txt_meter_customer) && !PhoenixSupport.isValidate(cb_duration)) {
            SearchReport(1);
        } else {
            SearchReport(2);
        }
    }

    @FXML
    private void btn_search_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (PhoenixSupport.isValidate(txt_meter_customer) && !PhoenixSupport.isValidate(cb_duration)) {
                SearchReport(1);
            } else {
                SearchReport(2);
            }
        }
    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!billList.isEmpty()) {
                printAllReceipt();
            }
        }
    }

    public void initTable() {
        tclbillno = new JFXTreeTableColumn<>("बिल क्रमांक");
        tclbillno.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillref()).asObject());
        tclbillno.setPrefWidth(95);
        tclreceiptdate = new JFXTreeTableColumn<>("पावतीची तारिक");
        tclreceiptdate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPdate()));
        tclreceiptdate.setPrefWidth(95);
        tclcustomername = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
        tclcustomername.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCust().getName()));
        tclcustomername.setPrefWidth(95);
        tclmode = new JFXTreeTableColumn<>("देयक पद्धत");
        tclmode.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getPmode()).asObject());
        tclmode.setPrefWidth(95);
        tclreceiptnumber = new JFXTreeTableColumn<>("पावती क्रमांक");
        tclreceiptnumber.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getRid()).asObject());
        tclreceiptnumber.setPrefWidth(95);
        tclamount = new JFXTreeTableColumn<>("भरलेली रक्कम");
        tclamount.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal()).asObject());
        tclamount.setPrefWidth(95);
        tclaction = new JFXTreeTableColumn<>("");
        tclaction.setPrefWidth(95);
        tclaction.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillno()).asObject());
        tclaction.setCellFactory(e -> new ActionCell(tbl_receipt));
        tbl_receipt.getColumns().addAll(tclaction, tclcustomername, tclreceiptnumber, tclreceiptdate, tclbillno, tclmode, tclamount);
    }

    public void refreshTable() {
        TreeItem<Bill> treeItem = new RecursiveTreeItem<>(billList, RecursiveTreeObject::getChildren);
        tbl_receipt.setRoot(treeItem);
        tbl_receipt.setShowRoot(false);
    }

    public void SearchReport(int i) {
        billList = FXCollections.observableArrayList();
        if (i == 2) {
            recieptdb = new ReceiptOperation();
            if (PhoenixSupport.isValidate(cb_duration)) {
                if (txt_meter_customer.getText().isEmpty()) {
                    billList = recieptdb.getAllReceipt(cb_duration.getSelectionModel().getSelectedItem());
                } else {
                    billList = recieptdb.getAllReceipts(cb_duration.getSelectionModel().getSelectedItem(), txt_meter_customer.getText());
                }
            }
        } else if (i == 1) {
            billList = recieptdb.getAllReceiptName(txt_meter_customer.getText());
        }
        ObservableList<Bill> tmeplist = FXCollections.observableArrayList();
        tmeplist.addAll(billList);
        billList.clear();
        tmeplist.forEach((e) -> {
            MarathiNumber mn = new MarathiNumber();
            e.setNumberInWord(mn.getMarathiNumber(e.getTotal()));
            e.setPdate(e.getPdate().split(" ")[0]);
            e.setPeriod(cb_duration.getSelectionModel().getSelectedItem());
            billList.add(e);
        });
        refreshTable();
        txt_meter_customer.clear();
        cb_duration.getSelectionModel().clearSelection();
    }

    private void printAllReceipt() {
        ArrayList<Bill> meterBillList = new ArrayList<>();
        meterBillList.addAll(billList);
        PhoenixSupport ps = new PhoenixSupport();
        ps.printAllReceipt(meterBillList);
    }

    @FXML
    private void printallList(ActionEvent event) {
        ArrayList<Bill> meterBillList = new ArrayList<>();
        meterBillList.addAll(billList);
        PhoenixSupport ps = new PhoenixSupport();
        ps.printAllReceiptList(meterBillList);
    }

    public class ActionCell extends JFXTreeTableCell<Bill, Integer> {

        final JFXButton print = new JFXButton("Print");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<Bill> table) {
            actiongroup.setAlignment(Pos.CENTER);
            actiongroup.getChildren().addAll(print);
            print.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Print_24px.png"));
            print.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            print.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    tbl_receipt.getSelectionModel().select(getTreeTableRow().getIndex());
                    Bill b = tbl_receipt.getSelectionModel().getSelectedItem().getValue();
                    PhoenixSupport ps = new PhoenixSupport();
                    ArrayList<Bill> bill = new ArrayList<>();
                    bill.add(b);
                    ps.printReceipt(bill);
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
