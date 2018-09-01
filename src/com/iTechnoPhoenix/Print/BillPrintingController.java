package com.iTechnoPhoenix.Print;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class BillPrintingController implements Initializable {

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private JFXComboBox<?> cb_duration;

    @FXML
    private JFXTreeTableView<?> tbl_bill;

    @FXML
    void btn_print_all(ActionEvent event) {

    }

    @FXML
    void btn_search(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
