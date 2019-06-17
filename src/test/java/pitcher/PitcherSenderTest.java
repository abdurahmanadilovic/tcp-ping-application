package pitcher;

import io.OutputStreamWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PitcherSenderTest {
    private Pitcher pitcher;
    private PitcherSender pitcherSender;
    private OutputStreamWrapper outputStreamWrapper;
    private int messagesPerSecond = 50;

    @Before
    public void setUp() {
        pitcher = Mockito.mock(Pitcher.class);
        outputStreamWrapper = Mockito.mock(OutputStreamWrapper.class);
        pitcherSender = new PitcherSender(messagesPerSecond, pitcher, outputStreamWrapper);
    }


    @Test
    public void pitcherSenderShouldSendMultipleMessages() throws Exception {
        Message message = new Message(1, 50, 0, 0, 0);
        Mockito.when(pitcher.getNextMessage(Mockito.anyLong())).thenReturn(message);

        pitcherSender.run();

        Mockito.verify(outputStreamWrapper,
                Mockito.times(messagesPerSecond)).write(message.serialize());

    }
}