package com.example.helloworld;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;


public class EmployeeTable implements Initializable {

    @FXML
    private TableColumn<EmployeeDailyReport, String> columnStatus;

    @FXML
    private TableColumn<EmployeeDailyReport, String> columnTI;

    @FXML
    private TableColumn<EmployeeDailyReport, String> columnTO;

    @FXML
    private TableView<EmployeeDailyReport> tableView = new TableView<>();

    private EmployeeDailyReport employeeDailyReport;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date date = new Date();

    }

    public EmployeeDailyReport getEmployeeDailyReport() {
        return employeeDailyReport;
    }

    public void setEmployeeDailyReport(EmployeeDailyReport employeeDailyReport) {
        this.employeeDailyReport = employeeDailyReport;

        ObservableList<EmployeeDailyReport> tableData = tableView.getItems();
        tableData.add(employeeDailyReport);

        columnTI.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("timeIn"));
        columnTO.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("timeOut"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("status"));

        tableView.setItems(tableData);
        tableView.refresh();
    }
}
