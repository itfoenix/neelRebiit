package com.iTechnoPhoenix.bills;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.database.MeterOperation;
import com.iTechnoPhoenix.database.UnitsOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.model.Unit;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.sun.deploy.panel.JreDialog;
import com.sun.javafx.print.Units;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class BillTransactionController implements Initializable {

    @FXML
    private JFXComboBox<String> cb_period;

    @FXML
    private JFXDatePicker txt_duration;

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private Label txt;

    @FXML
    private Label lbl_customer_name;

    @FXML
    private JFXTreeTableView<Bill> tbl_meter;

    @FXML
    private JFXTextArea txt_remark;

    @FXML
    private Label txt_total_amt;

    @FXML
    private StackPane window;

    private JFXTreeTableColumn<Bill, String> tcMeterNumber;
    private JFXTreeTableColumn<Bill, Long> tcPreviousReading;
    private JFXTreeTableColumn<Bill, Long> tcCurrentReading;
    private JFXTreeTableColumn<Bill, Long> tcUseUnit;
    private JFXTreeTableColumn<Bill, Double> tcOutstanding;
    private JFXTreeTableColumn<Bill, Double> tcInterest;
    private JFXTreeTableColumn<Bill, Double> tcServiceCharge;
    private JFXTreeTableColumn<Bill, Double> tcBillAmount;
    private JFXTreeTableColumn<Bill, Double> tcTotal;

    private double scharge, outrate, outstanding, total, finaltotal, grandtotal;
    private long use_unit;
    private int billno, billref;

    private BillOperation billdb;
    private ObservableList<Meter> meterList;
    private ObservableList<Bill> billList;
    private boolean open = false;
    private ObservableMap<String, Long> meterMap;
    private JFXListView listView;
    private Meter meter;
    private JFXDialog dialog;
    private StringConverter<Long> strConvert;

    @FXML
    void btn_cancel(ActionEvent event) {

    }

    @FXML
    void btn_save(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        meterList = FXCollections.observableArrayList();
        billList = FXCollections.observableArrayList();
        meterMap = FXCollections.observableHashMap();
        cb_period.setItems(PhoenixConfiguration.getMonth());
        initTable();
        txt_duration.setDayCellFactory(param -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    setDisable(empty || item.compareTo(LocalDate.now()) < 0);
                }
            };
        });
        txt_meter_customer.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!txt_meter_customer.getText().isEmpty()) {
                        if (PhoenixSupport.isValidate(cb_period) && !txt_duration.getValue().equals(null)) {
                            CustomerOperation co = new CustomerOperation();
                            meterList = co.getCustomerDetails(txt_meter_customer.getText());
                            System.out.println(meterList);
                            VBox vb = new VBox();
                            vb.setSpacing(16);
                            listView = new JFXListView();
                            ObservableList<Meter> tempList = FXCollections.observableArrayList();
                            for (Meter meter1 : meterList) {
                                meterMap.put(meter1.getMetor_num(), meter1.getCurr_reading());
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
                                });
                                btnCancel.setOnKeyPressed(e -> {
                                    if (e.getCode() == KeyCode.ENTER) {
                                        dialog.close();
                                        open = false;
                                    }
                                });
                                listView.setOnMouseClicked((MouseEvent event) -> {
                                    if (event.getClickCount() == 2) {
                                        billList.clear();
                                        meter = (Meter) listView.getFocusModel().getFocusedItem();
                                        for (String s : meter.getMetor_num().split(" , ")) {
                                            billdb = new BillOperation();
                                            Bill bill = billdb.getMeterInfo(s);
                                            billList.add(bill);
                                        }
                                        dialog.close();
                                        open = false;
                                        refreshTable();
                                    }
                                });
                                listView.setOnKeyPressed((KeyEvent event) -> {
                                    if (event.getCode() == KeyCode.ENTER) {
                                        billList.clear();
                                        meter = (Meter) listView.getFocusModel().getFocusedItem();
                                        for (String s : meter.getMetor_num().split(" , ")) {
                                            billdb = new BillOperation();
                                            Bill bill = billdb.getMeterInfo(s);
                                            billList.add(bill);
                                        }
                                        dialog.close();
                                        open = false;
                                        refreshTable();
                                    }
                                });
                                dialog.show();
                                open = true;
                                dialog.setOnDialogOpened(e -> listView.requestFocus());
                            }
                        } else {
                            PhoenixSupport.Error("कृपया सर्व माहिती भरा.");
                        }
                    }
                }
            }
        });
    }

    @FXML
    void btn_cancel_key(KeyEvent event) {

    }

    @FXML
    void btn_save_key(KeyEvent event) {

    }

    private void initTable() {
        tcMeterNumber = new JFXTreeTableColumn<>("मीटर क्रमांक");
        tcMeterNumber.setCellValueFactory(param -> new SimpleObjectProperty(new Hyperlink(param.getValue().getValue().getMeter().getMetor_num())));
        tcPreviousReading = new JFXTreeTableColumn<>("मागील रिडिंग");
        tcPreviousReading.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getMeter().getCurr_reading()).asObject());
        tcCurrentReading = new JFXTreeTableColumn<>("चालु रिडिंग");
        tcCurrentReading.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getCurunit()).asObject());
        tcCurrentReading.setCellFactory(param -> {
            TextFieldTreeTableCell<Bill, Long> txtCur = new TextFieldTreeTableCell();
            strConvert = new StringConverter<Long>() {

                @Override
                public String toString(Long object) {
                    return String.valueOf(object);
                }

                @Override
                public Long fromString(String string) {
                    return PhoenixSupport.getLong(string);
                }
            };
            txtCur.setConverter(strConvert);
            return txtCur;
        });
        tcCurrentReading.setOnEditCommit((TreeTableColumn.CellEditEvent<Bill, Long> event) -> {
            if (event.getNewValue() > tbl_meter.getTreeItem(event.getTreeTablePosition().getRow()).getValue().getMeter().getCurr_reading()) {
                TreeItem<Bill> tblItem = tbl_meter.getTreeItem(event.getTreeTablePosition().getRow());
                tblItem.getValue().setCurunit(event.getNewValue());
                calculate(tblItem.getValue());
            } else {
                TreeItem<Bill> tblItem = tbl_meter.getTreeItem(event.getTreeTablePosition().getRow());
                tblItem.getValue().setCurunit(0);
                PhoenixSupport.Error("चालू रीडीग ही कमी आहे मागील रीडीग पेक्षा. रीडीग तपासून पहा.");
            }
            tbl_meter.refresh();
//            tbl_meter.getVisibleLeafColumn(8).
        });
        tcUseUnit = new JFXTreeTableColumn<>("वापर युनिट");
        tcUseUnit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getUseunit()).asObject());
        tcOutstanding = new JFXTreeTableColumn<>("थकबाकी");
        tcOutstanding.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getMeter().getOutstanding()).asObject());
        tcInterest = new JFXTreeTableColumn<>("१८% व्याज");
        tcInterest.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getInterested()).asObject());
        tcServiceCharge = new JFXTreeTableColumn<>("सर चार्ज");
        tcServiceCharge.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getScharges()).asObject());
        tcBillAmount = new JFXTreeTableColumn<>("बिलाची रक्क्म");
        tcBillAmount.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getCuramount()).asObject());
        tcTotal = new JFXTreeTableColumn<>("एकूण रक्क्म");
        tcTotal.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal()).asObject());
        tbl_meter.getColumns().addAll(tcMeterNumber, tcPreviousReading, tcCurrentReading, tcUseUnit, tcOutstanding, tcInterest, tcServiceCharge, tcBillAmount, tcTotal);
    }

    private void refreshTable() {
        TreeItem<Bill> treeItem = new RecursiveTreeItem<>(billList, RecursiveTreeObject::getChildren);
        tbl_meter.setRoot(treeItem);
        tbl_meter.setEditable(true);
        tbl_meter.setShowRoot(false);
    }

    private void calculate(Bill bill) {
        if (bill != null) {
            long cur_reading = bill.getCurunit();
            use_unit = cur_reading - bill.getMeter().getCurr_reading();
            ObservableList<Unit> unitlist = new UnitsOperation().getAllUnits();
            double differance = 0;
            total = 0;
            for (Unit u : unitlist) {

                if (use_unit <= u.getMax()) {
                    total = total + ((use_unit - differance) * u.getUnitprice());
                } else {
                    differance = differance + (u.getMax() - u.getMin());
                    total = differance * u.getUnitprice();
                }
            }
            scharge = (total * 0.10);
//            total = total + scharge;
            outstanding = bill.getMeter().getOutstanding();
            if (outstanding > 0) {
                outrate = (outstanding * 0.18) / 6;

            } else {
                outrate = 0;
            }
        }

        if (use_unit < 5) {
            total = Math.round(60);
            scharge = Math.round(5);
        } else {
            total = Math.round(total);
            scharge = Math.round(scharge);
        }
        outrate = Math.round(outrate);
        outstanding = Math.round(outstanding);
        bill.setScharges(scharge);
        bill.setInterested(outrate);
        bill.setCuramount(total);
        finaltotal = (outrate + outstanding + scharge + total);
        finaltotal = Math.round(finaltotal);
        bill.setTotal(finaltotal);
    }
}
