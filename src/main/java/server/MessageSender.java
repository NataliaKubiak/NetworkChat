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

    public MessageSender(ConcurrentMap<Integer, ClientHandler> activeClients,
                         BlockingQueue<Message> msgQueue) {
        this.activeClients = activeClients;
        this.msgQueue = msgQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = getMessageFromQueue();

                for (int port : activeClients.keySet()) {
                    if (port != msg.getSenderPort()) {

                        if (msg.containsText("joined our cool chat") || msg.containsText("quit this amazing chat")) {
                            sendInfoMessage(port, msg);
                        } else {
                            sendMessageToClient(port, msg);
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error("Thread was interrupted: {}", e.getMessage());
            }
        }
    }

    private Message getMessageFromQueue() throws InterruptedException {
        Message msg = null;

        try {
            msg = msgQueue.take();
        } catch (InterruptedException e) {
            logger.error("Error getting message from Message Queue", e);
        }
        return msg;
    }

    private void sendInfoMessage(int recipientPort, Message msg) {
        activeClients
                .get(recipientPort)
                .sendMessage(msg.getShortTimeStamp() + " | INFO: " + msg.getText());

        logger.info("Msg was sent from Server to Client on PORT {}. MSG INFO: Author: Info-message. TimeStamp: {}. Text: {}",
                recipientPort, msg.getFullTimeStamp(), msg.getText());
    }

    private void sendMessageToClient(int recipientPort, Message msg) {
        activeClients
                .get(recipientPort)
                .sendMessage(msg.getShortTimeStamp() + " | " + msg.getName() + ": " + msg.getText());

        logger.info("Msg was sent from Server to Client on PORT {}. MSG INFO: Author: {}. TimeStamp: {}. Text: {}",
                recipientPort, msg.getName(), msg.getFullTimeStamp(), msg.getText());
    }


}
