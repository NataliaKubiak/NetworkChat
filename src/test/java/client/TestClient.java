//package client;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//public class TestClient extends BaseClient {
//    private Socket socket;
//    private BufferedReader in;
//    private PrintWriter out;
//
//    public TestClient(Socket socket, BufferedReader in, PrintWriter out) {
//        this.socket = socket;
//        this.in = in;
//        this.out = out;
//    }
//
//    public void start() {
//        runLogic();
//    }
//
//    protected void runLogic() {
//        out.println("Connected to server");
//        try {
//            String message;
//            while ((message = in.readLine()) != null) {
//                out.println("Received: " + message);
//                if (message.equals("/exit")) { // Условие выхода из цикла
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
