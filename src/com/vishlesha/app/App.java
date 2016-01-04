package com.vishlesha.app;

//product of Vishlesha
//created by Ridwan

import com.vishlesha.dataType.Node;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.FileListShareRequest;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.Request;
import com.vishlesha.response.FileListShareResponse;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.RegisterResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

		bootstrapAddress = "127.0.0.1";
		bootstrapPort = 1040;

		Node clientForBS = new Node();
		clientForBS.setIpaddress(bootstrapAddress);
		clientForBS.setPortNumber(bootstrapPort);

		Node localServer = new Node();
		localServer.setIpaddress("127.0.0.1");
		int serverPort =GlobalConstant.PORT_MIN + (int) (Math.random() * GlobalConstant.PORT_RANGE);

		localServer.setPortNumber(serverPort);

		GlobalState.setLocalServerNode(localServer);
		final Client client = new Client();
		final Server server = new Server();
		server.start(localServer.getIpaddress(), localServer.getPortNumber());
		Request regRequest = new RegisterRequest(clientForBS);




        // TODO delete these!!!!
        GlobalState.getLocalFiles().add("Sherloc_Holmes");
        GlobalState.getLocalFiles().add("a_b_c");
        GlobalState.getLocalFiles().add("d_e");




		client.sendTCPRequest(regRequest, new CallBack() {
			public void run(String responseMessage, Node respondNode) {
				System.out.println("BootStrap Node: " + responseMessage);
				RegisterResponse serverResponse =
						new RegisterResponse(responseMessage, respondNode);
				serverResponse.show();
				ArrayList<Node> neighbour = serverResponse.getNodeList();
				for (int i = 0; i < neighbour.size(); i++) {
					GlobalState.addNeighbor(neighbour.get(i));
				}
				Random rand = new Random();
				int j;
				int l = neighbour.size();
				for (j = 0; j < 2 && j < l; j++) {
					int rand1 = rand.nextInt() % l;
					if (l <3) {
						rand1 = j;
					}
					System.out.println(rand1);
					JoinRequest jr = new JoinRequest(neighbour.get(rand1));

					client.sendUDPRequest(jr, new CallBack() {
						@Override
						public void run(String message, Node node) {
							JoinResponse joinResponse = new JoinResponse(message, node);
							System.out.println("Join response returned ! " + joinResponse + "  " + node);

                            // send file list to new neighbor
                            client.sendUDPRequest(new FileListShareRequest(node, GlobalState.getLocalFiles()), new CallBack() {
                                @Override
                                public void run(String message, Node node) {
                                    FileListShareResponse shareResponse = new FileListShareResponse(message);
                                    GlobalState.addNeighborFiles(shareResponse.getRespondNode(), shareResponse.getFiles());
                                }
                            });
						}
					});
				}
				if (j == 0) {
					System.out.println("First Node --> No joins ");
				}

				if (serverResponse.isFail()) {
				}
			}
		});
	}
}


