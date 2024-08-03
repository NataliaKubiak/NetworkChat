package server;

import utils.Message;
import utils.logs.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientHandler implements Runnable {

    private Logger logger = Logger.getInstance();
    private final String source;

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
        source = "Client handler (Server Side) " + port;
    }

    @Override
    public void run() {

        try {
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            logger.log(source, "New connection accepted on port " + port);
            output.println(port);

            //in - what we get from client
            String name = input.readLine();
            if (name == null) {
                return;
            } else if (name.isEmpty()) {
                name = "Client " + port;
            }
            logger.log(source, "Received a name: " + name);

            LocalDateTime time = LocalDateTime.now();
            System.out.printf("[%s] Client name: %s\n", time, name);

            boolean flag = true;
            while (flag) {
                String input = this.input.readLine();
                if ("/exit".equalsIgnoreCase(input)) {
                    System.out.printf("[%s] %s quit this amazing chat (what a fool!)\n", time, name);
                    flag = false;
                } else if (input == null) {
                    return;
                } else {
                    Message msg = new Message(port, name, LocalDateTime.now(), input);
                    msgQueue.put(msg);
                    logger.log(source, "Msg was added to Queue: " + msg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                activeClients.remove(port);
                logger.log(source, "Client was removed from HashMap activeClients");
                clientSocket.close();
                logger.log(source, "ClientSocket on port " + port + " closed");
                if (input != null) {
                    input.close();
                    logger.log(source, "PORT " + port + " INPUT Stream closed");
                }
                if (output != null) {
                    output.close();
                    logger.log(source, "PORT " + port + " OUTPUT Stream closed");
                }
            } catch (IOException e) {
                logger.log(source, "Throw IOException");
                throw new RuntimeException(e);
            }
        }
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void sendMessage(String msg) {
        output.println(msg);
        logger.log(source, "Msg was sent from " + port + ". Msg text: " + msg);
    }

    public int getPort() {
        return port;
    }
}
