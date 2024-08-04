package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PropertiesLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public abstract class BaseClient {
    private int clientPort;

    protected final Logger logger;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

    public BaseClient() {
        logger = LogManager.getLogger(getClass());
    }

    protected void runLogic() {
        PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
        int serverPort = Integer.parseInt(propertiesLoader.getProperty("server.port"));

        try (Socket clientSocket = new Socket("localhost", serverPort);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            clientPort = Integer.parseInt(in.readLine());
            logger.info("Client on PORT {} started", clientPort);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            System.out.println("Hello " + name + "! Type your msg and press ENTER (for quitting type '/exit')");
            out.println(name);
            logger.info("User entered Name: {}", name);

            new Thread(() -> {
                logger.info("Reading msg thread for Client on PORT {} started", clientPort);
                while (true) {
                    String input;
                    try {
                        input = in.readLine();
                        if (input == null) {
                            return;
                        }
                    } catch (IOException e) {
                        return;
                    }

                    System.out.println(input);
                    logger.info("Received and printed to console PORT: {} msg: '{}'", clientPort, input);
                }
            }).start();

            boolean flag = true;
            while (flag) {
                System.out.println(LocalDateTime.now().format(formatter) + " | You: ");
                String msg = scanner.nextLine();
                logger.info("User entered the msg: {}", msg);

                out.println(msg);
                if ("/exit".equalsIgnoreCase(msg)) {
                    flag = false;
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred while performing the task", e);
        }
    }
}
