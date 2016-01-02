package com.vishlesha.test;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.errorHandler.RegisterErrorHandler;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.RegisterRequest;
import com.vishlesha.request.Request;
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

        GlobalState.setTestMode(true);
        initialize();
        System.out.println("Running register user test");
        testRegisterSuccess();
        System.out.println("Running register same user test");
        testRegisterSameUserName();
        System.out.println("Running unregister user test");
        testUnregisterSuccess();
        terminate();

    }

    private void terminate(){
        server.Stop();
        clientForBS.stop();

    }

    private void initialize(){
        bootstrapServer = new Node();
        bootstrapServer.setIpaddress("127.0.0.1");
        bootstrapServer.setPortNumber(1040);

        localServer = new Node();
        localServer.setIpaddress("127.0.0.1");
        localServer.setPortNumber(GlobalConstant.PORT_LISTEN);

        GlobalState.setLocalServerNode(localServer);



        clientForBS = new Client();
        server = new Server();
        server.start();


    }

    private void testRegisterSuccess(){
        RegisterRequest registerRequest = new RegisterRequest(bootstrapServer);
        clientForBS.sendTCPRequest(registerRequest, new CallBack() {
            @Override
            public void run(String responseMessage, Node respondNode) {
                RegisterResponse serverResponse = new RegisterResponse(responseMessage, respondNode);
                if (!serverResponse.isFail()) {
                    System.out.println("Register User Test: Success");
                } else
                    System.out.println("Register User Test: Fail");
            }
        });
        sleep();

    }

    private void testRegisterSameUserName(){
        RegisterRequest registerRequest = new RegisterRequest(bootstrapServer, GlobalState.getUsername());
        clientForBS.sendTCPRequest(registerRequest, new CallBack() {
            @Override
            public void run(String responseMessage, Node respondNode) {
                RegisterResponse serverResponse = new RegisterResponse(responseMessage, respondNode);
                if (serverResponse.isFail() && serverResponse.getError().getErrorCode() == GlobalConstant.ERR_CODE_REG_USERNAME) {
                    System.out.println("Register Same User Test: Success");
                    RegisterErrorHandler registerErrorHandler = new RegisterErrorHandler(serverResponse.getError());
                    if (registerErrorHandler.isRetry()) {
                        Request newRegisterRequest = registerErrorHandler.getRequest();
                        clientForBS.sendTCPRequest(newRegisterRequest, this);
                    }
                } else {
                    System.out.println("Register Same  User Error Handle Test: Success");
                }
            }
        });
        sleep();
    }

    private void testUnregisterSuccess(){

        UnregisterRequest unregisterRequest = new UnregisterRequest(bootstrapServer);
        clientForBS.sendTCPRequest(unregisterRequest, new CallBack() {
            @Override
            public void run(String responseMessage, Node respondNode) {
                UnregisterResponse serverResponse = new UnregisterResponse(responseMessage, respondNode);
                if (!serverResponse.isFail()) {
                    System.out.println("Unregister User Test: Success");
                } else
                    System.out.println("Unregister User Test: Fail");
            }
        });

        sleep();
    }

    public void sleep(){
        try{
            Thread.sleep(3000);
        }catch (Exception ex){

        }
    }
}
