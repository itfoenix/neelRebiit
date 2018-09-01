/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iTechnoPhoenix.neelSupport;

import com.jfoenix.controls.JFXTreeTableColumn;

/**
 *
 * @author NARENDRA JADHAV
 */
public class UnitsTable<T, C> {

//    private JFXTreeTableColumn<T, C> min_unit, max_unit;
//    private JFXTreeTableColumn<T, C> unit_price;
//    private UnitsTable() {
//        min_unit = new JFXTreeTableColumn<>("किमान युनिट");
//        min_unit.setCellValueFactory((param) -> {
//            return new SimpleIntegerProperty(param.getValue().getValue().getMin()).asObject();
//        });
//        max_unit = new JFXTreeTableColumn<>("कमाल युनिट");
//        max_unit.setCellValueFactory((param) -> {
//            return new SimpleIntegerProperty(param.getValue().getValue().getMax()).asObject();
//        });
//        unit_price = new JFXTreeTableColumn<>("युनिट रक्कम");
//        unit_price.setCellValueFactory((param) -> {
//            return new SimpleDoubleProperty(param.getValue().getValue().getUnitprice()).asObject();
//        });
//    }
//    public JFXTreeTableColumn getTableColumn() {
//        return min_unit;
//    }
    public void getTable(C element, String columnHeading) {
        JFXTreeTableColumn<T, C> unit_pric = new JFXTreeTableColumn<>(columnHeading);
//        unit_pric.setCellValueFactory((param) -> {
//            return SimpleStringProperty(param.getValue().getValue().getClass().get); //To change body of generated lambdas, choose Tools | Templates.
//        });
    }

}
