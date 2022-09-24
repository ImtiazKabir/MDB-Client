package com.mdb.movieclient;

import archive.entity.Movie;
import archive.info.InfoWrapper;
import archive.info.LoginInfo;
import archive.info.SessionInfo;
import com.google.gson.Gson;
import javafx.application.Platform;
import util.SocketWrapper;

import java.io.IOException;

public class Client {
    private final SocketWrapper serverSocket;
    private final ClientApplication app;

    public Client(final String serverAddress, final int serverPort, final ClientApplication app) {
        try {
            serverSocket = new SocketWrapper(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.app = app;
        new ReadFromServerThread(this);
    }

    public void closeConnection() {
        try {
            serverSocket.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public SocketWrapper getServerSocket() {
        return serverSocket;
    }

    public void addMovieInDatabase(Movie movie) {
        app.getSessionInfo().getDatabase().addMovie(movie);
        //TODO live change
        Platform.runLater(() -> {
            try {
                app.getDashboardController().updateMovieEntityList();
                app.getDashboardController().updateSelectedMovies();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setSessionInfo(SessionInfo sessionInfo) {
        app.setSessionInfo(sessionInfo);
    }
}
