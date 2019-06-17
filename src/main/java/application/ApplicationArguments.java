package application;

public class ApplicationArguments {
    private boolean isCatcher;
    private String catcherServerAddress = "127.0.0.1";
    private String catcherAddress;
    private int port = 1024;
    private int size = 300;
    private int messagesPerSecond = 1;
    private boolean argumentsAreValid = true;
    public static final int minimumMessageSize = 50;
    public static final int maximumMessageSize = 2000;
    public static final String defaultTimeFormat = "H:m:s:S";

    public void parseArguments(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains("-c")) {
                    isCatcher = true;
                }
                if (arg.contains("port")) {
                    port = Integer.parseInt(args[i + 1]);
                }
                if (arg.contains("bind")) {
                    catcherServerAddress = args[i + 1];
                }
                if (arg.contains("size")) {
                    size = Integer.parseInt(args[i + 1]);
                    if (size < minimumMessageSize || size > maximumMessageSize) {
                        System.out.println("Invalid size argument, min is 50 max is 2000");
                        argumentsAreValid = false;
                    }
                }
                if (arg.contains("mps")) {
                    messagesPerSecond = Integer.parseInt(args[i + 1]);
                    if (messagesPerSecond <= 0) {
                        System.out.println("Invalid mps argument, min is 1");
                        argumentsAreValid = false;
                    }
                }
            }

            catcherAddress = args[args.length - 1];

        } catch (NumberFormatException ex) {
            System.out.println("Invalid integer provided, arguments not valid!");
            argumentsAreValid = false;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Invalid number of arguments provided!");
            argumentsAreValid = false;
        }
    }

    public String getCatcherServerAddress() {
        return catcherServerAddress;
    }

    public String getCatcherAddress() {
        return catcherAddress;
    }

    public int getPort() {
        return port;
    }

    public int getSize() {
        return size;
    }

    public int getMessagesPerSecond() {
        return messagesPerSecond;
    }

    public boolean isCatcher() {
        return isCatcher;
    }

    public boolean isArgumentsAreValid() {
        return argumentsAreValid;
    }
}
