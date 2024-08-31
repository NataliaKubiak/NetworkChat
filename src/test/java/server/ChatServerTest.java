package server;

import clientSocket.ClientSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Message;

import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

@ExtendWith(MockitoExtension.class)
class ChatServerTest {

    @Mock
    ServerSocket serverSocketMock;
    @Mock
    ConcurrentMap<Integer, ClientHandler> activeClientsMock;
    @Mock
    BlockingQueue<Message> msgQueueMock;
    @Mock
    MessageSender messageSenderMock;
    @Mock
    ExecutorService clientThreadPoolMock;
    @Mock
    ClientSocket clientSocketMock;
    @Mock
    ClientHandler clientHandlerMock;
    ChatServer chatServer;

    @BeforeEach
    void setUp() {
        chatServer = Mockito.spy(new ChatServer(serverSocketMock,
                activeClientsMock,
                msgQueueMock,
                messageSenderMock,
                clientThreadPoolMock));
    }

    //str.ifPresent(name -> assertEquals(name, str));
    @Test
    void messageSenderThreadStartSuccessfully() {
        chatServer.startMessageSenderThread(messageSenderMock, false);
        Mockito.verify(messageSenderMock, Mockito.times(1)).run();
    }

//    @Test
//    void throwExceptionIfMessageSenderNotStart() {
//        Mockito.doThrow(new Exception("Test!!!")).when(clientThreadPoolMock).submit(messageSenderMock).get();
//
//        Assertions.assertThrows(Exception.class, () -> chatServer.startMessageSenderThread(messageSenderMock, true));
//    }

    @Test
    void clientHandlerThreadStartSuccessfully() {
        ClientHandler x = Mockito.doReturn(clientHandlerMock).when(chatServer).getClientHandler(clientSocketMock);
        Mockito.doNothing().when(clientHandlerMock).getPort();
//        Mockito.doNothing().when(activeClientsMock).put(clientHandlerMock.getPort(), clientHandlerMock);

        chatServer.startClientThread(x);
        Mockito.verify(x, Mockito.times(1)).run();
    }

}