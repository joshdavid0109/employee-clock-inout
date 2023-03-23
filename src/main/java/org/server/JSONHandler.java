package org.server;

//dapat server lang nakakagamit nitong class na ito

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import org.shared_classes.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONHandler {

    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setDateFormat("MMM dd YYYY, HH:mm:ss")
            .setPrettyPrinting();
    private static final Gson gson = gsonBuilder
            .create();
    static private final String employeesJSONPath = "employees.json";
    static public final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");
    static private final String pendingRegistersList = "registers.json";

    public static int checkIfValidLogIn(String username, String password) {
        try {
            List<EmployeeProfile> employees = getFromFile();
            for (EmployeeProfile employee : employees) {
                if (employee.getUserName().equals(username)) {
                    if (employee.getPassWord().equals(password)) {
                        employee.setLoggedIn(true);
                        System.out.println("EMPLOYEE " + employee.getUserName() + " HAS LOGGED IN");
                        setEmployeeStatus(employee, true);
                        return 0;//login successful:)
                    } else {
                        return 1;// wrong password
                    }
                }
            }
            return 2;//no account exists
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;//?bro how?
    }

    /*public static int checkIfValidLogIn(String username, String password) {
        try {
            List<EmployeeProfile> employees = getFromFile();
            for (EmployeeProfile employee : employees) {
                if (employee.getUserName().equals(username) && employee.getPassWord().equals(password)) {
                    employee.setLoggedIn(true);
                    System.out.println("EMPLOYEE " + employee.getUserName() + " HAS LOGGED IN");
                    setEmployeeStatus(employee, true);
                    return employee;
                }
            }
            throw new CredentialsErrorException("OOPSIES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    //true = logged in, false logged out
    public static void setEmployeeStatus(EmployeeProfile employee, boolean loggedIn) {
        try {
            List<EmployeeProfile> employees = getFromFile();

            for (EmployeeProfile emp : employees) {
                if (emp.getEmpID().equals(employee.getEmpID())) {
                    emp.setLoggedIn(loggedIn);
                    break;
                }
            }

            String json = gson.toJson(employees);
            FileWriter writer = new FileWriter(employeesJSONPath);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkIfValidRegistration(EmployeeProfile employeeProfile) {
        try {
            List<EmployeeProfile> employees = getFromFile();

//            System.out.println(employees);

            for (EmployeeProfile emp : employees) {
                // *|MARCADOR_CURSOR|*
                if (emp.getUserName().equals(employeeProfile.getUserName()) || !checkValidPassword(employeeProfile.getPassWord())) {
//                    System.out.println(emsp);
                    System.out.println("username taken or password invalid or passwords aint the saem lol try again noob");
                } else {
                    System.out.println(emp);
                    System.out.println("Successful login.");
                    registerEmployee(employeeProfile.getFullName(), employeeProfile.getPassWord()); //TODO for some reason nabubura yung employees.json if this runs idk why
                }
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerEmployee(String username, String password) {
        try (FileWriter writer = new FileWriter(pendingRegistersList)) {
            List<EmployeeProfile> pendingRegisters = getPendingRegistersFromFile();
            if (pendingRegisters == null) {
                pendingRegisters = new ArrayList<>();
                pendingRegisters.add(new EmployeeProfile(username, password));
                gson.toJson(pendingRegisters, writer);
            } else {
                pendingRegisters.add(new EmployeeProfile(username, password));
                gson.toJson(pendingRegisters, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkValidPassword(String password) {
        String PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void addTimeIn(String employeeID, Date d) {

        try {
            List<EmployeeProfile> employees = getFromFile();

            for (int i = 0; i < Objects.requireNonNull(employees).size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(employeeID)) {
                    JsonElement jsonElement = gson.toJsonTree(emp);

                    if (emp.getEmployeeDailyReport() == null) {
                        emp.setEmployeeDailyReport(new EmployeeDailyReport(String.valueOf(d.getDate())));
                    }

                    //add timein
                    List<String> timeIs = emp.getEmployeeDailyReport().getListofTimeIns();

                    if (!timeIs.get(0).split(", ")[0].equals(dateFormat.format(d).split(", ")[0])) {
                        List<String> ins = new ArrayList<>(emp.getEmployeeDailyReport().getListofTimeIns());
                        List<String> outs = new ArrayList<>(emp.getEmployeeDailyReport().getListofTimeOuts());

                        emp.setSummaryReport(new SummaryReport(emp.getEmployeeDailyReport().getListofTimeIns().get(0).split(", ")[0]));
                        emp.getSummaryReport().setTimeIns(ins);
                        emp.getSummaryReport().setTimeOuts(outs);

                        emp.getEmployeeDailyReport().getListofTimeIns().clear();
                        emp.getEmployeeDailyReport().getListofTimeOuts().clear();
                        emp.getEmployeeDailyReport().setTimeIn(dateFormat.format(d));
                        employees.add(i, emp);
                        employees.remove(i);
                        break;
                    }

                    emp.getEmployeeDailyReport().setTimeIn(dateFormat.format(d));
                    JsonElement timeIns = gson.toJsonTree(timeIs);

                    //add timein sa json file
                    jsonElement.getAsJsonObject().get("employeeDailyReport").getAsJsonObject().add("listofTimeIns", timeIns);

                    String updatedEmployee = jsonElement.toString();
                    // get sa json as EmployeeProfile object
                    emp = gson.fromJson(updatedEmployee, EmployeeProfile.class);
                    employees.add(i, emp);
                    employees.remove(i);
                    //write to json file
                    break;
                }
            }

            try (FileWriter writer = new FileWriter(employeesJSONPath)) {
                gson.toJson(employees, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND!");
            e.printStackTrace();
        }
    }

    public static void addTimeOut(String employeeID, Date d) {
//        SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");
        try {
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = getFromFile();

            for (int i = 0; i < Objects.requireNonNull(employees).size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(employeeID)) {
                    JsonElement jsonElement = gson.toJsonTree(emp);
                    //add timeout
                    d = new Date();

                    if (emp.getEmployeeDailyReport() == null) {
                        emp.setEmployeeDailyReport(new EmployeeDailyReport(String.valueOf(d.getDate())));
                    }

                    List<String> timeOs = emp.getEmployeeDailyReport().getListofTimeOuts();

                    emp.getEmployeeDailyReport().setTimeOut(dateFormat.format(d));
                    JsonElement timeOuts = gson.toJsonTree(timeOs);

                    //add timeout sa json file
                    jsonElement.getAsJsonObject().get("employeeDailyReport").getAsJsonObject().add("listofTimeOuts", timeOuts);

                    String updatedEmployee = jsonElement.toString();
                    // get sa json as EmployeeProfile object
                    emp = gson.fromJson(updatedEmployee, EmployeeProfile.class);
                    employees.add(i, emp);
                    employees.remove(i);
                    break;
                }
            }
            //write to json file
            try (FileWriter writer = new FileWriter(employeesJSONPath)) {
                gson.toJson(employees, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Date date = new Date();
        EmployeeDetails ed = new EmployeeDetails("as", "asd", 18, "Female");
        EmployeeProfile ep = new EmployeeProfile("asd", "asweq", "asdcasxd");
        ep.setPersonalDetails(ed);
        ep.setEmployeeDailyReport(new EmployeeDailyReport(dateFormat.format(date).split(", ")[0]));
        ep.getEmployeeDailyReport().setTimeIn(dateFormat.format(date));
        addTimeIn(ep.getEmpID(), date);
        addTimeOut(ep.getEmpID(), date);
    }

    public static List<EmployeeProfile> getFromFile() {
        try (Reader reader = Files.newBufferedReader(Paths.get(employeesJSONPath))) {
            List<EmployeeProfile> employeeProfiles = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonParser parser = new JsonParser();
            JsonArray rootoObj = parser.parse(reader).getAsJsonArray();
            EmployeeDailyReport employeeDailyReport;
            for (int i = 0; i < rootoObj.size(); i++) {
                JsonElement element = rootoObj.get(i);
                JsonElement listofTimeouts = element.getAsJsonObject().get("employeeDailyReport");
                employeeProfiles.add(gson.fromJson(element, EmployeeProfile.class));
                employeeDailyReport = gson.fromJson(listofTimeouts, EmployeeDailyReport.class);
                if (employeeDailyReport == null) {
                    Date d = new Date();
                    employeeDailyReport = new EmployeeDailyReport(String.valueOf(d.getDate()));
                }
                employeeProfiles.get(i).setEmployeeDailyReport(employeeDailyReport);
            }
            return employeeProfiles;
//            JsonElement jsonElement = gson.toJsonTree(new EmployeeProfile());
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
        return null;
    }

    static List<EmployeeProfile> getPendingRegistersFromFile() {
        try (Reader reader = Files.newBufferedReader(Paths.get(pendingRegistersList))) {
            Type dataType = new TypeToken<List<EmployeeProfile>>() {
            }.getType();
            List<EmployeeProfile> temp = gson.fromJson(reader, dataType);
            if (temp == null) {
                temp = new ArrayList<>();
                return temp;
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    static void addToFile(List<EmployeeProfile> employeesList) {
        try (FileWriter writer = new FileWriter(employeesJSONPath)) {
            gson.toJson(employeesList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<EmployeeProfile> populateTable() {
        try (Reader reader = Files.newBufferedReader(Paths.get(employeesJSONPath))) {
            List<EmployeeProfile> employeeProfiles = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonParser parser = new JsonParser();
            JsonArray rootoObj = parser.parse(reader).getAsJsonArray();
            EmployeeProfile employeeProfile;
            for (int i = 0; i < rootoObj.size(); i++) {
                JsonElement employee = rootoObj.get(i);
//                JsonElement eID = employee.getAsJsonObject().get("empID");
//                JsonElement dailyReport = employee.getAsJsonObject().get("employeeDailyReport");
                employeeProfile = gson.fromJson(employee, EmployeeProfile.class);
                employeeProfiles.add(employeeProfile);
            }
            return employeeProfiles;
//            JsonElement jsonElement = gson.toJsonTree(new EmployeeProfile());
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * lalagay yung employee sa register.json, para iaccept or reject
     *
     * @param profile The profile to be added to the register.
     */
    public static void appendToRegister(EmployeeProfile profile) {
        List<EmployeeProfile> pendingEmployees = getPendingRegistersFromFile();
        pendingEmployees.add(profile);

        /*System.out.println("A "+profile);
        System.out.println("B "+pendingEmployees);*/

        try (Writer writer = new FileWriter(pendingRegistersList)) {
            gson.toJson(pendingEmployees, writer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static EmployeeProfile getEmployeeProfile(String username) {
        try {
            List<EmployeeProfile> employees = getFromFile();
            for (EmployeeProfile emp : employees) {
                if (emp.getUserName().equals(username))
                    return emp;
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
