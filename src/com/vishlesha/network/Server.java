package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ridwan on 1/1/16.
 */
public class Server extends Base implements Runnable {


    ExecutorService workerPool = Executors.newFixedThreadPool(GlobalConstant.NUM_THREADS_SERVER_WORKER_POOL);
    DatagramSocket serverSocket;

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void run() {

        try {

            serverSocket = new DatagramSocket(GlobalConstant.PORT_LISTEN);
            System.out.println("Server socket created and waiting for requests..");
            while (!serverSocket.isClosed()) {
                try {

                    byte[] receiveData = new byte[GlobalConstant.MSG_BYTE_MAX_LENGTH];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String requestMessage = new String(receivePacket.getData(),0, receivePacket.getLength());
                    if (GlobalState.isTestMode())
                    System.out.println("Server Received: " + requestMessage);

                    workerPool.submit(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                byte[] sendData = new byte[GlobalConstant.MSG_BYTE_MAX_LENGTH];
                                InetAddress IPAddress = receivePacket.getAddress();
                                int port = receivePacket.getPort();
                                sendData = "Server Received".getBytes();
                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                            } catch (IOException ex) {
                                System.out.println("Server Exception: " + ex);
                            }
                        }

                    });

                } catch (IOException ex) {
                    System.out.println("Server Exception:  " + ex);
                }

            }

        } catch (IOException ex) {
            System.out.println("Server Exception:  " + ex);
        }

    }


    public void Stop() {

        workerPool.shutdown();
        serverSocket.close();

    }


}
