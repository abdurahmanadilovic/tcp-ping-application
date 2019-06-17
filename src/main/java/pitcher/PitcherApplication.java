package pitcher;

import application.ApplicationState;
import io.InputStreamWrapper;
import io.InputStreamWrapperImplementation;
import io.OutputStreamWrapper;
import io.OutputStreamWrapperImplementation;
import ui.StatisticsPresenter;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PitcherApplication {
    private int messageLength;
    private int messagesPerSecond;
    private ApplicationState applicationState;
    private int port;
    private String address;
    private Pitcher pitcher;
    private StatisticsPresenter statisticsPresenter;

    public PitcherApplication(int messageLength,
                              int messagesPerSecond,
                              ApplicationState applicationState, int port,
                              String address,
                              StatisticsPresenter statisticsPresenter,
                              Pitcher pitcher) {
        this.messageLength = messageLength;
        this.messagesPerSecond = messagesPerSecond;
        this.applicationState = applicationState;
        this.statisticsPresenter = statisticsPresenter;
        this.port = port;
        this.address = address;
        this.pitcher = pitcher;
    }

    public void run() {
        try {
            Socket pitcherSocket = new Socket(address, port);
            InputStreamWrapper pitcherInputStream = new InputStreamWrapperImplementation(pitcherSocket.getInputStream());
            OutputStreamWrapper pitcherOutStream = new OutputStreamWrapperImplementation(pitcherSocket.getOutputStream());
            PitcherSender pitcherSender = new PitcherSender(messagesPerSecond, pitcher, pitcherOutStream);
            PitcherReceiver pitcherReceiver = new PitcherReceiver(pitcherInputStream, pitcher, applicationState, messageLength);

            Thread pitcherReceiverThread = new Thread(() -> {
                try {
                    pitcherReceiver.run();
                } catch (IOException e) {
                    System.out.println("IO error occurred, probably catcher closed the connection or there are network issues!");
                    applicationState.stop();
                }
            });
            pitcherReceiverThread.start();

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (!applicationState.isRunning()) {
                            cancel();
                        }
                        statisticsPresenter.showAllStats(System.currentTimeMillis());
                        pitcher.clearSuccessfulMessages();
                        pitcherSender.run();
                    } catch (IOException e) {
                        System.out.println("IO error occurred, probably catcher closed the connection or there are network issues!");
                        applicationState.stop();
                        cancel();
                    }
                }
            }, 0, 1000);

            timer.schedule(new TimerTask() {
                               @Override
                               public void run() {
                                   if (!applicationState.isRunning()) {
                                       cancel();
                                   }
                                   statisticsPresenter.showLostMessages(System.currentTimeMillis());
                                   pitcher.clearStaleMessages(System.currentTimeMillis());
                               }
                           },
                    0,
                    TimeUnit.MINUTES.toMillis(1));

            pitcherReceiverThread.join();
            pitcherOutStream.close();
            pitcherInputStream.close();
        } catch (IOException e) {
            System.out.println("Could not connect to a Catcher, make sure catcher is running!");
            System.out.println("Also make sure that the correct address is given to the pitcher application!");
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted, exiting!");
            applicationState.stop();
        }
    }
}
