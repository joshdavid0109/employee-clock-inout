package com.example.helloworld;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.server.AttendanceServant;
import org.server.JSONHandler;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private Text adminNameLabel;

    @FXML
    private Text companyNameLabel;

    @FXML
    private TextField fromTF;

    @FXML
    private TextField searchTF;

    @FXML
    private TextField toTF;

    @FXML
    private TableColumn<EmployeeProfile, String> columnId;

    @FXML
    private TableColumn<EmployeeProfile, Date> dateColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> fnColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> lnColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> statusColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> workHoursColumn;

    @FXML
    private TableView<EmployeeDailyReport> tableView;

    @FXML
    private Button logOutButton;

    @FXML
    private Button printBtn;

    @FXML
    private Button refreshButton;

    private static final AttendanceServant ers = new AttendanceServant();

    @FXML
    void logOut(ActionEvent event) {

    }

    @FXML
    void refresh(ActionEvent event) {

    }


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
        Attendance stub;
        try {
            stub = (Attendance) UnicastRemoteObject.exportObject(ers, 0);
            Registry registry = null;
            registry = LocateRegistry.createRegistry(2345);
            registry.rebind("sayhi", stub);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }



        List<EmployeeProfile> list = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);

        columnId.setCellValueFactory(new PropertyValueFactory<EmployeeProfile, String>("empID"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<EmployeeProfile, String>(s));
    }
}
