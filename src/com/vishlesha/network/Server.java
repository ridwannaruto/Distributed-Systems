package com.vishlesha.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ridwan on 1/1/16.
 */
public class Server extends Base {

    ServerSocket socketService;
    Socket socket;

    public Socket getSocket(){
        return socket;
    }

    public void Start(){
        try{
            socketService = new ServerSocket(globalConstant.LISTEN_PORT);
            socket = socketService.accept();
            setInputStream(socket);
            setOutputStream(socket);
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
