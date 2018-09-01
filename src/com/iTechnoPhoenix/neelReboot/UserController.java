package com.iTechnoPhoenix.neelReboot;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class UserController implements Initializable {

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXComboBox<?> cb_role;

    @FXML
    private JFXTextField txt_username;

    @FXML
    private JFXTextField txt_password;

    @FXML
    private JFXTreeTableView<?> tbl_users;

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
