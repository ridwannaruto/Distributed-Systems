package com.vishlesha.app;

/**
 * Created by ridwan on 1/1/16.
 */
public class GlobalConstant {

    public static final int LISTEN_PORT = 1234;
    public static final int CLIENT_PORT = 1235;

    public static final String REGISTER_SUCCESS_RESPONSE = "Successfully Registered";
    public static final String UNREGISTER_SUCCESS_RESPONSE = "Successfully Unregistered";
    public static final String UNREGISTER_ERROR_RESPONSE = "IP and Port not found in BS register";


    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        GlobalConstant.username = username;
    }






}
