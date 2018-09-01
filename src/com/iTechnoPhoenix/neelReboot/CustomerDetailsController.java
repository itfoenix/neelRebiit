/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.neelReboot;

import com.iTechnoPhoenix.database.CustomerOperation;
import com.iTechnoPhoenix.model.Meter;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author NARENDRA JADHAV
 */
public class CustomerDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txt_search;

    @FXML
    private JFXTreeTableView<Meter> txt_customer;
    private JFXTreeTableColumn<Meter, String> tc_meter, tc_connnection, tc_name, tc_mobile, tc_email, tc_address;
    private JFXTreeTableColumn<Meter, Long> tc_currentReading;
    private JFXTreeTableColumn<Meter, Double> tc_outstanding, tc_deposit;
    private JFXTreeTableColumn<Meter, Integer> tc_action;
    @FXML
    private StackPane window;

    private ObservableList<Meter> meterlist, allmeter;
    private ObservableSet<String> suggestionlist;

    @FXML
    void btn_search(ActionEvent event) {
        String search = txt_search.getText();
        if (search.isEmpty()) {
            meterlist.clear();
            meterlist.addAll(allmeter);
        } else {
            ObservableList<Meter> copylist = FXCollections.observableArrayList();
            copylist.addAll(meterlist);
            meterlist.clear();
            copylist.forEach((m) -> {
                if (m.getMetor_num().equals(search) || m.getCustomeObject().getName().equals(search)) {
                    meterlist.add(m);
                }
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        suggestionlist = FXCollections.observableSet();
        allmeter = FXCollections.observableArrayList();
        tc_name = new JFXTreeTableColumn<>("नाव");
        tc_email = new JFXTreeTableColumn<>("ईमेल");
        tc_mobile = new JFXTreeTableColumn<>("मोबाई नं");
        tc_address = new JFXTreeTableColumn<>("पत्ता");
        tc_meter = new JFXTreeTableColumn<>("मीटर क्रमांक");
        tc_connnection = new JFXTreeTableColumn<>("दिनांक");
        tc_currentReading = new JFXTreeTableColumn<>("चालू रीडीग");
        tc_outstanding = new JFXTreeTableColumn<>("थकबाकी");
        tc_deposit = new JFXTreeTableColumn<>("जमा");
        tc_action = new JFXTreeTableColumn();

        tc_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomeObject().getName()));
        tc_email.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomeObject().getEmail()));
        tc_mobile.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomeObject().getPhone()));
        tc_address.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCustomeObject().getAddress()));
        tc_meter.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMetor_num()));
        tc_connnection.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getCon_date()));
        tc_currentReading.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getCurr_reading()).asObject());
        tc_outstanding.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getOutstanding()).asObject());
        tc_deposit.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getDeposit()).asObject());
        tc_action.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject());

        CustomerOperation co = new CustomerOperation();
        meterlist = co.getCustomerByName();
        allmeter.addAll(meterlist);
        if (!meterlist.isEmpty()) {
            meterlist.forEach((e) -> {
                suggestionlist.add(e.getCustomeObject().getName());
                suggestionlist.add(e.getMetor_num());
            });
        }
        TextFields.bindAutoCompletion(txt_search, suggestionlist);
        final TreeItem<Meter> root = new RecursiveTreeItem<>(meterlist, RecursiveTreeObject::getChildren);
        txt_customer.getColumns().addAll(tc_action, tc_name, tc_address, tc_meter, tc_connnection, tc_currentReading, tc_outstanding, tc_deposit, tc_mobile, tc_email);
        txt_customer.setRoot(root);
        txt_customer.setShowRoot(false);

    }

    @FXML
    private void btn_new_customer(ActionEvent event) {

        try {
            StackPane root = FXMLLoader.load(getClass().getResource("/com/iTechnoPhoenix/neelReboot/Customer.fxml"));
            Support.getDialog(window, root, JFXDialog.DialogTransition.CENTER).show();

        } catch (IOException ex) {
            Logger.getLogger(CustomerDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
