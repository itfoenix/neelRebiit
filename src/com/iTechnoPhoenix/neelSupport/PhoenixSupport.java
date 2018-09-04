package com.iTechnoPhoenix.neelSupport;

import com.iTechnoPhoenix.model.Bill;
import com.iTechnoPhoenix.model.MeterBill;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class PhoenixSupport {

    public static int uid = 0;
    public static int role = 0;
    public static ArrayList<MeterBill> singleBill;

    public static void EmailValidation(TextField t) {
        t.focusedProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (!newValue) {
                if (!t.getText().matches("(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                    t.getStyleClass().addAll("inValid");
                    t.clear();
                } else {
                    t.getStyleClass().removeAll("inValid");
                }
            }
        });
    }

    public static void PhoneValidation(TextField t) {
        t.focusedProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (!newValue) {
                if (!t.getText().matches("[0|91]?[7-9][0-9]{9}")) {
                    t.getStyleClass().addAll("inValid");

                    t.clear();

                } else {
                    t.getStyleClass().removeAll("inValid");
                }
            }
        });
    }

    public static void onlyNumber(TextField t) {
        t.focusedProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (!newValue) {
                Locale l = new Locale("mr", "IN");

                try {
                    if (t.getText().isEmpty()) {
                        NumberFormat nf = NumberFormat.getInstance(l);
                        double d = nf.parse(t.getText()).doubleValue();
                        t.getStyleClass().remove("inValid");
                    }

                } catch (ParseException ex) {
                    t.clear();
                }
            }
        });
    }

    public static boolean isValidate(TextField... n) {
        boolean b = false;
        for (TextField t : n) {
            if ((t.getText() == null) || t.getText().length() == 0) {
                b = false;
                t.getStyleClass().add("inValid");
                break;
            } else {
                t.getStyleClass().remove("inValid");
                b = true;
            }
        }
        return b;
    }

    public static boolean isValidate(ComboBox... c) {
        boolean result = false;
        for (ComboBox cb : c) {
            if (cb.getSelectionModel().isEmpty()) {
                result = false;
                cb.getStyleClass().add("inValid");
                break;
            } else {
                cb.getStyleClass().remove("inValid");
                result = true;
            }
        }
        return result;
    }

    public static void Error(String errorMessage) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
        dialog.showAndWait();
    }

    public static void Info(String ContentMessage, String HeaderText) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setHeaderText(HeaderText);
        dialog.setContentText(ContentMessage);
        dialog.setResult(ButtonType.OK);
        dialog.setResult(ButtonType.CANCEL);
        dialog.showAndWait();
    }

    public static void Error(String contain, StackPane sp) {
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        Label lblheader = new Label();
        lblheader.setText("सूचना");
        lblheader.setMaxWidth(1.7976931348623157E308);
        lblheader.setPadding(new Insets(8));
        lblheader.setStyle("-fx-background-color:red; -fx-text-fill:white");
        jfxDialogLayout.setHeading(lblheader);
        jfxDialogLayout.setBody(new Text(contain));
        JFXDialog jfxDialog = new JFXDialog(sp, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton okay = new JFXButton("रद्ध करा");
        okay.setPrefWidth(110);
        okay.setStyle("-fx-background-color: #3E4A4F; -fx-text-fill: white;");
        okay.setButtonType(JFXButton.ButtonType.RAISED);
        okay.setOnAction(event -> {
            jfxDialog.close();
        });
        jfxDialogLayout.setActions(okay);
        jfxDialog.show();
    }

    public static void Info(String ContentMessage, String HeaderText, StackPane root) {
        JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
        Label lblheader = new Label();
        lblheader.setText(HeaderText);
        lblheader.setMaxWidth(1.7976931348623157E308);
        lblheader.setPadding(new Insets(8));
        lblheader.setStyle("-fx-background-color:#3E4A4F; -fx-text-fill:white");
        jfxDialogLayout.setHeading(lblheader);
        jfxDialogLayout.setBody(new Text(ContentMessage));
        JFXDialog jfxDialog = new JFXDialog(root, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
        JFXButton okay = new JFXButton("रद्ध करा");
        okay.setPrefWidth(110);
        okay.setStyle("-fx-background-color: #3E4A4F; -fx-text-fill: white;");
        okay.setButtonType(JFXButton.ButtonType.RAISED);
        okay.setOnAction(event -> {
            jfxDialog.close();
        });
        jfxDialogLayout.setActions(okay);
        jfxDialog.show();
    }

    public static void setFullScreen(StackPane window) {
        window.setPrefWidth(PhoenixConfiguration.getWidth());
        window.setPrefHeight(PhoenixConfiguration.getheight());

    }

    public static void setFullScreen(BorderPane window) {
        window.setPrefWidth(PhoenixConfiguration.getWidth());
        window.setPrefHeight(PhoenixConfiguration.getheight());

    }

    public static long getLong(String value) {

        Locale l = new Locale("mr", "IN");

        try {
            if (!value.isEmpty()) {
                NumberFormat nf = NumberFormat.getInstance(l);
                return nf.parse(value).longValue();
            }
        } catch (ParseException ex) {

        }
        return 0;
    }

    public static double getDouble(String value) {

        Locale l = new Locale("mr", "IN");

        try {
            if (!value.isEmpty()) {
                NumberFormat nf = NumberFormat.getInstance(l);
                return nf.parse(value).doubleValue();
            }
        } catch (ParseException ex) {

        }
        return 0;
    }

    public static int getInteger(String value) {

        Locale l = new Locale("mr", "IN");

        try {
            if (!value.isEmpty()) {
                NumberFormat nf = NumberFormat.getInstance(l);
                return nf.parse(value).intValue();
            }
        } catch (ParseException ex) {

        }
        return 0;
    }

    public static boolean close = false;

    public static void printMeterBill(ArrayList<MeterBill> blist) {
        singleBill = new ArrayList<>();
        for (MeterBill bs : blist) {
            singleBill.add(bs);
        }
        printing();
    }

    public static void printing() {
        String billformate = "report1.jrxml";
        JasperReport report;
        try {
            report = JasperCompileManager.compileReport(billformate);
            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(singleBill);
            JasperPrint jp = JasperFillManager.fillReport(report, null, datasource);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(PhoenixSupport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printReceipt(ArrayList<Bill> blist) {
        String billformate = "ReceiptFormate.jrxml";
        try {
            ArrayList<Bill> singleReceipt = new ArrayList<>();
            for (Bill bs : blist) {
                singleReceipt.add(bs);
            }
            JasperReport report = JasperCompileManager.compileReport(billformate);
            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(singleReceipt);
            JasperPrint jp = JasperFillManager.fillReport(report, null, datasource);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(PhoenixSupport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
