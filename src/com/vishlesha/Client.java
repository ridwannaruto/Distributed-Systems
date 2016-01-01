package com.vishlesha;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ridwan on 1/1/16.
 */
public class Client extends Base {

    Socket socket = null;
    String sendAddress;
    int sendPortNumber;

    public Socket getSocket(){
        return socket;
    }


    public Client(String sendAddress, int sendPortNumber){
        super();
        this.sendAddress = sendAddress;
        this.sendPortNumber = sendPortNumber;
        try{
            socket = new Socket(sendAddress,sendPortNumber);
            setInputStream(socket);
            setOutputStream(socket);
        }catch (IOException ex){
            System.out.println(ex);
        }
    }


    public String sendRequest(String requestMessage){
        String responseLine = null;
        if (socket != null && getOutputStream() != null && getInputStream() != null) {
            try {
                getOutputStream().write(requestMessage);
                getOutputStream().flush();
                responseLine = getInputStream().readLine();
                getInputStream().close();
                getOutputStream().close();
                socket.close();

            } catch (UnknownHostException ex) {
                System.out.println("Unknown Host");
            } catch (IOException ex) {
                System.out.println("IO exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return responseLine;
    }


}
