package it.TiaSirio.client;


import it.TiaSirio.utils.Messages;
import it.TiaSirio.utils.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoClicker extends JFrame implements Observer<String> {

    public static boolean checked = false;
    public static int value = 0;
    private Client client;
    private JPanel panel1;
    private JButton enableButton;
    private JButton disableButton;
    private JTextField textField1;

    public AutoClicker (Client client){ //throws IOException {
        this.client = client;
        //Image img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Maiale.png"));
        //this.setIconImage(img);
        //Image img = Toolkit.getDefaultToolkit().getImage(AutoClicker.class.getResource("Maiale.png"));
        //this.setIconImage(img);
        setResizable(false);
        setTitle("Autoclicker");
        setContentPane(panel1);
        setListeners();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setListeners () throws NumberFormatException{
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checked = true;
                sendMessage(Messages.START);
                if (textField1.getText().equals("")) {
                    value = 0;
                } else {
                    value = Integer.parseInt(textField1.getText());
                }
                sendMessage(String.valueOf(value));
            }
        });

        disableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checked = false;
                sendMessage(Messages.STOP);
            }
        });
    }

    private void sendMessage(String message){
        this.client.writeToSocket(message);
    }

    @Override
    public void update(String msg) {
        System.out.println(msg);
        if(msg.equals(Messages.START)){
            this.disableButton.setEnabled(true);
            this.enableButton.setEnabled(false);
        } else if (msg.equals(Messages.STOP)){
            this.disableButton.setEnabled(false);
            this.enableButton.setEnabled(true);
        } else {
            this.textField1.setText(msg);
        }
    }
}