package org.server;

import org.shared_classes.Attendance;
import org.shared_classes.CredentialsErrorException;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class AttendanceServant implements Attendance {
    static Date serverDate = new Date();
    static List<EmployeeProfile> empList = JSONHandler.getFromFile();
    private String nasaanYungJsonList = "src/main/resources/employees.json";
    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
    

    @Override
    public int logIn(String username, String password) throws RemoteException, CredentialsErrorException {
        return JSONHandler.checkIfValidLogIn(username, password);
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

//        System.out.println(employee.getFullName()+" HAS TIMED OUT");

        Date date = getDateAndTime();
        System.out.println("Date : "  + this.dateFormat.format(date));
//        for (EmployeeProfile employeeProfile : empList) {
//            if (employeeProfile.getEmpID().equals(employee.getEmpID())) {
//                EmployeeDailyReport dailyReport = new EmployeeDailyReport();
//                dailyReport.setTimeOut(date);
//                employeeProfile.setEmployeeDailyReport(dailyReport);
//            }
//        }
//        ListIterator<EmployeeProfile> iterator = empList.listIterator();
//        while (iterator.hasNext()) {
//            EmployeeProfile temp = iterator.next();
//            if (Objects.equals(temp.getEmpID(), employee.getEmpID())) {
//                temp.getEmployeeDailyReport().setTimeOut(date);
//                iterator.set(temp);
//            }
//        }

        JSONHandler.addTimeOut(employeeId, date);
        return date;
    }

    @Override
    public void getSummary() throws RemoteException {

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

    @Override
    public EmployeeProfile getEmployeeProfileFromUserName(String username) throws RemoteException {
        return JSONHandler.getEmployeeProfile(username);
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
