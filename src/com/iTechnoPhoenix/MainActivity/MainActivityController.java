/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.MainActivity;

import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author NARENDRA JADHAV
 */
public class MainActivityController implements Initializable {

    @FXML
    private StackPane window;
    @FXML
    private BorderPane mainWindow;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btn_customer(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/neelReboot/CustomerDetails.fxml");
    }

    @FXML
    private void btn_unit(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/neelReboot/Units.fxml");
    }

    @FXML
    private void btn_user(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/neelReboot/User.fxml");
    }

    @FXML
    private void btn_bill(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/bills/BillTransaction.fxml");
    }

    @FXML
    private void btn_bill_change(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Edit/Bill_Editing.fxml");
    }

    @FXML
    private void btn_receipt(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Receipt/ReceiptTransaction.fxml");
    }

    @FXML
    private void btn_cheque_cancel(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/bills/CancelCheque.fxml");
    }

    @FXML
    private void btn_bill_printl(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Print/BillPrinting.fxml");
    }

    @FXML
    private void btn_receipt_print(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Print/ReceiptPrinting.fxml");
    }

    @FXML
    private void btn_bill_report(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Report/BillReport.fxml");
    }

    @FXML
    private void btn_customer_report(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Report/CustomerReport.fxml");
    }

    @FXML
    private void btn_home_page(ActionEvent event) {
        CenterPanel("HomePage.fxml");
    }

    @FXML
    private void btn_account_bill(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Account/AccountingBill.fxml");
    }

    @FXML
    private void btn_account_receipt(ActionEvent event) {
        CenterPanel("/com/iTechnoPhoenix/Account/AccountReceipt.fxml");
    }

    private void CenterPanel(String name) {
        try {
            URL newclient = getClass().getResource(name);
            Parent p = FXMLLoader.load(newclient);
            mainWindow.setCenter(p);
        } catch (IOException ex) {
            PhoenixConfiguration.loggedRecored("Main Activity Controller", ex.getMessage());
            PhoenixSupport.Error("Please Close application and start again", window);
        }
    }

}
