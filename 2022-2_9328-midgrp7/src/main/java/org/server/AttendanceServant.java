package org.server;

import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceServant implements Attendance {
    private List<EmployeeProfile> empList = new ArrayList<>();
    private String nasaanYungJsonList = "src/main/resources/employees.json";
    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
    

    @Override
    public EmployeeProfile LogIn(String username, String password) throws RemoteException {
        EmployeeProfile a = JSONHandler.checkIfValidLogIn(username, password);

        //Hardcode muna in the meantime na hindi
        //pa mahanap yung json file

        a = new EmployeeProfile("69696", "Darren", "1234");

        return a;
    }

    @Override
    public EmployeeProfile SignUp() throws RemoteException {
        //TODO
        return null;
    }

    @Override
    public void TimeIn(EmployeeProfile employee) throws RemoteException {
        Date date = new Date();
        System.out.println("Date : "  + this.dateFormat.format(date));
        for (EmployeeProfile employeeProfile : empList) {
            if (employeeProfile.getEmpID().equals(employee.getEmpID())) {
                EmployeeDailyReport dailyReport = new EmployeeDailyReport();
                dailyReport.setTimeIn(date);
                employeeProfile.setEmployeeDailyReport(dailyReport);
            }
        }
        //TODO
    }

    @Override
    public void TimeOut() throws RemoteException {
        //TODO
    }

    @Override
    public void getSummary() throws RemoteException {
        //TODO
    }

    @Override
    public void getCurrentStatus() throws RemoteException {
        //TODO
    }

    @Override
    public synchronized void addEmployee(EmployeeProfile emp) throws RemoteException {
        System.out.println("User \"" + emp.getUserName() + "\" has logged in!");
        boolean employeeExists = false;
        for (EmployeeProfile eP :
                empList) {
            if (eP.getEmpID().equals(emp.getEmpID())) {
                employeeExists = true;
            }
        }
        if (!employeeExists)
            empList.add(emp);
    }

    protected List<EmployeeProfile> getEmpList() {
        return empList;
    }
}
