package application;

import ui.StatisticsView;

public class ConsoleView implements StatisticsView {
    @Override
    public void showTime(String formattedTime) {
        System.out.println(formattedTime);
    }

    @Override
    public void showTotalSentMessages(String total) {
        System.out.println(total);
    }

    @Override
    public void showTotalSentMessagesLastSecond(String total) {
        System.out.println(total);
    }

    @Override
    public void showAverageRTTLastSecond(String average) {
        System.out.println(average);
    }

    @Override
    public void showMaxRTT(String max) {
        System.out.println(max);
    }

    @Override
    public void showAverageAtoBTimeLastSecond(String averageAtoB) {
        System.out.println(averageAtoB);
    }

    @Override
    public void showAverageBtoATimeLastSecond(String averageBtoA) {
        System.out.println(averageBtoA);
    }

    @Override
    public void showLostMessages(String lostMessages) {
        System.out.println(lostMessages);
    }
}
