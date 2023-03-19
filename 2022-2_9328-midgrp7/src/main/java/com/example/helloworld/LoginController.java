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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shared_classes.Attendance;
import org.shared_classes.EmployeeProfile;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public TextField logInUsername;
    public TextField logInPassword;
    public Button logInButton;
    public ImageView samcisLogo;
    @FXML
    private Button loadRegisterGUIbtn;

    @FXML
    private AnchorPane loginAnchorPane;

    @FXML
    private StackPane parentContainer;
    private Attendance stub;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Registry registry = LocateRegistry.getRegistry(2001);
            stub = (Attendance) registry.lookup("sayhi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logInNa(ActionEvent actionEvent) throws IOException {
        String userName = logInUsername.getText();
        String passWord = logInPassword.getText();

        logInButton.getScene().getWindow().hide();

        EmployeeProfile employee = stub.LogIn(userName, passWord);

        if(employee == null){
            System.out.println("mali");
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmployeeInterface.fxml"));
            EmployeeController employeeController = new EmployeeController(employee);
            loader.setController(employeeController);
            Parent root = loader.load();

            Stage emp = new Stage();
            Scene scene = new Scene(root);
            emp.setScene(scene);
            emp.show();
            emp.setResizable(false);
        }
    }
}
