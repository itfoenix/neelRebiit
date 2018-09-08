package com.iTechnoPhoenix.Report;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author NARENDRA JADHAV
 */
public class CustomerReportController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField txtmeternumber;

    @FXML
    private JFXTreeTableView<Bill> tbl_bills;
    JFXTreeTableColumn<Bill, Integer> tcl_billnumber;
    JFXTreeTableColumn<Bill, String> tcl_period, tcl_remark;
    JFXTreeTableColumn<Bill, Double> tcl_balance, tcl_intreset, tcl_billtotal, tcl_scharges, tcl_finaltotal;
    JFXTreeTableColumn<Bill, Long> tcl_preunit, tcl_current;
    ObservableList<Bill> bill_list;
    ObservableList<Meter> meter_list;
    ObservableList<String> suggestionlist;
    @FXML
    private HBox hb_meterlist;
    private CustomerOperation co;
    @FXML
    private StackPane window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        co = new CustomerOperation();
        suggestionlist = co.getAllCustomerName();
        TextFields.bindAutoCompletion(txtmeternumber, suggestionlist);

        tcl_billnumber = new JFXTreeTableColumn<>("बिल क्रमांक");
        tcl_period = new JFXTreeTableColumn<>("महिना");
        tcl_remark = new JFXTreeTableColumn<>("शेरा");
        tcl_balance = new JFXTreeTableColumn<>("थकबाकी");
        tcl_intreset = new JFXTreeTableColumn<>("१८%");
        tcl_billtotal = new JFXTreeTableColumn<>("बिल रक्कम");
        tcl_scharges = new JFXTreeTableColumn<>("सर चार्ज");
        tcl_finaltotal = new JFXTreeTableColumn<>("एकूण रक्कम");
        tcl_preunit = new JFXTreeTableColumn<>("मागील युनिट");
        tcl_current = new JFXTreeTableColumn<>("चालू युनिट");

        tcl_billnumber.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillref()).asObject());
        tcl_balance.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getBalance()).asObject());
        tcl_intreset.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getInterested()).asObject());
        tcl_billtotal.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getCuramount()).asObject());
        tcl_scharges.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getScharges()).asObject());
        tcl_finaltotal.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getTotal()).asObject());
        tcl_preunit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getPerunit()).asObject());
        tcl_current.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getCurunit()).asObject());
        tbl_bills.getColumns().addAll(tcl_billnumber, tcl_period, tcl_preunit, tcl_current, tcl_balance, tcl_intreset, tcl_scharges, tcl_billtotal,
                tcl_finaltotal, tcl_remark);
    }

    @FXML
    void btn_print(ActionEvent event) {

    }

    @FXML
    private void btn_search_key(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchMeter();
        }

    }

    @FXML
    private void btn_search(ActionEvent event) {
        searchMeter();
    }
    int meters = 0;

    private void searchMeter() {
        meter_list = co.getCustomerDetails(txtmeternumber.getText());
        BillOperation bo = new BillOperation();
        hb_meterlist.getChildren().clear();
        VBox vb = new VBox();
        JFXNodesList nodelist = new JFXNodesList();
        nodelist.addAnimatedNode(new JFXButton(txtmeternumber.getText()));
        vb.setSpacing(16);
        JFXListView listView = new JFXListView();
        ObservableList<Meter> tempList = FXCollections.observableArrayList();
        for (Meter meter1 : meter_list) {
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
        JFXDialog dialog = Support.getDialog(window, new Label("ग्राहक नवडा"), vb, btnCancel);
        btnCancel.setOnAction(e -> {
            dialog.close();
        });
        btnCancel.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                dialog.close();
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Meter meter = (Meter) listView.getFocusModel().getFocusedItem();
                for (String s : meter.getMetor_num().split(",")) {

                    JFXButton meternumber = new JFXButton(s);
                    meter_list.forEach((x) -> {
                        if (x.getMetor_num().equals(s)) {
                            meters = x.getId();
                        }
                    });

                    meternumber.setOnMouseClicked((e) -> {
                        bill_list = bo.getBillHistory(meters);
                        TreeItem<Bill> treeItem = new RecursiveTreeItem<>(bill_list, RecursiveTreeObject::getChildren);
                        tbl_bills.setRoot(treeItem);
                        tbl_bills.setShowRoot(false);
                        tbl_bills.refresh();
                    });
                    nodelist.addAnimatedNode(meternumber);
                }
                dialog.close();
                nodelist.setSpacing(30);
                nodelist.setRotate(270);
                hb_meterlist.getChildren().add(nodelist);
                txtmeternumber.clear();
            }
        });
        dialog.show();

    }

}
