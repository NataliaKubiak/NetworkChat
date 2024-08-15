package server;

import client.baseClient.ClientSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientHandler implements Runnable {

    private ClientSocket socketClient;
    private final int port;
    private String clientName;
    private ConcurrentMap<Integer, ClientHandler> activeClients;
    private BlockingQueue<Message> msgQueue;

    private static final Logger logger = LogManager.getLogger();

    public ClientHandler(ClientSocket socketClient,
                         BlockingQueue<Message> msgQueue,
                         ConcurrentMap<Integer, ClientHandler> activeClients) {
        this.socketClient = socketClient;
        this.msgQueue = msgQueue;
        this.activeClients = activeClients;
        port = socketClient.getPort();
    }

    @Override
    public void run() {
        try {
            acceptNewConnection();

            if (!setClientNameAndGreet()) return;

            processChatClientInput();
        } catch (InterruptedException e) {
            logger.error("Thread was interrupted: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("IO Error occurred: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
        } finally {
            disconnectClient();
        }
    }

    private void processChatClientInput() throws IOException, InterruptedException {
        try {
            boolean flag = true;
            while (flag) {
                String input = socketClient.getMessage();
                if ("/exit".equalsIgnoreCase(input)) {
                    msgQueue.put(new Message(port, clientName, LocalDateTime.now(), clientName + " quit this amazing chat"));
                    logger.info("Client on PORT {} sent '/exit' message", port);
                    flag = false;
                } else if (input == null) {
                    break;
                } else {
                    Message msg = new Message(port, clientName, LocalDateTime.now(), input);
                    msgQueue.put(msg);
                    logger.info("Message was added to Queue: {}", msg);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Error adding Message to Queue while reading client message", e);
            throw e;
        } catch (IOException e) {
            logger.error("Error reading from socket while reading client message", e);
            throw e;
        }
    }

    private boolean setClientNameAndGreet() throws IOException, InterruptedException {
        try {
            clientName = socketClient.getMessage();
            if (clientName == null) {
                return false;
            } else if (clientName.isEmpty()) {
                clientName = "Client " + port;
            }
            logger.info("Client on PORT {} received a name: {}", port, clientName);
            msgQueue.put(new Message(port, clientName, LocalDateTime.now(), clientName + " joined our cool chat!"));
            return true;

        } catch (InterruptedException e) {
            logger.error("Error adding Message to Queue while setting client the name", e);
            throw e;
        } catch (IOException e) {
            logger.error("Error reading from socket while setting client the name", e);
            throw e;
        }
    }

    private void acceptNewConnection() {
        socketClient.sendMessage(String.valueOf(port));
        logger.info("New connection accepted on PORT {}", port);
    }

    private void disconnectClient() {
        try {
            activeClients.remove(port);
            logger.info("Client on PORT: {} was removed from ActiveClients", port);
            socketClient.close();
            logger.info("Client socket on PORT {} closed successfully", port);
        } catch (IOException e) {
            logger.error("Error closing socket client", e);
        }
    }

    public void sendMessage(String msg) {
        socketClient.sendMessage(msg);
        logger.info("Message was sent successfully from ClientHandler on PORT {}", port);
    }

    public int getPort() {
        return port;
    }
}
