package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private int senderId;
    private String name = null;
    private LocalDateTime timeStamp;
    private String text;

    public Message(int senderId, String name, LocalDateTime timeStamp, String text) {
        this.senderId = senderId;
        this.name = name;
        this.timeStamp = timeStamp;
        this.text = text;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getName() {
        if (name == null) {
            return "Client " + senderId;
        }
        return name;
    }

    public String getTimeStamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return timeStamp.format(formatter);
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId=" + senderId +
                ", timeStamp='" + timeStamp + '\'' +
                ", msg='" + text + '\'' +
                '}';
    }
}
