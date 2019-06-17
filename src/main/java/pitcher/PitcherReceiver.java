package pitcher;

import application.ApplicationState;
import io.InputStreamWrapper;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PitcherReceiver {
    private InputStreamWrapper inputStreamWrapper;
    private Pitcher pitcher;
    private ApplicationState applicationState;
    private byte[] buffer;

    public PitcherReceiver(InputStreamWrapper inputStreamWrapper,
                           Pitcher pitcher,
                           ApplicationState applicationState,
                           int messageSize) {
        this.inputStreamWrapper = inputStreamWrapper;
        this.pitcher = pitcher;
        this.applicationState = applicationState;
        this.buffer = new byte[messageSize];
    }

    public void run() throws IOException {
        while (applicationState.isRunning()) {
            inputStreamWrapper.read(buffer);
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            Message message = Message.deserialize(byteBuffer);
            message.setReceivedTimestamp(System.currentTimeMillis());
            pitcher.messageReceived(message);
        }
    }
}
