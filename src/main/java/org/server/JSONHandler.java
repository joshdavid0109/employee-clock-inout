package org.server;

//dapat server lang nakakagamit nitong class na ito

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.shared_classes.EmployeeProfile;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JSONHandler {

    static private final String employeesJSONPath = "src/main/resources/employees.json";

    public static EmployeeProfile checkIfValidLogIn(String username, String password) {
        try (Reader reader = Files.newBufferedReader(Paths.get(employeesJSONPath))) {
            Gson gson = new Gson();
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
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

}
