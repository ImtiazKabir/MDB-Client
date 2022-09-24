package com.mdb.movieserver;


import archive.info.*;
import util.SocketWrapper;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ReadFromClientThread implements Runnable {
    private final SocketWrapper clientSocketWrapper;
    private final Thread thread;
    private final Server server;

    private String clientName;

    public ReadFromClientThread(final Socket clientSocket, final Server server) throws IOException {
        this.server = server;
        clientSocketWrapper = new SocketWrapper(clientSocket);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

//        try {
//            InfoWrapper initInfo = (InfoWrapper) clientSocketWrapper.read();
//            assert(initInfo.getCommand().equals("login"));
//            LoginInfo loginInfo = initInfo.getContent();
//            new Thread(()->{handleLoginInfo(loginInfo);}).start();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        while(!thread.isInterrupted()) {
            try {
                InfoWrapper infoFromClient = (InfoWrapper) clientSocketWrapper.read();
                new Thread(()->{handle(infoFromClient);}).start();
            } catch(SocketException | EOFException e) {
                server.getClientsByName().remove(clientName);
                System.out.println("Client closed connection");
                break;
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

    }

    private void handleLoginInfo(LoginInfo loginInfo) {
        SessionInfo sessionInfo;
        if (!loginInfo.getUsername().isEmpty() && loginInfo.getPassword().equals(loginInfo.getUsername())) {
            sessionInfo = server.getSessionInfo(loginInfo);
        } else {
            sessionInfo = null;
        }

        server.getClientsByName().put(loginInfo.getUsername(), clientSocketWrapper);
        InfoWrapper infoWrapper = new InfoWrapper("session", sessionInfo);
        try {
            clientSocketWrapper.write(infoWrapper);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handle(InfoWrapper infoWrapper) {
        String cmd = infoWrapper.getCommand();
        if (cmd.equals("login")) {
            LoginInfo loginInfo = infoWrapper.getContent();
            handleLoginInfo(loginInfo);
        } else if (cmd.equals("create")) {
            CreateInfo createInfo = infoWrapper.getContent();
            server.processCreateInfo(createInfo);
        } else if (cmd.equals("transaction")) {
            TransactionInfo transactionInfo = infoWrapper.getContent();
            server.processTransactionInfo(transactionInfo);
        }
    }

}
