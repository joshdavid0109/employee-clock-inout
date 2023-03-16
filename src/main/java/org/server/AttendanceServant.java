package org.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceServant implements Attendance {
    private final List<EmployeeProfile> empList = new ArrayList<>();
    private String nasaanYungJsonList = "src/main/resources/employees.json";

    @Override
    public EmployeeProfile LogIn(String username, String password) throws RemoteException {
        System.out.println("aaaa");
        try (Reader reader = Files.newBufferedReader(Paths.get(nasaanYungJsonList))) {
            Gson gson = new Gson();
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = gson.fromJson(reader, dataType);
            for (EmployeeProfile employee : employees) {
                if (employee.getUserName().equals(username) && employee.getPassWord().equals(password)) {
                    employee.setLoggedIn(true);
                    System.out.println("kasjay");
                    return employee;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EmployeeProfile SignUp() throws RemoteException {
        //TODO
        return null;
    }

    @Override
    public void TimeIn() throws RemoteException {
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
        empList.add(emp);
    }

    protected List<EmployeeProfile> getEmpList() {
        return empList;
    }
}
