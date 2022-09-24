package com.mdb.movieclient;

import archive.entity.Movie;
import archive.info.TransactionInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TransferController {

    private ClientApplication app;

    @FXML
    private VBox companyListVbox;

    @FXML
    private Label movieNameLabel;
    @FXML
    private Label companyNameLabel;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    public void setApplication(ClientApplication app) {
        this.app = app;
        app.setTransferController(this);

        companyNameLabel.setText(app.getClientName());

        boolean isSelectedAlready = false;
        List<String> companies = app.getSessionInfo().getCompanyList();
        Collections.sort(companies);
        for (String company : companies) {
            if (company.equals(app.getClientName())) {
                continue;
            }
            RadioButton rb = new RadioButton(company);
            rb.setToggleGroup(toggleGroup);
            if (!isSelectedAlready) {
                toggleGroup.selectToggle(rb);
                isSelectedAlready = true;
            }

            companyListVbox.getChildren().add(rb);
        }
    }

    public void setMovieName(String movieName) {
        movieNameLabel.setText(movieName);
    }

    public void confirmCallback(ActionEvent event) throws IOException {
        String title = movieNameLabel.getText();
        String previousOwner = companyNameLabel.getText();
        String newOwner = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
        Movie movie = app.getSessionInfo().getDatabase().removeMovieWithTitle(title);
        TransactionInfo transactionInfo = new TransactionInfo(movie, previousOwner, newOwner);
        app.processTransactionInfo(transactionInfo);

        // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-scene.fxml"));
        Scene scene = new Scene(loader.load());
        DashboardController controller = loader.getController();
        controller.setApplication(app);
        app.getStage().setScene(scene);
        app.getStage().show();
    }

    public void cancelCallback(ActionEvent event) throws IOException {
        // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-scene.fxml"));
        Scene scene = new Scene(loader.load());
        DashboardController controller = loader.getController();
        controller.setApplication(app);
        app.getStage().setScene(scene);
        app.getStage().show();
    }

}
