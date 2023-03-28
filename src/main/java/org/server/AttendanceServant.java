package org.server;

import org.shared_classes.*;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendanceServant implements Attendance {
    static Date serverDate = new Date();
    static List<EmployeeProfile> empList = JSONHandler.getEmployeesFromFile();
    private String nasaanYungJsonList = "src/main/resources/employees.json";
    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");


    @Override
    public EmployeeProfile logIn(String username, String password) throws RemoteException {
        EmployeeProfile employeeProfile = null;
        try {
            employeeProfile = JSONHandler.checkIfValidLogIn(username, password);
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
        System.out.println("Date : "  + this.dateFormat.format(date));
        JSONHandler.addTimeIn(employeeId, date);
        return date;
    }

    @Override
    public Date timeOut(String employeeId) throws RemoteException {
        Date date = getDateAndTime();
        JSONHandler.addTimeOut(employeeId, date);
        return date;
    }

    @Override
    public void setStatus(String employeeID, boolean loggedIn) throws RemoteException {
        JSONHandler.setEmployeeStatus(employeeID, loggedIn);
    }

    @Override
    public void setPersonalDetails(String employeeID, EmployeeDetails employeeDetails) throws RemoteException {
//        JSONHandler.setEmployeeDetails(employeeID, employeeDetails);
    }

    @Override
    public List<EmployeeReport> getSummary(String empID) throws RemoteException {
        List<EmployeeProfile> employeeProfiles = JSONHandler.getEmployeesFromFile();
        for(EmployeeProfile employeeProfile : employeeProfiles ) {
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
