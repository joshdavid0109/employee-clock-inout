package com.example.helloworld;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeProfile;


public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button loginButton;

    private Attendance stub;

    @FXML
    public void loginButton(MouseEvent event) {

        /*String username = "ASDASD";
        String password = "sadasd";*/

        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            EmployeeProfile employee = stub.LogIn(username, password);

            if (employee == null) {
                System.out.println("invalid yung login");
            } else {
                loginButton.getScene().getWindow().hide();

                Stage emp = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(EmployeeInterface.class.getResource("/fxml/EmployeeInterface.fxml")));
                Scene scene = new Scene(root);
                emp.setScene(scene);
                emp.show();
                emp.setResizable(false);
                System.out.println("go to employee interface na");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 2001);
            stub = (Attendance) registry.lookup("sayhi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
