package org.server;

import javafx.application.Application;
import javafx.stage.Stage;
import org.server.gui.interfaces.AdminLoginInterface;

public class ServerMain extends Application{

    @Override
    public void start(Stage primaryStage) {
        try {
            AdminLoginInterface adminLoginInterface = new AdminLoginInterface();
            adminLoginInterface.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}