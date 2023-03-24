package controller;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.server.AttendanceServant;
import org.server.JSONHandler;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ServerController implements Initializable {

    @FXML
    private Text adminNameLabel, companyNameLabel, genReport;

    @FXML
    private TextField fromTF, searchTF, toTF;

    @FXML
    private TableColumn<EmployeeProfile, String> columnId;

    @FXML
    private TableColumn<EmployeeProfile, Date> dateColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> fnColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> lnColumn;

    @FXML
    private TableColumn<EmployeeProfile, String > statusColumn;

    @FXML
    private TableColumn<EmployeeProfile, String> workHoursColumn;

    @FXML
    private TableView<EmployeeProfile> tableView;

    @FXML
    private Button logOutButton, printBtn, refreshButton, addEmployeeButton;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MMMM - dd");

//    String jsonString = new String(Files.readAllBytes(Paths.get("employees.json")));
//    JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
    private static final AttendanceServant ers = new AttendanceServant();

    public ServerController() throws IOException {
    }

    @FXML
    void logOut(ActionEvent event) {

    }

    @FXML
    void refresh(ActionEvent event) {
        List<EmployeeProfile> list = JSONHandler.populateTable();
//        EmployeeProfile employeeProfile = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);
        tableView.setItems(tableData);
        tableView.refresh();
    }

    @FXML
    void searchEmpID(InputMethodEvent event) {

    }

    /**
     * zephhhhhhhhhhhhhhhhhhh
     */
//    public void generateReport (ActionEvent actionEvent) throws IOException {
//        genReport.setText("Generate Report");
//        //var fromTF = fromTF.getText();
//        //var toTF = toTF.getText();
//
//        LocalDate startDate = LocalDate.parse("2023-03-19");
//        LocalDate endDate = LocalDate.parse("2023-03-20");
//
//        JsonArray dataArray = jsonObject.getAsJsonArray("listofTimeIns");
//        for(JsonElement dataElement : dataArray) {
//            JsonObject dataObject = dataElement.getAsJsonObject();
//            String dateString = dataObject.get("date").getAsString();
//            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
//            if (date.isAfter(startDate) && date.isBefore(endDate)) {
//                String value = dataObject.get("listofTimeIns").getAsString();
//                // extract the data you need from the data object
//                // e.g., String value = dataObject.get("key").getAsString();
//                // recommit
//            }
//        }
//    }


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
        lnColumn.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue().getPersonalDetails(), "lastName"));
        fnColumn.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue().getPersonalDetails(), "firstName"));
        statusColumn.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue(), "isLoggedIn"));

        tableView.setItems(tableData);
        tableView.refresh();


    }

    public void addEmployee(ActionEvent actionEvent) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConfirmUsersTable.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage addEmployeeWindow = new Stage();
        addEmployeeWindow.setScene(scene);
        addEmployeeWindow.show();
    }
}
