package ui;

import pitcher.Message;
import pitcher.Pitcher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticsPresenter {
    private StatisticsView statisticsView;
    private SimpleDateFormat simpleDateFormat;
    private Pitcher pitcher;

    public StatisticsPresenter(StatisticsView statisticsView,
                               SimpleDateFormat simpleDateFormat, Pitcher pitcher) {
        this.statisticsView = statisticsView;
        this.simpleDateFormat = simpleDateFormat;
        this.pitcher = pitcher;
    }

    public void showTime(long now) {
        statisticsView.showTime(simpleDateFormat.format(new Date(now)));
    }

    public void showTotalSentMessages() {
        statisticsView.showTotalSentMessages("Total messages sent: " + pitcher.sentMessagesCount());
    }

    public void showTotalSentMessagesLastSecond() {
        statisticsView.showTotalSentMessagesLastSecond("Total messages sent last second: " +
                pitcher.sentMessagesCountLastSecond());
    }

    public void showAverageRTTLastSecond() {
        statisticsView.showAverageRTTLastSecond("Average RTT last second: " +
                pitcher.getAverageRTTLastSecond() +
                " ms");
    }

    public void showMaxRTT() {
        statisticsView.showMaxRTT("Maximum RTT last second: " +
                pitcher.getMaxRTT() +
                " ms");
    }

    public void showAverageAtoBTimeLastSecond() {
        statisticsView.showAverageAtoBTimeLastSecond("Average A to B last second: " +
                pitcher.getAverageAtoBTimeLastSecond() +
                " ms");
    }

    public void showAverageBtoATimeLastSecond() {
        statisticsView.showAverageBtoATimeLastSecond("Average B to A last second: " +
                pitcher.getAverageBtoATimeLastSecond() +
                " ms");
    }

    public void showLostMessages(long now) {
        Message[] lostMessages = pitcher.getLostMessages(now);
        if (lostMessages.length > 0) {
            StringBuilder lostIds = new StringBuilder("Lost messages with ids: ");
            for (Message message : lostMessages) {
                lostIds.append(message.getId()).append(",");
            }
            lostIds.deleteCharAt(lostIds.length() - 1);
            statisticsView.showLostMessages(lostIds.toString());
        }
    }

    public void showAllStats(long now) {
        showTime(now);
        showTotalSentMessages();
        showTotalSentMessagesLastSecond();
        showAverageRTTLastSecond();
        showAverageAtoBTimeLastSecond();
        showAverageBtoATimeLastSecond();
        showMaxRTT();
    }
}
