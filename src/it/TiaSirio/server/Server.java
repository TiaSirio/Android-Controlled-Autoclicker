package it.TiaSirio.server;

import it.TiaSirio.view.RemoteView;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{

    private static final int PORT = 15986;
    private final ServerSocket serverSocket;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private AutoClickerAppFromServer autoClickerAppFromServer = null;
    private HashMap<SocketClientConnection, RemoteView> connectedClient = new HashMap<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public synchronized void startingApp (SocketClientConnection socketClientConnection) throws AWTException {
        this.autoClickerAppFromServer = new AutoClickerAppFromServer();
        autoClickerAppFromServer.addConnection(socketClientConnection);
        RemoteView remoteView = new RemoteView(socketClientConnection);
        remoteView.addObserver(this.autoClickerAppFromServer);
        this.connectedClient.put(socketClientConnection, remoteView);
        autoClickerAppFromServer.addObserver(remoteView);
        autoClickerAppFromServer.startApp();
    }

    public synchronized void addingConnection (SocketClientConnection socketClientConnection){
        autoClickerAppFromServer.addConnection(socketClientConnection);
        RemoteView remoteView = new RemoteView(socketClientConnection);
        remoteView.addObserver(this.autoClickerAppFromServer);
        this.connectedClient.put(socketClientConnection, remoteView);
        autoClickerAppFromServer.addObserver(remoteView);
    }

    public synchronized void removeConnection(SocketClientConnection socketClientConnection){
        RemoteView remoteView = connectedClient.get(socketClientConnection);
        remoteView.removeObserver(this.autoClickerAppFromServer);
        this.autoClickerAppFromServer.removeObserver(remoteView);
    }

    public synchronized boolean appInExecution(){
        return this.autoClickerAppFromServer != null;
    }

    @Override
    public void run() {
        System.out.println("Server is listening on port: " + PORT);
        do {
            try {
                Socket socket = serverSocket.accept();
                SocketClientConnection socketClientConnection = new SocketClientConnection(socket,this);
                executorService.submit(socketClientConnection);
                System.out.println("Connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (true);
    }
}
