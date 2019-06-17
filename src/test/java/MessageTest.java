import org.junit.Test;
import pitcher.Message;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class MessageTest {

    @Test
    public void testMessageCreatesByteArrayOfCorrectSize(){
        int length = 50;
        Message message = new Message(0, length, System.currentTimeMillis(), 0, 0);
        assertEquals(length, message.serialize().length);
    }

    @Test
    public void testMessageAssignsCorrectIdAndTimestamp(){
        int id = 1;
        long now = System.currentTimeMillis();
        long sentTime = System.currentTimeMillis() - 100;
        long serverTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
        int size = 50;
        Message message = new Message(id, size, sentTime, serverTime, now);
        ByteBuffer result = ByteBuffer.wrap(message.serialize());
        assertEquals(id, result.getInt());
        assertEquals(size, result.getInt());
        assertEquals(sentTime, result.getLong());
        assertEquals(serverTime, result.getLong());
        assertEquals(now, result.getLong());
    }

    @Test
    public void testTripTimeCalculations(){
        int id = 1;
        long now = System.currentTimeMillis();
        long receiveTime = now - 500;
        long sentTime = now - 500 - 500;

        Message message = new Message(id, 50, sentTime, receiveTime, now);
        assertEquals(500, message.getAtoBTimeInMillis());
        assertEquals(500, message.getBtoATimeInMillis());
        assertEquals(1000, message.getRTTInMillis());
    }

}