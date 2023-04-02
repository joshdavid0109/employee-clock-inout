package org.client.gui.controllers;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shared_classes.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public static Attendance stub;
    public static int port;
    public static String hostname;
    public static String refName;
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
    public void LoadRegisterInformationGUI() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/RegisterInterfacePersonalDetails.fxml")));
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
            logInPassword.toFront();
        } else {
            logInPasswordHide.setText(logInPassword.getText());
            logInPasswordHide.toFront();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void logInNa(ActionEvent actionEvent) throws Exception {
        String userName = logInUsername.getText();
        String passWord = (logInPassword.getText() == null ? logInPassword.getText() : logInPasswordHide.getText());

        try {
            EmployeeProfile employee = stub.logIn(userName, passWord);

            logInButton.getScene().getWindow().hide();
            System.out.println("log in OK");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/EmployeeInterface.fxml"));
            EmployeeController employeeController = new EmployeeController();
            EmployeeController.ip_address = hostname;
            EmployeeController.port = port;
            EmployeeController.remoteReferenceName = refName;

            employeeController.setEmployee(employee);
            employeeController.setStub(stub);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) logInButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(windowEvent ->
            {
                try {
                    employeeController.shutdown();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch (UserCurrentlyLoggedInException | CredentialsErrorException | EmptyFieldsException
                | UserNotExistingException e) {
            Alert dialog = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            dialog.show();
        }
    }

    public void setStub(Attendance stub) {
        LoginController.stub = stub;
    }
}
