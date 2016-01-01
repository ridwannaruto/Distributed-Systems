package com.vishlesha.test;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.UnregisterRequest;
import com.vishlesha.response.RegisterResponse;
import com.vishlesha.response.UnregisterResponse;

import java.io.IOException;

/**
 * Created by ridwan on 1/1/16.
 */
public class OverlayNetworkTest {

    String bootstrapAddress, localAddress, responseMessage;
    int bootstrapPort, localPort;
    GlobalConstant globalConstant;
    Client client;
    Server server;

    public void runTest() {

        globalConstant = new GlobalConstant();
        initialize();
        testRegisterSuccess();
        testUnregisterSuccess();

    }

    private void initialize(){
        bootstrapAddress = "127.0.0.1";
        bootstrapPort = 1122;

        localAddress = "127.0.0.1";
        localPort = globalConstant.LISTEN_PORT;

        client = new Client(bootstrapAddress,bootstrapPort);
        server = new Server();
    }

    private void testRegisterSuccess(){
        RegisterRequest registerRequest = new RegisterRequest(localAddress,localPort);
        responseMessage = client.sendRequest(registerRequest.getRequest());
        RegisterResponse registerResponse = new RegisterResponse(responseMessage);

    }

    private void testUnregisterSuccess(){
        UnregisterRequest unregisterRequest = new UnregisterRequest(localAddress,localPort);
        responseMessage = client.sendRequest(unregisterRequest.getRequest());
        UnregisterResponse unregisterResponse = new UnregisterResponse(responseMessage);
    }
}
