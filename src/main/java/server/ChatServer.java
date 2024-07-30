package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ChatServer {
    public static final int PORT = 8060;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер стартовал");
            while (true) {
                try (Socket clientSocket = serverSocket.accept(); //waiting client to connect
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    System.out.println("New connection accepted");
                    //in - what we get from client
                    final String name = in.readLine();
                    LocalDateTime time = LocalDateTime.now();
                    System.out.printf("[%s] Client name: %s\n", time, name);

                    boolean flag = true;
                    while (flag) {
                        String msg = in.readLine();
                        if ("exit".equals(msg)) {
                            System.out.printf("[%s] %s quit this amazing chat (what a fool!)\n", time, name);
                            flag = false;
                        } else {
                            System.out.printf("[%s] %s: %s\n", time, name, msg);
                            //out - what we send to client
                            out.println(String.format("[%s] %s: %s", time, name, msg));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
