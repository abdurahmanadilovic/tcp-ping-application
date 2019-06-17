package catcher;

import application.ApplicationState;
import io.OutputStreamWrapper;
import pitcher.Message;

import java.io.IOException;
import java.nio.BufferOverflowException;

public class CatcherSender {
    private OutputStreamWrapper outputStreamWrapper;
    private Catcher catcher;
    private ApplicationState applicationState;

    public CatcherSender(OutputStreamWrapper outputStreamWrapper,
                         Catcher catcher,
                         ApplicationState applicationState) {
        this.outputStreamWrapper = outputStreamWrapper;
        this.catcher = catcher;
        this.applicationState = applicationState;
    }

    public void run() throws IOException, BufferOverflowException {
        while (applicationState.isRunning()) {
            Message message = catcher.getNextMessageToSend(System.currentTimeMillis());
            if (message != null) {
                message.serialize();
                outputStreamWrapper.write(message.serialize());
            }
        }

        outputStreamWrapper.close();
    }
}
