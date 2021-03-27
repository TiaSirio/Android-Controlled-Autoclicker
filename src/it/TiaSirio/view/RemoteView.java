package it.TiaSirio.view;

import it.TiaSirio.server.SocketClientConnection;
import it.TiaSirio.utils.Observer;

public class RemoteView extends View {
    private final SocketClientConnection socketClientConnection;

    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String msg) {
            notifyObservers(msg);
        }
    }

    public RemoteView(SocketClientConnection c) {
        this.socketClientConnection = c;
        MessageReceiver messageReceiver = new MessageReceiver();
        c.addObserver(messageReceiver);
    }



    @Override
    protected void sendMessage(Object message) {
        socketClientConnection.send(message);
    }

    @Override
    public void update(String arg) {
        sendMessage(arg);
    }


    public SocketClientConnection getConnection(){
        return this.socketClientConnection;
    }

}
