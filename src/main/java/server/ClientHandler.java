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
    private static final Logger logger = LogManager.getLogger();

    private ConcurrentMap<Integer, ClientHandler> activeClients;
    private BlockingQueue<Message> msgQueue;

    public ClientHandler(ClientSocket socketClient,
                         BlockingQueue<Message> msgQueue,
                         ConcurrentMap<Integer,ClientHandler> activeClients) {
        this.socketClient = socketClient;
        this.msgQueue = msgQueue;
        this.activeClients = activeClients;
        port = socketClient.getPort();
    }

    @Override
    public void run() {
        try {
            logger.info("New connection accepted on PORT {}", port);
            socketClient.sendMessage(String.valueOf(port));

            String name = socketClient.getMessage();
            if (name == null) {
                return;
            } else if (name.isEmpty()) {
                name = "Client " + port;
            }
            logger.info("Received a name: {}", name);

            msgQueue.put(new Message(port, name, LocalDateTime.now(), name + " joined our cool chat!"));

            boolean flag = true;
            while (flag) {
                String input = socketClient.getMessage();
                if ("/exit".equalsIgnoreCase(input)) {
                    msgQueue.put(new Message(port, name, LocalDateTime.now(), name + " quit this amazing chat"));
                    flag = false;
                } else if (input == null) {
                    return;
                } else {
                    Message msg = new Message(port, name, LocalDateTime.now(), input);
                    msgQueue.put(msg);
                    logger.info("Msg was added to Queue: {}", msg);
                }
            }

        } catch (Exception e) {
            logger.error("An error occurred while performing the task", e);
        } finally {
            try {
                activeClients.remove(port);
                logger.info("Client on PORT: {} was removed from HashMap activeClients", port);
                socketClient.close();
            } catch (IOException e) {
                logger.error("An error occurred while performing the task", e);
            }
        }
    }

    public void sendMessage(String msg) {
        socketClient.sendMessage(msg);
    }

    public int getPort() {
        return port;
    }
}
