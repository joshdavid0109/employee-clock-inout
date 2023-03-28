package controller;

import com.google.gson.JsonObject;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.server.AttendanceServant;
import org.server.JSONHandler;
import org.server.Attendance;
import org.shared_classes.EmployeeProfile;
import org.shared_classes.EmployeeReport;

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
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ServerController implements Initializable {

    public TableColumn<EmployeeProfile, String> timeOutColumn;
    public TableColumn<EmployeeProfile, String> timeInColumn;
    public TableColumn<EmployeeProfile, String> activeStatusColumn;
    @FXML
    private Text adminNameLabel, companyNameLabel, genReport;

    @FXML
    private TextField fromTF, searchField, toTF;

    @FXML
    private TableColumn<EmployeeProfile, String> columnId;

    @FXML
    private TableColumn<EmployeeProfile, String> dateColumn;

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
    private DatePicker startDate, endDate;

    @FXML
    private Label selectedDateFrom, selectedDateTo;

    @FXML
    private Button logOutButton, searchButton, printBtn, refreshButton, addEmployeeButton;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MMMM - dd");

//    String jsonString = new String(Files.readAllBytes(Paths.get("employees.json")));
//    JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
    private static final AttendanceServant ers = new AttendanceServant();

    public ServerController() throws IOException {}

    @FXML
    void logOut(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/AdminLoginInterface.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void refresh(ActionEvent event) {
        List<EmployeeProfile> list = JSONHandler.populateTable();
//        EmployeeProfile employeeProfile = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);
        tableView.setItems(tableData);
        tableView.refresh();
    }

    /**
     * zephhhhhhhhhhhhhhhhhhh
     */
//    void summary(ActionEvent event) throws IOException {
//
//        Date startDate = fromTF.getValue();
//        Date endDate = toTf.getValue();
//
//        if (startDate != null && endDate != null) {
//            List<Data> searchResults = dataService.search(startDate, endDate);
//
//        }
//    }

    @FXML
    void getTimeLogs(ActionEvent event) throws IOException {

        LocalDate fromDate = startDate.getValue();
        LocalDate toDate = endDate.getValue();
        String fromDateFormat = fromDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        String toDateFormat = toDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));

        selectedDateFrom.setText(fromDateFormat);
        selectedDateTo.setText(toDateFormat);

        // code to calculate the total time worked by each employee(total hours rendered)?
//        if (startDate.getValue() !=null && endDate.getValue() !=null) {
//            for (EmployeeProfile e : employee.get)
//        }
    }

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

        activeStatusColumn.setCellValueFactory(cell -> {
            Bindings.selectString(cell.getValue(), "isLoggedIn");
            return Bindings.selectString(cell.getValue(), "isLoggedIn");
        });
        columnId.setCellValueFactory(new PropertyValueFactory<EmployeeProfile, String>("empID"));
        lnColumn.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue().getPersonalDetails(), "lastName"));
        fnColumn.setCellValueFactory(cell ->
                Bindings.selectString(cell.getValue().getPersonalDetails(), "firstName"));
        dateColumn.setCellValueFactory(cell ->
        {
            Bindings.selectString(cell.getValue().getEmployeeDailyReport(), "date");
            return Bindings.selectString(cell.getValue().getEmployeeDailyReport(), "date");
        });
        timeInColumn.setCellValueFactory(cell -> {
            Bindings.selectString(cell.getValue().getEmployeeDailyReport(), "timeIn")  ;
            return Bindings.selectString(cell.getValue().getEmployeeDailyReport(), "timeIn");
        });
        timeOutColumn.setCellValueFactory(cell -> {
            Bindings.selectString(cell.getValue().getEmployeeDailyReport(), "timeOut");
            return Bindings.selectString(cell.getValue().getEmployeeDailyReport(), "timeOut");
        });
        statusColumn.setCellValueFactory(cell ->
        {
            Bindings.selectString(cell.getValue(), "status");
            return Bindings.selectString(cell.getValue(), "status");
        });

        tableView.setItems(tableData);
        tableView.refresh();


        // For searchField -- works only for empID
        // uncomment lines if gusto nyo working pati sa name

        FilteredList<EmployeeProfile> filteredList = new FilteredList<>(tableData, p -> true);
        tableView.setItems(filteredList);

        searchField.textProperty().addListener((observableValue, s, t1) -> {
            filteredList.setPredicate(employeeProfile -> {
                if (t1 == null || t1.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = t1.toLowerCase();

                return employeeProfile.getEmpID().toLowerCase().contains(lowerCaseFilter);

                /*if  (employeeProfile.getEmpID().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else return employeeProfile.getFullName().toLowerCase().contains(lowerCaseFilter);*/
            });
        });

        SortedList<EmployeeProfile> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedList);


        //active status column
        activeStatusColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                    setStyle("-fx-font-weight: bold");
                } else if (item.equals("online")) {
                    setStyle("-fx-background-color: #50C878; -fx-font-weight: bold;");
                } else {
                    setStyle("-fx-background-color: #FA5F55; -fx-font-weight: bold;");
                }
            }
        });



        tableView.setRowFactory(tv ->{
            TableRow<EmployeeProfile> tableRow = new TableRow<>();

/*            tableView.setRowFactory(tvv -> new TableRow<>(){

                @Override
                protected void updateItem(EmployeeProfile item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) {
                        setStyle("");
                        setStyle("-fx-font-weight: bold");
                    } else if (item.getIsLoggedIn().equals("online")) {
                        setStyle("-fx-background-color: #50C878; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: #FA5F55; -fx-font-weight: bold;");
                    }
                }
            }) ;*/

            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! tableRow.isEmpty())) {
                    EmployeeProfile employeeProfile = tableRow.getItem();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/fxml/TreeTableView.fxml"));

                        List<EmployeeReport> reports = new ArrayList<>();

                        if (employeeProfile.getEmployeeDailyReport().getListofTimeOuts().size() ==
                                employeeProfile.getEmployeeDailyReport().getListofTimeIns().size()) {
                            for (int i = 0; i < employeeProfile.getEmployeeDailyReport().getListofTimeOuts().size(); i++) {

                                String timeIn = employeeProfile.getEmployeeDailyReport().getListofTimeIns().get(i);
                                String timeOut = employeeProfile.getEmployeeDailyReport().getListofTimeOuts().get(i);
                                EmployeeReport employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut.split(", ")[1]);
                                if (i == 0) {
                                    employeeReport.setDate(timeOut.split(", ")[0]);
                                }
                                reports.add(employeeReport);
                            }
                        } else if (employeeProfile.getEmployeeDailyReport().getListofTimeOuts().size() <
                                employeeProfile.getEmployeeDailyReport().getListofTimeIns().size()) {
                            for (int i = 0; i < employeeProfile.getEmployeeDailyReport().getListofTimeIns().size(); i++) {
                                String timeIn = employeeProfile.getEmployeeDailyReport().getListofTimeIns().get(i);
                                String timeOut;
                                EmployeeReport employeeReport = null;
                                if (employeeProfile.getEmployeeDailyReport().getListofTimeIns().size() - 1 == i){
                                    timeOut = "";
                                    employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut);
                                }else {
                                    timeOut = employeeProfile.getEmployeeDailyReport().getListofTimeOuts().get(i);
                                    employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut.split(", ")[1]);
                                }
                                if (i == 0) {
                                    employeeReport.setDate(timeOut.split(", ")[0]);
                                }
                                reports.add(employeeReport);
                            }
                        }

                        EmployeeTable.employeeDailyReport = reports;

                        Pane employeeTable = fxmlLoader.load();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane((DialogPane) employeeTable);
                        dialog.setTitle("Summary");

                        //close button
                        Window window = dialog.getDialogPane().getScene().getWindow();
                        window.setOnCloseRequest(event1 ->
                                window.hide());

                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
            });
            return tableRow;
        });
    }
    
    public void updateTable() {
        List<EmployeeProfile> list = JSONHandler.populateTable();
//        EmployeeProfile employeeProfile = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);
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

    public void printReport(ActionEvent event) {
    }

/*    @FXML
    public void printReport(ActionEvent event) {
        String jsonString;
        JsonObject jsonObject;

        try {
            jsonString = new String(Files.readAllBytes(Paths.get("employees.json")));

            jsonObject = gson.fromJson(jsonString);


        }
    }*/
}
