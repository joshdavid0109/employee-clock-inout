package org.client;

import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Register {
    public static void main(String[] args) {
        try {

            //hardcode lng
            Date d = new Date();
            Registry registry = LocateRegistry.getRegistry(2001);
            Attendance stub = (Attendance) registry.lookup("sayhi");
            EmployeeDetails ed = new EmployeeDetails("Test", "asd", 14, "Male");
            EmployeeProfile ep = new EmployeeProfile("c123c", "testuser", "testuser");
            ep.setPersonalDetails(ed);
            ep.setEmployeeDailyReport(new EmployeeDailyReport(d));
            ep.setTotalDates(new EmployeeDailyReport(d));


            EmployeeDetails ed1 = new EmployeeDetails("Jason", "Todd", 24, "Male");
            EmployeeProfile ep1 = new EmployeeProfile("ushensga82", "redhood", "edgyboi");
            ep1.setPersonalDetails(ed1);

            EmployeeDetails ed2 = new EmployeeDetails("Stephanie", "Brown", 18, "Female");
            EmployeeProfile ep2 = new EmployeeProfile("amuu291", "spoiler", "spoiler");
            ep2.setPersonalDetails(ed2);
            ep2.setEmployeeDailyReport(new EmployeeDailyReport(d));

            EmployeeDetails ed3 = new EmployeeDetails("as", "asd", 18, "Female");
            EmployeeProfile ep3 = new EmployeeProfile("asd", "asweq", "asdcasxd");
            ep3.setPersonalDetails(ed3);
            ep3.setEmployeeDailyReport(new EmployeeDailyReport(d));
            stub.addEmployee(ep);
            stub.addEmployee(ep1);
            stub.addEmployee(ep2);
            stub.addEmployee(ep3);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
