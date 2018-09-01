package com.iTechnoPhoenix.bills;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class BillTransactionController implements Initializable {

    @FXML
    private JFXComboBox<?> cb_period;

    @FXML
    private JFXDatePicker txt_duration;

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private Label txt;

    @FXML
    private Label lbl_customer_name;

    @FXML
    private JFXTreeTableView<?> tbl_meter;

    @FXML
    private JFXTextArea txt_remark;

    @FXML
    private Label txt_total_amt;

    @FXML
    void btn_cancel(ActionEvent event) {

    }

    @FXML
    void btn_save(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
