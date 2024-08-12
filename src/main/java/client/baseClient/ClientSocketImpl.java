package client.baseClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketImpl implements ClientSocket {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientSocketImpl(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public String getMessage() throws IOException {
        return in.readLine();
    }

    @Override
    public void sendMessage(String msg) {
        out.println(msg);
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    @Override
    public int getPort() {
        return socket.getPort();
    }
}
