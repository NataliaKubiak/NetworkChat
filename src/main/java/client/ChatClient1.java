package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient1 {
    public static void main(String[] args) {

        try (Socket clientSocket = new Socket("localhost", 8060);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name:");
            String strFromConsole = scanner.nextLine();
            //out - what we send to server
            out.println(strFromConsole);

            boolean flag = true;
            while (flag) {
                System.out.print("Enter your msg (for quitting enter 'exit'): ");
                String msg = scanner.nextLine();
                out.println(msg);
                if ("exit".equals(msg)) {
                    flag = false;
                } else {
                    //in - what we get from server
                    System.out.println(in.readLine());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
