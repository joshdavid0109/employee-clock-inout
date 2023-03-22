package com.example.helloworld;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;
import org.shared_classes.EmployeeReport;
import org.shared_classes.SummaryReport;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class EmployeeTable implements Initializable {
//    public TreeTableColumn<SummaryReport, String> dateTTColumn;
//    public TreeTableColumn<SummaryReport, String> timeInTTColumn;
//    public TreeTableColumn<SummaryReport, String> timeOutTTColumn;
//    public TreeTableView<SummaryReport> treeTableView;
//
//    /**
//     * Called to initialize a controller after its root element has been
//     * completely processed.
//     *
//     * @param location  The location used to resolve relative paths for the root object, or
//     *                  {@code null} if the location is not known.
//     * @param resources The resources used to localize the root object, or {@code null} if
//     *                  the root object was not localized.
//     */
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//
//    }

    @FXML
    private TableColumn<EmployeeReport, String> columnTI = new TableColumn<>();

    @FXML
    private TableColumn<EmployeeReport, String> columnTO = new TableColumn<>();

    @FXML
    private TableView<EmployeeReport> tableView = new TableView<>();

    static List<EmployeeReport> employeeDailyReport;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ObservableList<EmployeeReport> tableData = FXCollections.observableList(employeeDailyReport);

        columnTI.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue(), "timeIn"));
        columnTO.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue(), "timeOut"));


        tableView.setItems(tableData);
        tableView.refresh();
    }

}
