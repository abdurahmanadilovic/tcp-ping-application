package application;

public class ApplicationStateImplementation implements ApplicationState {
    private boolean running = true;

    @Override
    public synchronized boolean isRunning() {
        return running;
    }

    @Override
    public synchronized void stop() {
        running = false;
    }
}
