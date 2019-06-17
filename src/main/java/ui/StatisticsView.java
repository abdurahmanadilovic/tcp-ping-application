package ui;

public interface StatisticsView {
    void showTime(String formattedTime);
    void showTotalSentMessages(String total);
    void showTotalSentMessagesLastSecond(String total);
    void showAverageRTTLastSecond(String average);
    void showMaxRTT(String max);
    void showAverageAtoBTimeLastSecond(String averageAtoB);
    void showAverageBtoATimeLastSecond(String averageBtoA);
    void showLostMessages(String lostMessages);
}
