package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.request.handler.RequestHandler;
import com.vishlesha.response.handler.ResponseHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/1/16.
 */
public class Server implements Runnable {

    private final ExecutorService workerPool = Executors.newFixedThreadPool(GlobalConstant.NUM_THREADS_SERVER_WORKER_POOL);
    private final Logger log = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);
    private DatagramSocket serverSocket;
    private final Node node;


    public Server(Node node) {
        this.node = node;
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void run() {

        try {
            serverSocket = new DatagramSocket(node.getPortNumber());
            log.info("Server socket created and waiting for requests..");
            while (!serverSocket.isClosed()) {
                try {

                    byte[] receiveData = new byte[GlobalConstant.MSG_BYTE_MAX_LENGTH];
                    final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    final String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = receivePacket.getPort();
                    final Node sender = new Node(IPAddress.getHostAddress(), port);
                    log.info("UDP Server Received: " + message + " from " + sender.toString());

                    workerPool.submit(new Runnable() {
                        @Override
                        public void run() {

                            String[] token = message.split(" ");

                            //check if a new request or a response to a request
                            if (token[1].contains("OK")) {
                                ResponseHandler responseHandler = new ResponseHandler();
                                responseHandler.handle(message, sender);
                            } else {
                                RequestHandler requestHandler = new RequestHandler();
                                requestHandler.handle(message, sender);
                            }
                        }


                    });

                } catch (IOException ex) {
                    log.severe("Server Exception:  " + ex);
                    log.severe(Util.getStackTrace(ex));
                    ex.printStackTrace();
                }
            }

        } catch (IOException ex) {
            System.out.println("Server Exception:  " + ex);
            log.severe(Util.getStackTrace(ex));
            ex.printStackTrace();
        }
    }

    public void Stop() {
        workerPool.shutdown();
        serverSocket.close();
    }
}
