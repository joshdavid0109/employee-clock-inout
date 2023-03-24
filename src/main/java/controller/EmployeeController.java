package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.server.JSONHandler;
import org.shared_classes.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    private Attendance stub;
    public static EmployeeProfile employee;
    private Date date;

    @FXML
    private Button summaryButton;

    @FXML
    private Text companyName, dateLabel, employeeName, timeLabel, statusLabel;

    @FXML
    private Button logOutButton, timeInButton, timeOutButton;

    // Column Employee ID
    @FXML
    private TableColumn<EmployeeDailyReport, Date> column1;

    // Column Username
    @FXML
    private TableColumn<EmployeeDailyReport, Date> column2;

    @FXML
    private TableColumn<EmployeeDailyReport, String> column3;

    @FXML
    private TableView<EmployeeDailyReport> tableView;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm : ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MMMM - dd");

    public EmployeeController() {
    }

    public EmployeeController(EmployeeProfile employee) {
        EmployeeController.employee = employee;
    }

    public void setStub(Attendance stub) {
        this.stub = stub;
    }

    public void setEmployee(EmployeeProfile employee) throws RemoteException {

        if (employee.getEmployeeDailyReport() == null) {
            employee.setEmployeeDailyReport(new EmployeeDailyReport(dateFormat.format(date)));
        }

        this.employee = employee;
    }

    @FXML
    void addTimeIn(MouseEvent event) {

        statusLabel.setText("TIMED IN");
        Date date = new Date();
        try {
            date = stub.timeIn(employee.getEmpID());
            timeInButton.setDisable(true);
            timeOutButton.setDisable(false);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        employee.getEmployeeDailyReport().setStatus("Working");
        employee.setStatus("Working");
        employee.getEmployeeDailyReport().setTimeIn(timeFormat.format(date));
    }

    @FXML
    void addTimeOut(MouseEvent event) {

        statusLabel.setText("TIMED OUT");
        Date date = new Date();
        try {
            date = stub.timeOut(employee.getEmpID());
            timeInButton.setDisable(false);
            timeOutButton.setDisable(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        employee.getEmployeeDailyReport().setStatus("On Break");
        employee.setStatus("On Break");
        employee.getEmployeeDailyReport().setTimeOut(timeFormat.format(date));


    }

    @FXML
    void showSummary(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/TreeTableView.fxml"));

            List<EmployeeReport> reports = new ArrayList<>();


            for (int i = 0; i < employee.getEmployeeDailyReport().getListofTimeOuts().size(); i++) {
                String timeIn = employee.getEmployeeDailyReport().getListofTimeIns().get(i);
                String timeOut = employee.getEmployeeDailyReport().getListofTimeOuts().get(i);
                EmployeeReport employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut.split(", ")[1]);
                if (i == 0) {
                    employeeReport.setDate(timeOut.split(", ")[0]);
                }
                reports.add(employeeReport);
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

    @FXML
    void logOut(MouseEvent event) throws IOException {

        logOutButton.getScene().getWindow().hide();

        JSONHandler.setEmployeeStatus(employee.getEmpID(), false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/LoginInterface.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void shutdown() throws RemoteException {
        System.out.println("EXITING...");
        stub.setStatus(employee.getEmpID(), false);
    }

    public Text getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(Text timeLabel) {
        this.timeLabel = timeLabel;
    }

    public Text getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(Text employeeName) {
        this.employeeName = employeeName;
    }

    public Text getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(Text dateLabel) {
        this.dateLabel = dateLabel;
    }

    public Text getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Text companyName) {
        this.companyName = companyName;
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
        try {
            Registry registry = LocateRegistry.getRegistry(2345);
            stub = (Attendance) registry.lookup("sayhi");
        } catch (Exception e) {
            e.printStackTrace();
        }

        employeeName.setText(employee.getFullName());

        date = new Date();
        System.out.println("B  " + date);
        dateLabel.setText(dateFormat.format(date));


        // Timer Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH : mm : ss")));
            }
        };
        timer.start();

        if (employee.getEmployeeDailyReport().getListofTimeIns().size() >
                employee.getEmployeeDailyReport().getListofTimeOuts().size()) {
            timeInButton.setDisable(true);
            timeOutButton.setDisable(false);
        } else {
            timeInButton.setDisable(false);
            timeOutButton.setDisable(true);
        }
    }
}