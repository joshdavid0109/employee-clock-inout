package org.shared_classes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface Attendance extends Remote {
    Object logIn(String username, String password) throws RemoteException, CredentialsErrorException;
    void signUp(EmployeeProfile employeeProfile) throws RemoteException;
    Date timeIn(String EmployeeID) throws RemoteException;
    Date timeOut(String EmployeeID) throws RemoteException;
    void setStatus(String EmployeeID, boolean loggedIn) throws RemoteException;
    void getSummary() throws RemoteException;
    void getCurrentStatus() throws RemoteException;
}
