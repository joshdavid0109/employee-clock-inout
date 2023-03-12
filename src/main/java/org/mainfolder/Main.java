package org.mainfolder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Employee employee = new Employee(
                    "Norman",
                    "Reedus",
                    26,
                    false
            );

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println("The JSON representation of object Employee is: \n");
            System.out.println(gson.toJson(employee));

            try (FileWriter writer = new FileWriter("src/main/resources/employee.json")) {
                gson.toJson(employee, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}