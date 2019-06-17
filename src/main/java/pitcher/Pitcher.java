package pitcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Pitcher {
    private int messageLength;
    private int nextMessageId = 1;
    private Map<Integer, Message> messages;
    private long maxRTT = 0;

    public Pitcher(int messageLength, int messagesPerSecond) {
        this.messageLength = messageLength;
        int initialCapacity = (int) Math.ceil(messagesPerSecond / 0.75);
        this.messages = new ConcurrentHashMap<>(initialCapacity);
    }

    public int getMessageLength() {
        return messageLength;
    }

    public int getNextMessageId() {
        return nextMessageId;
    }

    public Message getNextMessage(long nowTimestamp) {
        Message message = new Message(nextMessageId++, messageLength, nowTimestamp, 0, 0);
        messages.put(message.getId(), message);
        return message;
    }

    public int sentMessagesCount() {
        return nextMessageId - 1;
    }

    public int sentMessagesCountLastSecond() {
        int count = 0;

        for (Message message : messages.values()) {
            if (messageArrivedInLastSecond(message)) {
                count++;
            }
        }

        return count;
    }

    public void messageReceived(Message message) {
        if (messages.containsKey(message.getId())) {
            messages.put(message.getId(), message);
        }
    }

    public long getAverageRTTLastSecond() {
        long sum = 0;
        int count = 0;

        for (Message receivedMessage : messages.values()) {
            if (messageArrivedInLastSecond(receivedMessage)) {
                sum += receivedMessage.getRTTInMillis();
                count += 1;
            }
        }

        return getAverage(sum, count);
    }

    public long getMaxRTT() {
        for (Message receivedMessage : messages.values()) {
            maxRTT = Math.max(maxRTT, receivedMessage.getRTTInMillis());
        }

        return maxRTT;
    }

    public long getAverageAtoBTimeLastSecond() {
        long sum = 0;
        int count = 0;

        for (Message receivedMessage : messages.values()) {
            if (messageArrivedInLastSecond(receivedMessage)) {
                sum += receivedMessage.getAtoBTimeInMillis();
                count += 1;
            }
        }

        return getAverage(sum, count);
    }

    public long getAverageBtoATimeLastSecond() {
        long sum = 0;
        int count = 0;

        for (Message receivedMessage : messages.values()) {
            if (messageArrivedInLastSecond(receivedMessage)) {
                sum += receivedMessage.getBtoATimeInMillis();
                count += 1;
            }
        }

        return getAverage(sum, count);
    }

    private long getAverage(long sum, int count) {
        return count > 0 && sum > 0 ? sum / count : 0;
    }

    private boolean messageArrivedInLastSecond(Message message) {
        return message.getReceivedTimestamp() != 0 &&
                message.getSentTimestamp() + TimeUnit.SECONDS.toMillis(1) >= message.getReceivedTimestamp();
    }

    public Message[] getLostMessages(long now) {
        List<Message> lostMessages = new ArrayList<>();
        for (Message message : messages.values()) {
            if (message.getCatcherTimestamp() == 0 && message.getSentTimestamp() <= now - TimeUnit.MINUTES.toMillis(1)) {
                lostMessages.add(message);
            }
        }

        return lostMessages.toArray(new Message[0]);
    }

    public void clearStaleMessages(long now) {
        for (Message message : messages.values()) {
            if (message.getSentTimestamp() <= now - TimeUnit.MINUTES.toMillis(1)) {
                messages.remove(message.getId());
            }
        }
    }

    public int getMessagesInTheBuffer() {
        return messages.size();
    }

    public void clearSuccessfulMessages() {
        for (Message message : messages.values()) {
            if (messageArrivedInLastSecond(message)) {
                messages.remove(message.getId());
            }
        }
    }
}
