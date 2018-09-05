package com.iTechnoPhoenix.Print;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.database.MeterOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.MeterBill;
import com.iTechnoPhoenix.neelSupport.BillSupport;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import org.controlsfx.control.textfield.TextFields;

public class BillPrintingController implements Initializable {

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private JFXComboBox<String> cb_duration;

    @FXML
    private JFXTreeTableView<Bill> tbl_bill;

    private JFXTreeTableColumn<Bill, String> tcbilldate;
    private JFXTreeTableColumn<Bill, Integer> tcbillnumber;
    private JFXTreeTableColumn<Bill, String> tcmeterno;
    private JFXTreeTableColumn<Bill, Double> tcbalance;
    private JFXTreeTableColumn<Bill, Double> tcscharges;
    private JFXTreeTableColumn<Bill, Double> tcamount;
    private JFXTreeTableColumn<Bill, Long> tcuseunit;
    private JFXTreeTableColumn<Bill, Integer> tcaction;
    private JFXTreeTableColumn<Bill, String> tccustomer;

    private BillOperation billbd;
    private ObservableSet<String> meterBillCustList;
    private ObservableList<Bill> billList;
    private ObservableList<Bill> tmeplist;

    @FXML
    private void btn_print_all(ActionEvent event) {
        if (!billList.isEmpty()) {
            printAllBills();
        }
    }

    @FXML
    private void btn_search(ActionEvent event) {
        if (PhoenixSupport.isValidate(txt_meter_customer) && !PhoenixSupport.isValidate(cb_duration)) {
            search(1);
        } else {
            search(2);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cb_duration.setItems(PhoenixConfiguration.getMonth());
        cb_duration.getStyleClass().add("label-marathi");
        meterBillCustList = FXCollections.observableSet();
        CustomerOperation co = new CustomerOperation();
        for (Customer customer : co.getCustomerName()) {
            meterBillCustList.add(customer.getName());
        }
        MeterOperation mo = new MeterOperation();
        meterBillCustList.addAll(mo.getMeterNumber());
        billbd = new BillOperation();
        meterBillCustList.addAll(billbd.getAllBillNum());
        TextFields.bindAutoCompletion(txt_meter_customer, meterBillCustList);
        initTable();
    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {
        if (!billList.isEmpty()) {
            printAllBills();
        }
    }

    @FXML
    private void btn_search_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (PhoenixSupport.isValidate(txt_meter_customer) && !PhoenixSupport.isValidate(cb_duration)) {
                search(1);
            } else {
                search(2);
            }
        }
    }

    private void initTable() {
        tcbillnumber = new JFXTreeTableColumn<>("बिल क्रमांक");
        tcbillnumber.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillref()).asObject());
        tcmeterno = new JFXTreeTableColumn<>("मीटर क्रमांक");
        tcmeterno.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMeter().getMetor_num()));
        tcbilldate = new JFXTreeTableColumn<>("बिलाची तारिक");
        tcbilldate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getBdate()));
        tcbalance = new JFXTreeTableColumn<>("थकबाकी");
        tcbalance.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getBalance()).asObject());
        tcamount = new JFXTreeTableColumn<>("बिलाची रक्कम");
        tcamount.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal()).asObject());
        tcscharges = new JFXTreeTableColumn<>("सर चार्ज");
        tcscharges.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getScharges()).asObject());
        tcuseunit = new JFXTreeTableColumn<>("वापर युनिट");
        tcuseunit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getUseunit()).asObject());
        tccustomer = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
        tccustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCust().getName()));
        tcaction = new JFXTreeTableColumn<>("");
        tcaction.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getMeter().getId()).asObject());
        tcaction.setCellFactory(e -> new ActionCell(tbl_bill));
        tbl_bill.getColumns().addAll(tcaction, tccustomer, tcbillnumber, tcbilldate, tcmeterno, tcbalance, tcuseunit, tcscharges, tcamount);
    }

    private void refreshTable() {
        TreeItem<Bill> treeItem = new RecursiveTreeItem<>(billList, RecursiveTreeObject::getChildren);
        tbl_bill.setRoot(treeItem);
        tbl_bill.setShowRoot(false);
    }

    private void printAllBills() {
        ObservableList<Bill> billListPrint = FXCollections.observableArrayList();
        BillOperation bo = new BillOperation();
        for (TreeItem<Bill> treeItem : tbl_bill.getRoot().getChildren()) {
            billListPrint = bo.getAllBill(treeItem.getValue().getBillref());
            BillSupport billSupport = new BillSupport();
            ArrayList<MeterBill> meterBillList = billSupport.assignBillValue(billListPrint);
            PhoenixSupport ps = new PhoenixSupport();
            ps.printAllBill(meterBillList);
        }
    }

    private void search(int i) {
        billList = FXCollections.observableArrayList();
        if (i == 2) {
            if (PhoenixSupport.isValidate(cb_duration)) {
                if (txt_meter_customer.getText().isEmpty()) {
                    billList = billbd.getAllBills(cb_duration.getSelectionModel().getSelectedItem());
                } else {
                    billList = billbd.getAllBillsadd(cb_duration.getSelectionModel().getSelectedItem(), txt_meter_customer.getText());
                }
            }
        } else if (i == 1) {
            billList = billbd.getAllBillByName(txt_meter_customer.getText());
        }
        tmeplist = FXCollections.observableArrayList();
        tmeplist.addAll(billList);
        billList.clear();
        tmeplist.forEach((e) -> {
            if (!billList.contains(e)) {
                billList.add(e);
            } else {
                for (Bill bil : billList) {
                    if (bil.getBillref() == e.getBillref()) {
                        bil.getMeter().setMetor_num(bil.getMeter().getMetor_num() + " , " + e.getMeter().getMetor_num());
                        bil.setTotal(bil.getTotal() + e.getTotal());
                        bil.setBalance(bil.getBalance() + e.getBalance());
                    }
                }
            }
        });
        refreshTable();
        txt_meter_customer.clear();
        cb_duration.getSelectionModel().clearSelection();
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
                    ObservableList<Bill> billListPrint;
                    tbl_bill.getSelectionModel().select(getTreeTableRow().getIndex());
                    Bill b = tbl_bill.getSelectionModel().getSelectedItem().getValue();
                    billListPrint = billbd.getAllBill(b.getBillref());
                    BillSupport billSupport = new BillSupport();
                    ArrayList<MeterBill> meterBillList = billSupport.assignBillValue(billListPrint);
                    PhoenixSupport.printMeterBill(meterBillList);
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
