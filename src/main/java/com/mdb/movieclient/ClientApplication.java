package com.mdb.movieclient;

import archive.info.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import com.google.gson.*;

public class ClientApplication extends Application {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    private String clientName;

    private LoginController loginController;
    private DashboardController dashboardController;
    private CreateController createController;
    private TransferController transferController;

    private SessionInfo sessionInfo;

    private Client client;

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;
        client = new Client(SERVER_ADDRESS, SERVER_PORT, this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-scene.fxml"));
        Scene scene = new Scene(loader.load());
        loader.<LoginController>getController().setApplication(this);
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.show();
    }


    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public void setCreateController(CreateController createController) {
        this.createController = createController;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }


    public void setTransferController(TransferController transferController) {
        this.transferController = transferController;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public void closeClient() {
        client.closeConnection();
        System.exit(0);
    }

    public boolean isValidSession(SessionInfo sessionInfo) {
        return sessionInfo != null && sessionInfo.getDatabase().getMovieList().size() != 0;
    }

    public void processCreateInfo(CreateInfo createInfo) {
        try {
            InfoWrapper infoWrapper = new InfoWrapper("create", createInfo);
            client.getServerSocket().write(new Gson().toJson(infoWrapper));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sessionInfo.getDatabase().addMovie(createInfo.getMovie());
    }



    public void processTransactionInfo(TransactionInfo transactionInfo) {
        try {
            InfoWrapper infoWrapper = new InfoWrapper("transaction", transactionInfo);
            client.getServerSocket().write(new Gson().toJson(infoWrapper));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void setSessionInfo(SessionInfo sessionInfo) {
        if (!isValidSession(sessionInfo)) {
            loginController.getInvalidInfoLabel().setVisible(true);
            return;
        }
        this.sessionInfo = sessionInfo;
        try {
            loginController.proceedToDashboard(sessionInfo.getClientName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void renewClientConnection() {
        client.closeConnection();
        client = new Client(SERVER_ADDRESS, SERVER_PORT, this);
    }

    public Stage getStage() {
        return stage;
    }


    public static void main(String[] args) {
        launch();
    }


    public void processLoginInfo(LoginInfo loginInfo) {
        try {
            InfoWrapper infoWrapper = new InfoWrapper("login", loginInfo);
            client.getServerSocket().write(new Gson().toJson(infoWrapper));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
