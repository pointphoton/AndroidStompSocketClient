package com.example.socket.provider;

import android.os.Looper;

import com.example.dlog.DLog;
import com.example.socket.dto.LifecycleEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.subjects.PublishSubject;

public abstract class AbstractConnectionProvider implements ConnectionProvider {

    @NonNull
    private final PublishSubject<LifecycleEvent> lifecycleStream;
    @NonNull
    private final PublishSubject<String> messagesStream;

    public AbstractConnectionProvider() {
        DLog.write();
        lifecycleStream = PublishSubject.create();
        messagesStream = PublishSubject.create();
    }

    @NonNull
    @Override
    public Observable<String> connect(String userToken) {
        DLog.write("+ MDENE DENE DENE ");
        return messagesStream.startWith(initSocket(userToken).toObservable());
    }

    /**
     * Simply close socket.
     * <p>
     * For example:
     * <pre>
     * webSocket.close();
     * </pre>
     */
    protected abstract void rawDisconnect();

    @Override
    public Completable disconnect() {
        return Completable
                .fromAction(this::rawDisconnect);
    }

    private Completable initSocket(String userToken) {
        DLog.write();
        //return Completable.fromAction(this::createWebSocketConnection);
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                DLog.write("Processing initSocket on: " + Thread.currentThread().getName());
                createWebSocketConnection(userToken);
            }
        });
    }

    /**
     * Most important method: connects to websocket and notifies program of messages.
     * <p>
     * See implementations in OkHttpConnectionProvider and WebSocketsConnectionProvider.
     */
    protected abstract void createWebSocketConnection(String userToken);

    // protected abstract void hello();

    @NonNull
    @Override
    public Completable send(String stompMessage) {
        return Completable.fromCallable(() -> {
            if (getSocket() == null) {
                return Completable.error(new IllegalStateException("Not connected"));
                //  throw new IllegalStateException("Not connected");
            } else {
//                DLog.write("Send STOMP message: " + stompMessage);
                rawSend(stompMessage);
                return null;
            }
        });
    }

    /**
     * Just a simple message send.
     * <p>
     * For example:
     * <pre>
     * webSocket.send(stompMessage);
     * </pre>
     *
     * @param stompMessage message to send
     */
    protected abstract void rawSend(String stompMessage);

    /**
     * Get socket object.
     * Used for null checking; this object is expected to be null when the connection is not yet established.
     * <p>
     * For example:
     * <pre>
     * return webSocket;
     * </pre>
     */
    @Nullable
    protected abstract Object getSocket();

    protected void emitLifecycleEvent(@NonNull LifecycleEvent lifecycleEvent) {
        DLog.write("Emit lifecycle event: " + lifecycleEvent.getType().name());
        lifecycleStream.onNext(lifecycleEvent);
    }

    protected void emitMessage(String stompMessage) {
        DLog.write("Receive STOMP message: " + stompMessage);
        messagesStream.onNext(stompMessage);
    }

    @NonNull
    @Override
    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleStream;
    }
}
