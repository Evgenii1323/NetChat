package org.example.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static List<Connection> connections = new LinkedList<>();
    private static final String PATH = "Settings.txt";

    public static void main(String[] args) throws  IOException {
        File file = new File(PATH);
        Scanner scanner = new Scanner(file);
        String host = scanner.nextLine();
        String input = scanner.nextLine();
        int port = Integer.parseInt(input);

        System.out.println("Server started ");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    connection.start();
                    connections.add(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}