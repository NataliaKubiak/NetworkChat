package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.PropertiesLoader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.*;

class BaseClientTest {
    @Mock
    private Socket mockSocket;
    @Mock
    private PrintWriter mockOut;
    @Mock
    private BufferedReader mockIn;
    private TestClient testClient;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        when(mockSocket.getOutputStream()).thenReturn(System.out);
        when(mockSocket.getInputStream()).thenReturn(System.in);

        testClient = new TestClient(mockSocket, mockIn, mockOut) {
            @Override
            protected void runLogic() {
                PropertiesLoader propertiesLoader = mock(PropertiesLoader.class);
                when(propertiesLoader.getProperty("server.port")).thenReturn("12345");

                try {
                    when(mockIn.readLine()).thenReturn("Connected to server", "/exit");

                    System.setIn(new ByteArrayInputStream("name\n/exit\n".getBytes()));

                    super.runLogic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Test
    public void testClient() throws IOException {
        testClient.start();

        verify(mockOut, atLeastOnce()).println(anyString());
        verify(mockIn, atLeastOnce()).readLine();
    }
}