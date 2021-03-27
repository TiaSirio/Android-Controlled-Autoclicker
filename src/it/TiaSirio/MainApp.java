package it.TiaSirio;

import it.TiaSirio.client.Client;
import it.TiaSirio.server.Server;

import java.io.IOException;

public class MainApp implements Runnable {

    public MainApp() throws IOException{

    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = null;
                try {
                    server = new Server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server.run();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MainApp mainApp = null;
                try {
                mainApp = new MainApp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainApp.run();
            }
        }).start();
    }

    @Override
    public void run() {
        Client client = new Client("127.0.0.1", 15986);
        try {
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
