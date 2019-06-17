package pitcher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PitcherTest {
    @Test
    public void testGetterForMessageLength() {
        int messageLength = 600;
        Pitcher pitcher = new Pitcher(messageLength, 0);
        assertEquals(messageLength, pitcher.getMessageLength());
    }

    @Test
    public void testPitcherAssignsIncrementIds() {
        int messageLength = 600;
        Pitcher pitcher = new Pitcher(messageLength, 2);
        assertEquals(1, pitcher.getNextMessageId());
        pitcher.getNextMessage(System.currentTimeMillis());
        assertEquals(2, pitcher.getNextMessageId());
    }

    @Test
    public void testPitcherCreateCorrectMessageObject() {
        long timeStamp = System.currentTimeMillis();
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 2);
        Message message = pitcher.getNextMessage(timeStamp);
        assertEquals(1, message.getId());
        assertEquals(timeStamp, message.getSentTimestamp());
        assertEquals(50, message.getSize());
    }

    @Test
    public void afterCallingPitcherClearOldMessagesAreDiscarded() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 2);
        pitcher.clearStaleMessages(System.currentTimeMillis());
        pitcher.messageReceived(new Message(1, 0, 0, 0, 0));
        assertEquals(0, pitcher.getMessagesInTheBuffer());
    }

    @Test
    public void pitcherWillKeepTrackOfMaxRTT() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 2);
        long now = System.currentTimeMillis();
        Message message = pitcher.getNextMessage(now - 100 - 200);
        Message message2 = pitcher.getNextMessage(now - 200 - 300);
        Message message3 = pitcher.getNextMessage(now - 300 - 400);
        message.setCatcherTimestamp(now - 100);
        message.setReceivedTimestamp(now - 100);
        message2.setCatcherTimestamp(now - 200);
        message2.setReceivedTimestamp(now - 300);
        message3.setCatcherTimestamp(now - 300);
        message3.setReceivedTimestamp(now - 400);

        pitcher.messageReceived(message);
        pitcher.messageReceived(message2);
        pitcher.messageReceived(message3);

        assertEquals(message3.getRTTInMillis(), pitcher.getMaxRTT());
    }

    @Test
    public void pitcherWillKeepTrackOfMaxRTTAfterClearingSuccessfulMessages() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 2);
        long now = System.currentTimeMillis();
        Message message = pitcher.getNextMessage(now - 100 - 200);
        Message message2 = pitcher.getNextMessage(now - 200 - 300);
        Message message3 = pitcher.getNextMessage(now - 300 - 400);
        message.setCatcherTimestamp(now - 100);
        message.setReceivedTimestamp(now - 100);
        message2.setCatcherTimestamp(now - 200);
        message2.setReceivedTimestamp(now - 300);
        message3.setCatcherTimestamp(now - 300);
        message3.setReceivedTimestamp(now - 400);

        pitcher.messageReceived(message);
        pitcher.messageReceived(message2);
        pitcher.messageReceived(message3);


        assertEquals(message3.getRTTInMillis(), pitcher.getMaxRTT());

        pitcher.clearSuccessfulMessages();

        Message newMessage = pitcher.getNextMessage(now - 50 - 100);
        newMessage.setCatcherTimestamp(now - 50);
        newMessage.setReceivedTimestamp(now - 100);

        pitcher.messageReceived(newMessage);

        assertEquals(message3.getRTTInMillis(), pitcher.getMaxRTT());
    }
}
