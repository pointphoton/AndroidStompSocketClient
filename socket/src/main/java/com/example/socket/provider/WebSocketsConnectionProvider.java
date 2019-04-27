package com.example.socket.provider;

import com.example.dlog.DLog;
import com.example.socket.dto.LifecycleEvent;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WebSocketsConnectionProvider extends AbstractConnectionProvider {


    private final String mUri;
    @NonNull
    private final Map<String, String> mConnectHttpHeaders;

    private WebSocketClient mWebSocketClient;
    private boolean haveConnection;
    private TreeMap<String, String> mServerHandshakeHeaders;

    /**
     * Support UIR scheme ws://host:port/path
     *
     * @param connectHttpHeaders may be null
     */
    public WebSocketsConnectionProvider(String uri, @Nullable Map<String, String> connectHttpHeaders) {
        mUri = uri;
        mConnectHttpHeaders = connectHttpHeaders != null ? connectHttpHeaders : new HashMap<>();
    }

    @Override
    public void rawDisconnect() {
        try {
            mWebSocketClient.closeBlocking();
        } catch (InterruptedException e) {
            DLog.write("Thread interrupted while waiting for Websocket closing: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void createWebSocketConnection(String userToken) {
        if (haveConnection)
            throw new IllegalStateException("Already have connection to web socket");

        mWebSocketClient = new WebSocketClient(URI.create(mUri+(userToken != null ? userToken : "")), new Draft_6455(), mConnectHttpHeaders, 0) {

            @Override
            public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, @NonNull ServerHandshake response) throws InvalidDataException {
                DLog.write("onWebsocketHandshakeReceivedAsClient with response: " + response.getHttpStatus() + " " + response.getHttpStatusMessage());
                mServerHandshakeHeaders = new TreeMap<>();
                Iterator<String> keys = response.iterateHttpFields();
                while (keys.hasNext()) {
                    String key = keys.next();
                    mServerHandshakeHeaders.put(key, response.getFieldValue(key));
                }
            }

            @Override
            public void onOpen(@NonNull ServerHandshake handshakeData) {
                DLog.write("onOpen with handshakeData: " + handshakeData.getHttpStatus() + " " + handshakeData.getHttpStatusMessage());
                LifecycleEvent openEvent = new LifecycleEvent(LifecycleEvent.Type.OPENED);
                openEvent.setHandshakeResponseHeaders(mServerHandshakeHeaders);
                emitLifecycleEvent(openEvent);
            }

            @Override
            public void onMessage(String message) {
                DLog.write("onMessage: " + message);
                emitMessage(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                DLog.write("onClose: code=" + code + " reason=" + reason + " remote=" + remote);
                haveConnection = false;
                emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.CLOSED));

                DLog.write("Disconnect after close.");
                disconnect();
            }

            @Override
            public void onError(Exception ex) {
                DLog.write("onError", ex);
                emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.ERROR, ex));
            }
        };

        if (mUri.startsWith("wss")) {
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, null, null);
                SSLSocketFactory factory = sc.getSocketFactory();
                mWebSocketClient.setSocket(factory.createSocket());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mWebSocketClient.connect();
        haveConnection = true;
    }

    @Override
    protected void rawSend(String stompMessage) {
        mWebSocketClient.send(stompMessage);
    }

    @Override
    protected Object getSocket() {
        return mWebSocketClient;
    }
}