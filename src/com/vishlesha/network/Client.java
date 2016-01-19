package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.log.AppLogger;
import com.vishlesha.request.Request;
import com.vishlesha.response.Response;
import com.vishlesha.response.handler.ResponseHandler;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by ridwan on 1/1/16.
 */
public class Client extends Base {

    Socket socket = null;
    ExecutorService workerPool = Executors.newFixedThreadPool(GlobalConstant.NUM_THREADS_CLIENT_WORKER_POOL);
    Logger log = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);
    public Socket getSocket() {
        return socket;
    }

    public void sendUDPRequest(final Request request) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    DatagramSocket clientSocket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(request.getRecipientNode().getIpaddress());
                    int portNumber = request.getRecipientNode().getPortNumber();
                    byte[] sendData;
                    String requestMessage = request.getRequestMessage();
                    log.info("UDP sent: " + requestMessage);
                    sendData = requestMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,portNumber );
                    clientSocket.send(sendPacket);

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(ex.getStackTrace().toString());
                    ex.printStackTrace();
                }
            }


        });
    }

    public void sendUDPResponse(final Response response) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    DatagramSocket clientSocket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(response.getRecipientNode().getIpaddress());
                    int portNumber = response.getRecipientNode().getPortNumber();
                    byte[] sendData;
                    String requestMessage = response.getResponseMessage();
                    log.info("UDP sent: " + requestMessage);
                    sendData = requestMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
                    clientSocket.send(sendPacket);

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(ex.getStackTrace().toString());
                    ex.printStackTrace();
                }
            }


        });
    }

    public void sendTCPRequest(final Request request) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseLine = null;
                    socket = new Socket(request.getRecipientNode().getIpaddress(), request.getRecipientNode().getPortNumber());
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    if (socket != null && outputStream != null && inputStream != null) {
                        String requestMessage = request.getRequestMessage();
                        log.info("TCP sent: " + requestMessage);
                        outputStream.write(requestMessage);
                        outputStream.flush();
                        responseLine = inputStream.readLine();
                        log.info("TCP received: " + responseLine);
                    }
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    ResponseHandler responseHandler = new ResponseHandler();
                    responseHandler.handle(responseLine, request.getRecipientNode());

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(ex.getStackTrace().toString());
                    ex.printStackTrace();
                }
            }
        });
    }

    public void stop(){
        workerPool.shutdown();
    }
}
