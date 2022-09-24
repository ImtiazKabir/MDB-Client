package com.mdb.movieclient;

import archive.info.LoginInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private ClientApplication app;

    @FXML
    private Label invalidInfoLabel;
    @FXML
    private TextField userNameInput;
    @FXML
    private PasswordField passwordInput;


    @FXML
    private void loginButtonCallback(ActionEvent event) throws IOException {

        String username = userNameInput.getText();
        String password = passwordInput.getText();
        LoginInfo loginInfo = new LoginInfo(username, password);
        app.processLoginInfo(loginInfo);
//        if (!app.isValidCredential(loginInfo)) {
//            invalidInfoLabel.setVisible(true);
//            app.renewClientConnection();
//            return;
//        }

//        if (username.isEmpty() || !password.equals(username)) {
//            invalidInfoLabel.setVisible(true);
//            return;
//        }

    }

    public void setApplication(ClientApplication app) {
        this.app = app;
        app.setLoginController(this);
    }

    @FXML
    private void closeButtonCallback(ActionEvent event) {
        app.closeClient();
    }

    public Label getInvalidInfoLabel() {
        return invalidInfoLabel;
    }

    public void proceedToDashboard(String username) throws IOException {
        app.setClientName(username);
        Platform.runLater(() -> {
            invalidInfoLabel.setVisible(false);
            app.getStage().setTitle(username.toUpperCase());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-scene.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DashboardController controller = loader.getController();
            try {
                controller.setApplication(app);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            app.getStage().setScene(scene);
            app.getStage().show();

        });

    }
}