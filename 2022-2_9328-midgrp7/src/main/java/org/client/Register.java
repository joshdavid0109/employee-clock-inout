package org.client;

import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class Register {
    public static void main(String[] args) {
        try {

            //hardcode lng
            Date d = new Date();
            Registry registry = LocateRegistry.getRegistry(2001);
            Attendance stub = (Attendance) registry.lookup("sayhi");
            EmployeeDetails ed = new EmployeeDetails("Damian", "Wayne", 14, "Male");
            EmployeeProfile ep = new EmployeeProfile("ue72ysh", "robin", "batmansidekick");
            ep.setPersonalDetails(ed);
            ep.setTotalDates(d);
            EmployeeDetails ed1 = new EmployeeDetails("Jason", "Todd", 24, "Male");
            EmployeeProfile ep1 = new EmployeeProfile("ushensga82", "redhood", "edgyboi");
            ep1.setPersonalDetails(ed1);
            ep1.setTotalDates(d);
            EmployeeDetails ed2 = new EmployeeDetails("Stephanie", "Brown", 18, "Female");
            EmployeeProfile ep2 = new EmployeeProfile("amuu291", "spoiler", "spoiler");
            ep2.setPersonalDetails(ed2);
            ep2.setTotalDates(d);
            stub.addEmployee(ep);
            stub.addEmployee(ep1);
            stub.addEmployee(ep2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
