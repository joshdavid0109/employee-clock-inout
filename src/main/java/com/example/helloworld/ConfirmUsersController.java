package com.example.helloworld;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeProfile;
import org.server.*;
import org.shared_classes.EmployeeReport;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConfirmUsersController implements Initializable {

    Attendance stub;

    @FXML
    private TableView<EmployeeProfile> employeeTable;

    @FXML
    private TableColumn<EmployeeProfile, String> columnUsername;

    @FXML
    private TableColumn<EmployeeProfile, String> columnDepartment;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    static List<EmployeeProfile> pendingEmployees;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pendingEmployees = JSONHandler.getPendingRegistersFromFile();

        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(pendingEmployees);

        System.out.println(tableData);

        employeeTable.setItems(tableData);
        employeeTable.refresh();
    }
}
