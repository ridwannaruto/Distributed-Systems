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
import com.vishlesha.webservice.model.SearchContext;
import com.vishlesha.webservice.server.ServicePublisher;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Logger;

/*
    * Runs the interface for the client application
    * Displays the dialog box asking for connection details and query commands
    * Connects to server and displays the responseMessage
*/

class App {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(final String[] args) throws IOException {
        Client client = GlobalState.getClient();

        System.out.println("Vishlesha Distributed System");
        System.out.println("----------------------------\n");
        setup(client);

        Request regRequest = new RegisterRequest(GlobalState.getBootstrapNode());
        client.sendTCPRequest(regRequest);

        // TODO modify to issue multiple queries
        System.out.println("connecting to the network..........");
        if (args.length > 0) {
            if (args[0].equals("-ws")) {
                webServiceBasedFlow();
            }
            else {
                socketBasedFlow();
            }
        }else {
            socketBasedFlow();
        }

    }

    private static void socketBasedFlow() {
        boolean print = true;

        while (true) {
            if (GlobalState.getNeighbors().size() > 0) {
                if (print) {
                    System.out.println("connected to network");
                    System.out.println("\nInitiate Search\n---------------------");
                    print = false;
                }

                System.out.print("\nEnter your search query (or _TABLE = neighbor table, _FILES = file list, _STATS = stats): ");
                String query = scanner.nextLine();

                if (query.startsWith("_TABLE")) {
                    showNeighborTable();
                } else if (query.startsWith("_FILES")) {
                    showFileList();
                } else if (query.startsWith("_STATS")) {
                    showStats();
                } else {
                    SearchRequestHandler searchRequestHandler = new SearchRequestHandler();
                    searchRequestHandler.initiateSearch(query);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showNeighborTable() {
        System.out.println("\nNeighbor table:\n");
        Map<Node, Integer> neighborConnections = GlobalState.getNeighborCountList();
        System.out.printf("%9s:%-4s  %2s  %s\n\n", "IP_ADDR", "PRT", "NC", " FILES");

        for (Map.Entry<Node, List<String>> neighbor : GlobalState.getNeighbors().entrySet()) {
            Node node = neighbor.getKey();
            List<String> files = neighbor.getValue();

            Integer neighborCount = neighborConnections.get(node);
            if (neighborCount == null) {
                neighborCount = -1;
            }
            System.out.printf("%9s:%-4d  %2d  %s\n", node.getIpaddress(), node.getPortNumber(), neighborCount, files);
        }
        System.out.println();
    }

    private static void showFileList() {
        System.out.println("\nLocal files:\n");
        for (String file : GlobalState.getLocalFiles()) {
            System.out.println(file);
        }
        System.out.println();
    }

    private static void showStats() {
        System.out.println("\nSystem statistics\n-----------------");
        System.out.println("Number of request received: " + GlobalState.getReceivedRequestCount());
        System.out.println("Number of request forwarded: " + GlobalState.getForwardedRequestCount());
        System.out.println("Number of request answered: " + GlobalState.getAnsweredRequestCount());
        System.out.println();
    }

    private static void webServiceBasedFlow() {
        System.out.println("LOCAL IP "+GlobalState.getLocalServerNode().getIpaddress());
        ServicePublisher.publish(GlobalState.getLocalServerNode().getIpaddress(), 8888); //Server for handling incomming requests

        System.out.println(GlobalState.getNeighbors().keySet());
        //        mf.searchFile();

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
                SearchContext.initiateSearch(GlobalState.getLocalServerNode().getIpaddress(),GlobalState.getLocalServerNode().getPortNumber()+"",searchQuery,0);
            }
        }
    }

    private static void setup(final Client client) {
        int bootstrapPort;
        final Logger log = Logger.getLogger(AppLogger.APP_LOGGER_NAME);
        Server server;

        final Node bootstrapServerNode = new Node();
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

        GlobalState.setBootstrapNode(bootstrapServerNode);

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

                showStats();
            }
        });
        try {
           //AppLogger.setup();

            // switch to seed-based IP addresses on 127.0.0.1 (local environment)
            Node localServer = new Node();
            String localIp = InetAddress.getLocalHost().getHostAddress();
            if ("127.0.0.1".equals(localIp)) {
                System.out.print("Enter seed: ");
                int seed = Integer.parseInt(scanner.nextLine().trim());
                localServer.setIpaddress("127.0.0." + seed);
                localServer.setPortNumber(GlobalConstant.PORT_MIN + seed);
            } else {
                localServer.setIpaddress(localIp);
                localServer.setPortNumber(GlobalConstant.PORT_LISTEN);
            }

            GlobalState.setLocalServerNode(localServer);
            server = GlobalState.getServer();
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


