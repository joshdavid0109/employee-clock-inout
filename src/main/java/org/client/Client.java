package org.client;

import org.server.AttendanceServant;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

public class Client {

    static EmployeeProfile employee;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Registry registry = LocateRegistry.getRegistry(2001);
            Attendance stub = (Attendance) registry.lookup("sayhi");

            while (employee == null) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                employee = stub.LogIn(username, password);

                if (employee == null) {
                    System.out.println("Invalid username or password. Please try again.");
                }
            }

            System.out.println("Logged in as " + employee.getUserName());

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
