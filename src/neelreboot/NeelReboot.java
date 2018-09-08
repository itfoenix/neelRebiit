/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neelreboot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author NARENDRA JADHAV
 */
public class NeelReboot extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/iTechnoPhoenix/MainActivity/LoginPage.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("/com/iTechnoPhoenix/Receipt/ReceiptTransaction.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("/com/iTechnoPhoenix/bills/BillTransaction.fxml"));
//            Parent root = FXMLLoader.load(getClass().getResource("/com/iTechnoPhoenix/bills/CancelCheque.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(NeelReboot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
