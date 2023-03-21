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
import javafx.scene.control.cell.PropertyValueFactory;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;
import org.shared_classes.EmployeeReport;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class EmployeeTable implements Initializable {

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

       /* for (int i = 0; i < employeeDailyReport.getListofTimeOuts().size(); i++) {
            int finalI = i;
            columnTI.setText(employeeDailyReport.getListofTimeIns().get(i));
            columnTI.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue()[finalI]));
            columnTO.setText(employeeDailyReport.getListofTimeOuts().get(i));
            columnTO.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue()[finalI]));
            columnStatus.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("status"));
            tableView.getColumns().add(columnTI);
            tableView.getColumns().add(columnTO);
            tableView.getColumns().add(columnStatus);
        }*/

        /*columnTI.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeDailyReport, String> p ) -> {
            List<String> timeins = p.getValue().getListofTimeIns();
            String val = String.join(", ", timeins);
            return new ReadOnlyStringWrapper(val);
        });

        columnTO.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeDailyReport, String> p ) -> {
            List<String> timouts = p.getValue().getListofTimeOuts();
            String val = String.join(", ", timouts);
            return new ReadOnlyStringWrapper(val);
        });*/


//        tableView.setItems(tableData);
//        tableView.refresh();

}
