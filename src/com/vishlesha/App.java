package com.vishlesha;

//product of Vishlesha
//created by Ridwan

import java.io.*;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

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

        System.out.println("Please enter the details of the bootstrap server");
       // System.out.print("Bootstrap Server IP address: ");
       // bootstrapAddress = scanner.next();
        //System.out.print("\nBootstrap Server port: ");
        //bootstrapPort = scanner.nextInt();

        bootstrapAddress = "127.0.0.1";
        bootstrapPort = 1122;
        String currentTime = String.valueOf(new Date().getTime());
        userName = currentTime.substring(currentTime.length()-8);

        Client client = new Client(bootstrapAddress,bootstrapPort);
        Server server = new Server();
        server.Start();
        String registerRequest = " REG " + client.getSocket().getLocalAddress().toString().substring(1) + " " + constant.LISTEN_PORT + " " + userName;
        registerRequest = String.format("%04d",registerRequest.length()+4) +  registerRequest;
        String serverResponse = client.sendRequest(registerRequest);
        System.out.println("BS Server: " + serverResponse);

    }
}


