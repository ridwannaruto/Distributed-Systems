package com.vishlesha.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ridwan on 1/1/16.
 */
public class Server extends Base implements Runnable {

    ServerSocket socketService;
    Socket socket;

    public Socket getSocket(){
        return socket;
    }

    public void run(){
        try{
            socketService = new ServerSocket(globalConstant.PORT_LISTEN);
            socket = socketService.accept();
            setInputStream(socket);
            setOutputStream(socket);

            while(true){
                String serverRequest = getInputStream().readLine();
                System.out.println("Local Server Node Received: " + serverRequest);
            }

        }catch (IOException ex){
            System.out.println(ex);
        }

    }

    public void Stop(){
        try{
            getInputStream().close();
            getOutputStream().close();
            socket.close();
        }catch (IOException ex){
            System.out.println(ex);
        }
    }


}
