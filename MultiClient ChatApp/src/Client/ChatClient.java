package client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 1234);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            // Ask for username
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            out.println(username);

            System.out.println("You joined the chat");

            // Thread to receive messages from server
            Thread receiveThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            receiveThread.start();

            // Send messages to server
            while (true) {

                String message = scanner.nextLine();

                // Exit command
                if (message.equalsIgnoreCase("exit")) {
                    out.println(message);
                    System.out.println("You left the chat.");
                    socket.close();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Unable to connect to server.");
        }
    }
}