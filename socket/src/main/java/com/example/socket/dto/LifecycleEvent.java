package com.example.socket.dto;

import com.example.dlog.DLog;

import java.util.TreeMap;

public class LifecycleEvent {

    public enum Type {
        OPENED, CLOSED, ERROR, FAILED_SERVER_HEARTBEAT,UNAUTHORIZED
    }

    private final Type mType;

    //Nullable
    private Exception mException;

    //Nullable
    private String mMessage;

    private TreeMap<String, String> handshakeResponseHeaders = new TreeMap<>();

    public LifecycleEvent(Type type) {
        DLog.write();
        mType = type;
    }

    public LifecycleEvent(Type type, Exception exception) {
        DLog.write();
        mType = type;
        mException = exception;
    }

    public LifecycleEvent(Type type, String message) {
        DLog.write();
        mType = type;
        mMessage = message;
    }

    public Type getType() {
        return mType;
    }

    public Exception getException() {
        return mException;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setHandshakeResponseHeaders(TreeMap<String, String> handshakeResponseHeaders) {
        this.handshakeResponseHeaders = handshakeResponseHeaders;
    }

    public TreeMap<String, String> getHandshakeResponseHeaders() {
        return handshakeResponseHeaders;
    }
}
