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
import org.server.resources.AttendanceServant;
import org.server.resources.JSONHandler;
import org.shared_classes.EmployeeProfile;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminLoginInterfaceController implements Initializable {

    public static int port;
    public static String stubName;

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

    static public final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");


    @FXML
    public void cancelAdmin() {
        Platform.exit();
    }

    public void setPort(int port) {
        AdminLoginInterfaceController.port = port;
    }

    public void setStubName(String stubName) {
        AdminLoginInterfaceController.stubName = stubName;
    }

    @FXML
    void loginNa(ActionEvent event) throws IOException {
        try {
            String userName = loginUsername.getText();
            String passWord = (loginHidePassword.getText().equals("")
                    ? loginPassword.getText() : loginHidePassword.getText());

            Alert message = new Alert(Alert.AlertType.INFORMATION);
            if (userName.equals("user") && passWord.equals("admin")) {
                enterLogin.getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ServerInterface.fxml"));

                ServerController serverController = new ServerController();
                serverController.setPort(port);
                serverController.setStubName(stubName);


                if (!AttendanceServant.serverDate.toString().equals(dateFormat.format(new Date()).toString())) {
                    List<EmployeeProfile> employeeProfileList = JSONHandler.getEmployeesFromFile();

                    for (EmployeeProfile employeeProfile: employeeProfileList) {
                        JSONHandler.setDefaultValues(employeeProfile.getEmpID());
                    }
                }

                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) enterLogin.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                stage.setOnCloseRequest(e -> {
                    JSONHandler.setAllEmployeesOffline();
                });

            } else {
                message.setContentText("Invalid Login Details.");
                message.setTitle("Unsuccessful Login");
                message.show();
            }

            loginHidePassword.setText("");
            loginPassword.setText("");
            loginUsername.setText("");
        }catch (Exception e){
            e.printStackTrace();
        }
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
