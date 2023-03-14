package org.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.io.FileWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Server extends AttendanceServant {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd")
            .create();
    private static List<EmployeeProfile> employeesList = new ArrayList<>();

    private static final AttendanceServant ers = new AttendanceServant();

    public static void main(String[] args) {
        try {
            Attendance stub = (Attendance) UnicastRemoteObject.exportObject(ers, 0);
            Registry registry = LocateRegistry.createRegistry(2001);
            registry.rebind("sayhi", stub);

            int choice = 0;

            do {
                menuList();
                choice = Integer.parseInt(scanner.next());
                options(choice);
            } while (choice != 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void menuList() {
        System.out.println("\n--------ADMIN PANEL---------");
        System.out.println("[1] GET LIST OF EMPLOYEES");
        System.out.println("[2] PRINT LIST TO JSON FILE");
        System.out.println("[3] EXIT");
        System.out.print("Enter choice: ");
    }

    private static void options(int choice) {
        switch (choice) {
            case 1 -> {
                employeesList = ers.getEmpList();
                printEmpList();
            }
            case 2 -> {
                addToFile();
                System.out.println("EMPLOYEE LIST ADDED TO .JSON FILE SUCCESSFULLY!");
            }
            case 3 -> {
                System.out.println("Ending server...");
                System.exit(0);
            }
        }
    }

    private static void addToFile() {
        try (FileWriter writer = new FileWriter("src/main/resources/employees.json")) {
            gson.toJson(employeesList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printEmpList() {
        for (EmployeeProfile profile : employeesList) {
            System.out.println("\n" + profile);
        }
    }
}
