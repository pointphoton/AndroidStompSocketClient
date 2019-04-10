package com.example.dlog;


import android.util.Log;

public class DLog {

    static {
        isDebuggable = BuildConfig.LOG;
    }

    private static final int MAX_INDEX = 4000;
    private static final int MIN_INDEX = 3000;
    private static boolean isDebuggable;


    /**
     * Writes debugging log.
     */

    public static void write() {
        //   Log.d("*** DEBUG MODE *** ", String.valueOf(isDebuggable));
        if (isDebuggable) {
            StringBuilder sb = new StringBuilder();
            final StackTraceElement stackTrace = new Exception().getStackTrace()[1];
            String fileName = stackTrace.getFileName();
            if (fileName == null) {
                fileName = "";  // It is necessary if you want to use proguard obfuscation.
            }
            sb.append(stackTrace.getMethodName()).append("(").append(fileName).append(":").append(stackTrace.getLineNumber()).append(")");
            Log.d("***", sb.toString());
        }
    }

    public static void write(final Object message) {
        if (isDebuggable) {
            StringBuilder sb = new StringBuilder();
            final StackTraceElement stackTrace = new Exception().getStackTrace()[1];
            String fileName = stackTrace.getFileName();
            if (fileName == null) {
                fileName = "";  // It is necessary if you want to use proguard obfuscation.
            }

            sb.append(stackTrace.getMethodName()).append("(").append(fileName).append(":").append(stackTrace.getLineNumber()).append(")");
            StringBuilder fullMessage = new StringBuilder(String.valueOf(message));
            if (fullMessage.length() > MAX_INDEX) {

                String theSubstring = fullMessage.substring(0, MAX_INDEX);
                int theIndex = MAX_INDEX;

                // Try to find a substring break at a line end.
                // theIndex = theSubstring.lastIndexOf('\n');
                if (theIndex >= MIN_INDEX) {
                    theSubstring = theSubstring.substring(0, theIndex);
                } else {
                    theIndex = MAX_INDEX;
                }
                //log the substring
                sb.append(":").append(theSubstring);
                Log.d("***", sb.toString());

                // Recursively log the remainder.
                write(fullMessage.substring(theIndex));

            } else {
                sb.append(":").append(String.valueOf(message));
                Log.d("***", sb.toString());
            }


        }

    }

    public static void write(final String tag, final Object message) {
        if (isDebuggable) {
            StringBuilder sb = new StringBuilder();
            final StackTraceElement stackTrace = new Exception().getStackTrace()[1];
            String fileName = stackTrace.getFileName();
            if (fileName == null) {
                fileName = "";  // It is necessary if you want to use proguard obfuscation.
            }
            final String info = stackTrace.getMethodName() + " (" + fileName + ":" + stackTrace.getLineNumber() + ")";
            sb.append(stackTrace.getMethodName()).append("(").append(fileName).append(":").append(stackTrace.getLineNumber()).append(")")
                    .append(" *** ").append(String.valueOf(message));

            Log.d("_" + tag, sb.toString());
        }

    }
}
