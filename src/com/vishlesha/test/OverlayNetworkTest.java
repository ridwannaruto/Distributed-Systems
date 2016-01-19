package com.vishlesha.test;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.errorHandler.RegisterErrorHandler;
import com.vishlesha.network.CallBack;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.*;
import com.vishlesha.response.JoinResponse;
import com.vishlesha.response.LeaveResponse;
import com.vishlesha.response.RegisterResponse;
import com.vishlesha.response.UnregisterResponse;

import java.net.InetAddress;

/**
 * Created by ridwan on 1/1/16.
 */
public class OverlayNetworkTest {

    String responseMessage;
    Node bootstrapServer, localServer;
    Client clientInstance;
    Server serverInstance;
    String bootstrapIP = "127.0.0.1";
    int bootstrapPort = 1040;

    int shortSleepDuration = 3000, longSleepDuration = 10000;

    public void runTest() {
        GlobalState.setTestMode(true);
        initialize();
        System.out.println("Running register user test");
    //    testRegister();
        System.out.println("Running register same user test");
    //    testRegisterSameUserName();
        System.out.println("Running unregister user test");
    //    testUnregister();
        System.out.println("Running join server test");
        testJoinServer();
        System.out.println("Running leave server test");
        testLeaveServer();
        terminate();
    }



    private void terminate(){
        serverInstance.Stop();
        clientInstance.stop();

    }

    private void initialize(){
        bootstrapServer = new Node();
        bootstrapServer.setIpaddress(bootstrapIP);
        bootstrapServer.setPortNumber(bootstrapPort);

        try{
            localServer = new Node();
            localServer.setIpaddress(InetAddress.getLocalHost().getHostAddress());
            localServer.setPortNumber(GlobalConstant.PORT_LISTEN);
        }catch (Exception ex){
            System.out.println(ex);
        }

        GlobalState.setLocalServerNode(localServer);
        clientInstance = new Client();
        serverInstance = new Server(new Node(null,0));
        serverInstance.start();
    }

    /*
    private void testRegister (){
        RegisterRequest registerRequest = new RegisterRequest(bootstrapServer);
        clientInstance.sendTCPRequest(registerRequest, new CallBack() {
            @Override
            public void run(String responseMessage, Node respondNode) {
                RegisterResponse serverResponse = new RegisterResponse(responseMessage, respondNode);
                if (!serverResponse.isFail()) {
                    System.out.println("Register User Test: Success");
                } else
                    System.out.println("Register User Test: Fail");
            }
        });
        sleep(shortSleepDuration);

    }

    private void testRegisterSameUserName(){
        RegisterRequest registerRequest = new RegisterRequest(bootstrapServer, GlobalState.getUsername());
        clientInstance.sendTCPRequest(registerRequest, new CallBack() {
            @Override
            public void run(String responseMessage, Node respondNode) {
                RegisterResponse serverResponse = new RegisterResponse(responseMessage, respondNode);
                if (serverResponse.isFail() && serverResponse.getError().getErrorCode() == GlobalConstant.ERR_CODE_REG_USERNAME) {
                    System.out.println("Register Same User Test: Success");
                    RegisterErrorHandler registerErrorHandler = new RegisterErrorHandler(serverResponse.getError());
                    if (registerErrorHandler.isRetry()) {
                        Request newRegisterRequest = registerErrorHandler.getRequest();
                        clientInstance.sendTCPRequest(newRegisterRequest, this);
                    }
                } else {
                    System.out.println("Register Same  User Error Handle Test: Success");
                }
            }
        });
        sleep(shortSleepDuration);
    }

    private void testUnregister(){

        UnregisterRequest unregisterRequest = new UnregisterRequest(bootstrapServer);
        clientInstance.sendTCPRequest(unregisterRequest, new CallBack() {
            @Override
            public void run(String responseMessage, Node respondNode) {
                UnregisterResponse serverResponse = new UnregisterResponse(responseMessage, respondNode);
                if (!serverResponse.isFail()) {
                    System.out.println("Unregister User Test: Success");
                } else
                    System.out.println("Unregister User Test: Fail");
            }
        });

        sleep(shortSleepDuration);
    }

    */

    private void testJoinServer(){
        JoinRequest joinRequest = new JoinRequest(localServer);
        clientInstance.sendUDPRequest(joinRequest);
        sleep(shortSleepDuration);
    }



    private void testLeaveServer(){
        LeaveRequest leaveRequest = new LeaveRequest(localServer);
        clientInstance.sendUDPRequest(leaveRequest);
        sleep(shortSleepDuration);
    }


    public void sleep(int duratrion){
        try{
            Thread.sleep(duratrion);
        }catch (Exception ex){

        }
    }
}
