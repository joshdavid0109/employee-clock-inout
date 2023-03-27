package controller;

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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.server.Attendance;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminLoginInterfaceController implements Initializable {

    public static Attendance stub;

    private AnchorPane adminAnchorPane;


    @FXML
    private ImageView smcisLogo;

    @FXML
    private TextField loginUsername;

    @FXML
    private TextField loginPassword;

    @FXML
    private Button enterLogin;

    @FXML
    private Button cancel;

    @FXML
    private CheckBox showPass;

    @FXML
    private PasswordField loginHidePassword;

    @FXML
    private Button loadRegister;

    Stage stage;


    @FXML
    void LoadRegisterInformation() {

    }

    @FXML
    public void cancelAdmin() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Do you want to cancel?");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) adminAnchorPane.getScene().getWindow();
            System.out.println(" ");
            stage.close();
        }

    }

    @FXML
    void loginNa(ActionEvent event) {
       //TO DO
    }

    @FXML
    void showPassword() {
        if(showPass.isSelected()){
            loginPassword.setText(loginHidePassword.getText());
            loginPassword.toFront();
        }else{
            loginHidePassword.setText(loginPassword.getText());
            loginHidePassword.toFront();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setStub(Attendance stub){
        this.stub =stub;
    }
}
