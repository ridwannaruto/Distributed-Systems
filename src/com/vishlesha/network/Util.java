package com.vishlesha.network;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by janaka on 2/8/16.
 */
public class Util {
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
