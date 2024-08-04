package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private int senderPort;
    private String name = null;
    private LocalDateTime timeStamp;
    private String text;

    public Message(int senderPort, String name, LocalDateTime timeStamp, String text) {
        this.senderPort = senderPort;
        this.name = name;
        this.timeStamp = timeStamp;
        this.text = text;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public String getName() {
        if (name == null) {
            return "Client " + senderPort;
        }
        return name;
    }

    public String getFullTimeStamp() {
        return timeStamp + "";
    }

    public String getShortTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return timeStamp.format(formatter);
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderPort=" + senderPort +
                ", timeStamp='" + timeStamp + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
