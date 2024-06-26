package org.server.gui.controllers;


import javafx.beans.property.SimpleStringProperty;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.shared_classes.*;

import java.net.URL;
import java.util.*;

public class EmployeeTable implements Initializable {

    @FXML
    public TreeTableColumn<EmployeeReport, String> dateTTColumn;
    @FXML
    public TreeTableColumn<EmployeeReport, String> timeInTTColumn;
    @FXML
    public TreeTableColumn<EmployeeReport, String> timeOutTTColumn;

    @FXML
    public TreeTableView<EmployeeReport> treeTableView;

    public static List<EmployeeReport> employeeDailyReport;


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String date = null;
        TreeItem<EmployeeReport> root = new TreeItem<>(new EmployeeReport());

        for (int i = 0; i < employeeDailyReport.size(); i++) {
            TreeItem<EmployeeReport> parent = null;
            if (i == 0) {
                date = employeeDailyReport.get(i).getDate();
                parent = new TreeItem<>(new EmployeeReport("", "", date));
            }else if (date.equals(employeeDailyReport.get(i).getDate()) || employeeDailyReport.get(i).getDate().equals("")) {
                continue;
            }

                date = employeeDailyReport.get(i).getDate();
                parent = new TreeItem<>(new EmployeeReport("", "", date));

                for (EmployeeReport e :
                        employeeDailyReport) {
                    if (e.getDate().equals(date)) {
                        e.setDate("");
                        TreeItem<EmployeeReport> employeeReportTreeItem = new TreeItem<>(e);
                        parent.getChildren().add(employeeReportTreeItem);
                    }
                }
                root.getChildren().add(parent);
                parent = null;

        }

        dateTTColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<EmployeeReport, String> param)
                        -> new SimpleStringProperty(param.getValue().getValue().getDate()));
        timeInTTColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<EmployeeReport, String> param)
                        -> new SimpleStringProperty(param.getValue().getValue().getTimeIn()));
        timeOutTTColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<EmployeeReport, String> param)
                        -> new SimpleStringProperty(param.getValue().getValue().getTimeOut()));

        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
    }
}
