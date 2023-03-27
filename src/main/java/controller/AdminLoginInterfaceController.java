package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.server.Attendance;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLoginInterfaceController implements Initializable {

    public static Attendance stub;

    @FXML
    private Pane loginAnchorPane;

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

    @FXML
    void LoadRegisterInformation() {
        //TO DO
    }

    @FXML
    void cancelAdmin() {
        //TO DO
    }

    @FXML
    void loginNa(ActionEvent event) {
        String username = loginUsername.getText();
        String password = (loginPassword.getText() == null ? loginPassword.getText() : loginHidePassword.getText());

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
