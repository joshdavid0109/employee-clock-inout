package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.server.Attendance;
import org.shared_classes.EmployeeDetails;
import org.shared_classes.EmployeeProfile;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterInformationController implements Initializable {

    Attendance stub;

    @FXML
    private TextField lastNameField, ageField, firstNameField;

    @FXML
    private RadioButton maleBTN, femaleBTN;

    @FXML
    private AnchorPane registerInformationAnchorPane;
    @FXML
    private StackPane parentContainer;
    @FXML
    private Button nextButton, loadGuiButton;

    @FXML
    public void loadLoginGUI() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LoginInterface.fxml")));
        Scene scene = loadGuiButton.getScene();

        root.translateYProperty().set(scene.getHeight());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> parentContainer.getChildren().remove(registerInformationAnchorPane));
        timeline.play();
    }

    @FXML
    public void loadRegisterGUI() throws IOException {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int age = Integer.parseInt(ageField.getText());
        //todo gender hardcode muna
        String gender = "Male";

        EmployeeDetails employeeDetails = new EmployeeDetails(firstName, lastName, age, gender);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterInterface.fxml"));
        Parent root = loader.load();
        RegisterController registerController = loader.getController();

        registerController.setEmployeeDetails(employeeDetails);

        /*registerController.setFirstName(firstName);
        registerController.setLastName(lastName);
        registerController.setAge(age);
        registerController.setGender(gender);*/
        registerController.setStub(stub);

        Scene scene = nextButton.getScene();

        root.translateXProperty().set(scene.getWidth());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(event1 -> parentContainer.getChildren().remove(registerInformationAnchorPane));
        timeline.play();
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
}
