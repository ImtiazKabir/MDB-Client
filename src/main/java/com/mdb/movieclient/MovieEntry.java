package com.mdb.movieclient;

import archive.entity.Movie;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class MovieEntry {
    private final Node node;
    private final ClientApplication app;
    public MovieEntry(Movie movie, final ClientApplication app) throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("movie-entry-scene.fxml"));
        node = loader.load();
        MovieEntryController controller = loader.getController();
        controller.setApplication(app);
        controller.getNameLabel().setText(movie.getTitle());
        controller.getBudgetLabel().setText(String.valueOf(movie.getBudget()));
        controller.getDurationLabel().setText(String.valueOf(movie.getRunningTime()));
        controller.getRevenueLabel().setText(String.valueOf(movie.getRevenue()));
        controller.getYearOfReleaseLabel().setText(String.valueOf(movie.getYearOfRelease()));
        String[] genre = movie.getGenre();
        controller.getGenre1Label().setText(genre[0]);
        controller.getGenre2Label().setText(genre[1]);
        controller.getGenre3Label().setText(genre[2]);
        this.app = app;
    }

    public Node getNode() {
        return node;
    }
}
