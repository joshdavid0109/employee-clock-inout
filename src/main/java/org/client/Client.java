package org.client;

import org.shared_classes.Attendance;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    static EmployeeProfile employee;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(2001);
            Attendance stub = (Attendance) registry.lookup("sayhi");
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}
