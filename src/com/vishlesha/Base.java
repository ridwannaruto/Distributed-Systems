package com.vishlesha;

import java.io.*;
import java.net.Socket;

/**
 * Created by ridwan on 1/1/16.
 */
abstract public class Base {

    private BufferedReader inputStream = null;
    private BufferedWriter outputStream = null;
    Constant constant;

    public Base(){
        constant = new Constant();
    }

    public void setInputStream(Socket socket){
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void setOutputStream(Socket socket){
        try {
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public BufferedReader getInputStream(){
        return inputStream;
    }

    public BufferedWriter getOutputStream(){
        return outputStream;
    }


}
