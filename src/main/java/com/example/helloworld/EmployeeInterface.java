package com.example.helloworld;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.shared_classes.EmployeeProfile;

import java.io.IOException;

public class EmployeeInterface extends Application {
    private EmployeeProfile employeeProfile;
    private Text text;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Image image = new Image("SLU_LOGO.jpg");

        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeInterface.class.getResource("/fxml/EmployeeInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("EMPLOYEE");
        primaryStage.getIcons().add(image);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
