package com.iTechnoPhoenix.bills;

import com.iTechnoPhoenix.database.FailureOperation;
import com.iTechnoPhoenix.database.ReceiptOperation;
import com.iTechnoPhoenix.model.Cheque;
import com.iTechnoPhoenix.neelSupport.PhoenixSupport;
import com.iTechnoPhoenix.neelSupport.Support;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;

public class CancelChequeController implements Initializable {

    @FXML
    private JFXTextField txt_chequenumber;

    @FXML
    private JFXTreeTableView<Cheque> tbl_cheque_number;

    @FXML
    private StackPane window;

    private JFXTreeTableColumn<Cheque, Integer> tcreceiptno;
    private JFXTreeTableColumn<Cheque, Integer> tcbillno;
    private JFXTreeTableColumn<Cheque, String> tcchequno;
    private JFXTreeTableColumn<Cheque, Double> tcextracharges;
    private JFXTreeTableColumn<Cheque, Double> tctotal;
    private JFXTreeTableColumn<Cheque, String> tcdate;
    private JFXTreeTableColumn<Cheque, String> tcstatus;
    private JFXTreeTableColumn<Cheque, Integer> tcaction;

    private Cheque cheque;
    private FailureOperation failuredb;
    private ReceiptOperation receiptdb;
    private ObservableList<Cheque> chequeList;
    private ObservableList<Cheque> cheqList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        failuredb = new FailureOperation();
        receiptdb = new ReceiptOperation();
        txt_chequenumber.textProperty().addListener((observable, oldValue, newValue) -> tbl_cheque_number.setPredicate(t -> t.getValue().getChequenumber().startsWith(newValue)));
        getAll();

        TextFields.bindAutoCompletion(txt_chequenumber, chequeList);

        checking();

        initTable();
        refreshTable();
    }

    public void getAll() {
        chequeList = receiptdb.getAllCheckPayment();
    }

    public void checking() {
        cheqList = failuredb.getAllFailedReceipts();
        for (Cheque c : chequeList) {
            if (cheqList.contains(c)) {
                c.setId(cheqList.get(cheqList.indexOf(c)).getId());
                c.setExtrachages(c.getExtrachages() + cheqList.get(cheqList.indexOf(c)).getExtrachages());
                c.setAmount(cheqList.get(cheqList.indexOf(c)).getAmount());
                c.setStatus("रद्ध");
            }
        }
    }

    private void initTable() {
        tcbillno = new JFXTreeTableColumn<>("बिल क्रमांक");
        tcbillno.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getBillno()).asObject());
        tcchequno = new JFXTreeTableColumn<>("चेक क्रमांक");
        tcchequno.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getChequenumber()));
        tcdate = new JFXTreeTableColumn<>("तारिक");
        tcdate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getBdate()));
        tcextracharges = new JFXTreeTableColumn<>("अधिक चार्ज");
        tcextracharges.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getExtrachages()).asObject());
        tcreceiptno = new JFXTreeTableColumn<>("पावती क्रमांक");
        tcreceiptno.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getReceiptno()).asObject());
        tctotal = new JFXTreeTableColumn<>("एकूण रक्कम");
        tctotal.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getValue().getAmount()).asObject());
        tcstatus = new JFXTreeTableColumn<>("स्तिथी");
        tcstatus.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getStatus()));
        tcaction = new JFXTreeTableColumn<>("");
        tcaction.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().getId()).asObject());
        tcaction.setCellFactory(param -> new ActionCell(tbl_cheque_number));
        tbl_cheque_number.getColumns().addAll(tcaction, tcchequno, tcreceiptno, tcbillno, tcdate, tcextracharges, tctotal, tcstatus);
    }

    private void refreshTable() {
        TreeItem<Cheque> treeItem = new RecursiveTreeItem<>(chequeList, RecursiveTreeObject::getChildren);
        tbl_cheque_number.setRoot(treeItem);
        tbl_cheque_number.setShowRoot(false);
    }

    private void search_Cheque(ActionEvent event) {

//        if (!txt_chequenumber.getText().isEmpty()) {
//            cheque = receiptdb.getReceiptByChequeNum(txt_chequenumber.getText());
//            for (Cheque c : chequeList) {
//                if (c.getChequenumber() != cheque.getChequenumber()) {
//                    chequeList.remove(c);
//                }
//            }
//            checking();
//        } else {
//            getAll();
//            checking();
//        }
//        refreshTable();
    }

    @FXML
    private void search_Cheque_key(KeyEvent event) {
    }

    @FXML
    private void search_Cheque_action(ActionEvent event) {

    }

    public class ActionCell extends JFXTreeTableCell<Cheque, Integer> {

        final JFXButton edit = new JFXButton("Edit");
        final JFXButton delete = new JFXButton("Delete");
        final HBox actiongroup = new HBox();
        final StackPane paddedButton = new StackPane();

        public ActionCell(final JFXTreeTableView<Cheque> table) {
            actiongroup.setAlignment(Pos.CENTER);
            actiongroup.getChildren().addAll(edit, delete);
            edit.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Edit Row_48px.png"));
            edit.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            delete.setGraphic(new ImageView("/com/iTechnoPhoenix/resource/Trash Can_48px.png"));
            delete.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            edit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    JFXDialog dialog;
                    table.getSelectionModel().select(getTreeTableRow().getIndex());
                    Cheque chq = table.getSelectionModel().getSelectedItem().getValue();
                    VBox vb = new VBox();
                    JFXTextField txtExtra = new JFXTextField();
                    PhoenixSupport.onlyNumber(txtExtra);
                    txtExtra.setPromptText("अधिक चार्ज");
                    txtExtra.setLabelFloat(true);
                    txtExtra.setMaxWidth(200);
                    vb.getChildren().addAll(txtExtra);
                    JFXButton btnSave = new JFXButton("जतन");
                    btnSave.getStyleClass().add("btn-search");
                    JFXButton btnClose = new JFXButton("राध");
                    btnClose.getStyleClass().add("btn-cancel");
                    dialog = Support.getDialog(window, new Label("चेक रद्द करणे"), vb, btnSave, btnClose);
                    btnSave.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            chq.setAmount((chq.getAmount() - chq.getExtrachages()) + PhoenixSupport.getDouble(txtExtra.getText()));
                            chq.setExtrachages(PhoenixSupport.getDouble(txtExtra.getText()));
                            chq.setStatus("रद्ध");
                            failuredb.saveFailureBanktransaction(chq);
                            refreshTable();
                            dialog.close();
                        }
                    });
                    btnClose.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            dialog.close();
                        }
                    });
                    dialog.show();
                }
            });
            delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    JFXDialog dialog;
                    table.getSelectionModel().select(getTreeTableRow().getIndex());
                    Cheque chq = table.getSelectionModel().getSelectedItem().getValue();
                    JFXButton btnSave = new JFXButton("होय");
                    btnSave.getStyleClass().add("btn-search");
                    JFXButton btnClose = new JFXButton("रद्ध");
                    btnClose.getStyleClass().add("btn-cancel");
                    dialog = Support.getDialog(window, new Label("चेक रद्द करणे"), new Label("ग्राहकच्या थकबाकी मध्ये अधिक करणे."), btnSave, btnClose);
                    btnSave.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            failuredb.deleteFailure(chq.getChequenumber());
                            chequeList.remove(chq);
                            refreshTable();
                            dialog.close();
                        }
                    });
                    btnClose.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            dialog.close();
                        }
                    });
                    dialog.show();
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
