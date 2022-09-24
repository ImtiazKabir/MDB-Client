package com.mdb.movieserver;


import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ListeningForConnectionThread implements Runnable {
    private final Thread thread;

    private final Server server;

    public ListeningForConnectionThread(Server server) {
        this.server = server;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            try {
                Socket client = server.getServerSocket().accept();
                new ReadFromClientThread(client, server);
            } catch (SocketException e) {
                System.out.println("Stopping listening for new connections");
            }
            catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
