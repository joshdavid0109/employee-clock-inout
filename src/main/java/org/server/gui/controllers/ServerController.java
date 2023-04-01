package org.server.gui.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.server.resources.*;
import org.shared_classes.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ServerController implements Initializable {

    public static String stubName;
    public static int port;

    public TableColumn<EmployeeProfile, String> timeOutColumn;
    public TableColumn<EmployeeProfile, String> timeInColumn;
    public TableColumn<EmployeeProfile, String> activeStatusColumn;
    public Text dateLabel;
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
    private TableColumn<EmployeeProfile, Integer> workHoursColumn;

    @FXML
    private TableView<EmployeeProfile> tableView;

    @FXML
    private DatePicker startDate, endDate;

    @FXML
    private Label selectedDateFrom, selectedDateTo;

    @FXML
    private Button logOutButton, searchButton, printBtn, refreshButton, addEmployeeButton;

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

//    String jsonString = new String(Files.readAllBytes(Paths.get("employees.json")));
//    JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
    private static final AttendanceServant ers = new AttendanceServant();

    public static Registry registry = null;

    public ServerController() throws IOException {}

    public TableView<EmployeeProfile> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<EmployeeProfile> tableView) {
        this.tableView = tableView;
    }

    public void setPort(int port) {
        ServerController.port = port;
    }

    public void setStubName(String stubName) {
        ServerController.stubName = stubName;
    }

    @FXML
    void logOut(ActionEvent event) throws IOException, NotBoundException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/AdminLoginInterface.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void refresh(ActionEvent event) throws IOException, ParseException {
        List<EmployeeProfile> list = JSONHandler.populateTable();
        computeWorkingHours("src/main/java/org/server/resources/summaryReports.json");
//        EmployeeProfile employeeProfile = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);
        tableView.setItems(tableData);
        tableView.refresh();
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

        if (registry == null) {
            try {
                Attendance stub = (Attendance) UnicastRemoteObject.exportObject(ers, 0);
                registry = LocateRegistry.createRegistry(port);
                registry.rebind("sayhi", stub);
                InetAddress inetAddress = InetAddress.getLocalHost();
                String ipAddress = inetAddress.getHostAddress();
                LocalTime time = LocalTime.now();
                System.out.println("["+time.format(DateTimeFormatter.ofPattern("HH:mm"))+"] Server has been opened at port: "+port+" at address: "+ipAddress);
            } catch (RemoteException | UnknownHostException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            Date date = new Date();
            dateLabel.setText(dateFormat.format(date));
            computeWorkingHours("src/main/java/org/server/resources/summaryReports.json");
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }

        EventHandler<ActionEvent> calPickerEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    LocalDate fromDate = startDate.getValue();
                    LocalDate toDate = endDate.getValue();

                    String fromDateFormat = fromDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                    String toDateFormat = toDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                    selectedDateFrom.setText(fromDateFormat);
                    selectedDateTo.setText(toDateFormat);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }


            }
        };

        try {
            List<EmployeeProfile> list = JSONHandler.populateTable();
            ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);

            activeStatusColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getIsLoggedIn());
                return Bindings.createStringBinding(() -> value.orElse("default value"));
            });

            columnId.setCellValueFactory(new PropertyValueFactory<EmployeeProfile, String>("empID"));
            lnColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getPersonalDetails())
                        .map(EmployeeDetails::getLastName);
                return Bindings.createStringBinding(() -> value.orElse(""));
            });
            fnColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getPersonalDetails())
                        .map(EmployeeDetails::getFirstName);
                return Bindings.createStringBinding(() -> value.orElse(""));
            });
            dateColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getEmployeeDailyReport())
                        .map(EmployeeDailyReport::getDate);
                return Bindings.createStringBinding(() -> value.orElse(""));
            });
            timeInColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getEmployeeDailyReport())
                        .map(EmployeeDailyReport::getTimeIn);
                return Bindings.createStringBinding(() -> value.orElse(""));
            });
            timeOutColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getEmployeeDailyReport())
                        .map(EmployeeDailyReport::getTimeOut);
                return Bindings.createStringBinding(() -> value.orElse(""));
            });
            statusColumn.setCellValueFactory(cell -> {
                Optional<String> value = Optional.ofNullable(cell.getValue().getStatus());
                return Bindings.createStringBinding(() -> value.orElse(""));
            });
            workHoursColumn.setCellValueFactory(cell -> {
                Optional<Integer> value = Optional.of(Math.round(cell.getValue().getTotalWorkingHours()));
                return Bindings.createObjectBinding(() -> value.orElse(0));
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        tableView.setRowFactory(tv ->{
            TableRow<EmployeeProfile> tableRow = new TableRow<>();

            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! tableRow.isEmpty())) {
                    EmployeeProfile employeeProfile = tableRow.getItem();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/fxml/TreeTableView.fxml"));
                        Alert errorMessage = new Alert(Alert.AlertType.WARNING);

                        if (startDate.getValue() == null || endDate.getValue() == null) {
                            errorMessage.setTitle("No dates selected");
                            errorMessage.setContentText("Please select a date from the calendar");
                            errorMessage.show();
                        }
                        LocalDate fromDate = startDate.getValue();
                        LocalDate toDate = endDate.getValue();


                        String fromDateFormat = fromDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                        String toDateFormat = toDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                        selectedDateFrom.setText(fromDateFormat);
                        selectedDateTo.setText(toDateFormat);

                        List<EmployeeProfile> employeeList = JSONHandler.populateTable();
                        ObservableList<EmployeeProfile> tableData1 = FXCollections.observableList(employeeList);

                        List<SummaryReport> summaryReports = JSONHandler.getSummaryReportsFromFile();
                        List<SummaryReport> filteredTimeLogs = new ArrayList<>();

                        for (SummaryReport summaryReport : summaryReports) {
                            if (employeeProfile.getEmpID().equals(summaryReport.getEmpID())) {
                                LocalDate logDate = LocalDate.parse(summaryReport.getDate(), DateTimeFormatter.ofPattern("MMM dd yyyy"));
                                if (!logDate.isBefore(fromDate) && !logDate.isAfter(toDate)) {
                                    filteredTimeLogs.add(summaryReport);
                                }
                            }
                        }

                        if (!filteredTimeLogs.isEmpty()) {
                            List<EmployeeReport> reports = new ArrayList<>();
                            for (int i = 0; i < filteredTimeLogs.size(); i++) {
                                SummaryReport summaryReport = filteredTimeLogs.get(i);
                                for (int j = 0; j < summaryReport.getTimeOuts().size(); j++) {
                                    EmployeeReport employeeReport = new EmployeeReport(summaryReport.getTimeIns().get(j).split(", ")[1],
                                            summaryReport.getTimeOuts().get(j).split(", ")[1], summaryReport.getDate());
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
                        } else {
                            errorMessage.setTitle("Error generating report");
                            errorMessage.setContentText("No generated summary for the range of dates selected");
                            errorMessage.show();
                        }

                    } catch (Exception ignored) {

                    }
                }
            });
            return tableRow;
        });



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
    }

    public static void computeWorkingHours(String jsonFilePath) throws IOException, ParseException {
        try{
            String json = Files.readString(Paths.get(jsonFilePath));
            Gson gson = new Gson();
            List<SummaryReport> reports = gson.fromJson(json,
                    new TypeToken<List<SummaryReport>>(){}.getType());

            Map<String, Long> totalWorkingHoursPerEmployee = new HashMap<>();
            for (SummaryReport report : reports) {
                String empID = report.getEmpID();

                List<String> timeIns = report.getTimeIns();
                List<String> timeOuts = report.getTimeOuts();
                long totalWorkingHours = 0;
                for (int i = 0; i < timeIns.size(); i++) {
                    Date timeIn = timeFormat.parse(timeIns.get(i));
                    Date timeOut = timeFormat.parse(timeOuts.get(i));
                    totalWorkingHours += timeOut.getTime() - timeIn.getTime();
                }
                totalWorkingHoursPerEmployee.merge(empID, totalWorkingHours, Long::sum);
            }

            for (String empID : totalWorkingHoursPerEmployee.keySet()) {
                long totalWorkingHours = totalWorkingHoursPerEmployee.get(empID);
                //System.out.println("Employee ID: " + empID + ", Total working hours: " + (totalWorkingHours / 3600000f));
            }

            List<EmployeeProfile> employees = JSONHandler.getEmployeesFromFile();

            for (EmployeeProfile employee : employees) {
                if (totalWorkingHoursPerEmployee.containsKey(employee.getEmpID())) {
                    long totalWorkingSeconds = totalWorkingHoursPerEmployee.get(employee.getEmpID());
                    float totalWorkingHours = totalWorkingSeconds / 3600000f;
                    int updatedTotalWorkingHoursList = (int) totalWorkingHours;
                    employee.setTotalWorkingHours(updatedTotalWorkingHoursList);
                }
            }

            // Writing the updated employees list to the employees.json file.
            String updatedJson = gson.toJson(employees);
            FileWriter writer = new FileWriter("src/main/java/org/server/resources/employees.json");
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("  ");
            gson.toJson(employees, new TypeToken<List<EmployeeProfile>>(){}.getType(), jsonWriter);
            writer.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
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
