package catcher;

import application.ApplicationState;
import application.ApplicationStateImplementation;
import io.InputStreamWrapper;
import io.InputStreamWrapperImplementation;
import io.OutputStreamWrapper;
import io.OutputStreamWrapperImplementation;

import java.io.IOException;
import java.net.Socket;
import java.nio.BufferOverflowException;

public class CatcherApplication {
    private Catcher catcher;
    private ApplicationState applicationState;
    private Socket socket;
    private int minimumSize;

    public CatcherApplication(Catcher catcher, Socket socket, int minimumSize) {
        this.catcher = catcher;
        this.applicationState = new ApplicationStateImplementation();
        this.socket = socket;
        this.minimumSize = minimumSize;
    }

    public void run() {
        try {
            InputStreamWrapper inputStreamWrapper = new InputStreamWrapperImplementation(socket.getInputStream());
            CatcherReceiver catcherReceiver = new CatcherReceiver(inputStreamWrapper, catcher, applicationState, minimumSize);
            OutputStreamWrapper outputStreamWrapper = new OutputStreamWrapperImplementation(socket.getOutputStream());
            CatcherSender catcherSender = new CatcherSender(outputStreamWrapper, catcher, applicationState);

            Thread receiverThread = new Thread(() -> {
                try {
                    catcherReceiver.run();
                } catch (IOException ex) {
                    System.out.println("IO error occurred, probably pitcher closed the connection or there are network issues!");
                    applicationState.stop();
                }
            });
            Thread senderThread = new Thread(() -> {
                try {
                    catcherSender.run();
                } catch (IOException ex) {
                    System.out.println("IO error occurred, probably pitcher closed the connection or there are network issues!");
                    applicationState.stop();
                } catch (BufferOverflowException ex) {
                    System.out.println("Could not serialize a message, probably got a corrupt message! Quitting this catcher instance");
                    applicationState.stop();
                }
            });

            receiverThread.start();
            senderThread.start();
            receiverThread.join();
            senderThread.join();

            outputStreamWrapper.close();
            inputStreamWrapper.close();
        } catch (IOException e) {
            System.out.println("Network error occurred, shutting down the Catcher application");
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted, exiting!");
            applicationState.stop();
        }
    }
}
