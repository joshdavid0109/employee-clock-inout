package org.client.gui.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.server.gui.controllers.EmployeeTable;
import org.shared_classes.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @FXML
    private ImageView imageViewIcon;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH : mm : ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");

    public static int port;
    public static String remoteReferenceName;
    public static String ip_address;

    public EmployeeController() {}

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
    void addTimeIn(MouseEvent event) throws RemoteException {
        Image timeInImage = new Image("TYPING_ICON.gif");

        imageViewIcon.setImage(timeInImage);

        Date date = new Date();
        try {
            employee.setStatus("Working");
            date = stub.timeIn(employee.getEmpID());
            String s = stub.getCurrentStatus(employee.getEmpID());
            statusLabel.setText(s);
            timeInButton.setDisable(true);
            timeOutButton.setDisable(false);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        employee.getEmployeeDailyReport().setStatus("Working");

        employee.getEmployeeDailyReport().setTimeIn(dateFormat.format(date));
    }

    @FXML
    void addTimeOut(MouseEvent event) throws RemoteException {
        Image timOutImage = new Image("TIME_OUT.gif");


        imageViewIcon.setImage(timOutImage);
        Date date = new Date();
        try {
            employee.setStatus("On Break");
            date = stub.timeOut(employee.getEmpID());
            String s = stub.getCurrentStatus(employee.getEmpID());
            statusLabel.setText(s);
            timeInButton.setDisable(false);
            timeOutButton.setDisable(true);
        } catch (RemoteException e) {

        }

        employee.getEmployeeDailyReport().setStatus("On Break");

        employee.getEmployeeDailyReport().setTimeOut(dateFormat.format(date));

    }

    @FXML
    void showSummary(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/TreeTableView.fxml"));

            List<EmployeeReport> reports = stub.getSummary(employee.getEmpID());
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
        stub.setStatus(employee.getEmpID(), false);
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
        stub.setStatus(employee.getEmpID(), false);
        System.out.println("EXITING...");
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
            /**
             * CHANGE HOST
             */
//            Registry registry = LocateRegistry.getRegistry("192.168.254.101",2345);
            System.out.println(ip_address);
            System.out.println(port);
            Registry registry = LocateRegistry.getRegistry(ip_address, port);
            stub = (Attendance) registry.lookup(remoteReferenceName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String s =stub.getCurrentStatus(employee.getEmpID());
            if (!s.equals("")) {
                statusLabel.setText(s);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        employeeName.setText(employee.getFullName());

        date = new Date();
        dateLabel.setText(dateFormat.format(date).split(", ")[0]);


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