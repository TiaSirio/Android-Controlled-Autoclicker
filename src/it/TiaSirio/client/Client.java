package it.TiaSirio.client;

import it.TiaSirio.server.SocketClientConnection;
import it.TiaSirio.utils.Observable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Client extends Observable<String> {
    private final String ip;
    private final int PORT;
    private boolean active = true;
    private PrintWriter socketOut;

    public Client (String ip, int PORT) {
        this.ip = ip;
        this.PORT = PORT;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketInput){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()){
                        Object inputObject = socketInput.readObject();
                        if (inputObject instanceof String) {
                            notifyObservers((String)inputObject);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return thread;
    }

    public void writeToSocket(String msg){
        socketOut.println(msg);
        socketOut.flush();
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, PORT);
        System.out.println("Client connected to the server.");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        AutoClicker autoClicker = new AutoClicker(this);
        addObserver(autoClicker);
        autoClicker.pack();
        autoClicker.setVisible(true);
        try {
            Thread thread = asyncReadFromSocket(socketIn);
            thread.join();
        } catch (InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side.");
        } finally {
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
