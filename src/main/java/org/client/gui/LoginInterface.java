package org.client.gui;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.shared_classes.Attendance;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginInterface extends Application {
    private Attendance stub;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Image image = new Image("SLU_LOGO.jpg");

        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeInterface.class.getResource("/fxml/LoginInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        try {
            Registry registry = LocateRegistry.getRegistry(2345);
            stub = (Attendance) registry.lookup("sayhi");
/*
            "Can not connect to the server. The server" +
                    "is not yet running."*/

        } catch (Exception e) {
            Alert dialog = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            dialog.show();
        }
        if (stub != null) {
            LoginController.stub = stub;
            primaryStage.setTitle("EMPLOYEE");
            primaryStage.getIcons().add(image);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}