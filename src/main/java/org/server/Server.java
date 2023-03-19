package org.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Server extends AttendanceServant {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setDateFormat("MMM dd YYYY, HH:mm:ss")
            .setPrettyPrinting();
    private static final Gson gson = gsonBuilder
            .create();



    private static final EmployeeProfile employeeProfile = new EmployeeProfile();
    private static final AttendanceServant ers = new AttendanceServant();
    private static final List<EmployeeProfile> employeesList = ers.getEmpList(); // this updates in real time

    public static void main(String[] args) {
        try {
            Attendance stub = (Attendance) UnicastRemoteObject.exportObject(ers, 0);
            Registry registry = LocateRegistry.createRegistry(2345);
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
        System.out.println("[1] GET LIST OF EMPLOYEES FROM JSON FILE");
        System.out.println("[2] ADD EMPLOYEES TO JSON FILE");
        System.out.println("[3] ADD TIMEOUT SA EMPLOYEE");
        System.out.println("[4] EXIT");
        System.out.print("Enter choice: ");
    }

    private static void options(int choice) throws IOException, ParseException {
        switch (choice) {
            case 1 -> {
                getFromFile();
                System.out.println("EMPLOYEE LIST READ FROM .JSON FILE SUCCESSFULLY");
            }
            case 2 -> {
                addToFile();
                System.out.println("EMPLOYEE LIST ADDED TO .JSON FILE SUCCESSFULLY!");
            }
            case 3 -> {
                addtimeOut();
            }
            case 4 -> {
                System.out.println("Ending server...");
                System.exit(0);
            }
        }
    }


    /**
     * Add sa json file ng values
     * @throws IOException
     * @throws ParseException
     */
    private static void addtimeOut() throws IOException, ParseException {

         SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");
        Date d = new Date();

        // Sample existing employee laang
        EmployeeDetails ed = new EmployeeDetails("Test", "asd", 14, "Male");
        EmployeeProfile ep = new EmployeeProfile("c123c", "testuser", "testuser");
        ep.setPersonalDetails(ed);
            ep.setEmployeeDailyReport(new EmployeeDailyReport());
            ep.setTotalDates(new EmployeeDailyReport());
            ep.getEmployeeDailyReport().setTimeIn(d);

        try(Reader reader = Files.newBufferedReader(Paths.get("employees.json"))) {
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = gson.fromJson(reader, dataType);

            for (int i =0; i < employees.size();i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(ep.getEmpID())) {
                    JsonElement jsonElement= gson.toJsonTree(ep);

                    //add timeout sa json file
                    jsonElement.getAsJsonObject().get("employeeDailyReport").getAsJsonObject().addProperty("timeOut", format1.format(d));

                    String updatedEmployee = jsonElement.toString();
                    // get sa json as EmployeeProfile object
                    emp  = gson.fromJson(updatedEmployee, EmployeeProfile.class);
                    employees.remove(i);
                    employees.add(i, emp);

                    //write to json file
                    try (FileWriter writer = new FileWriter("employees.json")) {
                        gson.toJson(employees, writer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }


        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }

    }

    private static void addToFile() {
        try (FileWriter writer = new FileWriter("employees.json")) {
            gson.toJson(employeesList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getFromFile() {
        try(Reader reader = Files.newBufferedReader(Paths.get("employees.json"))) {
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = gson.fromJson(reader, dataType);
            for (EmployeeProfile temp : employees) {
                System.out.println("\n" + temp);
            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
    }

}
