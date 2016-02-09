package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.log.AppLogger;
import com.vishlesha.message.Message;
import com.vishlesha.request.Request;
import com.vishlesha.response.Response;
import com.vishlesha.response.handler.ResponseHandler;
import com.vishlesha.timer.task.RetryRequestTask;

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
public class Client {

    private Socket socket = null;
    private final ExecutorService workerPool = Executors.newFixedThreadPool(GlobalConstant.NUM_THREADS_CLIENT_WORKER_POOL);
    private final Logger log = Logger.getLogger(AppLogger.NETWORK_LOGGER_NAME);

    public Socket getSocket() {
        return socket;
    }

    public void sendUDPMessage (final Message message) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Node localServer = GlobalState.getLocalServerNode();
                    message.setSenderNode(localServer);

                    String requestMessage = message.getMessage();
                    log.info("UDP Message: " + requestMessage + " sent to " + message.getRecipientNode().toString());

                    DatagramSocket clientSocket = new DatagramSocket(0,
                            InetAddress.getByName(localServer.getIpaddress()));
                    InetAddress destAddress = InetAddress.getByName(message.getRecipientNode().getIpaddress());
                    int portNumber = message.getRecipientNode().getPortNumber();
                    byte[] sendData;

                    sendData = requestMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destAddress, portNumber);
                    clientSocket.send(sendPacket);
                    clientSocket.close();

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(Util.getStackTrace(ex));
                } catch (Exception ex) {
                    log.severe(Util.getStackTrace(ex));
                }
            }
        });
    }

    public void sendUDPRequest(final Request request) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Node localServer = GlobalState.getLocalServerNode();
                    request.setSenderNode(localServer);

                    String requestMessage = request.getRequestMessage();
                    log.info("UDP Request: " + requestMessage + " sent to " + request.getRecipientNode().toString());

                    DatagramSocket clientSocket = new DatagramSocket(0,
                            InetAddress.getByName(localServer.getIpaddress()));
                    InetAddress destAddress = InetAddress.getByName(request.getRecipientNode().getIpaddress());
                    int portNumber = request.getRecipientNode().getPortNumber();
                    byte[] sendData;

                    if (!GlobalState.isResponsePending(request) || request.getRetryCount() == 0) {
                        GlobalState.addResponsePendingRequest(request.getHashCode(), request);
                    }

                    sendData = requestMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destAddress, portNumber);
                    clientSocket.send(sendPacket);
                    clientSocket.close();

                    TimerTask retryTask = new RetryRequestTask(request);
                    Timer timer = new Timer();
                    timer.schedule(retryTask, GlobalState.getRoundTripTime());

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(Util.getStackTrace(ex));
                } catch (Exception ex) {
                    log.severe(Util.getStackTrace(ex));
                }
            }
        });
    }

    public void sendUDPResponse(final Response response) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Node localServer = GlobalState.getLocalServerNode();
                    response.setSenderNode(localServer);

                    String responseMessage = response.getResponseMessage();
                    log.info("UDP Response: " + responseMessage + " to " + response.getRecipientNode().toString());

                    DatagramSocket clientSocket = new DatagramSocket(0,
                            InetAddress.getByName(GlobalState.getLocalServerNode().getIpaddress()));
                    InetAddress IPAddress = InetAddress.getByName(response.getRecipientNode().getIpaddress());
                    int portNumber = response.getRecipientNode().getPortNumber();

                    byte[] sendData;
                    sendData = responseMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
                    clientSocket.send(sendPacket);

                } catch (UnknownHostException ex) {
                    log.severe("Unknown Host");
                } catch (IOException ex) {
                    log.severe("IO exception: " + ex.getMessage());
                    log.severe(Util.getStackTrace(ex));
                    ex.printStackTrace();
                } catch (Exception ex) {
                    log.severe(Util.getStackTrace(ex));
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

                    String requestMessage = request.getRequestMessage();
                    log.info("TCP Client: " + requestMessage + " to " + request.getRecipientNode().toString());

                    socket = new Socket(request.getRecipientNode().getIpaddress(), request.getRecipientNode().getPortNumber());
                    socket.setTcpNoDelay(true);
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String responseLine = null;

                    if (socket != null) {
                        outputStream.write(requestMessage);
                        start = System.currentTimeMillis();
                        outputStream.flush();
                        responseLine = inputStream.readLine();
                        end = System.currentTimeMillis();
                        GlobalState.setRoundTripTime(end - start + 1000);
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
                    log.severe(Util.getStackTrace(ex));
                    System.out.println("Could not connect to boostrap server");
                } catch (Exception ex) {
                    log.severe(Util.getStackTrace(ex));
                }
            }
        });
    }

    public void stop() {
        workerPool.shutdown();
    }
}
