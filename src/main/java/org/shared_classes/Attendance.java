package org.shared_classes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Attendance extends Remote {
    String LogIn() throws RemoteException;
    void addEmployee(EmployeeProfile emp) throws RemoteException;

}
