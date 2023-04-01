package org.server.gui.interfaces;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.server.gui.controllers.AdminLoginInterfaceController;

import java.io.IOException;

public class AdminLoginInterface extends Application {

    public String stubName;
    public int port;
    public Image image;

    @Override
    public void start(Stage primaryStage) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AdminLoginInterface.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            AdminLoginInterfaceController adminLoginInterfaceController = fxmlLoader.getController();
            adminLoginInterfaceController.setPort(port);
            adminLoginInterfaceController.setStubName(stubName);
            primaryStage.setTitle("ADMIN");
            primaryStage.getIcons().add(image);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
