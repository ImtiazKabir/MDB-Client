package com.mdb.movieclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MovieEntryController {
    private ClientApplication app;
    @FXML
    private Label genre3Label;
    @FXML
    private Label genre2Label;
    @FXML
    private Label genre1Label;
    @FXML
    private Label yearOfReleaseLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private Label revenueLabel;
    @FXML
    private Label nameLabel;

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getGenre3Label() {
        return genre3Label;
    }

    public Label getGenre2Label() {
        return genre2Label;
    }

    public Label getGenre1Label() {
        return genre1Label;
    }

    public Label getYearOfReleaseLabel() {
        return yearOfReleaseLabel;
    }

    public Label getDurationLabel() {
        return durationLabel;
    }

    public Label getBudgetLabel() {
        return budgetLabel;
    }

    public Label getRevenueLabel() {
        return revenueLabel;
    }

    public void transferButtonCallback(ActionEvent event) throws IOException {
        // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transfer-scene.fxml"));
        Scene scene = new Scene(loader.load());
        TransferController controller = loader.getController();
        controller.setApplication(app);
        controller.setMovieName(nameLabel.getText());
        app.getStage().setScene(scene);
        app.getStage().show();
    }

    public void setApplication(ClientApplication app) {
        this.app = app;
    }
}
