package org.server;

//dapat server lang nakakagamit nitong class na ito

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;
import org.shared_classes.GsonDateDeSerializer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONHandler {

    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setDateFormat("MMM dd YYYY, HH:mm:ss")
            .setPrettyPrinting();
    private static final Gson gson = gsonBuilder
            .create();
    static private final String employeesJSONPath = "employees.json";
    static private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");


    public static EmployeeProfile checkIfValidLogIn(String username, String password) {
        try {
            List<EmployeeProfile> employees = getFromFile();
            for (EmployeeProfile employee : employees) {
                if (employee.getUserName().equals(username) && employee.getPassWord().equals(password)) {
                    employee.setLoggedIn(true);
                    System.out.println("EMPLOYEE "+employee.getUserName()+" HAS LOGGED IN");
                    System.out.println("kasjay");
                    setEmployeeStatus(employee, true);
                    return employee;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //awan the log in credentials
        return null;
    }

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

    public static EmployeeProfile checkIfValidRegistration(String username, String password, String verifyPassword) {
        try {
            List<EmployeeProfile> employees = getFromFile();

//            System.out.println(employees);

            for (EmployeeProfile emp : employees) {
                if (emp.getUserName().equals(username) || !checkValidPassword(password) || !password.equals(verifyPassword)) {
//                    System.out.println(emp);
                    System.out.println("username taken or password invalid or passwords aint the saem lol try again noob");
                    return null;
                } else {
                    System.out.println(emp);
                    System.out.println("Successful login.");
//                    addEmployee(employees, username, password); //TODO for some reason nabubura yung employees.json if this runs idk why
                    return new EmployeeProfile(username, password);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void addEmployee(List<EmployeeProfile> employees, String username, String password) {
        try (FileWriter writer = new FileWriter(employeesJSONPath)) {
//            System.out.println(employees);
//            employees.add(new EmployeeProfile(username, password));
//            gson.toJson(employees, writer);
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

    public static void addTimeIn(EmployeeProfile ep, Date d) {
        try {
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = getFromFile();
            for (EmployeeProfile emp :
                    employees) {
                System.out.println(emp);
            }

            for (int i =0; i < employees.size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(ep.getEmpID())) {
                    JsonElement jsonElement= gson.toJsonTree(ep);

                    //add timeout
                    emp.getEmployeeDailyReport().setTimeIn(dateFormat.format(d));

                    List<String> timeIs = emp.getEmployeeDailyReport().getListofTimeIns();
                    System.out.println(timeIs);
//                    if (timeIs.get(timeIs.size()-1).getDay()!= d.getDate())
                    JsonElement timeIns = gson.toJsonTree(timeIs);

                    //add timeout sa json file
                    jsonElement.getAsJsonObject().get("employeeDailyReport").getAsJsonObject().add("listofTimeIns", timeIns);

                    String updatedEmployee = jsonElement.toString();
                    // get sa json as EmployeeProfile object
                    emp  = gson.fromJson(updatedEmployee, EmployeeProfile.class);
                    employees.add(i, emp);
                    employees.remove(i);
                    //write to json file
                    try (FileWriter writer = new FileWriter(employeesJSONPath)) {
                        gson.toJson(employees, writer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }

        } catch (Exception e) {
            System.err.println("FILE NOT FOUND!");
            e.printStackTrace();
        }
    }

    public static void addTimeOut(EmployeeProfile ep, Date d) {
//        SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");
        try {
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = getFromFile();
            for (EmployeeProfile emp :
                    employees) {
                System.out.println(emp);
            }

            for (int i = 0; i < Objects.requireNonNull(employees).size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(ep.getEmpID())) {
                    JsonElement jsonElement= gson.toJsonTree(ep);
                    //add timeout
                    d = new Date();

                    emp.getEmployeeDailyReport().setTimeOut(dateFormat.format(d));

                    List<String> timeOs = ep.getEmployeeDailyReport().getListofTimeOuts();
//                    if (timeOs.get(timeOs.size()-1).getDay()!= d.getDate())
                    JsonElement timeOuts = gson.toJsonTree(timeOs);


                    //add timeout sa json file
                    jsonElement.getAsJsonObject().get("employeeDailyReport").getAsJsonObject().add("listofTimeOuts", timeOuts);

                    String updatedEmployee = jsonElement.toString();
                    // get sa json as EmployeeProfile object
                    emp  = gson.fromJson(updatedEmployee, EmployeeProfile.class);
                    employees.add(i, emp);
                    employees.remove(i);
                    //write to json file
                    try (FileWriter writer = new FileWriter(employeesJSONPath)) {
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

    public static void main(String[] args) {
        Date date = new Date();
        Date date1 = new Date();
        EmployeeDetails ed = new EmployeeDetails("Test", "asd", 14, "Male");
        EmployeeProfile ep = new EmployeeProfile("c123b", "testuser", "testuser");
        ep.setPersonalDetails(ed);
        ep.setEmployeeDailyReport(new EmployeeDailyReport());

        ep.setTotalDates(new EmployeeDailyReport());
        System.out.println(date);
        System.out.println(date1);
        addTimeIn(ep, date1);
        addTimeOut(ep, date);
    }

    static List<EmployeeProfile> getFromFile() {
        try(Reader reader = Files.newBufferedReader(Paths.get(employeesJSONPath))) {
            List<EmployeeProfile> employeeProfiles = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonParser parser = new JsonParser();
            JsonArray rootoObj = parser.parse(reader).getAsJsonArray();
            EmployeeDailyReport employeeDailyReport;
            for (int i = 0; i< rootoObj.size();i++) {
                JsonElement element = rootoObj.get(i);
                JsonElement listofTimeouts = element.getAsJsonObject().get("employeeDailyReport");
                employeeProfiles.add(gson.fromJson(element, EmployeeProfile.class));
                employeeDailyReport = gson.fromJson(listofTimeouts, EmployeeDailyReport.class);
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


    static void addToFile(List<EmployeeProfile> employeesList) {
        try (FileWriter writer = new FileWriter(employeesJSONPath)) {
            gson.toJson(employeesList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
