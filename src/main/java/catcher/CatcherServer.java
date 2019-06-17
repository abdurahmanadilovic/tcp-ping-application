package catcher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class CatcherServer {
    private ExecutorService pool;
    private int port;
    private String serverAddress;
    private int minimumSize;

    public CatcherServer(ExecutorService pool, int port, String serverAddress, int minimumSize) {
        this.pool = pool;
        this.port = port;
        this.serverAddress = serverAddress;
        this.minimumSize = minimumSize;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(serverAddress, port));
            while (!pool.isShutdown()) {
                System.out.println("Listening for clients!");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected, starting a new thread!");
                pool.submit(() -> {
                    Catcher catcher = new Catcher();
                    CatcherApplication catcherApplication = new CatcherApplication(catcher, clientSocket, minimumSize);
                    catcherApplication.run();
                });
            }
        } catch (IOException ex) {
            System.out.println("Could not start a catcher server, make sure the port is not in use!");
            System.out.println("If the given port is below 1024 make sure to run the catcher with root privileges");
            pool.shutdown();
        }
    }
}
