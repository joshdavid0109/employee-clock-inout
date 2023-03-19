package org.shared_classes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Attendance extends Remote {
    EmployeeProfile LogIn(String username, String password) throws RemoteException;
    EmployeeProfile SignUp() throws RemoteException;
    void TimeIn(EmployeeProfile employeeProfile) throws RemoteException;
    void TimeOut() throws RemoteException;
    void getSummary() throws RemoteException;
    void getCurrentStatus() throws RemoteException;
    void addEmployee(EmployeeProfile emp) throws RemoteException;

}
