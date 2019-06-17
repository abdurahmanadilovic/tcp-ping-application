package catcher;

import application.ApplicationState;
import io.OutputStreamWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pitcher.Message;

public class CatcherSenderTest {
    private OutputStreamWrapper outputStreamWrapper;
    private CatcherSender catcherSender;
    private Catcher catcher;
    private ApplicationState applicationState;
    private int messageLength = 50;

    @Before
    public void setUp() {
        outputStreamWrapper = Mockito.mock(OutputStreamWrapper.class);
        catcher = Mockito.mock(Catcher.class);
        applicationState = Mockito.mock(ApplicationState.class);
        catcherSender = new CatcherSender(outputStreamWrapper, catcher, applicationState);
    }

    @Test
    public void catcherSenderWillPoolDataFromCatcherAndSendIt() throws Exception {
        Message message = new Message(1, messageLength, 0, 0, 0);
        Mockito.when(catcher.getNextMessageToSend(Mockito.anyLong())).thenReturn(message);
        Mockito.when(applicationState.isRunning()).thenReturn(true, false);

        catcherSender.run();

        Mockito.verify(outputStreamWrapper,
                Mockito.times(1)).write(message.serialize());
    }

    @Test
    public void catcherSenderWillPoolMessagesWhileAppIsReady() throws Exception {
        Message message = new Message(1, messageLength, 0, 0, 0);
        Mockito.when(catcher.getNextMessageToSend(Mockito.anyLong())).thenReturn(message);
        Mockito.when(applicationState.isRunning()).thenReturn(true, true, false);

        catcherSender.run();

        Mockito.verify(outputStreamWrapper,
                Mockito.times(2)).write(message.serialize());
    }
}