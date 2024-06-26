package org.server.resources;

//dapat server lang nakakagamit nitong class na ito

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONHandler<TimeIn> {

    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setDateFormat("MMM dd YYYY, HH:mm:ss")
            .setPrettyPrinting();
    private static final Gson gson = gsonBuilder
            .create();


    static private final File employeesJSONPath = new File("src/main/java/org/server/resources/employees.json");
    static private final File summaryReportsFile = new File("src/main/java/org/server/resources/summaryReports.json");
    static private final File pendingRegistersList = new File("src/main/java/org/server/resources/registers.json");

    static public final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");

    public static EmployeeProfile checkIfValidLogIn(String username, String password) {
        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();

            if (username.equals("") || password.equals("")) {
                throw new EmptyFieldsException();
            }
            for (EmployeeProfile employee : employees) {
                if (employee.getUserName().equals(username)) {
                    if(employee.getPassWord().equals(password)){
                        if (!employee.isLoggedIn()) {
                            employee.setLoggedIn(true);
                            setEmployeeStatus(employee.getEmpID(), true);
                            return employee;
                        } else {
                            throw new UserCurrentlyLoggedInException();
                        }
                    }
                    throw new CredentialsErrorException();
                } else if (employees.get(employees.size()-1).equals(employee)) {
                    throw new UserNotExistingException();
                }
            }
            throw new CredentialsErrorException();
        } catch (UserCurrentlyLoggedInException | CredentialsErrorException  | EmptyFieldsException |
                UserNotExistingException e) {
            if (e instanceof UserCurrentlyLoggedInException) {
                throw new UserCurrentlyLoggedInException();
            } else if (e instanceof CredentialsErrorException) {
                throw new CredentialsErrorException();
            } else if (e instanceof EmptyFieldsException){
                throw new EmptyFieldsException();
            } else {
                throw new UserNotExistingException();
            }
        }
    }

    public static List<EmployeeReport> getSummaryForClient(EmployeeProfile employee) {
        List<EmployeeReport> reports = new ArrayList<>();

        if (employee.getEmployeeDailyReport().getListofTimeOuts().size() ==
                employee.getEmployeeDailyReport().getListofTimeIns().size()) {
            for (int i = 0; i < employee.getEmployeeDailyReport().getListofTimeOuts().size(); i++) {

                String timeIn = employee.getEmployeeDailyReport().getListofTimeIns().get(i);
                String timeOut = employee.getEmployeeDailyReport().getListofTimeOuts().get(i);
                EmployeeReport employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut.split(", ")[1]);
                employeeReport.setDate(timeOut.split(", ")[0]);

                reports.add(employeeReport);
            }
        } else if (employee.getEmployeeDailyReport().getListofTimeOuts().size() <
                employee.getEmployeeDailyReport().getListofTimeIns().size()) {
            for (int i = 0; i < employee.getEmployeeDailyReport().getListofTimeIns().size(); i++) {
                String timeIn = employee.getEmployeeDailyReport().getListofTimeIns().get(i);
                String timeOut;
                EmployeeReport employeeReport = null;
                if (employee.getEmployeeDailyReport().getListofTimeIns().size() - 1 == i){
                    timeOut = "";
                    employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut);
                }else {
                    timeOut = employee.getEmployeeDailyReport().getListofTimeOuts().get(i);
                    employeeReport = new EmployeeReport(timeIn.split(", ")[1], timeOut.split(", ")[1]);
                }
                    employeeReport.setDate(timeOut.split(", ")[0]);
                reports.add(employeeReport);
            }
        }
        return reports;
    }


    //true = logged in, false logged out
    public static void setEmployeeStatus(String employeeID, boolean loggedIn) {
        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();
            for (EmployeeProfile emp : employees) {
                if (emp.getEmpID().equals(employeeID)) {
                    LocalTime time = LocalTime.now();
                    System.out.println("["+time.format(DateTimeFormatter.ofPattern("HH:mm"))+"] Employee ID: " + employeeID + " has logged " + (loggedIn ? "in." : "out."));
                    emp.setLoggedIn(loggedIn);
                    break;
                }
            }
            FileWriter writer = new FileWriter(employeesJSONPath);
            String json = gson.toJson(employees);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //true = logged in, false logged out
    public static void setAllEmployeesOffline() {
        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();
            boolean loggedIn = false;
            for (EmployeeProfile emp : employees) {
                    emp.setLoggedIn(loggedIn);
            }
            FileWriter writer = new FileWriter(employeesJSONPath);
            String json = gson.toJson(employees);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setDefaultValues(String employeeID) {
        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();

            for (int i = 0; i < employees.size(); i++) {
                EmployeeProfile emp =  employees.get(i);
                if (emp.getEmpID().equals(employeeID)) {
                    if (emp.getEmployeeDailyReport().getDate()!= null) {
                        if (!emp.getEmployeeDailyReport().getDate().equals(AttendanceServant.serverDate.toString()))
                            emp.setStatus("");
                    }

                    //add timein
                    List<String> timeIs = emp.getEmployeeDailyReport().getListofTimeIns();
                    List<SummaryReport> summaryReports = getSummaryReportsFromFile();

                    if (timeIs.size() != 0) {
                        if (!timeIs.get(0).split(", ")[0].equals(dateFormat.format(AttendanceServant.serverDate).split(", ")[0])) {
                            List<String> ins = new ArrayList<>(emp.getEmployeeDailyReport().getListofTimeIns());
                            List<String> outs = new ArrayList<>(emp.getEmployeeDailyReport().getListofTimeOuts());


                            if (outs.size() < ins.size()) {
                                Calendar calendar = new GregorianCalendar();
                                calendar.getTime();
                                for (int j = outs.size(); j < ins.size(); j++) {
                                    calendar.setTime(dateFormat.parse(ins.get(outs.size())));
                                    outs.add(ins.get(outs.size()).split(", ")[0] + ", 17:30:00");
                                }
                            }

                            SummaryReport summaryReport = new SummaryReport(emp.getEmployeeDailyReport().getListofTimeIns().get(0).split(", ")[0]);
                            summaryReport.setTimeIns(ins);
                            summaryReport.setTimeOuts(outs);
                            summaryReport.setEmpID(emp.getEmpID());
                            summaryReports.add(summaryReport);

                            emp.getEmployeeDailyReport().getListofTimeIns().clear();
                            emp.getEmployeeDailyReport().getListofTimeOuts().clear();
                            emp.getEmployeeDailyReport().setTimeIn(null);
                            emp.getEmployeeDailyReport().setTimeOut(null);
                            emp.getEmployeeDailyReport().setDate(null);
                            employees.add(i, emp);
                            employees.remove(i);

                            addSummaryToFile(summaryReports);
                            break;
                        }
                    }
                    break;
                }
            }
            FileWriter writer = new FileWriter(employeesJSONPath);
            String json = gson.toJson(employees);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
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
            List<EmployeeProfile> employees = getEmployeesFromFile();

            for (int i = 0; i < employees.size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(employeeID)) {
                    JsonElement jsonElement = gson.toJsonTree(emp);

                    if (emp.getEmployeeDailyReport() == null) {
                        emp.setEmployeeDailyReport(new EmployeeDailyReport(String.valueOf(d.getDate())));
                    }

                    //add timein
                    List<String> timeIs = emp.getEmployeeDailyReport().getListofTimeIns();

                    emp.getEmployeeDailyReport().setTimeIn(dateFormat.format(d));
                    emp.status = "Working";
                    emp.getEmployeeDailyReport().setDate(dateFormat.format(d).split(", ")[0]);
                    JsonElement timeIns = gson.toJsonTree(timeIs);

                    //add timein sa json file

                    LocalTime time = LocalTime.now();
                    System.out.println("["+time.format(DateTimeFormatter.ofPattern("HH:mm"))+"] Employee ID: " + employeeID + " has timed in.");

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
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND!");
            System.out.println(e.getMessage());
        }
    }

    public static List<SummaryReport> getSummaryReportsFromFile() {
        try (Reader reader = Files.newBufferedReader(summaryReportsFile.toPath())) {
            List<SummaryReport> summaryReports = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonParser parser = new JsonParser();
            JsonArray rootoObj = JsonParser.parseReader(reader).getAsJsonArray();
            SummaryReport summaryReport = null;
            for (int i = 0; i < rootoObj.size(); i++) {
                JsonElement element = rootoObj.get(i);
                summaryReports.add(gson.fromJson(element, SummaryReport.class));
            }
            return summaryReports;
//            JsonElement jsonElement = gson.toJsonTree(new EmployeeProfile());
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void addSummaryToFile(List<SummaryReport> summaryReports) {
        try (FileWriter writer = new FileWriter(summaryReportsFile)) {
            gson.toJson(summaryReports, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addTimeOut(String employeeID, Date d) {
        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();

            for (int i = 0; i < employees.size(); i++) {
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
                    emp.status = "On Break";
                    JsonElement timeOuts = gson.toJsonTree(timeOs);

                    //add timeout sa json file
                    LocalTime time = LocalTime.now();
                    System.out.println("["+time.format(DateTimeFormatter.ofPattern("HH:mm"))+"] Employee ID: " + employeeID + " has timed out.");

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
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            System.out.println(e.getMessage());
        }
    }

    public static void writeGSon(List<EmployeeProfile> list, String path) {
        File file;
        switch(path) {
            case "employees":
                file = employeesJSONPath;
                break;
            case "pending":
                file = pendingRegistersList;
                break;
            default:
                return;
        }
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(list, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * It reads the JSON file, parses it, and returns a list of EmployeeProfile objects
     *
     * @return A list of EmployeeProfile objects.
     */
    public static List<EmployeeProfile> getEmployeesFromFile() {
        try (Reader reader = Files.newBufferedReader(employeesJSONPath.toPath())) {
            List<EmployeeProfile> employeeProfiles = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonArray rootoObj = JsonParser.parseReader(reader).getAsJsonArray();
            EmployeeDailyReport employeeDailyReport;
            for (int i = 0; i < rootoObj.size(); i++) {
                JsonElement element = rootoObj.get(i);
                JsonElement listofTimeouts = element.getAsJsonObject().get("employeeDailyReport");
                employeeProfiles.add(gson.fromJson(element, EmployeeProfile.class));
                employeeDailyReport = gson.fromJson(listofTimeouts, EmployeeDailyReport.class);
                if (employeeDailyReport == null) {
                    Date d  = new Date();
                    employeeDailyReport = new EmployeeDailyReport(String.valueOf(d.getDate()));
                }
                employeeProfiles.get(i).setEmployeeDailyReport(employeeDailyReport);
            }
            return employeeProfiles;
//            JsonElement jsonElement = gson.toJsonTree(new EmployeeProfile());
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<EmployeeProfile> getPendingRegistersFromFile() {
        try (Reader reader = Files.newBufferedReader(pendingRegistersList.toPath())) {
            Type dataType = new TypeToken<List<EmployeeProfile>>() {
            }.getType();
            List<EmployeeProfile> temp = gson.fromJson(reader, dataType);
            if (temp == null) {
                temp = new ArrayList<>();
                return temp;
            }
            return temp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    public static void addToFile(List<EmployeeProfile> employeesList) {
        try (FileWriter writer = new FileWriter(employeesJSONPath)) {
            gson.toJson(employeesList, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<EmployeeProfile> populateTable() {
        try (Reader reader = Files.newBufferedReader(employeesJSONPath.toPath())) {
            List<EmployeeProfile> employeeProfiles = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonArray rootoObj = JsonParser.parseReader(reader).getAsJsonArray();
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
            System.out.println(e.getMessage());
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
        try (Writer writer = new FileWriter(pendingRegistersList)) {
            LocalTime time = LocalTime.now();
            System.out.println("["+time.format(DateTimeFormatter.ofPattern("HH:mm"))+"] An employee ("+profile.getFullName()+") has applied to register.");
            gson.toJson(pendingEmployees, writer);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    /**
     * 0 - on break
     * 1 - working
     * @param employeeID
     * @return
     */
    public static String getCurrentStatus(String employeeID) {

        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();

            for (int i = 0; i < employees.size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(employeeID)) {
                    JsonElement jsonElement = gson.toJsonTree(emp);
                    //add timeout
                    Date d = new Date();

                    if (emp.getEmployeeDailyReport() == null) {
                        emp.setEmployeeDailyReport(new EmployeeDailyReport(String.valueOf(d.getDate())));
                    }

                    //add timeout sa json file
                    String stat = String.valueOf(jsonElement.getAsJsonObject().get("status")).replaceAll("\"", "");

                    if (stat.equals("Working"))
                        return "Working";
                    else if (stat.equals("On Break"))
                        return "On Break";
                    else
                        return "";
                }
            }
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            System.out.println(e.getMessage());
        }
        return "";
    }
}
