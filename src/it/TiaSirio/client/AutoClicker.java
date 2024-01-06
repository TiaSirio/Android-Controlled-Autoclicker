package it.TiaSirio.client;


import it.TiaSirio.utils.Messages;
import it.TiaSirio.utils.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.URI;

public class AutoClicker extends JFrame implements Observer<String> {

    public static boolean checked = false;
    public static int value = 0;
    private Client client;
    private JPanel panel1;
    private JButton enableButton;
    private JButton disableButton;
    private JTextField delay;
    private JLabel IPField;
    private JLabel appField;
    private JCheckBox sword;
    private JCheckBox mining;

    public AutoClicker (Client client){ //throws IOException {
        this.client = client;
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Maiale_logo.png"));
        this.setIconImage(img);
        setResizable(false);

        setTitle("Autoclicker");
        setContentPane(panel1);
        appField.setText("<html><u>To control your AutoClicker from your smartphone, click here to download the app!</u></html>");
        setListeners();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        String IPString = null;
        try {
            IPString = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        IPField.setText("Your current IP is: " + IPString + " ➪ Insert it in the smartphone app ;)");
        IPField.setHorizontalAlignment(SwingConstants.CENTER);
        IPField.setHorizontalTextPosition(SwingConstants.CENTER);

        int spaceBelowComponents = 10;
        appField.setBorder(BorderFactory.createEmptyBorder(spaceBelowComponents, 0, spaceBelowComponents, 0));
        IPField.setBorder(BorderFactory.createEmptyBorder(spaceBelowComponents, 0, spaceBelowComponents, 0));

        int spaceAboveTitle = 5;
        panel1.setBorder(BorderFactory.createEmptyBorder(spaceAboveTitle, 0, 0, 0));
    }

    public void setListeners () throws NumberFormatException{
        enableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checked = true;
                sendMessage(Messages.START);
                if (delay.getText().equals("")) {
                    value = 0;
                } else {
                    value = Integer.parseInt(delay.getText());
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

        appField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                URI uri = null;
                try {
                    uri = new URI("https://github.com/TiaSirio/Android-Controlled-Autoclicker-App");
                    Desktop.getDesktop().browse(uri);
                } catch (URISyntaxException | IOException exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        sword.setText("Sword");
        sword.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mining.setSelected(false);
                delay.setText("600");
            }
        });

        mining.setText("Mining");
        mining.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                sword.setSelected(false);
                delay.setText("100");
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
            this.delay.setText(msg);
        }
    }
}