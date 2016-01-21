package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.log.AppLogger;
import com.vishlesha.request.Request;
import com.vishlesha.response.Response;
import com.vishlesha.response.handler.ResponseHandler;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
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

                    if (!GlobalState.isResponsePending(request) || request.getRetryCount() == 0)
                        GlobalState.addResponsePendingRequest(request.getHashCode(),request);
                    DatagramSocket clientSocket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(request.getRecipientNode().getIpaddress());
                    int portNumber = request.getRecipientNode().getPortNumber();
                    byte[] sendData;
                    if (IPAddress == null )
                        log.warning("no recipient set for " + request.getClass());
                    String requestMessage = request.getRequestMessage();
                    log.info("UDP Request Message: " + requestMessage + " sent to " + request.getRecipientNode().toString());
                    sendData = requestMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,portNumber );
                    clientSocket.send(sendPacket);
                    TimerTask retryTask = new RetryTask(request);
                    Timer timer = new Timer();
                    timer.schedule(retryTask, GlobalState.getRoundTripTime());

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(ex.getStackTrace().toString());
                    ex.printStackTrace();
                } catch (Exception ex){
                    log.severe(ex.getMessage());
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

                    if (IPAddress == null )
                        log.warning("no recipient set for " + response.getClass());
                    byte[] sendData;
                    String responseMessage = response.getResponseMessage();
                    log.info("UDP Response Message: " + responseMessage + " sent to " + response.getRecipientNode().toString());
                    sendData = responseMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
                    clientSocket.send(sendPacket);

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(ex.getStackTrace().toString());
                    ex.printStackTrace();
                } catch (Exception ex){
                    log.severe(ex.getMessage());
                }
            }


        });
    }

    public void sendTCPRequest(final Request request) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long start, end;
                    String responseLine = null;
                    socket = new Socket(request.getRecipientNode().getIpaddress(), request.getRecipientNode().getPortNumber());
                    socket.setTcpNoDelay(true);
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    if (socket != null && outputStream != null && inputStream != null) {
                        String requestMessage = request.getRequestMessage();
                        log.info("TCP Client Message Sent: " + requestMessage + " to " + request.getRecipientNode().toString());
                        outputStream.write(requestMessage);
                        start = System.currentTimeMillis();
                        outputStream.flush();
                        responseLine = inputStream.readLine();
                        end = System.currentTimeMillis();
                        GlobalState.setRoundTripTime(end-start + 1000);
                        log.info("TCP Client Message Received: " + responseLine + " from " + request.getRecipientNode().toString());
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
                    System.out.println("Could not connect to boostrap server");
                } catch (Exception ex){
                    log.severe(ex.getMessage());
                }
            }
        });
    }

    public void stop(){
        workerPool.shutdown();
    }
}
