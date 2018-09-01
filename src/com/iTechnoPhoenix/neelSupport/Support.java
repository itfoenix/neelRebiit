/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.neelSupport;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 *
 * @author NARENDRA JADHAV
 */
public class Support {

    public static JFXDialog getDialog(StackPane sp, Region body, JFXDialog.DialogTransition anim) {
        JFXDialogLayout bodyContain = new JFXDialogLayout();
        bodyContain.setBody(body);
        JFXDialog dialog = new JFXDialog(sp, bodyContain, anim);
        return dialog;
    }

    public static JFXDialog getDialog(StackPane sp, Region header, Region body, JFXButton... button) {
        JFXDialogLayout bodyContain = new JFXDialogLayout();
        bodyContain.setHeading(header);
        bodyContain.setBody(body);
        bodyContain.setActions(button);
        JFXDialog dialog = new JFXDialog(sp, bodyContain, JFXDialog.DialogTransition.CENTER);
        dialog.setOverlayClose(false);
        return dialog;
    }
}
