package org.client.gui.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.shared_classes.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private String firstName, lastName, gender;
    private int age;

    private EmployeeDetails employeeDetails;

    private Attendance stub;
    @FXML
    private Button loadLoginGUIbtn, returnRegisterInformation;
    @FXML
    private AnchorPane registerAnchorPane;
    @FXML
    private StackPane parentContainer;
    @FXML
    private Button regButton;
    @FXML
    private Label questionMark;
    @FXML
    private Label questionMark1;
    @FXML
    private PasswordField regPasswordHide;
    @FXML
    private PasswordField regVerifyPasswordHide;
    @FXML
    private CheckBox regShowPassword;
    @FXML
    private CheckBox regVerifyShowPassword;
    @FXML
    private TextField regPassword;
    @FXML
    private TextField regVerifyPassword;
    @FXML
    private TextField regUsername;

    public void setStub(Attendance stub) {
        this.stub = stub;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmployeeDetails(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry(2345);
            stub = (Attendance) registry.lookup("sayhi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadLoginGUI() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LoginInterface.fxml")));
        Scene scene = loadLoginGUIbtn.getScene();

        root.translateYProperty().set(scene.getHeight());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> parentContainer.getChildren().remove(registerAnchorPane));
        timeline.play();
    }

    @FXML
    public void loadRegisterInformation() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RegisterInterfacePersonalDetails.fxml")));
        Scene scene = returnRegisterInformation.getScene();

        root.translateYProperty().set(scene.getHeight());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> parentContainer.getChildren().remove(registerAnchorPane));
        timeline.play();
    }

    public void registerNa() throws IOException {

        String username = regUsername.getText();
        String password = regPasswordHide.getText();
        String verify = regVerifyPasswordHide.getText();

        regButton.getScene().getWindow().hide();

        EmployeeProfile emp = new EmployeeProfile(username, password);
        emp.setPersonalDetails(employeeDetails);
        stub.signUp(emp);
    }

    @FXML
    public void regShowPassword() {
        if(regShowPassword.isSelected()) {
            regPassword.setText(regPasswordHide.getText());
            regPassword.setVisible(true);
            regPasswordHide.setVisible(false);
        } else {
            regPasswordHide.setText(regPassword.getText());
            regPasswordHide.setVisible(true);
            regPassword.setVisible(false);
        }
    }

    @FXML
    public void regVerifyShowPassword() {
        if(regVerifyShowPassword.isSelected()) {
            regVerifyPassword.setText(regVerifyPasswordHide.getText());
            regVerifyPassword.setVisible(true);
            regVerifyPasswordHide.setVisible(false);
        } else {
            regVerifyPasswordHide.setText(regVerifyPassword.getText());
            regVerifyPasswordHide.setVisible(true);
            regVerifyPassword.setVisible(false);
        }
    }
}
