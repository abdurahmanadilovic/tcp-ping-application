package catcher;

import application.ApplicationState;
import io.InputStreamWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pitcher.Message;

public class CatcherReceiverTest {
    private InputStreamWrapper inputStreamWrapper;
    private CatcherReceiver catcherReceiver;
    private Catcher catcher;
    private ApplicationState applicationState;

    @Before
    public void setUp() {
        inputStreamWrapper = Mockito.mock(InputStreamWrapper.class);
        catcher = Mockito.mock(Catcher.class);
        applicationState = Mockito.mock(ApplicationState.class);
        catcherReceiver = new CatcherReceiver(inputStreamWrapper,
                catcher,
                applicationState, 50);
    }

    @Test
    public void catcherReceiverWillPassMessagesToCatcher() throws Exception {
        Mockito.when(applicationState.isRunning()).thenReturn(true, false);

        catcherReceiver.run();

        Mockito.verify(catcher, Mockito.times(1)).messageReceived(Mockito.any(Message.class));
    }

    @Test
    public void catcherReceiverWillProcessMultipleMessages() throws Exception {
        Mockito.when(applicationState.isRunning()).thenReturn(true, true, false);

        catcherReceiver.run();

        Mockito.verify(inputStreamWrapper, Mockito.times(4)).read(Mockito.any());
        Mockito.verify(catcher, Mockito.times(2)).messageReceived(Mockito.any(Message.class));
    }
}