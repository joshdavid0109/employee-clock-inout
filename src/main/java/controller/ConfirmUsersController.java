package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;
import org.server.*;
import org.shared_classes.EmployeeReport;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class ConfirmUsersController implements Initializable {

    public TableColumn<EmployeeProfile, String> usernameColumn;
    Attendance stub;

    @FXML
    private TableView<EmployeeProfile> employeeTable;

    @FXML
    private TableColumn<EmployeeProfile, String> columnDepartment;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    static List<EmployeeProfile> pendingEmployees;


    @FXML
    void acceptUser(ActionEvent event) {
        EmployeeProfile employeeProfile = employeeTable.getSelectionModel().getSelectedItem();
        if (employeeProfile != null) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to" +
                    " accept " + employeeProfile.getUserName() + "?");
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            dialog.getButtonTypes().setAll(yes, no);

            Optional<ButtonType> input = dialog.showAndWait();
            if (input.get() == yes) {
                for (EmployeeProfile pendingEmp : pendingEmployees) {
                    if (pendingEmp.equals(employeeProfile)) {
                        pendingEmployees.remove(pendingEmp);
                        String employeeID = "EMP"+UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
                        pendingEmp.setEmpID(employeeID);
                        pendingEmp.setPersonalDetails(new EmployeeDetails());
                        List<EmployeeProfile> empi = JSONHandler.getFromFile();
                        empi.add(pendingEmp);
                        JSONHandler.addToFile(empi);
                        Alert accepted = new Alert(Alert.AlertType.INFORMATION, "Employee "+pendingEmp.getUserName()+" has been registered. \n" +
                                "ID: "+employeeID);
                        accepted.show();
                        break;
                    }
                }
                JSONHandler.writeGSon(pendingEmployees);
                updateTable();
            } else
                dialog.close();

        } else {
            Alert x = new Alert(Alert.AlertType.WARNING, "Choose an employee");
            x.show();
        }
    }

    @FXML
    void rejectUser(ActionEvent event) {
        EmployeeProfile employeeProfile = employeeTable.getSelectionModel().getSelectedItem();

        if (employeeProfile != null) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to" +
                    " reject " + employeeProfile.getUserName() + "?");
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            dialog.getButtonTypes().setAll(yes, no);

            Optional<ButtonType> input = dialog.showAndWait();
            if (input.get() == yes) {
                for (EmployeeProfile e : pendingEmployees) {
                    if (e.equals(employeeProfile)) {
                        pendingEmployees.remove(e);
                        break;
                    }
                }

                JSONHandler.writeGSon(pendingEmployees);
                updateTable();
            } else
                dialog.close();

        } else {
            Alert x = new Alert(Alert.AlertType.WARNING, "Choose an employee");
            x.show();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    public void updateTable(){
        pendingEmployees = JSONHandler.getPendingRegistersFromFile();

        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(Objects.requireNonNull(pendingEmployees));

        usernameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeProfile, String>("userName"));

        employeeTable.setItems(tableData);
        employeeTable.refresh();
    }
}