package com.example.helloworld;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    public void loginButton(ActionEvent event) {

        /*String username = "ASDASD";
        String password = "sadasd";*/

        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            EmployeeProfile employee = stub.LogIn(username, password);

            if (employee == null) {
                System.out.println("invalid yung login");
            } else {
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
