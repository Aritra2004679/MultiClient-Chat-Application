package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private JFrame frame;
    private JTextPane chatPane;
    private JTextField messageField;
    private JButton sendButton;

    public ChatClientGUI() {

        frame = new JFrame("Multi Client Chat");
        frame.setSize(450,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chatPane = new JTextPane();
        chatPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatPane);

        messageField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        connectToServer();
    }

    private void connectToServer() {

        try {

            socket = new Socket("localhost",1234);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(),true);

            String username =
                    JOptionPane.showInputDialog(frame,"Enter Username");

            out.println(username);

            appendMessage("You joined the chat",Color.GRAY);

            new Thread(() -> {

                try{

                    String message;

                    while((message = in.readLine())!=null){
                        appendMessage(message,Color.BLACK);
                    }

                }catch(IOException e){
                    appendMessage("Disconnected from server",Color.RED);
                }

            }).start();

        } catch(Exception e){
            JOptionPane.showMessageDialog(frame,"Server not running");
        }
    }

    private void sendMessage(){

        String message = messageField.getText();

        if(!message.isEmpty()){

            out.println(message);

            messageField.setText("");
        }
    }

    private void appendMessage(String message, Color color){

        try{

            SimpleDateFormat sdf =
                    new SimpleDateFormat("HH:mm:ss");

            String time = sdf.format(new Date());

            chatPane.setForeground(color);

            chatPane.getDocument().insertString(
                    chatPane.getDocument().getLength(),
                    "["+time+"] "+message+"\n",
                    null);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(() ->
                new ChatClientGUI());
    }
}