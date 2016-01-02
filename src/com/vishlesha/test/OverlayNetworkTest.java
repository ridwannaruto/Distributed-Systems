package com.vishlesha.test;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.UnregisterRequest;
import com.vishlesha.response.RegisterResponse;
import com.vishlesha.response.UnregisterResponse;

/**
 * Created by ridwan on 1/1/16.
 */
public class OverlayNetworkTest {

    String responseMessage;
    Node bootstrapServer, localServer;
    Client clientForBS;
    Server server;

    public void runTest() {

        GlobalConstant.setTestMode(false);
        initialize();
        testRegisterSuccess();
        testRegisterSameUserName();
        testUnregisterSuccess();

    }

    private void initialize(){
        bootstrapServer = new Node();
        localServer = new Node();

        bootstrapServer.setIpaddress("127.0.0.1");
        bootstrapServer.setPortNumber(1122);

        localServer.setIpaddress("127.0.0.1");
        localServer.setPortNumber(GlobalConstant.PORT_LISTEN);


        clientForBS = new Client(bootstrapServer);
        server = new Server();
        server.run();
    }

    private void testRegisterSuccess(){
        RegisterRequest registerRequest = new RegisterRequest(localServer);
        //responseMessage = clientForBS.sendRequest(registerRequest.getRequest());
       // RegisterResponse registerResponse = new RegisterResponse(responseMessage);

    }

    private void testRegisterSameUserName(){
        RegisterRequest registerRequest = new RegisterRequest(localServer);
       // responseMessage = clientForBS.sendRequest(registerRequest.getRequest());
        //RegisterResponse registerResponse = new RegisterResponse(responseMessage);
    }

    private void testUnregisterSuccess(){
        UnregisterRequest unregisterRequest = new UnregisterRequest(localServer);
       // responseMessage = clientForBS.sendRequest(unregisterRequest.getRequest());
        //UnregisterResponse unregisterResponse = new UnregisterResponse(responseMessage);
    }
}
