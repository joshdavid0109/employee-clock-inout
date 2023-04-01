package org.server.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.shared_classes.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminLoginInterfaceController implements Initializable {

    @FXML
    private ImageView smcisLogo;

    @FXML
    private TextField loginUsername;

    @FXML
    private TextField loginPassword;

    @FXML
    private Button enterLogin;

    @FXML
    private CheckBox showPass;

    @FXML
    private PasswordField loginHidePassword;

    @FXML
    private Button loadRegister;

    Stage stage;


    @FXML
    public void cancelAdmin() {
        Platform.exit();
    }

    @FXML
    void loginNa(ActionEvent event) throws IOException {
       String userName = loginUsername.getText();
       String passWord = (loginPassword.getText() == null ? loginPassword.getText() : loginHidePassword.getText());

       Alert message = new Alert(Alert.AlertType.INFORMATION);
       if(userName.equals("user")&& passWord.equals("admin")){
           enterLogin.getScene().getWindow().hide();
           System.out.println("log in OK");
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(getClass().getResource("/fxml/ServerInterface.fxml"));

           Parent root = loader.load();

           Scene scene = new Scene(root);
           Stage stage = (Stage) enterLogin.getScene().getWindow();
           stage.setScene(scene);
           stage.show();

       } else {
           message.setContentText("Invalid Login Details.");
           message.setTitle("Unsuccessful Login");
           message.show();
        }

       loginHidePassword.setText("");
       loginPassword.setText("");
       loginUsername.setText("");
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
}
