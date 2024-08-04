package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Message;
import utils.PropertiesLoader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatServer {
    private static int serverPort;
    private static final Logger logger = LogManager.getLogger();

    public static ConcurrentMap<Integer, ClientHandler> activeClients = new ConcurrentHashMap<>(10);
    public static BlockingQueue<Message> msgQueue = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
        serverPort = Integer.parseInt(propertiesLoader.getProperty("server.port"));

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(serverPort);
            logger.info("Server started");
            MessageSender messageSender = new MessageSender(activeClients, msgQueue);
            new Thread(messageSender).start();

            while (true) {
                clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, msgQueue, activeClients);
                activeClients.put(clientSocket.getPort(), clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (Exception ex) {
            logger.error("An error occurred while performing the task", ex);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                    logger.info("Server socket closed");
                } catch (IOException ex) {
                    logger.error("An error occurred while performing the task", ex);
                }
            }
        }
    }
}
