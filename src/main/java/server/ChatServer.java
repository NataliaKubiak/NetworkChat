package server;

import clientSocket.ClientSocket;
import clientSocket.ClientSocketImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class ChatServer {

    private final ServerSocket serverSocket;
    public static ConcurrentMap<Integer, ClientHandler> activeClients;
    public static BlockingQueue<Message> msgQueue;
    private final MessageSender messageSender;
    private final ExecutorService clientThreadPool;

    private static final Logger logger = LogManager.getLogger();

    public ChatServer(ServerSocket serverSocket,
                      ConcurrentMap<Integer, ClientHandler> activeClients,
                      BlockingQueue<Message> msgQueue,
                      MessageSender messageSender,
                      ExecutorService clientThreadPool) {
        this.serverSocket = serverSocket;
        this.activeClients = activeClients;
        this.msgQueue = msgQueue;
        this.messageSender = messageSender;
        this.clientThreadPool = clientThreadPool;
    }

    public void runLogic() {
        try {
            logger.info("Server started on PORT {}", serverSocket.getLocalPort());
            startMessageSenderThread(messageSender, false);

            while (!serverSocket.isClosed()) {
                try {
                    ClientSocket clientSocket = new ClientSocketImpl(serverSocket.accept());
                    ClientHandler clientHandler = getClientHandler(clientSocket);
                    startClientThread(clientHandler);
                } catch (IOException e) {
                    logger.error("Error accepting client connection", e);
                }
            }
        } finally {
            closeSocketServer();
            shutdownClientThreadPool();
        }
    }

    //это я вынесла, чтобы можно было замокировать и вернуть clientHandlerMock
    ClientHandler getClientHandler(ClientSocket clientSocket) {
        return new ClientHandler(clientSocket, msgQueue, activeClients);
    }

    void startMessageSenderThread(MessageSender messageSender, boolean runSynchronously) {
        try {
            if (runSynchronously) {
                clientThreadPool.submit(messageSender).get();
                logger.info("Message Sender Thread started synchronously");
            } else {
                new Thread(messageSender).start();
//                clientThreadPool.submit(messageSender);
                logger.info("Message Sender Thread started");
            }
        } catch (Exception e) {
            logger.error("Failed to start Message Sender Thread", e);
        }
    }

    void startClientThread(ClientHandler clientHandler) {
        try {
            clientThreadPool.submit(clientHandler);
            activeClients.put(clientHandler.getPort(), clientHandler);
            logger.info("New client started on PORT {}", clientHandler.getPort());
            logger.info("Active clients: {}", activeClients.size());
        } catch (Exception e) {
            logger.error("Failed to start client thread on PORT {}", clientHandler.getPort(), e);
        }
    }

    void closeSocketServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.info("Server socket closed successfully");
            } catch (IOException e) {
                logger.error("Error closing socket Server", e);
            }
        }
    }

    void shutdownClientThreadPool() {
        try {
            clientThreadPool.shutdown();
            if (!clientThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                clientThreadPool.shutdownNow();
            }
            logger.info("Client thread pool shutdown successfully");
        } catch (InterruptedException e) {
            logger.error("Client thread pool shutdown interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
