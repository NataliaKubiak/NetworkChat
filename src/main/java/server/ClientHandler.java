package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientHandler implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private Socket clientSocket;
    private ConcurrentMap<Integer, ClientHandler> activeClients;
    private BlockingQueue<Message> msgQueue;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private final int port;

    public ClientHandler(Socket clientSocket, BlockingQueue<Message> msgQueue, ConcurrentMap<Integer, ClientHandler> activeClients) {
        this.clientSocket = clientSocket;
        this.msgQueue = msgQueue;
        this.activeClients = activeClients;
        port = clientSocket.getPort();
    }

    @Override
    public void run() {

        try {
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            logger.info("New connection accepted on PORT {}", port);
            output.println(port);

            String name = input.readLine();
            if (name == null) {
                return;
            } else if (name.isEmpty()) {
                name = "Client " + port;
            }
            logger.info("Received a name: {}", name);

            msgQueue.put(new Message(port, name, LocalDateTime.now(), name + " joined our cool chat!"));

            boolean flag = true;
            while (flag) {
                String input = this.input.readLine();
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
                clientSocket.close();
                logger.info("ClientSocket on PORT {} closed", port);
                if (input != null) {
                    input.close();
                    logger.info("PORT {} INPUT Stream closed", port);
                }
                if (output != null) {
                    output.close();
                    logger.info("PORT {} OUTPUT Stream closed", port);
                }
            } catch (IOException e) {
                logger.error("An error occurred while performing the task", e);
            }
        }
    }

    public void sendMessage(String msg) {
        output.println(msg);
        logger.info("Msg was sent from Server to Client on PORT {}. Msg text: {}", port, msg);
    }
}
