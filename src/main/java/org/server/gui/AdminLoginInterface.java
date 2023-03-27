package org.server.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.client.gui.EmployeeInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginInterface extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EmployeeInterface.class.getResource("/fxml/AdminLoginInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Hello!");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void cancelAdmin(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Do you want to cancel?");

        if(alert.showAndWait().get() == ButtonType.OK){
            System.out.println(" ");
            stage.close();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
