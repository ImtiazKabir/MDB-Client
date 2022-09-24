package com.mdb.movieclient;

import archive.entity.Movie;
import archive.info.InfoWrapper;
import archive.info.SessionInfo;
import javafx.application.Platform;

import java.io.IOException;
import java.net.SocketException;

public class ReadFromServerThread implements Runnable {

    private final Thread thread;
    private final Client client;
    public ReadFromServerThread(Client client) {
        this.client = client;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            try {
                String json = (String) client.getServerSocket().read();
                InfoWrapper infoWrapper = new InfoWrapper(json);
                new Thread(()->{handle(infoWrapper);}).start();
            } catch (SocketException e) {
                System.out.println("client closed connection");
                break;
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private void handle(InfoWrapper infoWrapper) {
        String cmd = infoWrapper.getCommand();
        if (cmd.equals("session")) {
            SessionInfo sessionInfo = infoWrapper.getContent();
            client.setSessionInfo(sessionInfo);
        } else if (cmd.equals("create")) {
            Movie movie = infoWrapper.getContent();
            client.addMovieInDatabase(movie);
        }
    }
}
