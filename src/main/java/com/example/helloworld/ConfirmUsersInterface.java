package com.example.helloworld;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ConfirmUsersInterface extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Image image = new Image("SLU_LOGO.jpg");

        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeInterface.class.getResource("/fxml/ConfirmUsersTable.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ADMIN");
        stage.setResizable(false);
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }
}
