package com.vishlesha.app;

//product of Vishlesha
//created by Ridwan

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
    * Connects to server and displays the response

*/


public class App {

    public static void main(String[] args) throws IOException {
        String bootstrapAddress,userName;
        int bootstrapPort;
        Constant constant = new Constant();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vishlesha Distributed System");
        System.out.println("----------------------------");

        //System.out.println("Please enter the details of the bootstrap server");
       // System.out.print("Bootstrap Node IP address: ");
       // bootstrapAddress = scanner.next();
        //System.out.print("\nBootstrap Node port: ");
        //bootstrapPort = scanner.nextInt();

        bootstrapAddress = "127.0.0.1";
        bootstrapPort = 1122;

        Client client = new Client(bootstrapAddress,bootstrapPort);
        Server server = new Server();
        server.Start();
        Request regRequest = new RegisterRequest(client.getSocket().getLocalAddress().toString().substring(1), constant.LISTEN_PORT);
        String responseMessage = client.sendRequest(regRequest.getRequest());
        //System.out.println("BootStrap Node: " + responseMessage);
        RegisterResponse serverResponse =  new RegisterResponse(responseMessage);





    }
}


