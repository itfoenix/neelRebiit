package com.iTechnoPhoenix.bills;

import com.iTechnoPhoenix.database.BillOperation;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

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
    private JFXTreeTableView<?> tbl_meter;

    @FXML
    private JFXTextArea txt_remark;

    @FXML
    private Label txt_total_amt;

    @FXML
    private StackPane window;

    private BillOperation billdb;

    @FXML
    void btn_cancel(ActionEvent event) {

    }

    @FXML
    void btn_save(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cb_period.setItems(PhoenixConfiguration.getMonth());
        txt_duration.setDayCellFactory(param -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    setDisable(empty || item.compareTo(LocalDate.now()) < 0);
                }
            };
        });
        txt_meter_customer.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                b
            }
        });
    }

    @FXML
    void btn_cancel_key(KeyEvent event) {

    }

    @FXML
    void btn_save_key(KeyEvent event) {

    }

}
