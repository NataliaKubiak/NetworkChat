package client;

import clientSocket.ClientSocketImpl;
import clientSocket.ClientSocket;
import utils.PropertiesLoader;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient2 extends BaseClient {

    public ChatClient2(ClientSocket socketClient, Scanner scanner, PrintStream outStream) {
        super(socketClient, scanner, outStream);
    }

    public static void main(String[] args) throws IOException {

        PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
        int serverPort = Integer.parseInt(propertiesLoader.getProperty("server.port"));

        Socket clientSocket = new Socket("localhost", serverPort);
        ClientSocket realClientSocket = new ClientSocketImpl(clientSocket);

        Scanner scanner = new Scanner(System.in);
        PrintStream outStream = new PrintStream(System.out);

        BaseClient chatClient1 = new ChatClient1(realClientSocket, scanner, outStream);
        chatClient1.runLogic();
    }
}
