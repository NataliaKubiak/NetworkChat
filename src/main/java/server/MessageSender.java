package server;

import utils.Message;
import utils.logs.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class MessageSender implements Runnable {

    private Logger logger = Logger.getInstance();
    private final String source = "Message Sender";

    private ConcurrentMap<Integer, ClientHandler> activeClients;
    private BlockingQueue<Message> msgQueue;

    public MessageSender(ConcurrentMap<Integer, ClientHandler> activeClients, BlockingQueue<Message> msgQueue) {
        this.activeClients = activeClients;
        this.msgQueue = msgQueue;
    }

    //String.format("%s: %s\n", name, msg)

    @Override
    public void run() {
        while (true) {
            try {
                logger.log(source, "beginning while loop, waiting for msgQueue.take()");
                Message msg = msgQueue.take();
                logger.log(source, "msgQueue.take() was just proceed");

                for (int port : activeClients.keySet()) {
                    logger.log(source, "Executing for loop for Client port: " + port + " Msg text: " + msg.getText());

                    if (port != msg.getSenderId()) {
                        activeClients.get(port).sendMessage("[" + msg.getTimeStamp() + "] " + msg.getName() + ": " + msg.getText());
                        logger.log(source, "Msg was sent by activeClients.get(port).sendMessage(): " + msg.getText());
                    }
                }
                logger.log(source, "For loop was finished. Outside of for loop");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
