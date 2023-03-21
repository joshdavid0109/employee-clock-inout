package org.shared_classes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface Attendance extends Remote {
    EmployeeProfile logIn(String username, String password) throws RemoteException;
    void signUp(EmployeeProfile employeeProfile) throws RemoteException;
    Date timeIn(String EmployeeID) throws RemoteException;
    Date timeOut(String EmployeeID) throws RemoteException;
    void getSummary() throws RemoteException;
    void getCurrentStatus() throws RemoteException;
    void addEmployee(EmployeeProfile emp) throws RemoteException;
}
