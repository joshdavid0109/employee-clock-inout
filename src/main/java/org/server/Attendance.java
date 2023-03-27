package org.server;

import org.shared_classes.CredentialsErrorException;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface Attendance extends Remote {
    EmployeeProfile logIn(String username, String password) throws RemoteException, CredentialsErrorException;
    void signUp(EmployeeProfile employeeProfile) throws RemoteException;
    Date timeIn(String employeeID) throws RemoteException;
    Date timeOut(String employeeID) throws RemoteException;
    void setStatus(String employeeID, boolean loggedIn) throws RemoteException;
    void setPersonalDetails(String employeeID, EmployeeDetails employeeDetails) throws RemoteException;
    EmployeeDailyReport getSummary() throws RemoteException;
    byte getCurrentStatus(String employeeID) throws RemoteException;
}
