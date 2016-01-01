package com.vishlesha;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by ridwan on 1/1/16.
 */
public class Server extends Base{

    ServerSocket socket;

    public ServerSocket getSocket(){
        return socket;
    }

    public void Start(){
        try{
            ServerSocket socket = new ServerSocket(constant.LISTEN_PORT);

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
