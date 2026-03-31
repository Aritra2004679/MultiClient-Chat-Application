package server;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {

    // 🔹 Thread-safe Set
    private static Set<ClientHandler> clients =
            Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {

        System.out.println("Server started on port 1234...");

        try (ServerSocket serverSocket = new ServerSocket(1234)) {

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ClientHandler client =
                        new ClientHandler(socket, clients);

                clients.add(client);
                client.start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}