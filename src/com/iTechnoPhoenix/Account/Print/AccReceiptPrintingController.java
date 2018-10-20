package com.iTechnoPhoenix.Account.Print;

import com.iTechnoPhoenix.model.Account;
import com.iTechnoPhoenix.model.AccountReceipt;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

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

    private JFXTreeTableColumn<Account, Integer> tcareceipt_id;
    private JFXTreeTableColumn<Account, Integer> tcaccount_id;
    private JFXTreeTableColumn<Account, String> tcdate;
    private JFXTreeTableColumn<Account, Double> tcpaid_amt;
    private JFXTreeTableColumn<Account, Double> tctotal_amt;
    private JFXTreeTableColumn<Account, Double> tcdelay_amt;
    private JFXTreeTableColumn<Account, Integer> tcaction;
    private JFXTreeTableColumn<Account, String> tccustomer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btn_print_all(ActionEvent event) {

    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {

    }

    @FXML
    private void btn_search(ActionEvent event) {

    }

    @FXML
    private void btn_search_key(KeyEvent event) {

    }

    @FXML
    private void printallList(ActionEvent event) {

    }

    @FXML
    private void printallList_key(KeyEvent event) {

    }

}
