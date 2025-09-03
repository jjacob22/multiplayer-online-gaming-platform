import GUI.App;

import java.util.Arrays;

import static networking.PlatformServerManager.SERVER_IP_LOCAL;
import static networking.PlatformServerManager.SERVER_PORT_DEFAULT;

public class Main {
    public static void main(String[] args) {
        String serverIP;
        int serverPort;
        if (args.length == 0) {
            serverIP = SERVER_IP_LOCAL;
            serverPort = SERVER_PORT_DEFAULT;
        } else if (args.length == 1) {
            serverIP = args[0];
            serverPort = SERVER_PORT_DEFAULT;
        } else if (args.length == 2) {
            serverIP = args[0];
            serverPort = Integer.parseInt(args[1]);
        } else {
            throw new IllegalArgumentException(
                "Invalid arguments:\n" + Arrays.toString(args) + "\n" +
                    "usage: <this executable> [options]\n" +
                    "options:\n" +
                    "\t[no arguments]\n\t<server IP>\n\t<server IP> <server port>\n");
        }
        try {
            App app = new App(serverIP, serverPort);
        } catch (RuntimeException e) {
            System.err.println("Error: " + e);
            System.exit(769);
        }
    }
}