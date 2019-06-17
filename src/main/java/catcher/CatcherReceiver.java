package catcher;

import application.ApplicationState;
import io.InputStreamWrapper;
import pitcher.Message;

import java.io.IOException;
import java.nio.ByteBuffer;

import static java.lang.Integer.max;

public class CatcherReceiver {
    private InputStreamWrapper inputStreamWrapper;
    private Catcher catcher;
    private ApplicationState applicationState;
    private byte[] messageLengthBuffer = new byte[4];
    private int minimumSize;

    public CatcherReceiver(InputStreamWrapper inputStreamWrapper,
                           Catcher catcher,
                           ApplicationState applicationState,
                           int minimumSize) {
        this.inputStreamWrapper = inputStreamWrapper;
        this.catcher = catcher;
        this.applicationState = applicationState;
        this.minimumSize = minimumSize;
    }

    public void run() throws IOException {
        while (applicationState.isRunning()) {
            inputStreamWrapper.read(messageLengthBuffer);
            int messageLength = ByteBuffer.wrap(messageLengthBuffer).getInt();
            byte[] messageBuffer = new byte[max(messageLength, minimumSize)];
            inputStreamWrapper.read(messageBuffer);
            ByteBuffer byteBuffer = ByteBuffer.wrap(messageBuffer);
            Message message = Message.deserialize(byteBuffer);
            catcher.messageReceived(message);
        }
    }
}
