package com.example.helloworld;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shared_classes.Attendance;
import org.shared_classes.CredentialsErrorException;
import org.shared_classes.EmployeeProfile;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Attendance stub;
    @FXML
    public TextField logInUsername;
    @FXML
    public TextField logInPassword;
    @FXML
    private PasswordField logInPasswordHide;
    @FXML
    private CheckBox showPassword;
    @FXML
    public Button logInButton;
    @FXML
    public ImageView samcisLogo;
    @FXML
    private Button loadRegisterGUIbtn;
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private StackPane parentContainer;

    @FXML
    public void LoadRegisterGUI() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RegisterInterface.fxml")));
        Scene scene = loadRegisterGUIbtn.getScene();

        root.translateXProperty().set(scene.getWidth());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> parentContainer.getChildren().remove(loginAnchorPane));
        timeline.play();
    }

    @FXML
    public void showPassword() {
        if(showPassword.isSelected()) {
            logInPassword.setText(logInPasswordHide.getText());
            logInPassword.setVisible(true);
            logInPasswordHide.setVisible(false);
        } else {
            logInPasswordHide.setText(logInPassword.getText());
            logInPasswordHide.setVisible(true);
            logInPassword.setVisible(false);
        }
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

    public void logInNa(ActionEvent actionEvent) throws Exception {
        String userName = logInUsername.getText();
        String passWord = logInPasswordHide.getText();

        EmployeeProfile employee = stub.logIn(userName, passWord);

//        if(employee == null) {
//            Alert dialog = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
//            dialog.show();
//        }
//        else
        if (employee == null) {
            Alert dialog = new Alert(Alert.AlertType.WARNING, String.valueOf(new CredentialsErrorException()), ButtonType.OK);
            dialog.show();
//            throw new CredentialsErrorException("haha oopsie!");
        } else {

            logInButton.getScene().getWindow().hide();
            System.out.println("log in OK");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/EmployeeInterface.fxml"));
            Parent root = loader.load();
            EmployeeController employeeController = loader.getController();
            employeeController.setEmployee(employee);
            employeeController.setStub(stub);

            Scene scene = new Scene(root);
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}
