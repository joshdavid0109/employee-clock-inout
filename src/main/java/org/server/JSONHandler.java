package org.server;

//dapat server lang nakakagamit nitong class na ito

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
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

    static private final File employeesJSONPath = new File("employees.json");
    static private final File summaryReportsFile = new File("summaryReports.json");
    static private final File pendingRegistersList = new File("registers.json");

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
                            System.out.println("EMPLOYEE " + employee.getUserName() + " HAS LOGGED IN");
                            System.out.println("kasjay");
                            setEmployeeStatus(employee.getEmpID(), true);
                            return employee;
                        } else {
                            System.out.println("currently logged in");
                            throw new UserCurrentlyLoggedInException();
                        }
                    }
                    throw new CredentialsErrorException();
                } else
                    throw new UserNotExistingException();
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

    //true = logged in, false logged out
    public static void setEmployeeStatus(String EmployeeID, boolean loggedIn) {
        try {
            List<EmployeeProfile> employees = getEmployeesFromFile();

            for (EmployeeProfile emp : employees) {
                if (emp.getEmpID().equals(EmployeeID)) {
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
            List<EmployeeProfile> employees = getEmployeesFromFile();

            for (int i = 0; i < Objects.requireNonNull(employees).size(); i++) {
                EmployeeProfile emp = employees.get(i);
                if (emp.getEmpID().equals(employeeID)) {
                    JsonElement jsonElement = gson.toJsonTree(emp);

                    if (emp.getEmployeeDailyReport() == null) {
                        emp.setEmployeeDailyReport(new EmployeeDailyReport(String.valueOf(d.getDate())));
                    }

                    //add timein
                    List<String> timeIs = emp.getEmployeeDailyReport().getListofTimeIns();

                    List<SummaryReport> summaryReports = getSummaryReportsFromFile();

                    if (!timeIs.get(0).split(", ")[0].equals(dateFormat.format(d).split(", ")[0])) {
                        List<String> ins = new ArrayList<>(emp.getEmployeeDailyReport().getListofTimeIns());
                        List<String> outs = new ArrayList<>(emp.getEmployeeDailyReport().getListofTimeOuts());



                        SummaryReport summaryReport = new SummaryReport(emp.getEmployeeDailyReport().getListofTimeIns().get(0).split(", ")[0]);
                        summaryReport.setTimeIns(ins);
                        summaryReport.setTimeOuts(outs);
                        summaryReport.setEmpID(emp.getEmpID());
                        summaryReports.add(summaryReport);
//                        emp.getSummaryReport().add(summaryReport); // to be removed?

                        emp.getEmployeeDailyReport().getListofTimeIns().clear();
                        emp.getEmployeeDailyReport().getListofTimeOuts().clear();
                        emp.getEmployeeDailyReport().setTimeIn(dateFormat.format(d));
                        employees.add(i, emp);
                        employees.remove(i);

                        addSummaryToFile(summaryReports);

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

    private static List<SummaryReport> getSummaryReportsFromFile() {
        try (Reader reader = Files.newBufferedReader(summaryReportsFile.toPath())) {
            List<SummaryReport> summaryReports = new ArrayList<>();
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            JsonParser parser = new JsonParser();
            JsonArray rootoObj = parser.parse(reader).getAsJsonArray();
            SummaryReport summaryReport = null;
            for (int i = 0; i < rootoObj.size(); i++) {
                JsonElement element = rootoObj.get(i);
                summaryReports.add(gson.fromJson(element, SummaryReport.class));
            }
            return summaryReports;
//            JsonElement jsonElement = gson.toJsonTree(new EmployeeProfile());
        } catch (Exception e) {
            System.err.println("FILE NOT FOUND");
            e.printStackTrace();
        }
        return null;
    }

    private static void addSummaryToFile(List<SummaryReport> summaryReports) {
        try (FileWriter writer = new FileWriter(summaryReportsFile)) {
            gson.toJson(summaryReports, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTimeOut(String employeeID, Date d) {
//        SimpleDateFormat format1 = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss ");
        try {
//            Type dataType = new TypeToken<List<EmployeeProfile>>(){}.getType();
            List<EmployeeProfile> employees = getEmployeesFromFile();

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

    public static void writeGSon(List<EmployeeProfile> list) {
        try (FileWriter writer = new FileWriter(employeesJSONPath)) {
            gson.toJson(list, writer);
        } catch (Exception e) {
            e.printStackTrace();
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
            JsonParser parser = new JsonParser();
            JsonArray rootoObj = parser.parse(reader).getAsJsonArray();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static byte getCurrentStatus(String employeeID) {
        //habang ginagawa ko ito, i realized na wala pala sa json yung
        //current status, saan kukunin current status pala ng current user?
        return 2;
    }
}
