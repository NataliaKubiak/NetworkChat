package server;

import utils.Message;
import utils.logs.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatServer {
    public static final int PORT = 8060;
    private Logger logger = Logger.getInstance();
    private final String source = "Chat Server";

    public static ConcurrentMap<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>(10);
    public static BlockingQueue<Message> msgQueue = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");
            MessageSender messageSender = new MessageSender(activeClients, msgQueue);
            new Thread(messageSender).start();

            while (true) {
                    clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, msgQueue, activeClients);
                    activeClients.put(clientSocket.getPort(), clientHandler);
                    new Thread(clientHandler).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
