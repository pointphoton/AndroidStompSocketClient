package com.example.log;

public class SysLog {

    /**
     * Writes debugging log.
     */

    public static void write() {

        final StackTraceElement stackTrace = new Exception().getStackTrace()[1];
        String fileName = stackTrace.getFileName();
        if (fileName == null)
            fileName = "";  // It is necessary if you want to use proguard obfuscation.
        final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                + stackTrace.getLineNumber() + ")";

        System.out.println("***"+ info);


    }


    public static void write(final Object message) {

        final StackTraceElement stackTrace = new Exception().getStackTrace()[1];
        String fileName = stackTrace.getFileName();
        if (fileName == null)
            fileName = "";  // It is necessary if you want to use proguard obfuscation.
        final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                + stackTrace.getLineNumber() + ")";

        System.out.println("***"+ info + " : " + String.valueOf(message));


    }

    public static void write(final String tag, final Object message) {

        final StackTraceElement stackTrace = new Exception().getStackTrace()[1];
        String fileName = stackTrace.getFileName();
        if (fileName == null)
            fileName = "";  // It is necessary if you want to use proguard obfuscation.
        final String info = stackTrace.getMethodName() + " (" + fileName + ":"
                + stackTrace.getLineNumber() + ")";
        final String searcherMark = " *** ";
        System.out.println("_" + tag+ info + searcherMark + " : " + String.valueOf(message));


    }
}
