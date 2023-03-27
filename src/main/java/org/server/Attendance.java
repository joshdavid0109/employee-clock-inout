package org.server;

import org.shared_classes.CredentialsErrorException;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface Attendance extends Remote {
    EmployeeProfile logIn(String username, String password) throws RemoteException, CredentialsErrorException;
    void signUp(EmployeeProfile employeeProfile) throws RemoteException;
    Date timeIn(String EmployeeID) throws RemoteException;
    Date timeOut(String EmployeeID) throws RemoteException;
    void setStatus(String EmployeeID, boolean loggedIn) throws RemoteException;
    EmployeeDailyReport getSummary() throws RemoteException;
    byte getCurrentStatus(String employeeID) throws RemoteException;
}
