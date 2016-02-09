package com.vishlesha.app;

import java.util.ArrayList;
import java.util.List;

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
    public static final int PORT_RANGE = 150;

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

    public static final int[][] topology = new int[11][2];

    public static final List<String> ALL_FILES = new ArrayList<String>();
    private static final List<String> ALL_QUERIES = new ArrayList<String>();

    static {
        ALL_FILES.add("Adventures_of_Tintin");
        ALL_FILES.add("Jack_and_Jill");
        ALL_FILES.add("Glee");
        ALL_FILES.add("The_Vampire_Diarie");
        ALL_FILES.add("King_Arthur");
        ALL_FILES.add("Windows_XP");
        ALL_FILES.add("Harry_Potter");
        ALL_FILES.add("Kung_Fu_Panda");
        ALL_FILES.add("Lady_Gaga");
        ALL_FILES.add("Twilight");
        ALL_FILES.add("Windows_8");
        ALL_FILES.add("Mission_Impossible");
        ALL_FILES.add("Turn_Up_The_Music");
        ALL_FILES.add("Super_Mario");
        ALL_FILES.add("American_Pickers");
        ALL_FILES.add("Microsoft_Office_2010");
        ALL_FILES.add("Happy_Feet");
        ALL_FILES.add("Modern_Family");
        ALL_FILES.add("American_Idol");
        ALL_FILES.add("Hacking_for_Dummies");

        ALL_QUERIES.add("Twilight");
        ALL_QUERIES.add("Jack");
        ALL_QUERIES.add("American_Idol");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Twilight_saga");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Feet");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Twilight");
        ALL_QUERIES.add("Windows");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Mission_Impossible");
        ALL_QUERIES.add("Twilight");
        ALL_QUERIES.add("Windows_8");
        ALL_QUERIES.add("The");
        ALL_QUERIES.add("Happy");
        ALL_QUERIES.add("Windows_8");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Super_Mario");
        ALL_QUERIES.add("Jack_and_Jill");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Impossible");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Turn_Up_The_Music");
        ALL_QUERIES.add("Adventures_of_Tintin");
        ALL_QUERIES.add("Twilight_saga");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Super_Mario");
        ALL_QUERIES.add("American_Pickers");
        ALL_QUERIES.add("Microsoft_Office_2010");
        ALL_QUERIES.add("Twilight");
        ALL_QUERIES.add("Modern_Family");
        ALL_QUERIES.add("Jack_and_Jill");
        ALL_QUERIES.add("Jill");
        ALL_QUERIES.add("Glee");
        ALL_QUERIES.add("The_Vampire_Diarie");
        ALL_QUERIES.add("King_Arthur");
        ALL_QUERIES.add("Jack_and_Jill");
        ALL_QUERIES.add("King_Arthur");
        ALL_QUERIES.add("Windows_XP");
        ALL_QUERIES.add("Harry_Potter");
        ALL_QUERIES.add("Feet");
        ALL_QUERIES.add("Kung_Fu_Panda");
        ALL_QUERIES.add("Lady_Gaga");
        ALL_QUERIES.add("Gaga");
        ALL_QUERIES.add("Happy_Feet");
        ALL_QUERIES.add("Twilight");
        ALL_QUERIES.add("Hacking");
        ALL_QUERIES.add("King");

        topology[0][0] = 0;
        topology[0][1] = 0;
        topology[1][0] = 0;
        topology[1][1] = 0;
        topology[2][0] = 1;
        topology[2][1] = 0;
        topology[3][0] = 1;
        topology[3][1] = 2;
        topology[4][0] = 2;
        topology[4][1] = 3;
        topology[5][0] = 3;
        topology[5][1] = 4;
        topology[6][0] = 4;
        topology[6][1] = 5;
        topology[7][0] = 5;
        topology[7][1] = 6;
        topology[8][0] = 6;
        topology[8][1] = 7;
        topology[9][0] = 7;
        topology[9][1] = 8;
        topology[10][0] = 8;
        topology[10][1] = 9;
    }
}
