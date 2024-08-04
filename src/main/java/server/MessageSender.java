package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class MessageSender implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    private ConcurrentMap<Integer, ClientHandler> activeClients;
    private BlockingQueue<Message> msgQueue;

    public MessageSender(ConcurrentMap<Integer, ClientHandler> activeClients, BlockingQueue<Message> msgQueue) {
        this.activeClients = activeClients;
        this.msgQueue = msgQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = msgQueue.take();

                for (int port : activeClients.keySet()) {
                    if (port != msg.getSenderPort()) {
                        if (msg.getText().contains("joined our cool chat") || msg.getText().contains("quit this amazing chat")) {
                            activeClients.get(port).sendMessage(msg.getTimeStamp() + " | INFO: " + msg.getText());
                        } else {
                            activeClients.get(port).sendMessage(msg.getTimeStamp() + " | " + msg.getName() + ": " + msg.getText());
                        }
                        logger.info("Msg was sent: {}", msg.getText());
                    }
                }
            } catch (InterruptedException e) {
                logger.error("An error occurred while performing the task", e);
            }
        }
    }
}
