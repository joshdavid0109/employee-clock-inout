package com.example.helloworld;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class EmployeeTable implements Initializable {

    @FXML
    private TableColumn<EmployeeDailyReport, String> columnStatus = new TableColumn<>();

    @FXML
    private TableColumn<EmployeeDailyReport, String> columnTI = new TableColumn<>();

    @FXML
    private TableColumn<EmployeeDailyReport, String> columnTO = new TableColumn<>();

    @FXML
    private TableView<EmployeeDailyReport> tableView = new TableView<>();

    private EmployeeDailyReport employeeDailyReport;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date date = new Date();

    }

    public EmployeeDailyReport getEmployeeDailyReport() {
        return employeeDailyReport;
    }

    public void setEmployeeDailyReport(EmployeeDailyReport employeeDailyReport) {
        this.employeeDailyReport = employeeDailyReport;

        ObservableList<EmployeeDailyReport> tableData = tableView.getItems();
        tableData.add(employeeDailyReport);

       /* for (int i = 0; i < employeeDailyReport.getListofTimeOuts().size(); i++) {
            int finalI = i;
            columnTI.setText(employeeDailyReport.getListofTimeIns().get(i));
            columnTI.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue()[finalI]));
            columnTO.setText(employeeDailyReport.getListofTimeOuts().get(i));
            columnTO.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue()[finalI]));
            columnStatus.setCellValueFactory(new PropertyValueFactory<EmployeeDailyReport, String>("status"));
            tableView.getColumns().add(columnTI);
            tableView.getColumns().add(columnTO);
            tableView.getColumns().add(columnStatus);
        }*/

        columnTI.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeDailyReport, String> p ) -> {
            List<String> timeins = p.getValue().getListofTimeIns();
            String val = String.join(", ", timeins);
            return new ReadOnlyStringWrapper(val);
        });

        columnTO.setCellValueFactory((TableColumn.CellDataFeatures<EmployeeDailyReport, String> p ) -> {
            List<String> timouts = p.getValue().getListofTimeOuts();
            String val = String.join(", ", timouts);
            return new ReadOnlyStringWrapper(val);
        });

        tableView.setItems(tableData);
        tableView.refresh();
    }
}
