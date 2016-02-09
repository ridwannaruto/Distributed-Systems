package com.vishlesha.app;

//product of Vishlesha
//created by Ridwan

import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.LeaveRequest;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.Request;
import com.vishlesha.request.UnregisterRequest;
import com.vishlesha.request.handler.SearchRequestHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

/*
    * Runs the interface for the client application
    * Displays the dialog box asking for connection details and query commands
    * Connects to server and displays the responseMessage
*/

class App {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(final String[] args) throws IOException {
        Client client = new Client();
        final Node bootstrapServerNode = new Node();

        System.out.println("Vishlesha Distributed System");
        System.out.println("----------------------------\n");
        setup(bootstrapServerNode, client);

        Request regRequest = new RegisterRequest(bootstrapServerNode);
        client.sendTCPRequest(regRequest);

        // TODO modify to issue multiple queries

        System.out.println("connecting to the network..........");

        boolean print = true;

        while (true) {
            if (GlobalState.getNeighbors().size() > 0) {
                if (print) {
                    System.out.println("connected to network");
                    System.out.println("\nInitiate Search\n---------------------");
                    print = false;
                }

                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                System.out.print("Enter your search query: ");
                String searchQuery = scanner.nextLine();
                SearchRequestHandler searchRequestHandler = new SearchRequestHandler();
                searchRequestHandler.initiateSearch(searchQuery);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setup(final Node bootstrapServerNode, final Client client) {
        int bootstrapPort;
        final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
        Server server;
        bootstrapServerNode.setIpaddress("127.0.0.1");
        try {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            if (!"127.0.0.1".equals(localIp))
                bootstrapServerNode.setIpaddress("172.31.23.116");

        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }

        bootstrapPort = 1033;
        bootstrapServerNode.setPortNumber(bootstrapPort);

        // handleErrorResponse LEAVE and UNREG on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("\nUnregistering and leaving the network");
                Request unregRequest = new UnregisterRequest(bootstrapServerNode);
                client.sendTCPRequest(unregRequest);

                for (Node n : GlobalState.getNeighbors().keySet()) {
                    client.sendUDPRequest(new LeaveRequest(n));
                }

                // TODO add reasonable blocking mechanism
                try {
                    Thread.sleep(1000 * GlobalState.getNeighbors().size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Ready to shut down");
                log.info(this.getClass() + " : shutting down app");

                System.out.println("System statistics\n-----------------");
                System.out.println("Number of request received: " + GlobalState.getReceivedRequestCount());
                System.out.println("Number of request forwarded: " + GlobalState.getForwardedRequestCount());
                System.out.println("Number of request answered: " + GlobalState.getAnsweredRequestCount());

            }
        });
        try {
//            AppLogger.setup();

            // switch to seed-based IP addresses on 127.0.0.1 (local environment)
            Node localServer = new Node();
            String localIp = InetAddress.getLocalHost().getHostAddress();
            if ("127.0.0.1".equals(localIp)) {
                System.out.print("Enter seed: ");
                int seed = scanner.nextInt();
                localServer.setIpaddress("127.0.0." + seed);
                localServer.setPortNumber(GlobalConstant.PORT_MIN + seed);
            } else {
                localServer.setIpaddress(localIp);
                localServer.setPortNumber(GlobalConstant.PORT_LISTEN);
            }

            GlobalState.setLocalServerNode(localServer);
            server = new Server(localServer);
            server.start();

            System.out.println("Generating local file list\n................................");
            final Random rand = new Random();
            int index;
            Set<Integer> prevIndices = new HashSet<>();
            for (int i = 3 + rand.nextInt(3); i > 0; i--) {
                // pick previously unused file
                do {
                    index = rand.nextInt(GlobalConstant.ALL_FILES.size());
                } while (prevIndices.contains(index));

                prevIndices.add(index);
                String file = GlobalConstant.ALL_FILES.get(index);
                GlobalState.getLocalFiles().add(file);
                System.out.format("%2d %s\n", index, file);
            }
            System.out.println("File list generated\n");

        } catch (IOException ex) {
            System.out.println("Unable to create log files");
            ex.printStackTrace();
        }
    }
}


