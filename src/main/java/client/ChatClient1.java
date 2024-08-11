package client;

import client.baseClient.BaseClient;
import client.baseClient.RealSocketClient;
import client.baseClient.SocketClient;
import utils.PropertiesLoader;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient1 extends BaseClient {

    public ChatClient1(SocketClient socketClient, Scanner scanner, PrintStream outStream) {
        super(socketClient, scanner, outStream);
    }

    public static void main(String[] args) throws IOException {

        PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
        int serverPort = Integer.parseInt(propertiesLoader.getProperty("server.port"));

        Socket clientSocket = new Socket("localhost", serverPort);
        SocketClient realSocketClient = new RealSocketClient(clientSocket);

        Scanner scanner = new Scanner(System.in);
        PrintStream outStream = new PrintStream(System.out);

        BaseClient chatClient1 = new ChatClient1(realSocketClient, scanner, outStream);
        chatClient1.runLogic();
    }
}
