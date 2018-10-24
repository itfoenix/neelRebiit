/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.MainActivity;

import com.iTechnoPhoenix.database.UserOperation;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NARENDRA JADHAV
 */
public class LoginPageController implements Initializable {

    @FXML
    private Label lbl_softwareName;
    @FXML
    private JFXTextField txt_username;
    @FXML
    private JFXPasswordField txt_password;
    @FXML
    private StackPane window;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbl_softwareName.setText("Neel (Grampanchayat palidevad) Sukapur. Developed by iTechnoPhoenix");
    }

    @FXML
    private void btn_key_login(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!LocalDate.parse(PhoenixConfiguration.getTrialDate()).equals(LocalDate.now())) {
                if (PhoenixSupport.isValidate(txt_username, txt_password)) {
                    UserOperation up = new UserOperation();
                    if (up.login(txt_username.getText(), txt_password.getText()) != null) {
                        showWindow(event);
                    } else {
                        PhoenixSupport.Error("Invalid User", window);
                    }
                } else {
                    PhoenixSupport.Error("Enter Username and Password ", window);
                }
            } else {
                PhoenixSupport.Error("तुमचा १ महिन्याचा ट्रायल वापर संपला आहे, कृपया iTechnoPhoenix ला संपर्क करा.", window);
            }
        }
    }

    @FXML
    private void btn_login(ActionEvent event) {
        if (!LocalDate.parse(PhoenixConfiguration.getTrialDate()).equals(LocalDate.now())) {
            if (PhoenixSupport.isValidate(txt_username, txt_password)) {
                UserOperation up = new UserOperation();
                if (up.login(txt_username.getText(), txt_password.getText()) != null) {
                    showWindow(event);
                } else {
                    PhoenixSupport.Error("Invalid User", window);
                }
            } else {
                PhoenixSupport.Error("Enter Username and Password ", window);
            }
        } else {
            PhoenixSupport.Error("तुमचा १ महिन्याचा ट्रायल वापर संपला आहे, कृपया iTechnoPhoenix ला संपर्क करा.", window);
        }
    }

    @FXML
    private void btn_key_cancel(KeyEvent event
    ) {
        if (event.getCode() == KeyCode.ENTER) {
            clear();
        }
    }

    @FXML
    private void btn_cancel(ActionEvent event
    ) {
        clear();
    }

    private void clear() {
        System.exit(0);
    }

    public void showWindow(ActionEvent event) {
        try {

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
            FXMLLoader fl = new FXMLLoader(getClass().getResource("MainActivity.fxml"));
            Scene ss = new Scene(fl.load());
            Stage stage = new Stage();
            stage.setScene(ss);
            stage.setTitle("Neel (Grampanchayat palidevad) Sukapur. Developed by iTechnoPhoenix");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showWindow(KeyEvent event) {
        try {

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
            FXMLLoader fl = new FXMLLoader(getClass().getResource("MainActivity.fxml"));
            Scene ss = new Scene(fl.load());
            Stage stage = new Stage();
            stage.setTitle("Neel (Grampanchayat palidevad) Sukapur. Developed by iTechnoPhoenix");
            stage.setScene(ss);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
