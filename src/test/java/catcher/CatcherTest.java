package catcher;

import org.junit.Test;
import pitcher.Message;

import static org.junit.Assert.assertEquals;

public class CatcherTest {
    @Test
    public void testCatcherWillKeepQueueOfOutboundMessages() {
        Catcher catcher = new Catcher();
        long now = System.currentTimeMillis();
        Message receivedMessage = new Message(1, 50, now, 0, 0);
        Message receivedMessage2 = new Message(2, 50, now, 0, 0);
        catcher.messageReceived(receivedMessage);
        catcher.messageReceived(receivedMessage2);
        assertEquals(receivedMessage.getId(), catcher.getNextMessageToSend(now).getId());
        assertEquals(receivedMessage2.getId(), catcher.getNextMessageToSend(now).getId());
    }

    @Test
    public void testCatcherWillGenerateCorrectReplyMessage() {
        Catcher catcher = new Catcher();
        long now = System.currentTimeMillis();
        Message receivedMessage = new Message(1, 50, now, 0, 0);
        catcher.messageReceived(receivedMessage);
        Message replyMessage = catcher.getNextMessageToSend(now);
        assertEquals(now, replyMessage.getCatcherTimestamp());
        assertEquals(receivedMessage.getId(), replyMessage.getId());
        assertEquals(receivedMessage.getSize(), replyMessage.getSize());
        assertEquals(receivedMessage.getSentTimestamp(), replyMessage.getSentTimestamp());
    }
}