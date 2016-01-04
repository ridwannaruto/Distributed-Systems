package com.vishlesha.app;

import com.vishlesha.dataType.Node;
import com.vishlesha.search.FileIpMapping;

/**
 * Created by ridwan on 1/1/16.
 */
public class GlobalState {


    private static String username;
    private static boolean testMode;
    private static Node localServerNode;
    private static FileIpMapping fileIpMapping;

    public static Node getLocalServerNode() {
        return localServerNode;
    }

    public static void setLocalServerNode(Node localServerNode) {
        GlobalState.localServerNode = localServerNode;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        GlobalState.username = username;
    }

    public static boolean isTestMode() {
        return testMode;
    }

    public static void setTestMode(boolean testMode) {
        GlobalState.testMode = testMode;
    }

   public static FileIpMapping getFileIpMapping() {
      return fileIpMapping;
   }

   public static void setFileIpMapping(FileIpMapping fileIpMapping) {
      GlobalState.fileIpMapping = fileIpMapping;
   }
}
