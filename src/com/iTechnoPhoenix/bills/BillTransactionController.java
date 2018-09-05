package com.iTechnoPhoenix.bills;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.database.MeterOperation;
import com.iTechnoPhoenix.database.UnitsOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Customer;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.model.MeterBill;
import com.iTechnoPhoenix.model.Unit;
import com.iTechnoPhoenix.neelSupport.BillSupport;
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
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

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
    private JFXListView listView;
    private Meter meter;
    private JFXDialog dialog;
    private StringConverter<Long> strConvert;
    private ObservableSet<String> customermeterlist;

    @FXML
    private void btn_cancel(ActionEvent event) {
        cancel();
    }

    @FXML
    private void btn_save(ActionEvent event) {
        ArrayList<Boolean> valid = new ArrayList<>();
        for (Bill b : billList) {
            if (b.getTotal() == 0) {
                valid.add(false);
            }
        }
        if (valid.isEmpty()) {
            save();
            valid.clear();
        } else {
            PhoenixSupport.Error("कृपया चालु रिडिंग टाका.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        meterList = FXCollections.observableArrayList();
        billList = FXCollections.observableArrayList();
        customermeterlist = FXCollections.observableSet();
        CustomerOperation custdb = new CustomerOperation();
        for (Customer cust : custdb.getCustomerName()) {
            customermeterlist.add(cust.getName());
        }
        MeterOperation meterdb = new MeterOperation();
        customermeterlist.addAll(meterdb.getMeterNumber());
        cb_period.setItems(PhoenixConfiguration.getMonth());
        TextFields.bindAutoCompletion(txt_meter_customer, customermeterlist);
        initTable();
        txt_duration.setDayCellFactory(param -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    setDisable(empty || item.compareTo(LocalDate.now()) < 0);
                }
            };
        });
        txt_meter_customer.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txt_meter_customer.getText().isEmpty()) {
                    if (PhoenixSupport.isValidate(cb_period) && txt_duration.getValue() != null) {
                        CustomerOperation co = new CustomerOperation();
                        meterList = co.getCustomerDetails(txt_meter_customer.getText());
                        System.out.println(meterList);
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
                                txt_meter_customer.clear();
                            });
                            btnCancel.setOnKeyPressed(e -> {
                                if (e.getCode() == KeyCode.ENTER) {
                                    dialog.close();
                                    open = false;
                                    txt_meter_customer.clear();
                                }
                            });
                            listView.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    billList.clear();
                                    meter = (Meter) listView.getFocusModel().getFocusedItem();
                                    for (String s : meter.getMetor_num().split(" , ")) {
                                        billdb = new BillOperation();
                                        Bill bill = billdb.getMeterInfo(s);
                                        lbl_customer_name.setText(bill.getCust().getName());
                                        billList.add(bill);
                                    }
                                    dialog.close();
                                    open = false;
                                    refreshTable();
                                }
                            });
                            listView.setOnKeyPressed(event -> {
                                if (event.getCode() == KeyCode.ENTER) {
                                    billList.clear();
                                    meter = (Meter) listView.getFocusModel().getFocusedItem();
                                    for (String s : meter.getMetor_num().split(" , ")) {
                                        billdb = new BillOperation();
                                        Bill bill = billdb.getMeterInfo(s);
                                        billList.add(bill);
                                        lbl_customer_name.setText(bill.getCust().getName());
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
        });
    }

    @FXML
    private void btn_cancel_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            cancel();
        }
    }

    @FXML
    private void btn_save_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ArrayList<Boolean> valid = new ArrayList<>();
            for (Bill b : billList) {
                if (b.getTotal() == 0) {
                    valid.add(false);
                }
            }
            if (valid.isEmpty()) {
                save();
                valid.clear();
            } else {
                PhoenixSupport.Error("कृपया चालु रिडिंग टाका.");
            }
        }
    }

    private void initTable() {
        tcMeterNumber = new JFXTreeTableColumn<>("मीटर क्रमांक");
        tcMeterNumber.setCellValueFactory(param -> {
            int meternum = param.getValue().getValue().getMeter().getId();
            String mnum = param.getValue().getValue().getMeter().getMetor_num();
            Hyperlink meterlink = new Hyperlink(param.getValue().getValue().getMeter().getMetor_num());
            meterlink.setOnAction((e) -> {
                viewHistory(meternum, mnum);
            });
            return new SimpleObjectProperty(meterlink);
        });
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
        tcCurrentReading.setOnEditCommit(event -> {
            if (event.getNewValue() > tbl_meter.getTreeItem(event.getTreeTablePosition().getRow()).getValue().getMeter().getCurr_reading()) {
                TreeItem<Bill> tblItem = tbl_meter.getTreeItem(event.getTreeTablePosition().getRow());
                tblItem.getValue().setCurunit(event.getNewValue());
                calculate(tblItem.getValue());
                calculateGrand();
            } else {
                TreeItem<Bill> tblItem = tbl_meter.getTreeItem(event.getTreeTablePosition().getRow());
                tblItem.getValue().setCurunit(0);
                tblItem.getValue().setInterested(0);
                tblItem.getValue().setScharges(0);
                tblItem.getValue().setTotal(0);
                tblItem.getValue().setCuramount(0);
                tblItem.getValue().setUseunit(0);
                calculateGrand();
                PhoenixSupport.Error("चालू रीडीग ही कमी आहे मागील रीडीग पेक्षा. रीडीग तपासून पहा.");
            }
            tbl_meter.refresh();
        });
        tcUseUnit = new JFXTreeTableColumn<>("वापर युनिट");
        tcUseUnit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getUseunit()).asObject());
        tcUseUnit.setCellFactory(param -> {
            TextFieldTreeTableCell<Bill, Long> txtUse = new TextFieldTreeTableCell();
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
            txtUse.setConverter(strConvert);
            return txtUse;
        });
        tcUseUnit.setOnEditCommit(event -> {
            if (event.getNewValue() > 0) {
                TreeItem<Bill> treeItem = tbl_meter.getTreeItem(event.getTreeTablePosition().getRow());
                treeItem.getValue().setUseunit(event.getNewValue());
                treeItem.getValue().setCurunit(treeItem.getValue().getMeter().getCurr_reading() + event.getNewValue());
                calculate(treeItem.getValue());
                calculateGrand();
                treeItem.getValue().setCurunit(treeItem.getValue().getMeter().getCurr_reading());
            } else {
                TreeItem<Bill> tblItem = tbl_meter.getTreeItem(event.getTreeTablePosition().getRow());
                tblItem.getValue().setCurunit(0);
                tblItem.getValue().setInterested(0);
                tblItem.getValue().setScharges(0);
                tblItem.getValue().setTotal(0);
                tblItem.getValue().setCuramount(0);
                tblItem.getValue().setUseunit(0);
                calculateGrand();
                PhoenixSupport.Error("वापर युनिट ही खूप कमी आहे.");
            }
            tbl_meter.refresh();
        });
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
    private JFXTreeTableView<Bill> tbl_meter_history;

    public void viewHistory(int meternum, String mnum) {

        try {
            StackPane viewBox = FXMLLoader.load(getClass().getResource("MetorHistory.fxml"));
            VBox mainbox = (VBox) viewBox.getChildren().get(0);
            mainbox.getChildren().forEach((e) -> {
                if (e.getAccessibleText() != null) {
                    if (e.getAccessibleText().equals("lbl_meter_number")) {
                        ((Label) e).setText("मीटर क्रमांक : " + mnum);
                    }
                    if (e.getAccessibleText().equals("tbl_meter_history")) {
                        tbl_meter_history = ((JFXTreeTableView) e);
                        createHistory(meternum);
                    }
                }

            });
            Support.getDialog(window, viewBox, JFXDialog.DialogTransition.TOP).show();

        } catch (IOException ex) {
            Logger.getLogger(BillTransactionController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void createHistory(int meternum) {
        BillOperation bo = new BillOperation();
        JFXTreeTableColumn<Bill, Integer> tclbillno = new JFXTreeTableColumn<>("बिल क्रमांक");
        JFXTreeTableColumn<Bill, Long> tclcurrentreading = new JFXTreeTableColumn<>("चालू रीडिंग");
        JFXTreeTableColumn<Bill, String> tclperiod = new JFXTreeTableColumn<>("कालावधी");
        JFXTreeTableColumn<Bill, Long> tclprereading = new JFXTreeTableColumn<>("मागील रीडिंग");
        JFXTreeTableColumn<Bill, String> tclstatus = new JFXTreeTableColumn<>("स्थिती");
        JFXTreeTableColumn<Bill, Double> tcltotal = new JFXTreeTableColumn<>("एकूण रक्कम");

        tclbillno.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue().getBillno()).asObject());
        tclcurrentreading.setCellValueFactory(p -> new SimpleLongProperty(p.getValue().getValue().getCurunit()).asObject());
        tclperiod.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getPeriod()));
        tclperiod.getStyleClass().add("label-marathi");
        tclprereading.setCellValueFactory(p -> new SimpleLongProperty(p.getValue().getValue().getCurunit()).asObject());
        tclstatus.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().getStatus()));
        tcltotal.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().getValue().getCurunit()).asObject());
        ObservableList<Bill> bills = bo.getBillHistory(meternum);
        final TreeItem<Bill> root = new RecursiveTreeItem<>(bills, RecursiveTreeObject::getChildren);
        tbl_meter_history.getColumns().addAll(tclbillno, tclperiod, tclprereading, tclcurrentreading, tcltotal, tclstatus);
        tbl_meter_history.setRoot(root);
        tbl_meter_history.setShowRoot(false);
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
            outstanding = bill.getMeter().getOutstanding();
            if (outstanding > 0) {
                outrate = (outstanding * 0.18) / 6;
            } else {
                outrate = 0;
            }
        }

        if (use_unit <= 5) {
            total = Math.round(60);
            scharge = Math.round(5);
        } else {
            total = Math.round(total);
            scharge = Math.round(scharge);
        }
        outrate = Math.round(outrate);
        outstanding = Math.round(outstanding);
        bill.setPerunit(bill.getMeter().getCurr_reading());
        bill.setUseunit(Math.round(use_unit));
        bill.setScharges(scharge);
        bill.setInterested(outrate);
        bill.setCuramount(total);
        finaltotal = (outrate + outstanding + scharge + total);
        finaltotal = Math.round(finaltotal);
        bill.setTotal(finaltotal);
    }

    public void save() {
        if (PhoenixSupport.isValidate(cb_period) && txt_duration.getValue() != null && PhoenixSupport.isValidate(txt_meter_customer)) {
            if (!billList.isEmpty()) {
                for (Bill bill : billList) {
                    int i = billdb.checkBill(bill.getMeter().getMetor_num(), cb_period.getSelectionModel().getSelectedItem(), String.valueOf(LocalDate.now().getYear()));
                    bill.setRemark(txt_remark.getText());
                    if (i == 0) {
                        bill.setBdate(LocalDate.now().toString());
                        bill.setPdate(txt_duration.getValue().toString());
                        bill.setPeriod(cb_period.getSelectionModel().getSelectedItem());
                        bill.setYear(String.valueOf(LocalDate.now().getYear()));
                        if (billref != 0) {
                            bill.setBillref(billref);
                        } else {
                            bill.setBillref(0);
                        }
                        bill.setBalance(bill.getMeter().getOutstanding());
                        bill.setUid(PhoenixSupport.uid);
                        billno = billdb.saveBill(bill);
                        bill.setBillno(billno);
                        billref = billdb.getref(billno);
                        bill.setBillref(billref);
                        billno = 0;
                    } else {
                        PhoenixSupport.Error("मीटर क्रमांक " + bill.getMeter().getMetor_num() + ", ह्या महिन्याचे बिल आधीच बनवल आहे.");
                    }
                }
                if (billno == 0) {
                    BillSupport billSupport = new BillSupport();
                    JFXButton btnDPrint = new JFXButton("प्रिंट करा");
                    btnDPrint.getStyleClass().add("btn-search");
                    btnDPrint.setOnAction(e -> {
                        ArrayList<MeterBill> meterBillList = billSupport.assignBillValue(billList);
                        PhoenixSupport.printMeterBill(meterBillList);
                        cancel();
                        dialog.close();
                    });
                    btnDPrint.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ENTER) {
                            ArrayList<MeterBill> meterBillList = billSupport.assignBillValue(billList);
                            PhoenixSupport.printMeterBill(meterBillList);
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
                    dialog = Support.getDialog(window, new Label("बिल व्यवहार"), new Label("ह्या महिन्याचे बिल जतन झाले आहे."), btnDPrint, btnDCancel);
                    dialog.show();
                    dialog.setOnDialogOpened(e -> {
                        btnDCancel.requestFocus();
                    });
                }
            } else {
                PhoenixSupport.Error("कृपया ग्राहक किवा मीटरची माहिती टाका.");
            }
        } else {
            PhoenixSupport.Error("कृपया सर्व माहिती भरा.");
        }
    }

    public void cancel() {
        cb_period.getSelectionModel().clearSelection();
        txt_duration.setValue(null);
        txt_meter_customer.clear();
        txt_remark.clear();
        txt_total_amt.setText("००");
        lbl_customer_name.setText("");
        billList.clear();
        refreshTable();
        billno = 0;
        billref = 0;
        finaltotal = 0;
        grandtotal = 0;
        outrate = 0;
        outstanding = 0;
        scharge = 0;
        total = 0;
        use_unit = 0;
    }

    public void calculateGrand() {
        for (Bill b : billList) {
            grandtotal = grandtotal + b.getTotal();
        }
        txt_total_amt.setText(String.valueOf(grandtotal));
        grandtotal = 0;
    }
}
