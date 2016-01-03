package com.vishlesha.app;

//product of Vishlesha
//created by Ridwan

import com.vishlesha.dataType.Node;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.Request;
import com.vishlesha.response.RegisterResponse;

import java.io.*;
import java.util.Scanner;

/*
    * Runs the interface for the client application
    * Displays the dialog box asking for connection details and query commands
    * Connects to server and displays the responseMessage
*/

public class App {

    public static void main(String[] args) throws IOException {
        String bootstrapAddress,userName;
        int bootstrapPort;
        GlobalConstant globalConstant = new GlobalConstant();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vishlesha Distributed System");
        System.out.println("----------------------------");

        System.out.println("Please enter the details of the bootstrap server");
        System.out.print("Bootstrap Node IP address: ");
        bootstrapAddress = scanner.next();
        System.out.print("\nBootstrap Node port: ");
        bootstrapPort = scanner.nextInt();

        Node clientForBS = new Node();
        clientForBS.setIpaddress(bootstrapAddress);
        clientForBS.setPortNumber(bootstrapPort);

        Node localServer = new Node();
        localServer.setIpaddress("127.0.0.1");
        localServer.setPortNumber(globalConstant.PORT_LISTEN);
//        GlobalState.setLocalServerNode(localServer);

        Client client = new Client();
        GlobalState.setClient(client);

        Server server = new Server();
        server.start();
        Request regRequest = new RegisterRequest(localServer);
        client.sendTCPRequest(regRequest, new CallBack() {
            public void run(String responseMessage, Node respondNode) {
                System.out.println("BootStrap Node: " + responseMessage);
                RegisterResponse serverResponse = new RegisterResponse(responseMessage, respondNode);
                if (serverResponse.isFail()) {

                }

            }
        });
        //System.out.println("BootStrap Node: " + responseMessage);
    }
}


