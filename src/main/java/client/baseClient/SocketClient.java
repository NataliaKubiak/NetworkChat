package client.baseClient;

import java.io.IOException;

public interface SocketClient {

    String readLine() throws IOException;
    void sendMessage(String msg);
    void close() throws IOException;
}
