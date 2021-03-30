package it.TiaSirio.server;

import it.TiaSirio.utils.Messages;
import it.TiaSirio.utils.Observable;
import it.TiaSirio.utils.Observer;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;

public class AutoClickerAppFromServer extends Observable<String> implements Observer<String> {
    private ArrayList<SocketClientConnection> activeConnection = new ArrayList<>();
    private Robot robot;
    public boolean checked = false;
    public int value = 100;

    public AutoClickerAppFromServer() throws AWTException {
        robot = new Robot();
    }

    public void addConnection(SocketClientConnection socketClientConnection){
        this.activeConnection.add(socketClientConnection);
    }



    public void startApp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (checked && value >= 100){
                        try {
                            Thread.sleep(value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        //System.out.println("Attivo");
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //System.out.println("Inattivo");
                    }
                }
            }
        }).start();
    }

    private synchronized void changeStatus(boolean status) {
        if(this.value >= 100) {
            this.checked = status;
        }
        String message = Messages.STOP;
        if (this.checked) {
            message = Messages.START;
        }
        notifyObservers(message);
    }

    private synchronized void changeValue(int value){
        this.value = value;
        notifyObservers(String.valueOf(value));
    }

    @Override
    public void update(String msg) {
        if (msg.equals(Messages.START)){
            changeStatus(true);
        } else if (msg.equals(Messages.STOP)){
            changeStatus(false);
        } else {
            changeValue(Math.max(Integer.parseInt(msg),this.value));
        }
    }
}
