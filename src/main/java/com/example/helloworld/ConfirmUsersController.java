package com.example.helloworld;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

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
        //TODO
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
                for (EmployeeProfile e :
                        pendingEmployees) {
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
            //TODO dialog na dapat mamili admin ng user
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
