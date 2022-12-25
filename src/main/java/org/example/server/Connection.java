package org.example.server;

import java.io.*;
import java.net.Socket;
import java.time.*;
import java.time.format.*;
import java.util.Scanner;

public class Connection extends Thread {
    private final PrintWriter PRINT_WRITER;
    private final BufferedReader BUFFERED_READER;
    private final File FILE;

    public Connection(Socket socket) throws  IOException {
        this.PRINT_WRITER = new PrintWriter(socket.getOutputStream(), true);
        this.BUFFERED_READER = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.FILE = new File("Stories.txt");
    }

    @Override
    public void run() {
        try {
            String name = BUFFERED_READER.readLine();
            readStories();
            while (true) {
                String message = BUFFERED_READER.readLine();
                if (message.equals("EXIT")) {
                    PRINT_WRITER.println(message);
                    break;
                }
                writeStories(name, message);
                for (Connection connection : Server.connections) {
                    connection.sendMessage(name, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String name, String message) {
        PRINT_WRITER.println("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")) + "] " + name + " написал(а) " + message);
    }

    public synchronized void readStories() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(FILE))) {
            String c;
            while ((c = fileReader.readLine()) != null) {
                PRINT_WRITER.println(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeStories(String name, String message) {
        try (FileWriter fileWriter = new FileWriter(FILE, true)) {
            fileWriter.write("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm:ss")) + "] " + name + " написал(а) " + message);
            fileWriter.write('\n');
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}