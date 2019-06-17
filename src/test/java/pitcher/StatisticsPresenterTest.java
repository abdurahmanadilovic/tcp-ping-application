package pitcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ui.StatisticsPresenter;
import ui.StatisticsView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class StatisticsPresenterTest {
    private StatisticsPresenter presenter;
    private StatisticsView statisticsView;
    private Pitcher pitcher;

    @Before
    public void before() {
        this.statisticsView = Mockito.mock(StatisticsView.class);
        this.pitcher = Mockito.mock(Pitcher.class);
        String format = "HH:MM:SS";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        this.presenter = new StatisticsPresenter(this.statisticsView, formatter, pitcher);
    }

    @After
    public void after() {
    }

    @Test
    public void testPresenterWillFormatTimeCorrectly() {
        Date now = Date.from(Instant.now());
        this.presenter.showTime(now.getTime());
        String format = "HH:MM:SS";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showTime(formatter.format(now));
    }

    @Test
    public void testPresenterWillShowCorrectTotalMessagesCount() {
        Mockito.when(this.pitcher.sentMessagesCount()).thenReturn(1);
        presenter.showTotalSentMessages();
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showTotalSentMessages("Total messages sent: 1");
    }

    @Test
    public void testPresenterWillShowTotalMessagesLastSecond() {
        Mockito.when(this.pitcher.sentMessagesCountLastSecond()).thenReturn(1);
        presenter.showTotalSentMessagesLastSecond();
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showTotalSentMessagesLastSecond("Total messages sent last second: 1");
    }

    @Test
    public void testPresenterWillShowAverageRTT() {
        Mockito.when(this.pitcher.getAverageRTTLastSecond()).thenReturn(100L);
        presenter.showAverageRTTLastSecond();
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showAverageRTTLastSecond("Average RTT last second: 100 ms");
    }

    @Test
    public void testPresenterWillShowMaxRTT() {
        Mockito.when(this.pitcher.getMaxRTT()).thenReturn(100L);
        presenter.showMaxRTT();
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showMaxRTT("Maximum RTT last second: 100 ms");
    }

    @Test
    public void testAverageAToBLastSecond() {
        Mockito.when(this.pitcher.getAverageAtoBTimeLastSecond()).thenReturn(100L);
        presenter.showAverageAtoBTimeLastSecond();
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showAverageAtoBTimeLastSecond("Average A to B last second: 100 ms");
    }

    @Test
    public void testAverageBtoALastSecond() {
        Mockito.when(this.pitcher.getAverageBtoATimeLastSecond()).thenReturn(100L);
        presenter.showAverageBtoATimeLastSecond();
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showAverageBtoATimeLastSecond("Average B to A last second: 100 ms");
    }

    @Test
    public void testPresenterWillShowMissingPackets() {
        long now = System.currentTimeMillis();
        Message lostMessage = new Message(1, 0, 0, 0, 0);
        Message lostMessage2 = new Message(2, 0, 0, 0, 0);
        Message[] lostMessages = new Message[]{lostMessage, lostMessage2};

        Mockito.when(this.pitcher.getLostMessages(now)).thenReturn(lostMessages);

        presenter.showLostMessages(now);
        Mockito.verify(this.statisticsView,
                Mockito.times(1)).showLostMessages("Lost messages with ids: 1,2");
    }
}