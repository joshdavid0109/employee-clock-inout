package org.client;

import com.example.helloworld.LoginInterface;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeProfile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    static EmployeeProfile employee;
    static Scanner scanner = new Scanner(System.in);
    static Attendance stub;

    public static void main(String[] args) {
        new LoginInterface();
    }
}
