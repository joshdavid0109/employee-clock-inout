package org.client.gui.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.shared_classes.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterInformationController implements Initializable {
    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private DatePicker datePicker;
    Attendance stub;

    @FXML
    private TextField lastNameField, firstNameField;


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
        LocalDate localDate = LocalDate.now();
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            LocalDate birthDate = datePicker.getValue();
            int age = calculateAge(birthDate, localDate);
            String gender = genderBox.getValue();

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
        } catch (EmptyFieldsException e) {
            Alert dialog = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            dialog.show();
        }
    }

    private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return (Period.between(birthDate, currentDate).getYears());
        } else {
            throw new EmptyFieldsException();
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


    public void clicky(MouseEvent mouseEvent) {
        ObservableList<String> list = FXCollections.observableArrayList("Male", "Female", "Other");
        genderBox.getItems().setAll(list);
        genderBox.getSelectionModel().selectFirst();
    }
}
