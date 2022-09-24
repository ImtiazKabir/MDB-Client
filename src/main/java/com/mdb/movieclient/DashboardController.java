package com.mdb.movieclient;

import archive.entity.Movie;
import archive.entity.MovieDatabase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {


    private ClientApplication app;
    @FXML
    private Label companyNameLabel;
    @FXML
    private Label avatarLabel;

    @FXML
    private ToggleGroup filterToggle;

    @FXML
    private TextField nameField;
    @FXML
    private TextField yearOfReleaseField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField durationField;

    @FXML
    private TextField revenueField;
    @FXML
    private TextField budgetField;

    @FXML
    private VBox movieEntityList;

    @FXML
    private Label profitLabel;
    @FXML
    private Label numOfMovieLabel;


    private MovieDatabase chosenMovies;


    private void setCompanyLabel() {
        companyNameLabel.setText(app.getClientName());
        avatarLabel.setText(app.getClientName().substring(0, 1).toUpperCase());
    }

    @FXML
    private void initialize() {
//        TextField[] fields = new TextField[] {nameField, yearOfReleaseField, genreField, durationField, revenueField, budgetField};
//        for (TextField field : fields) {
//            field.textProperty().addListener(((observable, oldValue, newValue) -> {
//                try {
//                    textChanged(field, newValue);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }));
//        }
        ObservableList<Toggle> toggles = filterToggle.getToggles();
        for (Toggle toggle: toggles) {
            RadioButton rb = (RadioButton) toggle;
            TextField field = (TextField) ((AnchorPane) rb.getParent()).getChildren().get(1);
            field.textProperty().addListener(((observable, oldValue, newValue) -> {
                try {
                    textChanged(field, newValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }

    private void textChanged(TextField field, String value) throws IOException {
//        if (field == nameField) {
//            System.out.print("Name field is changed ");
//        } else if (field == yearOfReleaseField) {
//            System.out.print("Year field is changed ");
//        } else if (field == revenueField) {
//            System.out.print("Revenue field is changed ");
//        }
//        System.out.println("to " + value);

        updateChosenMoviesToField(field);
    }

    @FXML
    private void logoutButtonCallback(ActionEvent event) throws IOException {
        app.renewClientConnection();
        // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-scene.fxml"));
        Scene scene = new Scene(loader.load());
        loader.<LoginController>getController().setApplication(app);
        app.getStage().setScene(scene);
        app.getStage().show();
    }

    @FXML
    private void toggleSelectCallback(ActionEvent event) throws IOException {
//        ObservableList<Toggle> toggles = filterToggle.getToggles();
//        for (Toggle toggle: toggles) {
//            RadioButton rb = (RadioButton) toggle;
//            TextField field = (TextField) ((AnchorPane) rb.getParent()).getChildren().get(1);
//            field.setDisable(!toggle.isSelected());
//        }
//        RadioButton radioButton = (RadioButton) filterToggle.getSelectedToggle();
//        TextField field = (TextField) ((AnchorPane) radioButton.getParent()).getChildren().get(1);
//        chosenMovies = app.getSessionInfo().getDatabase();
//        updateChosenMoviesToField(field);
        updateSelectedMovies();
    }

    public void updateSelectedMovies() throws IOException {
        ObservableList<Toggle> toggles = filterToggle.getToggles();
        for (Toggle toggle: toggles) {
            RadioButton rb = (RadioButton) toggle;
            TextField field = (TextField) ((AnchorPane) rb.getParent()).getChildren().get(1);
            field.setDisable(!toggle.isSelected());
        }

        RadioButton radioButton = (RadioButton) filterToggle.getSelectedToggle();
        if (radioButton != null) {
            TextField field = (TextField) ((AnchorPane) radioButton.getParent()).getChildren().get(1);
            chosenMovies = app.getSessionInfo().getDatabase();
            updateChosenMoviesToField(field);
        }
    }

    private void updateChosenMoviesToField(TextField field) throws IOException {
        if (field.getText().isEmpty()) {
            resetChosenMovie();
        }
        String queryField = null;
        if (field == nameField) {
            queryField = "title";
        } else if (field == yearOfReleaseField) {
            queryField = "yearOfRelease";
        } else if (field == genreField) {
            queryField = "genre";
        } else if (field == durationField) {
            queryField = "runningTime";
        } else if (field == revenueField) {
            queryField = "revenue";
        } else if (field == budgetField) {
            queryField = "budget";
        }

        chosenMovies = app.getSessionInfo().getDatabase().databaseFromContainsQuery(queryField, field.getText());
        updateMovieEntityList();
    }

    @FXML
    private void clearFilterCallback(ActionEvent event) throws IOException {
        if (filterToggle.getSelectedToggle() == null) {
            return;
        }
        filterToggle.getSelectedToggle().setSelected(false);
        ObservableList<Toggle> toggles = filterToggle.getToggles();
        for (Toggle toggle: toggles) {
            RadioButton rb = (RadioButton) toggle;
            TextField field = (TextField) ((AnchorPane) rb.getParent()).getChildren().get(1);
            field.setText("");
        }

        resetChosenMovie();
    }

    public void setApplication(ClientApplication app) throws IOException {
        this.app = app;
        app.setDashboardController(this);
        setCompanyLabel();
        chosenMovies = app.getSessionInfo().getDatabase();
        updateMovieAndProfitCount();
        updateMovieEntityList();
    }

    public void updateMovieEntityList() throws IOException {
        updateMovieAndProfitCount();
        movieEntityList.getChildren().clear();
        for (Movie m : chosenMovies.getMovieList()) {
            addMovie(m);
        }
    }

    private void resetChosenMovie() throws IOException {
        chosenMovies = app.getSessionInfo().getDatabase();
        updateMovieEntityList();
    }

    @FXML
    private void createCallback(ActionEvent event) throws IOException {
        // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create-scene.fxml"));
        Scene scene = new Scene(loader.load());
        CreateController controller = loader.getController();
        controller.setApplication(app);
        app.getStage().setScene(scene);
        app.getStage().show();
    }

    public void addMovie(Movie movie) throws IOException {
        movieEntityList.getChildren().add(new MovieEntry(movie, app).getNode());
    }

    public void updateMovieAndProfitCount() {
        numOfMovieLabel.setText(chosenMovies.getMovieList().size() + " movie(s)");
        long totalProfit = app.getSessionInfo().getDatabase().getMovieList().stream()
                .mapToLong(Movie::getProfit)
                .sum();
        profitLabel.setText("$" + totalProfit);
    }
}
