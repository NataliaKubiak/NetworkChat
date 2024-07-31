package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("New connection accepted on port " + clientSocket.getPort());
            //in - what we get from client
            String name = in.readLine();
            if (name == null) {
                return;
            } else if (name.isEmpty()) {
                name = "Client " + clientSocket.getPort();
            }
            LocalDateTime time = LocalDateTime.now();
            System.out.printf("[%s] Client name: %s\n", time, name);

            boolean flag = true;
            while (flag) {
                String msg = in.readLine();
                if ("/exit".equalsIgnoreCase(msg)) {
                    System.out.printf("[%s] %s quit this amazing chat (what a fool!)\n", time, name);
                    flag = false;
                } else if (msg == null) {
                    return;
                } else {
                    System.out.printf("[%s] %s: %s\n", time, name, msg);
                    //out - what we send to client
                    out.println(String.format("[%s] %s: %s", time, name, msg));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println(">>>>> ClientSocket on port " + clientSocket.getPort() + " closed");
                if (in != null) {
                    in.close();
                    System.out.println(">>>>> PORT " + clientSocket.getPort() + " INPUT Stream closed");
                }
                if (out != null) {
                    out.close();
                    System.out.println(">>>>> PORT " + clientSocket.getPort() + " OUTPUT Stream closed");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
