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
    public static TableView<EmployeeDailyReport> tableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date date = new Date();
        ObservableList<EmployeeDailyReport> tableData = tableView.getItems();
        EmployeeDailyReport employeeDailyReport = new EmployeeDailyReport(String.valueOf(date.getDate()));
        employeeDailyReport.setStatus("");


        columnTI.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("timeIn"));
        columnTO.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("timeOut"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("status"));
    }
}
