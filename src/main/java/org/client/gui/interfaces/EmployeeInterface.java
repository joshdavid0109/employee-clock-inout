package org.client.gui.interfaces;

import org.client.gui.controllers.EmployeeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeInterface extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Image image = new Image("SLU_LOGO.jpg");

        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeInterface.class.getResource("/fxml/EmployeeInterface.fxml"));
        EmployeeController employeeController = fxmlLoader.getController();
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("EMPLOYEE");
        primaryStage.getIcons().add(image);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
