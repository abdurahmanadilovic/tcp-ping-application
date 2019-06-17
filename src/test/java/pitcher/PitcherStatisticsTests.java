package pitcher;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class PitcherStatisticsTests {

    @Test
    public void testPitcherWillKeepCountOfSentMessages() {
        int messageLength = 600;
        Pitcher pitcher = new Pitcher(messageLength, 2);
        assertEquals(0, pitcher.sentMessagesCount());
        pitcher.getNextMessage(System.currentTimeMillis());
        assertEquals(1, pitcher.sentMessagesCount());
    }

    @Test
    public void testPitcherWillKeepTrackOfSentMessagesLastSecond() {
        int messageLength = 50;
        long now = System.currentTimeMillis();
        Pitcher pitcher = new Pitcher(messageLength, 2);
        pitcher.getNextMessage(now - 200);
        pitcher.getNextMessage(now - 200);
        pitcher.getNextMessage(now - 1100);

        pitcher.messageReceived(new Message(1, messageLength, now - 100 - 100, now - 100, now));
        pitcher.messageReceived(new Message(2, messageLength, now - 100 - 100, now - 100, now));
        pitcher.messageReceived(new Message(3, messageLength, now - 500 - 600, now - 600, now));

        assertEquals(2, pitcher.sentMessagesCountLastSecond());
    }

    @Test
    public void testPitcherWillKeepTrackOfAverageRTTLastSecond() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 3);
        long now = System.currentTimeMillis();
        int message1AtoB = 500, message1BtoA = 400;
        int message2AtoB = 400, message2BtoA = 300;
        int message3AtoB = 500, message3BtoA = 600;

        Message message = pitcher.getNextMessage(now - message1AtoB - message1BtoA);
        Message message2 = pitcher.getNextMessage(now - message2AtoB - message2BtoA);
        Message message3 = pitcher.getNextMessage(now - message3AtoB - message3BtoA);

        pitcher.messageReceived(new Message(message.getId(),
                message.getSize(),
                now - message1AtoB - message1BtoA,
                now - message1BtoA, now));
        pitcher.messageReceived(new Message(message2.getId(),
                message2.getSize(),
                now - message2AtoB - message2BtoA,
                now - message2BtoA, now));
        pitcher.messageReceived(new Message(message3.getId(),
                message3.getSize(),
                now - message3AtoB - message3BtoA,
                now - message3BtoA, now));

        assertEquals((message1AtoB + message1BtoA + message2AtoB + message2BtoA) / 2,
                pitcher.getAverageRTTLastSecond(), 0);
    }

    @Test
    public void testPitcherWillKeepTrackOfMaxRTT() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 3);
        long now = System.currentTimeMillis();
        long fiveSecondsInFuture = System.currentTimeMillis() + 500;
        long threeSecondsInFuture = System.currentTimeMillis() + 300;
        long twoSecondsInFuture = System.currentTimeMillis() + 200;

        Message message = pitcher.getNextMessage(now - 500);
        Message message2 = pitcher.getNextMessage(now - 300);
        Message message3 = pitcher.getNextMessage(now - 200);

        pitcher.messageReceived(new Message(message.getId(),
                message.getSize(),
                now - 500,
                fiveSecondsInFuture, now));
        pitcher.messageReceived(new Message(message2.getId(),
                message2.getSize(),
                now - 300,
                threeSecondsInFuture, now));
        pitcher.messageReceived(new Message(message3.getId(),
                message3.getSize(),
                now - 200,
                twoSecondsInFuture, now));

        assertEquals(500, pitcher.getMaxRTT());
    }

    @Test
    public void testPitcherWillKeepTrackOfAverageAtoBTimeInLastSecond() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 3);
        long now = System.currentTimeMillis();
        int message1AtoB = 500, message1BtoA = 400;
        int message2AtoB = 400, message2BtoA = 300;
        int message3AtoB = 500, message3BtoA = 600;

        Message message = pitcher.getNextMessage(now - message1AtoB - message1BtoA);
        Message message2 = pitcher.getNextMessage(now - message2AtoB - message2BtoA);
        Message message3 = pitcher.getNextMessage(now - message3AtoB - message3BtoA);

        pitcher.messageReceived(new Message(message.getId(),
                message.getSize(),
                now - message1AtoB - message1BtoA,
                now - message1BtoA, now));
        pitcher.messageReceived(new Message(message2.getId(),
                message2.getSize(),
                now - message2AtoB - message2BtoA,
                now - message2BtoA, now));
        pitcher.messageReceived(new Message(message3.getId(),
                message3.getSize(),
                now - message3AtoB - message3BtoA,
                now - message3BtoA, now));

        assertEquals((message1AtoB + message2AtoB) / 2,
                pitcher.getAverageAtoBTimeLastSecond(), 0);
    }

    @Test
    public void testPitcherWillKeepTrackOfAverageBtoATimeInLastSecond() {
        int messageLength = 50;
        Pitcher pitcher = new Pitcher(messageLength, 3);
        long now = System.currentTimeMillis();
        int message1AtoB = 500, message1BtoA = 400;
        int message2AtoB = 400, message2BtoA = 300;
        int message3AtoB = 500, message3BtoA = 600;

        Message message = pitcher.getNextMessage(now - message1AtoB - message1BtoA);
        Message message2 = pitcher.getNextMessage(now - message2AtoB - message2BtoA);
        Message message3 = pitcher.getNextMessage(now - message3AtoB - message3BtoA);

        pitcher.messageReceived(new Message(message.getId(),
                message.getSize(),
                now - message1AtoB - message1BtoA,
                now - message1BtoA, now));
        pitcher.messageReceived(new Message(message2.getId(),
                message2.getSize(),
                now - message2AtoB - message2BtoA,
                now - message2BtoA, now));
        pitcher.messageReceived(new Message(message3.getId(),
                message3.getSize(),
                now - message3AtoB - message3BtoA,
                now - message3BtoA, now));

        assertEquals((message1BtoA + message2BtoA) / 2,
                pitcher.getAverageBtoATimeLastSecond(), 0);
    }

    @Test
    public void testPitcherWillKeepTrackOfLostMessages() {
        Pitcher pitcher = new Pitcher(50, 3);
        long now = System.currentTimeMillis();
        Message messageOne = pitcher.getNextMessage(now);
        Message messageTwo = pitcher.getNextMessage(now - TimeUnit.MINUTES.toMillis(1));
        Message messageThree = pitcher.getNextMessage(now);

        pitcher.messageReceived(new Message(messageOne.getId(),
                messageOne.getSize(),
                messageOne.getSentTimestamp(),
                now, now));
        pitcher.messageReceived(new Message(messageThree.getId(),
                messageThree.getSize(),
                messageThree.getSentTimestamp(),
                now, now));

        Message[] lostMessages = new Message[]{messageTwo};
        assertEquals(lostMessages[0].getId(), pitcher.getLostMessages(now)[0].getId());
    }
}
