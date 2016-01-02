package com.vishlesha.network;

import com.vishlesha.dataType.Node;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ridwan on 1/1/16.
 */
public class Client extends Base {

    Socket socket = null;
    String sendAddress;
    int sendPortNumber;
    ExecutorService clientService = Executors.newFixedThreadPool(globalConstant.NUM_THREADS_CLIENT_SERVICE);


    public Socket getSocket() {
        return socket;
    }


    public Client(Node node) {
        super();
        this.sendAddress = node.getIpaddress();
        this.sendPortNumber = node.getPortNumber();
    }

    public void sendRequest(String requestMessage, CallBack callBack) {
        clientService.submit(new Runnable() {
            @Override
            public void run() {
                String responseLine = null;
                try {
                    socket = new Socket(sendAddress, sendPortNumber);
                    setInputStream(socket);
                    setOutputStream(socket);
                    if (socket != null && getOutputStream() != null && getInputStream() != null) {
                        getOutputStream().write(requestMessage);
                        getOutputStream().flush();
                        responseLine = getInputStream().readLine();
                    }
                } catch (UnknownHostException ex) {
                    System.out.println("Unknown Host");
                } catch (IOException ex) {
                    System.out.println("IO exception: " + ex.getMessage());
                    ex.printStackTrace();
                }

                callBack.run(responseLine);
            }
        });

    }

    public void stop() {
        try {
            getInputStream().close();
            getOutputStream().close();
            socket.close();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}
