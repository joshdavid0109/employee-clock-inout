package org.client.gui;

import controller.EmployeeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EditProfileInterface extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EditProfile.fxml"));
        fxmlLoader.getController();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EMPLOYEE");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
