package org.server;

import org.shared_classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface Attendance extends Remote {
    EmployeeProfile logIn(String username, String password) throws RemoteException, CredentialsErrorException;
    void signUp(EmployeeProfile employeeProfile) throws RemoteException;
    Date timeIn(String employeeID) throws RemoteException;
    Date timeOut(String employeeID) throws RemoteException;
    void setStatus(String employeeID, boolean loggedIn) throws RemoteException;
    List<EmployeeReport> getSummary(String empID) throws RemoteException;
    String getCurrentStatus(String employeeID) throws RemoteException;
}
