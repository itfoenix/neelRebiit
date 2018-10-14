/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.Account;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author choudhary
 */
public class AccountReceiptController implements Initializable {

    @FXML
    private StackPane window;
    @FXML
    private JFXTextField txt_bill_number;
    @FXML
    private Label txt_customer;
    @FXML
    private Label txt_lastdate;
    @FXML
    private JFXTextField txt_delay_payment;
    @FXML
    private Label lbl_total;
    @FXML
    private JFXTextField txt_paid_amount;
    @FXML
    private Label txt_remaining_amt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btn_cash_paid_key(KeyEvent event) {
    }

    @FXML
    private void btn_cash_paid(ActionEvent event) {
    }

    @FXML
    private void btn_cheque_paid_key(KeyEvent event) {
    }

    @FXML
    private void btn_cheque_paid(ActionEvent event) {
    }

    @FXML
    private void btn_cancel_key(KeyEvent event) {
    }

    @FXML
    private void btn_cancel(ActionEvent event) {
    }

}
