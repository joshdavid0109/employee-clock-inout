package org.server;

import controller.EmployeeController;
import controller.ServerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.shared_classes.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AttendanceServant implements Attendance {
    static Date serverDate = new Date();
    static List<EmployeeProfile> empList = JSONHandler.getEmployeesFromFile();
    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");


    @Override
    public EmployeeProfile logIn(String username, String password) throws RemoteException {
        EmployeeProfile employeeProfile = null;
        try {
            employeeProfile = JSONHandler.checkIfValidLogIn(username, password);


            if (!dateFormat.format(AttendanceServant.serverDate).equals(employeeProfile.getEmployeeDailyReport().getDate())) {
                JSONHandler.setDefaultValues(employeeProfile.getEmpID());
            }

        } catch (UserCurrentlyLoggedInException | CredentialsErrorException | EmptyFieldsException |
                 UserNotExistingException e) {
            if (e instanceof UserCurrentlyLoggedInException) {
                throw new UserCurrentlyLoggedInException();
            } else if (e instanceof CredentialsErrorException) {
                throw new CredentialsErrorException();
            } else if (e instanceof EmptyFieldsException){
                throw new EmptyFieldsException();
            } else {
                throw new UserNotExistingException();
            }
        }
        return employeeProfile;
    }


    @Override
    public void signUp(EmployeeProfile employeeProfile) throws RemoteException {
        JSONHandler.appendToRegister(employeeProfile);
    }

    @Override
    public Date timeIn(String employeeId) throws RemoteException {
        Date date = getDateAndTime();
        JSONHandler.addTimeIn(employeeId, date);

        List<EmployeeProfile> list = JSONHandler.populateTable();
//        EmployeeProfile employeeProfile = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);

        ServerController serverController = null;
        try {
            serverController = new ServerController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TableView<EmployeeProfile> tv = new TableView<>();
        tv.setItems(tableData);
        serverController.setTableView(tv);
        return date;
    }

    @Override
    public Date timeOut(String employeeId) throws RemoteException {
        Date date = getDateAndTime();
        JSONHandler.addTimeOut(employeeId, date);

        List<EmployeeProfile> list = JSONHandler.populateTable();
//        EmployeeProfile employeeProfile = JSONHandler.populateTable();
        ObservableList<EmployeeProfile> tableData = FXCollections.observableList(list);

        ServerController serverController = null;
        try {
            serverController = new ServerController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TableView<EmployeeProfile> tv = new TableView<>();
        tv.setItems(tableData);
        serverController.setTableView(tv);
        return date;
    }

    @Override
    public void setStatus(String employeeID, boolean loggedIn) throws RemoteException {
        JSONHandler.setEmployeeStatus(employeeID, loggedIn);
    }


    @Override
    public List<EmployeeReport> getSummary(String empID) throws RemoteException {
        List<EmployeeProfile> employeeProfiles = JSONHandler.getEmployeesFromFile();
        for(EmployeeProfile employeeProfile : Objects.requireNonNull(employeeProfiles)) {
            if (employeeProfile.getEmpID().equals(empID)) {
                List<EmployeeReport> employeeReports =  JSONHandler.getSummaryForClient(employeeProfile);
                return employeeReports;
            }
        }
        return null;
    }

    /**
     * this method returns a byte value that corresponds to a status

     * @param employeeID employeeID
     * @return byte value that corresponds to a status
     */
    @Override
    public String getCurrentStatus(String employeeID) throws RemoteException {
        return JSONHandler.getCurrentStatus(employeeID);
    }

    public synchronized void addEmployee(EmployeeProfile emp) throws RemoteException {
        boolean employeeExists = false;
        for (EmployeeProfile eP :
                empList) {
            if (eP.getEmpID().equals(emp.getEmpID())) {
                employeeExists = true;
                break;
            }
        }
        if (!employeeExists)
            empList.add(emp);
    }


    public Date getDateAndTime() throws RemoteException {
        serverDate = new Date();
        System.out.println("A "+serverDate);
        return serverDate;
    }

    protected List<EmployeeProfile> getEmpList() {
        return empList;
    }

}
