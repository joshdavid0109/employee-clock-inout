package controller;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;
import org.shared_classes.EmployeeReport;
import org.shared_classes.SummaryReport;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class EmployeeTable implements Initializable {

    @FXML
    public TreeTableColumn<EmployeeReport, String> dateTTColumn;
    @FXML
    public TreeTableColumn<EmployeeReport, String> timeInTTColumn;
    @FXML
    public TreeTableColumn<EmployeeReport, String> timeOutTTColumn;

    @FXML
    public TreeTableView<EmployeeReport> treeTableView;

    static List<EmployeeReport> employeeDailyReport;

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
        TreeItem<EmployeeReport> root = new TreeItem<>(new EmployeeReport("Time In", "Time out",
                employeeDailyReport.get(0).getDate()));

        for (EmployeeReport e:
             employeeDailyReport) {
            e.setDate("");
            TreeItem<EmployeeReport> employeeReportTreeItem = new TreeItem<>(e);
            root.getChildren().add(employeeReportTreeItem);
        }

        System.out.println("test commit");

        dateTTColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<EmployeeReport, String> param) -> new SimpleStringProperty(param.getValue().getValue().getDate()));
        timeInTTColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<EmployeeReport, String> param) -> new SimpleStringProperty(param.getValue().getValue().getTimeIn()));

        timeOutTTColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<EmployeeReport, String> param) -> new SimpleStringProperty(param.getValue().getValue().getTimeOut()));
        treeTableView.setRoot(root);
    }


}
