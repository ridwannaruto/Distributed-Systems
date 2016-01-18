package com.vishlesha.app;

//product of Vishlesha
//created by Ridwan

import com.vishlesha.dataType.Node;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.*;
import com.vishlesha.response.FileListShareResponse;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.RegisterResponse;

import java.io.*;
import java.net.InetAddress;
import java.util.*;

/*
    * Runs the interface for the client application
    * Displays the dialog box asking for connection details and query commands
    * Connects to server and displays the responseMessage
*/

public class App {

    static int nodeId;

    public static void main(final String[] args) throws IOException {
        String bootstrapAddress, userName;
        int bootstrapPort;
        final GlobalConstant globalConstant = new GlobalConstant();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vishlesha Distributed System");
        System.out.println("----------------------------");
        //      System.out.println("Please enter the details of the bootstrap server");
        //      System.out.print("Bootstrap Node IP address: ");
        //      bootstrapAddress = scanner.next();
        //      System.out.print("\nBootstrap Node port: ");
        //      bootstrapPort = scanner.nextInt();

        bootstrapAddress = "172.31.23.116";
        bootstrapPort = 1033;

        final Node bootstrapServerNode = new Node();
        bootstrapServerNode.setIpaddress(bootstrapAddress);
        bootstrapServerNode.setPortNumber(bootstrapPort);

        /*

        System.out.print("Enter seed: ");
        int seed = scanner.nextInt();

        */

        Node localServerNode = new Node();
        localServerNode.setIpaddress(InetAddress.getLocalHost().getHostAddress());
        int serverPort = GlobalConstant.PORT_LISTEN;
        localServerNode.setPortNumber(serverPort);
        GlobalState.setLocalServerNode(localServerNode);


        final Client client = new Client();
        final Server server = new Server(localServerNode);


        server.start();
        Request regRequest = new RegisterRequest(bootstrapServerNode);

        System.out.println("----------------------------");
        System.out.println("Generating local file list...");
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
        System.out.println("----------------------------");

        client.sendTCPRequest(regRequest, new CallBack() {
            public void run(String responseMessage, Node senderNode) {
                System.out.println("BootStrap Node: " + responseMessage);
                RegisterResponse serverResponse = new RegisterResponse(responseMessage, senderNode);
                serverResponse.show();
                ArrayList<Node> neighbour = serverResponse.getNodeList();
                for (int i = 0; i < neighbour.size(); i++) {
                    GlobalState.addNeighbor(neighbour.get(i));
                }
                int j, prev = -1;
                int l = neighbour.size();
                for (j = 0; j < 2 && j < l; j++) {
                    int rand1;
                    if (l < 3) {
                        rand1 = j;
                    } else {
                        // pick a previously unused random number
                        do {
                            rand1 = rand.nextInt(l);
                        } while (prev == rand1);
                    }
                    prev = rand1;

                    System.out.println("Rand : " + rand1);
                    JoinRequest jr = new JoinRequest(neighbour.get(rand1));
                    client.sendUDPRequest(jr);


                }
                if (j == 0) {
                    System.out.println("First Node --> No joins ");
                }
                if (neighbour.size() > 3) {
                    long time = 10 * 1000;
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // TODO modify to issue multiple queries
                    System.out.println("Initiate Search Request......");
                    SearchRequest ser = new SearchRequest(GlobalState.getLocalServerNode(),
                            GlobalConstant.ALL_QUERIES.get(rand.nextInt(GlobalConstant.ALL_QUERIES.size())), 0);
                    client.sendUDPRequest(ser);
                }

                if (serverResponse.isFail()) {
                    System.out.println("Server Failed");
                }
            }
        });

        // handle LEAVE and UNREG on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Unregistering and leaving the network");
                Request unregRequest = new UnregisterRequest(bootstrapServerNode);
                client.sendTCPRequest(unregRequest, CallBack.emptyCallback);

                for (Node n : GlobalState.getNeighbors().keySet()) {
                    client.sendUDPRequest(new LeaveRequest(n));
                }

                // TODO add reasonable blocking mechanism
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Ready to shut down");
            }
        });

    }
}


