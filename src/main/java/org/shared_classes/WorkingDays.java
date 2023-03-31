package org.shared_classes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WorkingDays {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");
//    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

    public static void computeWorkingHours(String jsonFilePath) throws IOException, ParseException {
        String json = Files.readString(Paths.get(jsonFilePath));
        Gson gson = new Gson();
        List<SummaryReport> reports = gson.fromJson(json,
                new TypeToken<List<SummaryReport>>(){}.getType());

        Map<String, Long> totalWorkingHoursPerEmployee = new HashMap<>();
        for (SummaryReport report : reports) {
            String empID = report.getEmpID();
//            String dateStr = report.getDate();
//            Date date = dateFormat.parse(dateStr);
//            Map<String, Long> workingHoursPerDay = workingHoursPerEmployeePerDay.
//                    computeIfAbsent(empID, k -> new HashMap<>());

            List<String> timeIns = report.getTimeIns();
            List<String> timeOuts = report.getTimeOuts();
            long totalWorkingHours = 0;
            for (int i = 0; i < timeIns.size(); i++) {
                Date timeIn = timeFormat.parse(timeIns.get(i));
                Date timeOut = timeFormat.parse(timeOuts.get(i));
                totalWorkingHours += timeOut.getTime() - timeIn.getTime();
            }
            totalWorkingHoursPerEmployee.merge(empID, totalWorkingHours, Long::sum);
        }

        for (String empID : totalWorkingHoursPerEmployee.keySet()) {
            long totalWorkingHours = totalWorkingHoursPerEmployee.get(empID);
            System.out.println("Employee ID: " + empID + ", Total working hours: " + (totalWorkingHours / 3600000f));
        }

        json = Files.readString(Paths.get("employees.json"));
        List<EmployeeProfile> employees = gson.fromJson(json, new TypeToken<List<EmployeeProfile>>(){}.getType());

        for (EmployeeProfile employee : employees) {
            if (totalWorkingHoursPerEmployee.containsKey(employee.getEmpID())) {
                long totalWorkingSeconds = totalWorkingHoursPerEmployee.get(employee.getEmpID());
                float totalWorkingHours = totalWorkingSeconds / 3600000f;
                List<Float> totalWorkingHoursList = employee.getTotalWorkingHours();
                if (totalWorkingHoursList != null && !totalWorkingHoursList.isEmpty()) {
                    float lastTotalWorkingHours = totalWorkingHoursList.get(totalWorkingHoursList.size() - 1);
                    totalWorkingHours += lastTotalWorkingHours;
                }
                List<Float> updatedTotalWorkingHoursList = new ArrayList<>();
                updatedTotalWorkingHoursList.add(totalWorkingHours);
                employee.setTotalWorkingHours(updatedTotalWorkingHoursList);
            }
        }

        // Writing the updated employees list to the employees.json file.
        String updatedJson = gson.toJson(employees);
        FileWriter writer = new FileWriter("employees.json");
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setIndent("  ");
        gson.toJson(employees, new TypeToken<List<EmployeeProfile>>(){}.getType(), jsonWriter);
        writer.close();

    }


    // test
    public static void main(String[] args) throws IOException, ParseException {
        computeWorkingHours("summaryReports.Json");
    }
}
