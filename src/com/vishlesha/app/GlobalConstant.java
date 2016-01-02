package com.vishlesha.app;

import com.vishlesha.dataType.Node;

/**
 * Created by ridwan on 1/1/16.
 */
public class GlobalConstant {

    public static final int PORT_LISTEN = 1234;
    public static final int PORT_CLIENT = 1235;

    //Error Codes
    public static final int ERR_CODE_GENERAL = 0;

    public static final int ERR_CODE_REG_COMMAND = 10;
    public static final int ERR_CODE_REG_USERNAME = 11;
    public static final int ERR_CODE_REG_IPPORT = 12;
    public static final int ERR_CODE_REG_FULL = 13;

    public static final int ERR_CODE_UNREG_INVALID = 20;
    public static final int ERR_CODE_JOIN = 30;
    public static final int ERR_CODE_LEAVE = 40;
    public static final int ERR_CODE_SEARCH_GENERAL = 50;
    public static final int ERR_CODE_SEARCH_UNREACHABLE = 51;


    public static final int NUM_THREADS_CLIENT_WORKER_POOL = 100;
    public static final int NUM_THREADS_SERVER_WORKER_POOL = 100;
    public static final int PORT_MIN = 1050;
    public static final int PORT_RANGE = 5000;

    public static final int MSG_BYTE_MAX_LENGTH = 1024;

    //Error Message
    public static final String ERR_MSG_GENERAL = "Generic Error Message: Command not Understood";
    public static final String ERR_MSG_REG_COMMAND = "Some fail in Command";
    public static final String ERR_MSG_REG_USERNAME = "Username already registered";
    public static final String ERR_MSG_REG_IP_PORT = "IP and Port already registered";
    public static final String ERR_MSG_REG_BS_FULL = "Can't register. Server Full";
    public static final String ERR_MSG_UNREG_INVALID = "IP and Port not found or Incorrect Command";
    public static final String ERR_MSG_JOIN = "Error adding new node to routing table";
    public static final String ERR_MSG_LEAVE = "Error removing node from routing table";
    public static final String ERR_MSG_SEARCH_GENERAL = "Error searching file";
    public static final String ERR_MSG_SEARCH_UNREACHABLE = "Node not reachable";

    //Error Handler
    public static final String ERR_HANDLE_REG_FULL = "Bootstrap server is full. Try again later.";
    public static final String ERR_HANDLE_REG_DEFAULT = "Some fail in command";



    public static final String SUCCESS_MSG_REG = "Successfully registered to the system";
    public static final String SUCCESS_MSG_UNREG = "Successfully unregistered from the system";
    public static final String SUCCESS_MSG_JOIN = "Successfully joined server node";
    public static final String SUCCESS_MSG_LEAVE = "Successfully left server node";
    public static final String SUCCESS_MSG_SEARCH = "Search returned with results";

    public static final String MSG_SEARCH_NORESULT = "No matching result. Searched key not in key table";


}
