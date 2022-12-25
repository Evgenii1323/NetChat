package org.example.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SecondClient {
    private static final String PATH = "Settings.txt";

    public static void main(String[] args) throws  IOException {
        File file = new File(PATH);
        Scanner scanner = new Scanner(file);
        String host = scanner.nextLine();
        String input = scanner.nextLine();
        int port = Integer.parseInt(input);

        Scanner secondScanner = new Scanner(System.in);
        System.out.println("Напишите имя ");
        String name = secondScanner.nextLine();
        System.out.println("Напишите сообщение или EXIT для выхода ");
        try (Socket socket = new Socket(host, port);
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            printWriter.println(name);

            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        String serverMessage = bufferedReader.readLine();
                        if (serverMessage.equals("EXIT")) {
                            break;
                        }
                        System.out.println(serverMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            while (true) {
                String clientMessage = secondScanner.nextLine();
                if (clientMessage.equals("EXIT")) {
                    printWriter.println(clientMessage);
                    break;
                }
                printWriter.println(clientMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}