package catcher;

import pitcher.Message;

import java.util.LinkedList;

public class Catcher {
    private LinkedList<Message> outboundMessages = new LinkedList<>();

    public synchronized Message getNextMessageToSend(long now) {
        Message message = outboundMessages.poll();
        if (message != null) {
            message.setCatcherTimestamp(now);
            return message;
        } else {
            return null;
        }
    }

    public synchronized void messageReceived(Message message) {
        outboundMessages.add(message);
    }
}
