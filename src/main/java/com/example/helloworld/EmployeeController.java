package com.example.helloworld;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {



    private Attendance stub;
    private EmployeeProfile employee;

    @FXML
    private Button summaryButton;

    @FXML
    private Text companyName;

    @FXML
    private Text dateLabel;

    @FXML
    private Text employeeName;

    @FXML
    private Button logOutButton;

    @FXML
    private Button timeInButton;

    @FXML
    private Text timeLabel;

    @FXML
    private Button timeOutButton;

    // Column Employee ID
    @FXML
    private TableColumn<EmployeeDailyReport, Date> column1;

    // Column Username
    @FXML
    private TableColumn<EmployeeDailyReport, Date> column2;

    @FXML
    private TableColumn<EmployeeDailyReport, String> column3;

    @FXML
    private TableColumn<?, ?> column4;

    @FXML
    private TableView<EmployeeDailyReport> tableView;

    @FXML
    private Text statusLabel;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MMMM - dd");

    public EmployeeController(){

    }

    public EmployeeController(EmployeeProfile employee){
        this.employee = employee;
    }

    public void setEmployee(EmployeeProfile employee) {
        this.employee = employee;
    }

    @FXML
    void addTimeIn(MouseEvent event) {
        statusLabel.setText("TIMED IN");
        Date date = new Date();
        ObservableList<EmployeeDailyReport> tableData = tableView.getItems();
            EmployeeDailyReport employeeDailyReport = new EmployeeDailyReport();
            employeeDailyReport.setStatus("Working");
            employeeDailyReport.setTimeIn(date);

            tableData.add(employeeDailyReport);

            tableView.setItems(tableData);
            tableView.refresh();
    }

    @FXML
    void addTimeOut(MouseEvent event) {
        statusLabel.setText("TIMED OUT");
        Date date = new Date();
        ObservableList<EmployeeDailyReport> tableData = tableView.getItems();
        EmployeeDailyReport employeeDailyReport = new EmployeeDailyReport();
        employeeDailyReport.setStatus("Break");
        employeeDailyReport.setTimeOut(date);
        tableData.add(employeeDailyReport);
        tableView.setItems(tableData);
        tableView.refresh();
    }

    @FXML
    void showSummary(MouseEvent event) {

    }

    @FXML
    void logOut(MouseEvent event) {

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

        // Timer Animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        };
        timer.start();

        // Sample Employee
        Date d = new Date();
        EmployeeDetails ed = new EmployeeDetails("Test", "asd", 14, "Male");
        EmployeeProfile ep = new EmployeeProfile("c123c", "testuser", "testuser");
        ep.setPersonalDetails(ed);
        /*ep.setEmployeeDailyReport(new EmployeeDailyReport(d));
        ep.setTotalDates(new EmployeeDailyReport(d));*/

        Date date = new Date();
        employeeName.setText(ep.getFullName());
        dateLabel.setText(dateFormat.format(date));

        column1.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, Date>("timeIn"));
        column2.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, Date>("timeOut"));
        column3.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("status"));
    }

}
