/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Edit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.UnitsOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.MeterBill;
import com.iTechnoPhoenix.model.Unit;
import com.iTechnoPhoenix.neelSupport.BillSupport;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author choudhary
 */
public class Bill_EditingController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private StackPane window;

    @FXML
    private JFXTextField txt_BillNumber;

    @FXML
    private Label lbl_BillNo;

    @FXML
    private Label lbl_BillDate;

    @FXML
    private Label lbl_CustomerName;

    @FXML
    private Label lbl_CustomerAddress;

    @FXML
    private Label lbl_BillDuration;

    @FXML
    private Label lbl_BillLastDate;

    @FXML
    private JFXTreeTableView<Bill> tblMeters;

    @FXML
    private Label lbl_Remark;

    @FXML
    private Label lbl_FinalTotal;

    private ObservableList<Bill> billList;
    private JFXTreeTableColumn<Bill, String> tcMeterNumber;
    private JFXTreeTableColumn<Bill, Double> tcBalance;
    private JFXTreeTableColumn<Bill, Double> tcInterest;
    private JFXTreeTableColumn<Bill, Double> tcSCharge;
    private JFXTreeTableColumn<Bill, Double> tcCurrentAmt;
    private JFXTreeTableColumn<Bill, Double> tcTotalAmt;
    private JFXTreeTableColumn<Bill, Long> tcPreviousUnit;
    private JFXTreeTableColumn<Bill, Long> tcCurrentUnit;
    private JFXTreeTableColumn<Bill, Long> tcUseUnit;
    private JFXTreeTableColumn<Bill, HBox> tcAction;
    @FXML
    private VBox vbBill;
    private long oldReading = 0;
    private long use_unit;
    private double total, outstanding, scharge, outrate, ftotal, oldTotal, grandtotal;
    @FXML
    private JFXComboBox<String> cb_period;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cb_period.setItems(PhoenixConfiguration.getMonth());
        cb_period.getStyleClass().add("label-marathi");
        billList = FXCollections.observableArrayList();
        tcMeterNumber = new JFXTreeTableColumn<>("मीटर नं");
        tcBalance = new JFXTreeTableColumn<>("थकबाकी");
        tcInterest = new JFXTreeTableColumn<>("१८% व्याज");
        tcCurrentAmt = new JFXTreeTableColumn<>("रक्क्म");
        tcSCharge = new JFXTreeTableColumn<>("सरचार्ज");
        tcTotalAmt = new JFXTreeTableColumn<>("एकूण बिल");
        tcPreviousUnit = new JFXTreeTableColumn<>("मागील युनिट");
        tcCurrentUnit = new JFXTreeTableColumn<>("चालु युनिय");
        tcUseUnit = new JFXTreeTableColumn<>("वापर युनिट");
        tcAction = new JFXTreeTableColumn<>();
        initializeTable();
        tblMeters.getColumns().setAll(tcMeterNumber, tcBalance, tcInterest, tcCurrentAmt, tcSCharge, tcTotalAmt, tcPreviousUnit, tcCurrentUnit, tcUseUnit, tcAction);

    }

    @FXML
    private void cancel_Action(ActionEvent event) {
        cancel();
    }

    @FXML
    private void cancel_Key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            cancel();
        }
    }

    @FXML
    private void save_Action(ActionEvent event) {
        save();
    }

    @FXML
    private void save_Key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            save();
        }
    }

    @FXML
    private void search_Action(ActionEvent event) {
        search();
    }

    @FXML
    private void search_Key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            search();
        }
    }

    public void initializeTable() {
        tcMeterNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMeter().getMetor_num()));
        tcBalance.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getBalance()).asObject());
        tcInterest.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getInterested()).asObject());
        tcCurrentAmt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getCuramount()).asObject());
        tcSCharge.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getScharges()).asObject());
        tcTotalAmt.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal()).asObject());
        tcPreviousUnit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getPerunit()).asObject());
        tcCurrentUnit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getCurunit()).asObject());
        tcUseUnit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getUseunit()).asObject());
        tcAction.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue().getActionBox()));
    }

    public void search() {
        JFXSnackbar snack = new JFXSnackbar(window);
        snack.enqueue(new JFXSnackbar.SnackbarEvent("Loading Loads of Data"));
grandtotal =0;
        BillOperation bo = new BillOperation();
        billList = bo.getAllBill(PhoenixSupport.getInteger(txt_BillNumber.getText()));

        if (!billList.isEmpty()) {
            if (billList.get(0).getStatus().equals("1")) {
                lbl_BillNo.setText(txt_BillNumber.getText());
                cb_period.getSelectionModel().select(billList.get(0).getPeriod());
                lbl_BillLastDate.setText(billList.get(0).getPdate().split(" ")[0]);
                lbl_BillDate.setText(billList.get(0).getBdate().split(" ")[0]);
                lbl_CustomerName.setText(billList.get(0).getCustomername());
                lbl_CustomerAddress.setText(billList.get(0).getAddress());
                lbl_Remark.setText(billList.get(0).getRemark());
                for (Bill b : billList) {
                    grandtotal = grandtotal + b.getTotal();
                    HBox hb = new HBox();
                    JFXButton btnEdit = new JFXButton("Edit");
                    btnEdit.getStyleClass().add("btn-search");
                    btnEdit.setFocusTraversable(false);
                    btnEdit.setOnAction((ActionEvent event) -> {
                        tblMeters.getSelectionModel().select(tblMeters.getSelectionModel().getSelectedIndex());
                        JFXDialogLayout editDialog = new JFXDialogLayout();
                        JFXDialog dialog = new JFXDialog(window, editDialog, JFXDialog.DialogTransition.CENTER);
                        editDialog.setHeading(new Label("मीटर युनिट बदलने"));
                        VBox vb = new VBox();
                        vb.setAlignment(Pos.CENTER);
                        vb.setSpacing(16);
                        JFXTextField newUnit = new JFXTextField();
                        newUnit.setPromptText("चालु युनिट");
                        newUnit.setLabelFloat(true);
                        newUnit.setMaxWidth(200);
                        HBox hboxpop = new HBox();
                        hboxpop.setAlignment(Pos.CENTER);
                        hboxpop.setSpacing(16);
                        hboxpop.getChildren().addAll(new Label("मीटर नं : " + b.getMeter().getMetor_num()), new Label("मागील युनिट : " + b.getPerunit()));
                        vb.getChildren().addAll(hboxpop, newUnit);
                        editDialog.setBody(vb);
                        JFXButton btnSave = new JFXButton("Save");
                        btnSave.getStyleClass().add("btn-search");
                        btnSave.setOnAction((ActionEvent event1) -> {
                            long cReading = PhoenixSupport.getInteger(newUnit.getText());
                            if (b.getPerunit() <= cReading) {
                                oldTotal = b.getTotal();
                                oldReading = b.getCurunit();
                                b.setCurunit(cReading);
                                calculate(b);
                                tblMeters.refresh();
                                dialog.close();
                                lbl_FinalTotal.setText(String.valueOf((PhoenixSupport.getDouble(lbl_FinalTotal.getText()) - oldTotal) + ftotal));
                            }
                        });
                        editDialog.setActions(btnSave);
                        dialog.show();
                    });
                    hb.getChildren().add(btnEdit);
                    hb.setAlignment(Pos.CENTER);
                    b.setActionBox(hb);
                }
                lbl_FinalTotal.setText(String.valueOf(grandtotal));
                lbl_FinalTotal.getStyleClass().add("label-bigger");
                TreeItem<Bill> treeItm = new RecursiveTreeItem<>(billList, RecursiveTreeObject::getChildren);
                tblMeters.setRoot(treeItm);
                tblMeters.setShowRoot(false);
                vbBill.setVisible(true);

            } else {
                switch (billList.get(0).getStatus()) {
                    case "2":
                        PhoenixSupport.Error("पुढच्या महिन्याच बिल बनवले आहे.", window);
                        break;
                    case "3":
                        PhoenixSupport.Error("ह्या महिन्याच बिल आधीच भरले आहे.", window);
                        break;
                }
            }
        } else {
            PhoenixSupport.Error("बिल क्रमांक चुकीचा आहे.", window);
        }
    }

    private void calculate(Bill bill) {
        if (bill != null) {
            long cur_reading = bill.getCurunit();
            use_unit = cur_reading - bill.getPerunit();
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
            outstanding = bill.getBalance();
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
        bill.setUseunit(use_unit);
        bill.setInterested(outrate);
        bill.setCuramount(total);
        ftotal = (outrate + outstanding + scharge + total);
        ftotal = Math.round(ftotal);
        bill.setTotal(ftotal);
        bill.setPaidamt(ftotal);
    }

    public void cancel() {
        lbl_BillDate.setText("");
        lbl_BillDuration.setText("");
        lbl_BillLastDate.setText("");
        lbl_BillNo.setText("");
        lbl_CustomerAddress.setText("");
        lbl_CustomerName.setText("");
        lbl_FinalTotal.setText("");
        lbl_Remark.setText("");
        txt_BillNumber.clear();
        outrate = 0;
        ftotal = 0;
        grandtotal = 0;
        oldReading = 0;
        oldTotal = 0;
        outrate = 0;
        outstanding = 0;
        scharge = 0;
        total = 0;
        use_unit = 0;
        billList.clear();
        tblMeters.refresh();
        tblMeters.setRoot(null);
    }

    public void save() {
        if (!billList.isEmpty()) {
            for (Bill b : billList) {
                b.setPeriod(cb_period.getSelectionModel().getSelectedItem());
                BillOperation bo = new BillOperation();
                bo.updateBill(b);
            }
            JFXDialog dialog;
            JFXButton btnPrint = new JFXButton("प्रिंट करा");
            btnPrint.getStyleClass().add("btn-search");

            JFXButton btnCancel = new JFXButton("रद्द करा");
            btnCancel.getStyleClass().add("btn-cancel");

            dialog = Support.getDialog(window, new Label("बिल व्यवहार"), new Label("बिल जतन झालं आहे."), btnPrint, btnCancel);

            BillSupport billsupport = new BillSupport();
            btnPrint.setOnAction((e) -> {
                ArrayList<MeterBill> meterBillList = billsupport.assignBillValue(billList);
                PhoenixSupport.printMeterBill(meterBillList);
                dialog.close();
                cancel();
                vbBill.setVisible(false);
            });
            btnPrint.setOnKeyPressed((e) -> {
                if (e.getCode() == KeyCode.ENTER) {
                    ArrayList<MeterBill> meterBillList = billsupport.assignBillValue(billList);
                    PhoenixSupport.printMeterBill(meterBillList);
                    dialog.close();
                    cancel();
                    vbBill.setVisible(false);
                }
            });
            btnCancel.setOnAction(e -> {
                dialog.close();
                cancel();
                vbBill.setVisible(false);
            });
            btnCancel.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    dialog.close();
                    cancel();
                    vbBill.setVisible(false);
                }
            });

            dialog.show();
            dialog.setOnDialogOpened(e -> btnCancel.requestFocus());
        } else {
            PhoenixSupport.Error("कृपया बिल क्रमांक शोधा.", window);
        }
    }

    @FXML
    private void adjust_Key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            adjustment();
        }
    }

    @FXML
    private void adjust_Action(ActionEvent event) {
        adjustment();
    }

    private void adjustment() {
        JFXButton shut = new JFXButton("रद्ध करा");
        JFXButton open = new JFXButton("सेट");
        open.getStyleClass().add("btn-search");
        shut.getStyleClass().add("btn-cancel");
        Label lblOld = new Label("जुनी रक्कम : " + lbl_FinalTotal.getText());
        JFXTextField txtAdjust = new JFXTextField();
        txtAdjust.setLabelFloat(true);
        txtAdjust.setPromptText("एडजस्टमेंट रक्कम");
        Label lblNew = new Label("नवीन रक्कम : " + String.valueOf(PhoenixSupport.getDouble(lbl_FinalTotal.getText()) - PhoenixSupport.getDouble(txtAdjust.getText())));
        txtAdjust.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtAdjust.getText().isEmpty()) {
                    lblNew.setText("नवीन रक्कम : " + String.valueOf(PhoenixSupport.getDouble(lbl_FinalTotal.getText()) - PhoenixSupport.getDouble(txtAdjust.getText())));
                } else {
                    txtAdjust.setText("0");
                    lblNew.setText("नवीन रक्कम : " + String.valueOf(PhoenixSupport.getDouble(lbl_FinalTotal.getText()) - PhoenixSupport.getDouble(txtAdjust.getText())));
                }
            }
        });
        VBox vb = new VBox();
        vb.setSpacing(8);
        vb.getChildren().addAll(lblOld, txtAdjust, lblNew);
        JFXDialog dialog = Support.getDialog(window, new Label("एडजस्टमेंट करणे"), vb, open, shut);
        open.setOnAction(e -> {
            double adjust = PhoenixSupport.getDouble(txtAdjust.getText());
            for (Bill b : billList) {
                if (b.getBalance()>= adjust) {
                    b.setAdjustment(adjust);
                    b.setBalance(b.getBalance() - adjust);
                    b.setTotal(b.getBalance()+ b.getScharges() +b.getCuramount()+ b.getInterested());
                    b.setPaidamt(b.getTotal());
                    adjust = 0;
                } else if (b.getBalance() < adjust) {
                    if(b.getBalance() != 0) {
                    adjust = adjust - b.getBalance();
                    b.setAdjustment(b.getBalance());
                    b.setBalance(0);
                    b.setTotal(b.getBalance()+ b.getScharges() +b.getCuramount()+ b.getInterested());
                    b.setPaidamt(b.getTotal());
                    }
                    else {
                        PhoenixSupport.Error("थकबाकी हि adjustment रक्कम पेक्षा कमी आहे.", window);
                    }
                }
            }
            TreeItem<Bill> treeItm = new RecursiveTreeItem<>(billList, RecursiveTreeObject::getChildren);
            tblMeters.setRoot(treeItm);
            tblMeters.setShowRoot(false);
            dialog.close();
            lbl_FinalTotal.setText(String.valueOf(PhoenixSupport.getDouble(lbl_FinalTotal.getText()) - PhoenixSupport.getDouble(txtAdjust.getText())));
            tblMeters.requestFocus();
        });
        open.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double adjust = PhoenixSupport.getDouble(txtAdjust.getText());
                for (Bill b : billList) {
                    if (b.getBalance()>= adjust) {
                    b.setAdjustment(adjust);
                    b.setBalance(b.getBalance() - adjust);
                    b.setTotal(b.getBalance()+ b.getScharges() +b.getCuramount()+ b.getInterested());
                    b.setPaidamt(b.getTotal());
                    adjust = 0;
                } else if (b.getBalance() < adjust) {
                    adjust = adjust - b.getPaidamt();
                    b.setAdjustment(b.getPaidamt());
                    b.setBalance(0);
                    b.setTotal(b.getBalance()+ b.getScharges() +b.getCuramount()+ b.getInterested());
                    b.setPaidamt(b.getTotal());
                }
                }
                initializeTable();
                dialog.close();
                lbl_FinalTotal.setText(String.valueOf(PhoenixSupport.getDouble(lbl_FinalTotal.getText()) - PhoenixSupport.getDouble(txtAdjust.getText())));
                tblMeters.requestFocus();
            }
        });
        shut.setOnAction(e -> {
            dialog.close();
        });
        shut.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                dialog.close();
            }
        });
        dialog.show();
        dialog.setOverlayClose(false);
    }
}
