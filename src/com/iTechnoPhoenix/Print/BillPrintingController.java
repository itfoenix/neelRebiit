package com.iTechnoPhoenix.Print;

import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.Unit;
import com.iTechnoPhoenix.neelSupport.PhoenixConfiguration;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

public class BillPrintingController implements Initializable {

    @FXML
    private JFXTextField txt_meter_customer;

    @FXML
    private JFXComboBox<String> cb_duration;

    @FXML
    private JFXTreeTableView<Bill> tbl_bill;

    private JFXTreeTableColumn<Bill, String> tcbilldate;
    private JFXTreeTableColumn<Bill, Integer> tcbillnumber;
    private JFXTreeTableColumn<Bill, String> tcmeterno;
    private JFXTreeTableColumn<Bill, Double> tcbalance;
    private JFXTreeTableColumn<Bill, Double> tcscharges;
    private JFXTreeTableColumn<Bill, Double> tcamount;
    private JFXTreeTableColumn<Bill, Long> tcuseunit;
    private JFXTreeTableColumn<Bill, Integer> tcaction;
    private JFXTreeTableColumn<Bill, String> tccustomer;

    @FXML
    private void btn_print_all(ActionEvent event) {

    }

    @FXML
    private void btn_search(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cb_duration.setItems(PhoenixConfiguration.getMonth());
        cb_duration.getStyleClass().add("label-marathi");
        initTable();
    }

    @FXML
    private void btn_print_all_key(KeyEvent event) {

    }

    @FXML
    private void btn_search_key(KeyEvent event) {

    }

    private void initTable() {
        tcbillnumber = new JFXTreeTableColumn<>("बिल क्रमांक");
        tcbillnumber.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillref()).asObject());
        tcmeterno = new JFXTreeTableColumn<>("मीटर क्रमांक");
        tcmeterno.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMeter().getMetor_num()));
        tcbilldate = new JFXTreeTableColumn<>("बिलाची तारिक");
        tcbilldate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getBdate()));
        tcbalance = new JFXTreeTableColumn<>("थकबाकी");
        tcbalance.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getBalance()).asObject());
        tcamount = new JFXTreeTableColumn<>("बिलाची रक्कम");
        tcamount.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getCuramount()).asObject());
        tcscharges = new JFXTreeTableColumn<>("सर चार्ज");
        tcscharges.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getScharges()).asObject());
        tcuseunit = new JFXTreeTableColumn<>("वापर युनिट");
        tcuseunit.setCellValueFactory(param -> new SimpleLongProperty(param.getValue().getValue().getUseunit()).asObject());
        tccustomer = new JFXTreeTableColumn<>("ग्राहकाचे नाव");
        tccustomer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getMeter().getMetor_num()));
        tcaction = new JFXTreeTableColumn<>("");
        tcaction.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getMeter().getId()).asObject());
        tcaction.setCellFactory(param -> new ActionCell(tbl_bill));
    }

    public class ActionCell extends JFXTreeTableCell<Bill, Integer> {

        final JFXButton print = new JFXButton("Print");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<Bill> table) {
            actiongroup.getChildren().addAll(print);
            print.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Edit Row_48px.png"));
            print.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            print.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                }
            });
        }

        @Override

        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(actiongroup);
            } else {
                setGraphic(null);
            }
        }
    }
}
