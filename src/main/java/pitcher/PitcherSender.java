package pitcher;

import io.OutputStreamWrapper;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PitcherSender {
    private int messagesPerSecond;
    private Pitcher pitcher;
    private OutputStreamWrapper outputStreamWrapper;

    public PitcherSender(int messagesPerSecond, Pitcher pitcher, OutputStreamWrapper outputStreamWrapper) {
        this.pitcher = pitcher;
        this.outputStreamWrapper = outputStreamWrapper;
        this.messagesPerSecond = messagesPerSecond;
    }

    public void run() throws IOException {
        for (int i = 0; i < messagesPerSecond; i++) {
            Message message = pitcher.getNextMessage(System.currentTimeMillis());
            byte[] messageSerialized = message.serialize();
            outputStreamWrapper.write(ByteBuffer.allocate(4).putInt(messageSerialized.length).array());
            outputStreamWrapper.write(messageSerialized);
        }
    }
}
