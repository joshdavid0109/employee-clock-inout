package org.server;

//dapat server lang nakakagamit nitong class na ito

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.EmployeeDailyReport;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JSONHandler {

    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setDateFormat("MMM dd YYYY, HH:mm:ss")
            .setPrettyPrinting();
    private static final Gson gson = gsonBuilder
            .create();
    static private final String employeesJSONPath = "employees.json";

    public static EmployeeProfile checkIfValidLogIn(String username, String password) {
        try (Reader reader = Files.newBufferedReader(Paths.get(employeesJSONPath))) {
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = gson.fromJson(reader, dataType);
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
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = gson.fromJson(new FileReader(employeesJSONPath), dataType);

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

    public static void addTimeOut(EmployeeProfile ep, Date d) {
        SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");
        try(Reader reader = Files.newBufferedReader(Paths.get("employees.json"))) {
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = gson.fromJson(reader, dataType);

            for (int i =0; i < employees.size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(ep.getEmpID())) {
                    JsonElement jsonElement= gson.toJsonTree(ep);

                    //add timeout
                    emp.getEmployeeDailyReport().setTimeOut(d);

                    List<Date> timeOs = emp.getEmployeeDailyReport().getListofTimeOuts();
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

    public static void main(String[] args) {
        Date date = new Date();
        EmployeeDetails ed = new EmployeeDetails("Test", "asd", 14, "Male");
        EmployeeProfile ep = new EmployeeProfile("c123c", "testuser", "testuser");
        ep.setPersonalDetails(ed);
        ep.setEmployeeDailyReport(new EmployeeDailyReport());
        ep.setTotalDates(new EmployeeDailyReport());
        addTimeOut(ep, date);
    }

    static List<EmployeeProfile> getFromFile() {
        try(Reader reader = Files.newBufferedReader(Paths.get("employees.json"))) {
            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            return gson.fromJson(reader, dataType);
//            for (EmployeeProfile temp : employees) {
//                System.out.println("\n" + temp);
//            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
        return null;
    }

    static void addToFile(List<EmployeeProfile> employeesList) {
        try (FileWriter writer = new FileWriter("employees.json")) {
            gson.toJson(employeesList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
