package com.mdb.movieserver;

import archive.entity.Movie;
import archive.entity.MovieDatabase;
import archive.info.*;
import util.SocketWrapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Server {

    private final MovieDatabase movieDatabase;
    private final Map<String, SocketWrapper> clientsByName;
    private final ServerSocket serverSocket;


    public Server(final int serverPort, final String fileName) throws IOException {
        movieDatabase = new MovieDatabase(fileName);

        clientsByName = new HashMap<>();

        serverSocket = new ServerSocket(serverPort);
        new ListeningForConnectionThread(this);

        while (true) {
            new Scanner(System.in).nextLine();
            onExit();
            System.out.println("Closing server");
            serverSocket.close();
            System.exit(0);
        }
    }

    public SessionInfo getSessionInfo(LoginInfo info) {
//        MovieDatabase db = movieDatabase.databaseFromQuery("productionCompany", info.getUsername());
        MovieDatabase db = movieDatabase.databaseFromProductionCompany(info.getUsername());
        List<String> companies = movieDatabase.getProductionCompanies();
        return new SessionInfo(info.getUsername(), db, companies);
    }

    private void onExit() {
        try {
            movieDatabase.dumpToFile();
            System.out.println("Changes are made permanent");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Map<String, SocketWrapper> getClientsByName() {
        return clientsByName;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }


    public void processCreateInfo(CreateInfo createInfo) {
        movieDatabase.addMovie(createInfo.getMovie());
    }

    public void processTransactionInfo(TransactionInfo transactionInfo) {
        Movie movie =  transactionInfo.getMovie();
        movieDatabase.removeMovieWithTitle(movie.getTitle());
        movie.setProductionCompany(transactionInfo.getNewOwner());
        movieDatabase.addMovie(movie);
        if (clientsByName.containsKey(transactionInfo.getNewOwner())) {
            try {
                clientsByName.get(transactionInfo.getNewOwner()).write(new InfoWrapper("create", movie));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new Server(8080, "movies.txt");
    }

}
