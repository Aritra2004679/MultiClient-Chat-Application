package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private Set<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket, Set<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    public void run() {

        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(), true);

            // Receive username
            username = in.readLine();
            System.out.println("User logged in: " + username);

            broadcast("SERVER: " + username + " joined the chat");

            String message;

            while ((message = in.readLine()) != null) {

                System.out.println(username + ": " + message);

                // Send message with username
                broadcast(username + ": " + message);
            }

        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        }

        finally {

            try {
                clients.remove(this);

                broadcast("SERVER: " + username + " left the chat");

                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String message) {

        synchronized (clients) {

            for (ClientHandler client : clients) {

                if (client.out != null) {
                    client.out.println(message);
                }

            }
        }
    }
}