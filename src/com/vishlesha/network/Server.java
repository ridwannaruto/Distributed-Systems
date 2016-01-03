package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.request.handler.RequestHandler;

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
    int listenPort;
    String ip;

    public void start(String ip,int listenPort) {
        this.listenPort=listenPort;
        this.ip=ip;
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void run() {

        try {
            serverSocket = new DatagramSocket(listenPort);
            System.out.println("Server socket created and waiting for requests..");
            while (!serverSocket.isClosed()) {
                try {

                    byte[] receiveData = new byte[GlobalConstant.MSG_BYTE_MAX_LENGTH];
                    final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    final String requestMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    if (!GlobalState.isTestMode())
                        System.out.println("Server Received: " + requestMessage);

                    workerPool.submit(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                byte[] sendData;
                                InetAddress IPAddress = receivePacket.getAddress();
                                int port = receivePacket.getPort();
                                RequestHandler requestHandler = new RequestHandler(requestMessage);
                                sendData = requestHandler.getResponse().getResponseMessage().getBytes();
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
