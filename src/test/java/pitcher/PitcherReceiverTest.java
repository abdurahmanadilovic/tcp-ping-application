package pitcher;

import application.ApplicationState;
import io.InputStreamWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PitcherReceiverTest {
    private InputStreamWrapper inputStreamWrapper;
    private Pitcher pitcher;
    private PitcherReceiver pitcherReceiver;
    private int messageSize;
    private ApplicationState applicationState;

    @Before
    public void setUp() {
        this.inputStreamWrapper = Mockito.mock(InputStreamWrapper.class);
        this.pitcher = Mockito.mock(Pitcher.class);
        this.messageSize = 50;
        this.applicationState = Mockito.mock(ApplicationState.class);
        this.pitcherReceiver = new PitcherReceiver(inputStreamWrapper, pitcher, applicationState, this.messageSize);
    }

    @Test
    public void testPitcherReceiverWillReceiveMessage() throws Exception {
        Mockito.when(applicationState.isRunning()).thenReturn(true, false);

        pitcherReceiver.run();

        Mockito.verify(inputStreamWrapper, Mockito.times(1)).read(Mockito.any());
        Mockito.verify(pitcher, Mockito.times(1)).messageReceived(Mockito.any());
    }

    @Test
    public void testPitcherReceiverWillReceiveMultipleMessages() throws Exception {
        Mockito.when(applicationState.isRunning()).thenReturn(true, true, false);

        pitcherReceiver.run();

        Mockito.verify(inputStreamWrapper, Mockito.times(2)).read(Mockito.any());
        Mockito.verify(pitcher, Mockito.times(2)).messageReceived(Mockito.any());
    }
}