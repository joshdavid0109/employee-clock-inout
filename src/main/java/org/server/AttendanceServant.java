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
    public String LogIn() throws RemoteException {
        return "Hi there!";
    }

    @Override
    public synchronized void addEmployee(EmployeeProfile emp) throws RemoteException {
        empList.add(emp);
    }

    protected List<EmployeeProfile> getEmpList() {
        return empList;
    }
}
