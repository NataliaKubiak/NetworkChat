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
                            activeClients.get(port).sendMessage(msg.getShortTimeStamp() + " | INFO: " + msg.getText());
                            logger.info("Msg was sent from Server to Client on PORT {}. MSG INFO: Author: Info-message. TimeStamp: {}. Text: {}", port, msg.getFullTimeStamp(), msg.getText());
                        } else {
                            activeClients.get(port).sendMessage(msg.getShortTimeStamp() + " | " + msg.getName() + ": " + msg.getText());
                            logger.info("Msg was sent from Server to Client on PORT {}. MSG INFO: Author: {}. TimeStamp: {}. Text: {}", port, msg.getName(), msg.getFullTimeStamp(), msg.getText());
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error("An error occurred while performing the task", e);
            }
        }
    }
}
