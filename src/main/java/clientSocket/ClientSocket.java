package clientSocket;

import java.io.IOException;

public interface ClientSocket {

    String getMessage() throws IOException;

    void sendMessage(String msg);

    void close() throws IOException;

    int getPort();
}
