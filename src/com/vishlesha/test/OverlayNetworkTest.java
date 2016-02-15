package com.vishlesha.test;

import com.vishlesha.app.GlobalConstant;
import com.vishlesha.app.GlobalState;
import com.vishlesha.dataType.Node;
import com.vishlesha.network.Client;
import com.vishlesha.network.Server;
import com.vishlesha.request.JoinRequest;
import com.vishlesha.request.LeaveRequest;

import java.net.InetAddress;

/**
 * Created by ridwan on 1/1/16.
 */
class OverlayNetworkTest {

    String responseMessage;
    private Node bootstrapServer;
    private Node localServer;
    private Client clientInstance;
    private Server serverInstance;
    private final String bootstrapIP = "127.0.0.1";
    private final int bootstrapPort = 1040;

    private final int shortSleepDuration = 3000;
    int longSleepDuration = 10000;

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

    private void terminate() {
        serverInstance.stop();
        clientInstance.stop();
    }

    private void initialize() {
        bootstrapServer = new Node();
        bootstrapServer.setIpaddress(bootstrapIP);
        bootstrapServer.setPortNumber(bootstrapPort);

        try {
            localServer = new Node();
            localServer.setIpaddress(InetAddress.getLocalHost().getHostAddress());
            localServer.setPortNumber(GlobalConstant.PORT_LISTEN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GlobalState.setLocalServerNode(localServer);
        clientInstance = GlobalState.getClient();
        serverInstance = GlobalState.getServer();
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

    private void testJoinServer() {
        JoinRequest joinRequest = new JoinRequest(localServer);
        clientInstance.sendUDPRequest(joinRequest, false);
    }

    private void testLeaveServer() {
        LeaveRequest leaveRequest = new LeaveRequest(localServer);
        clientInstance.sendUDPRequest(leaveRequest, false);
    }

    void sleep(int duratrion) {
        try {
            Thread.sleep(duratrion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
