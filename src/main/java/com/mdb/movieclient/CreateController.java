package com.mdb.movieclient;

import archive.entity.Movie;
import archive.info.CreateInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateController {

    private ClientApplication app;
    @FXML
    private TextField titleField;
    @FXML
    private TextField genre1field;
    @FXML
    private TextField genre2field;
    @FXML
    private TextField genre3field;
    @FXML
    private TextField runningTimeField;
    @FXML
    private TextField budgetField;
    @FXML
    private TextField revenueField;

    @FXML
    private TextField yearOfReleaseField;

    @FXML
    private Label invalidFormatLabel;

    @FXML
    private Label companyNameLabel;


    public void setApplication(ClientApplication app) {
        this.app = app;
        app.setCreateController(this);
        companyNameLabel.setText(app.getClientName());

    }


    @FXML
    private void confirmCallback(ActionEvent event) {
        try {
            String title = titleField.getText();
            int yearOfRelease = Integer.parseInt(yearOfReleaseField.getText());
            String[] genre = new String[]{genre1field.getText(), genre2field.getText(), genre3field.getText()};
            int runningTime = Integer.parseInt(runningTimeField.getText());
            long budget = Integer.parseInt(budgetField.getText());
            long revenue = Integer.parseInt(revenueField.getText());
            Movie movie = new Movie(title, yearOfRelease, genre, runningTime, app.getClientName(), budget, revenue);
            app.processCreateInfo(new CreateInfo(movie));

            // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-scene.fxml"));
            Scene scene = new Scene(loader.load());
            DashboardController controller = loader.getController();
            controller.setApplication(app);
            controller.updateMovieEntityList();
            app.getStage().setScene(scene);
            app.getStage().show();
        } catch (Exception e) {
            invalidFormatLabel.setVisible(true);
        }

    }

    @FXML
    private void cancelCallback(ActionEvent event) throws IOException {
        // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-scene.fxml"));
        Scene scene = new Scene(loader.load());
        DashboardController controller = loader.getController();
        controller.setApplication(app);
        app.getStage().setScene(scene);
        app.getStage().show();
    }
}
