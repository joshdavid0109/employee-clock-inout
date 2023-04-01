package org.server.gui.interfaces;

import javafx.application.Application;
import javafx.stage.Stage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EmployeeSearchSummary extends Application {
    //private Attendance

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",2345);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
