package org.shared_classes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface Attendance extends Remote {
    EmployeeProfile LogIn(String username, String password) throws RemoteException;
    EmployeeProfile SignUp(String username, String password, String verify) throws RemoteException;
    void TimeIn(EmployeeProfile employeeProfile) throws RemoteException;
    void TimeOut(EmployeeProfile employeeProfile) throws RemoteException;
    void getSummary() throws RemoteException;
    void getCurrentStatus() throws RemoteException;
    void addEmployee(EmployeeProfile emp) throws RemoteException;
    Date getDateAndTime()throws RemoteException;
}
