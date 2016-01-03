package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.request.Request;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ridwan on 1/1/16.
 */
public class Client extends Base {

    Socket socket = null;
    ExecutorService workerPool = Executors.newFixedThreadPool(GlobalConstant.NUM_THREADS_CLIENT_WORKER_POOL);


    public Socket getSocket() {
        return socket;
    }

    public void sendUDPRequest(final Request request, final CallBack callBack) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {

                try {
                    DatagramSocket clientSocket = new DatagramSocket();
                    InetAddress IPAddress = InetAddress.getByName(request.getRecepientNode().getIpaddress());
                    byte[] sendData;
                    byte[] receiveData = new byte[GlobalConstant.MSG_BYTE_MAX_LENGTH];

                    sendData = request.getRequestMessage().getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, request.getRecepientNode().getPortNumber());
                    clientSocket.send(sendPacket);

                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    String responseLine = new String(receivePacket.getData(),0, receivePacket.getLength());
                    clientSocket.close();
                    callBack.run(responseLine, request.getRecepientNode());


                } catch (UnknownHostException ex) {
                    System.out.println("Unknown Host");
                } catch (IOException ex) {
                    System.out.println("IO exception: " + ex.getMessage());
                    ex.printStackTrace();
                }


            }


        });

    }

    public void sendTCPRequest(final Request request, final CallBack callBack) {
        workerPool.submit(new Runnable() {
            @Override
            public void run() {

                try {

                    String responseLine = null;
                    socket = new Socket(request.getRecepientNode().getIpaddress(), request.getRecepientNode().getPortNumber());
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    if (socket != null && outputStream != null && inputStream != null) {
                        outputStream.write(request.getRequestMessage());
                        outputStream.flush();
                        responseLine = inputStream.readLine();
                    }

                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    callBack.run(responseLine, request.getRecepientNode());


                } catch (UnknownHostException ex) {
                    System.out.println("Unknown Host");
                } catch (IOException ex) {
                    System.out.println("IO exception: " + ex.getMessage());
                    ex.printStackTrace();
                }


            }


        });

    }

    public void stop(){
        workerPool.shutdown();
    }


}
