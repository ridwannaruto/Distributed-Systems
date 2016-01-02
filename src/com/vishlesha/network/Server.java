package com.vishlesha.network;

import com.vishlesha.app.GlobalConstant;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ridwan on 1/1/16.
 */
public class Server extends Base implements Runnable {


    ExecutorService workerPool = Executors.newFixedThreadPool(GlobalConstant.NUM_THREADS_SERVER_WORKER_POOL);
    ServerSocket socketService;

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void run() {


        try {
            socketService = new ServerSocket(GlobalConstant.PORT_LISTEN);
            while (!socketService.isClosed()) {
                try {

                    final Socket socket = socketService.accept();

                    workerPool.submit(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                String serverRequest = inputStream.readLine();
                                System.out.println("Local Server Node Received: " + serverRequest);
                            } catch (IOException ex) {
                                System.out.println("Server:" + ex);
                            }
                        }

                    });

                } catch (IOException ex) {
                    System.out.println("Server: " + ex);
                }

            }
        } catch (IOException ex) {
            System.out.println("Server: " + ex);
        }
    }

    public void Stop() {
        try {
            workerPool.shutdown();
            socketService.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


}
