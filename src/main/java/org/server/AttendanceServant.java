package org.server;

import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceServant implements Attendance {
    private final List<EmployeeProfile> empList = new ArrayList<>();

    @Override
    public EmployeeProfile LogIn() throws RemoteException {
        //TODO
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
