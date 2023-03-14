package org.client;

import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class Client {
    public static void main(String[] args) {
        try {
            Date d = new Date();
            Date d1 = new Date();
            Registry registry = LocateRegistry.getRegistry(2001);
            Attendance stub = (Attendance) registry.lookup("sayhi");
            EmployeeDetails ed = new EmployeeDetails("Damian", "Wayne", 14, "Male");
            EmployeeProfile ep = new EmployeeProfile("ue72ysh", "robin", "batmansidekick");
            ep.setPersonalDetails(ed);
            ep.setTotalDates(d);
            stub.addEmployee(ep);
            EmployeeDetails ed1 = new EmployeeDetails("Jason", "Todd", 24, "Male");
            EmployeeProfile ep1 = new EmployeeProfile("ushensga82", "redhood", "edgyboi");
            ep1.setPersonalDetails(ed1);
            ep1.setTotalDates(d1);
            stub.addEmployee(ep1);
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
