package org.client;

import org.server.AttendanceServant;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class Client {

    static EmployeeProfile employee;

    public static void main(String[] args) {
        try {
            Date d = new Date();
            Registry registry = LocateRegistry.getRegistry(2001);
            Attendance stub = (Attendance) registry.lookup("sayhi");
            EmployeeDetails ed = new EmployeeDetails("Damian", "Wayne", 14, "Male");
            EmployeeProfile ep = new EmployeeProfile("ue72ysh", "robin", "batmansidekick");
            ep.setPersonalDetails(ed);
            ep.setTotalDates(d);
            stub.addEmployee(ep);

            String username = "DAsd";
            String password = "asds";

            employee = stub.LogIn(username, password);

            if (employee != null) {
                System.err.println("dapat hindi mo ito nakikita");
            } else {
                System.out.println("dapat nakikita mo ito");
            }

            username = "robin";
            password = "batmansidekick";

            employee = stub.LogIn(username, password);

            if (employee != null) {
                System.out.println("dapat nakikita mo ito");
                System.out.println("u have logged in as "+ employee.getUserName());
            } else {
                System.err.println("dapat hindi mo ito nakikita");
            }


        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
