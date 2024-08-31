package server;

import utils.Message;
import utils.PropertiesLoader;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class Main {
    public static final int MAX_CLIENTS = 10;

    public static void main(String[] args) throws IOException {
        PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
        int serverPort = Integer.parseInt(propertiesLoader.getProperty("server.port"));

        ServerSocket serverSocket = new ServerSocket(serverPort);

        ConcurrentMap<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>(10);
        BlockingQueue<Message> msgQueue = new ArrayBlockingQueue<>(100);
        MessageSender messageSender = new MessageSender(activeClients, msgQueue);
        ExecutorService clientThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);

        ChatServer chatServer = new ChatServer(serverSocket,
                activeClients,
                msgQueue,
                messageSender,
                clientThreadPool);
        chatServer.runLogic();
    }
}
