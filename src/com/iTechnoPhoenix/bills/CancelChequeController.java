package com.iTechnoPhoenix.bills;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class CancelChequeController implements Initializable {

    @FXML
    private JFXTextField txt_chequenumber;

    @FXML
    private JFXTreeTableView<?> tbl_cheque_number;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
