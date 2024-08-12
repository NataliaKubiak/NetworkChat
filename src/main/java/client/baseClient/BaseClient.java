package client.baseClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public abstract class BaseClient {

    private final ClientSocket clientSocket;
    private int clientPort;
    private final Scanner scanner;
    private final PrintStream outStream;

    protected final Logger logger;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

    public BaseClient(ClientSocket clientSocket,
                      Scanner scanner,
                      PrintStream outStream) {
        this.clientSocket = clientSocket;
        this.scanner = scanner;
        this.outStream = outStream;
        logger = LogManager.getLogger(getClass());
    }

    public void runLogic() {
        try {
            initializeClientPort();
            greetUser();

            startReadingThread();
            processUserInput();
        } catch (NumberFormatException e) {
            logger.error("Failed to parse client port number: {}", e.getMessage());
        } catch (IOException e) {
            logger.error("IO Error occurred: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
        } finally {
            closeSocketClient();
        }
    }

    private void initializeClientPort() throws IOException {
        try {
            clientPort = Integer.parseInt(clientSocket.getMessage());
            logger.info("Client on PORT {} started", clientPort);
        } catch (NumberFormatException e) {
            logger.error("Invalid port number received from server", e);
            throw e;
        } catch (IOException e) {
            logger.error("Error reading port number from server", e);
            throw e;
        }
    }

    private void greetUser() {
        outStream.println("Enter your name: ");
        String name = scanner.nextLine();
        outStream.println("Hello " + name + "! Type your msg and press ENTER (for quitting type '/exit')");
        clientSocket.sendMessage(name);
        logger.info("User entered Name: {}", name);
    }

    private void startReadingThread() {
        new Thread(() -> {
            logger.info("Reading msg thread for Client on PORT {} started", clientPort);
            try {
                while (true) {
                    String input = clientSocket.getMessage();
                    if (input == null) {
                        logger.info("Server closed the connection");
                        break;
                    }
                    outStream.println(input);
                    logger.info("Received and printed to console PORT: {} msg: '{}'", clientPort, input);
                }
            } catch (IOException e) {
                logger.error("Error reading from socket", e);
            }
        }).start();
    }

    private void processUserInput() {
        boolean flag = true;
        while (flag) {
            outStream.println(LocalDateTime.now().format(formatter) + " | You: ");
            String msg = scanner.nextLine();
            logger.info("User entered the msg: {}", msg);

            clientSocket.sendMessage(msg);
            if ("/exit".equalsIgnoreCase(msg)) {
                logger.info("User requested to exit. Stopping client on PORT {}", clientPort);
                flag = false;
            }
        }
    }

    private void closeSocketClient() {
        try {
            clientSocket.close();
            logger.info("Socket client closed successfully");
        } catch (IOException e) {
            logger.error("Error closing socket Client", e);
        }
    }
}
