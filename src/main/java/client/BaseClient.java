package client;

import utils.logs.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public abstract class BaseClient {
//    private int id;
    private Logger logger = Logger.getInstance();
//    private String sourse = "Base Client " + id;

    protected void runLogic() {
        try (Socket clientSocket = new Socket("localhost", 8060);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            int id = Integer.parseInt(in.readLine());
            String sourse = "Base Client " + id;
            System.out.println("Client port/id = " + id);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name:");
            String strFromConsole = scanner.nextLine();
            //out - what we send to server
            out.println(strFromConsole);
            logger.log(sourse, "User entered Name: " + strFromConsole + ". It was sent to Client Handler");

            new Thread(() -> {
                logger.log(sourse + "Reading msg thread", "Started");
                while (true) {
                    String input;
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(input);
                    logger.log(sourse + "Reading msg thread", "Received and printed to console msg: " + input);
                }
            }).start();


            boolean flag = true;
            while (flag) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                logger.log(sourse, "Before Scanner where User enters the Msg");
                System.out.println(">>>>>>>>>> Enter your msg (for quitting enter '/exit')");
                System.out.print(LocalDateTime.now().format(formatter) + " | You: ");
                String msg = scanner.nextLine();
                logger.log(sourse, "User entered the msg: " + msg);
                out.println(msg);
                logger.log(sourse, "Msg was sent to Client Handler");
                if ("/exit".equalsIgnoreCase(msg)) {
                    flag = false;
                    logger.log(sourse, "Exit while loop");
                } else {
                    //in - what we get from server
//                    String input = in.readLine();
//                    System.out.println(input);
//                    logger.log(sourse, "Received and printed to console msg: " + input);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            logger.log(sourse, "Exception happened");
        }
    }

//    public int getId() {
//        return id;
//    }
}
