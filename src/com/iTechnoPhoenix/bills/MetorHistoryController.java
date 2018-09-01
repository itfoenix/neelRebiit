/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.bills;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author NARENDRA JADHAV
 */
public class MetorHistoryController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lbl_meter_number;

    @FXML
    private JFXTreeTableView<?> tbl_meter_history;

    @FXML
    void btn_close(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
