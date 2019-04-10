package com.example.service.util;

public interface Constants {

    public static final String HOST_EMULATOR = "ws://10.0.2.2";
    public static final String HOST_REMOTE = "ws://176.33.14.111";
    public static final String HOST_LOCAL = "ws://192.168.1.20";
    public static final String SERVER_PORT = "8080";

    public static final String ENDPOINT = "/websocket/websocket/?access_token=";
    public static final String SUBSCRIBE = "/user/exchange/amq.direct/chat.message";
    public static final String DESTINATION = "/chat.private.";
    public static final String SEND_USER1 = "/chat.private.testuser1";
    public static final String SEND_USER2 = "/chat.private.testuser2";

    public static final String NAME_USER1 = "testuser1";
    public static final String NAME_USER2 = "testuser2";


    public static final String TOKEN_USER1 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlcjEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTU3Mjg3ODkzfQ" +
            ".TYuW5zEz-_ckg_f6V15Y06tmWKM5RYnjJlw80JuEm7DBkCvbvHRDxkfx0c0GqNhHHIrfTMEhbCTixHbaF0T_-A";

    public static final String TOKEN_USER2 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlcjIiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTU3Mjg3OTE0fQ" +
            ".DT2F5DTlsM2FMNahhm1yEFmEtkTqtYqVZHpumAcfjLoy9VxAd-TtGdap51NkApaE77JwzG6D59F2s4KF3W-NbQ";

    public static final String TOKEN_USER3 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlcjIiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTU3Mjg3OTE0fQ" +
            ".DT2F5DTlsM2FMNahhm1yEFmEtkTqtYqVZHpumAcfjLoy9VxAd-TtGdap51NkApaE77JwzG6D59F2s4KF3W-NbQ";


    public static final String LOCAL_HOST = "127.0.0.1";
    public static final String LOCAL_ENDPOINT = "/example-endpoint/websocket";
    public static final String LOCAL_SUBSCRIBE_1 = "topic/greetings";
    public static final String LOCAL_SEND_1 = "/echo-msg";
    public static final String LOCAL_SUBSCRIBE_2 = "/app/hello-msg2";
    public static final String LOCAL_SEND_2 = "/app/hello-msg2";

    public static final String NAME_TESTUSER1 = "testuser1";
    public static final String NAME_TESTUSER2 = "testuser2";
    public static final String NAME_TESTUSER3 = "testuser3";
}
