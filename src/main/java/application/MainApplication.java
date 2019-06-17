package application;

import catcher.CatcherServer;
import pitcher.Pitcher;
import pitcher.PitcherApplication;
import ui.StatisticsPresenter;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication {
    public static void main(String[] args) {

        ApplicationArguments applicationArguments = new ApplicationArguments();
        applicationArguments.parseArguments(args);

        if (applicationArguments.isArgumentsAreValid()) {
            if (applicationArguments.isCatcher()) {

                ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

                CatcherServer catcherServer = new CatcherServer(pool,
                        applicationArguments.getPort(),
                        applicationArguments.getCatcherServerAddress(),
                        ApplicationArguments.minimumMessageSize);
                catcherServer.run();
            } else {
                Pitcher pitcher = new Pitcher(applicationArguments.getSize(), applicationArguments.getMessagesPerSecond());

                SimpleDateFormat formatter = new SimpleDateFormat(ApplicationArguments.defaultTimeFormat);
                StatisticsPresenter statisticsPresenter = new StatisticsPresenter(new ConsoleView(), formatter, pitcher);
                ApplicationState applicationState = new ApplicationStateImplementation();

                PitcherApplication pitcherApplication = new PitcherApplication(applicationArguments.getSize(),
                        applicationArguments.getMessagesPerSecond(),
                        applicationState,
                        applicationArguments.getPort(),
                        applicationArguments.getCatcherAddress(),
                        statisticsPresenter,
                        pitcher);
                pitcherApplication.run();
            }
        } else {
            System.out.println("Invalid number of arguments provided or arguments invalid!");
        }

    }
}
