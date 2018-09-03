package com.iTechnoPhoenix.Receipt;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.ReceiptOperation;
import com.iTechnoPhoenix.model.Bill;
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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class ReceiptTransactionController implements Initializable {

    @FXML
    private JFXTextField txt_bill_number;

    @FXML
    private Label txt_customer;

    @FXML
    private Label txt_duration;

    @FXML
    private Label txt_lastdate;

    @FXML
    private JFXTreeTableView<Bill> tbl_meter;

    @FXML
    private Label lbl_preious_paid_amt;

    @FXML
    private Label lbl_total;

    @FXML
    private JFXTextField txt_delay_payment;

    @FXML
    private JFXTextField txt_paid_amount;

    @FXML
    private Label txt_remaining_amt;

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

    private BillOperation billdb;
    private ReceiptOperation recieptdb;
    private ObservableList<Bill> billList;

    @FXML
    void btn_cancel(ActionEvent event) {

    }

    @FXML
    void btn_cash_paid(ActionEvent event) {

    }

    @FXML
    void btn_cheque_paid(ActionEvent event) {
        chequeDialog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billdb = new BillOperation();
        recieptdb = new ReceiptOperation();
        initTable();
        txt_bill_number.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txt_bill_number.getText().isEmpty()) {
                    String s = txt_bill_number.getText();
                    billList = billdb.getBillRef(PhoenixSupport.getInteger(s));
                    int exist = recieptdb.checkBill(PhoenixSupport.getInteger(txt_bill_number.getText()));
                    if (exist == 1) {
                        if (!billList.isEmpty()) {
                            txt_duration.setText(billList.get(0).getPeriod());
                            String str1[] = billList.get(0).getPdate().split(" ");
                            txt_lastdate.setText(str1[0]);
                            txt_customer.setText(billList.get(0).getCust().getName());
                            refreshTable();
                        }
                    }
                }
            }
        });
    }

    private void refreshTable() {
        TreeItem<Bill> treeItem = new RecursiveTreeItem<>(billList, RecursiveTreeObject::getChildren);
        tbl_meter.setRoot(treeItem);
        tbl_meter.setShowRoot(false);
    }

    public void chequeDialog() {

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

        JFXComboBox<String> cb_banklist = new JFXComboBox<>();
        cb_banklist.setMaxWidth(1.7976931348623157E308);
        cb_banklist.setPromptText("बँक नाव");
        VBox.setMargin(cb_banklist, new Insets(8));

        JFXTextField txt_cheque = new JFXTextField();
        txt_cheque.setPromptText("चेक क्रमांक");
        VBox.setMargin(txt_cheque, new Insets(8));

        JFXButton btn_billsave = new JFXButton("जतन करा");
        btn_billsave.setStyle("-fx-background-color: #4BB89B;-fx-text-fill:white;");
        VBox.setMargin(btn_billsave, new Insets(8, 8, 8, 8));

        StackPane.setMargin(billbank, new Insets(8));
        billbank.setPadding(new Insets(8, 16, 8, 16));
        billbank.getChildren().addAll(cb_banklist, txt_cheque, btn_billsave);

        innerStock.getChildren().addAll(newbox, billbank);

        StackPane.setMargin(dialogWindow, new Insets(8));

        outerbox.getChildren().addAll(header, newBank, innerStock);
        dialogWindow.getChildren().add(outerbox);

        JFXDialog dialog = Support.getDialog(window, dialogWindow, JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(false);

        dialog.show();

        btn_bankclose.setOnMouseClicked((e) -> {
            newbox.setVisible(false);
            billbank.setVisible(true);
        });
        newBank.setOnMouseClicked((e) -> {

            newbox.setVisible(true);
            billbank.setVisible(false);

        });

        btn_billsave.setOnMouseClicked((e) -> {
            dialog.close();
        });

    }

    public void initTable() {
        tcMeterNumber = new JFXTreeTableColumn<>("मीटर क्रमांक");
        tcMeterNumber.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getValue().getMeter().getMetor_num()));
        tcPreviousReading = new JFXTreeTableColumn<>("मागील रिडिंग");
        tcPreviousReading.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getMeter().getCurr_reading()).asObject());
        tcCurrentReading = new JFXTreeTableColumn<>("चालु रिडिंग");
        tcCurrentReading.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getCurunit()).asObject());
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

}
