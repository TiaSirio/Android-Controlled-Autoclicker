package it.TiaSirio.server;

import it.TiaSirio.utils.Observable;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientConnection extends Observable<String> implements Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;
    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public boolean isActive () {
        return active;
    }

    public synchronized void send (Object msg) {
        try {
            out.reset();
            out.writeObject(msg);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner in;
        try {
            String read;
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            if (!server.appInExecution()){
                server.startingApp(this);
            } else {
                server.addingConnection(this);
            }
            while (isActive()){
                read = in.nextLine();
                notifyObservers(read);
            }
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }
    }
}