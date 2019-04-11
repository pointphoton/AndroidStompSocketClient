package com.example.service.util;

public interface Constants {

    public static final String HOST_EMULATOR = "ws://10.0.2.2";
    public static final String HOST_REMOTE = "ws://176.33.14.111";
    public static final String HOST_LOCAL = "ws://192.168.1.20";
    public static final String SERVER_PORT = "8080";

    public static final String ENDPOINT = "/websocket/websocket/?access_token=";
    public static final String SUBSCRIBE = "/user/exchange/amq.direct/chat.message";
    public static final String DESTINATION = "/chat.private.";


    public static final String TOKEN_USER1 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlcjEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTU3NTQ4MTg2fQ.CRLEL9SHIqCQdkvBhAF0p-GL88VFgLKkrTLr-ra8BJ9mHbKgmak0iFYY5HofmxZyridjjrCv4aR4lv-1ywWXMQ";

    public static final String TOKEN_USER2 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlcjIiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTU3NTQ4MzU2fQ.h26WcumCUm_v40sONC1Cwg2QQJw98cHrPe5zhOvG01LWLKbdWLU-U4cAjVi1oriJyD5rejzt-YDFrpnkTuiEjA";

    public static final String TOKEN_USER399 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlcjM5OSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE1NTc1NDg0MDd9.Jwly_9ZkkUG3oSZkLV1Y6Oc-7VuQ3U7hB2FBCWYH_tONonA9s20bkuAnGgvAyQx2tJQP9wV6bZZjDHwr3pU3vA";


    public static final String LOCAL_HOST = "127.0.0.1";
    public static final String LOCAL_ENDPOINT = "/example-endpoint/websocket";
    public static final String LOCAL_SUBSCRIBE_1 = "topic/greetings";
    public static final String LOCAL_SEND_1 = "/echo-msg";
    public static final String LOCAL_SUBSCRIBE_2 = "/app/hello-msg2";
    public static final String LOCAL_SEND_2 = "/app/hello-msg2";

    public static final String NAME_TESTUSER1 = "testuser1";
    public static final String NAME_TESTUSER2 = "testuser2";
    public static final String NAME_TESTUSER399 = "testuser399";
}
