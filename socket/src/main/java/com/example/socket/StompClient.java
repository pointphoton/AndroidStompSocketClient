package com.example.socket;

import android.annotation.SuppressLint;

import com.example.dlog.DLog;
import com.example.socket.dto.LifecycleEvent;
import com.example.socket.dto.StompCommand;
import com.example.socket.dto.StompHeader;
import com.example.socket.dto.StompMessage;
import com.example.socket.pathmatcher.PathMatcher;
import com.example.socket.pathmatcher.SimplePathMatcher;
import com.example.socket.provider.ConnectionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class StompClient {
    public static final String SUPPORTED_VERSIONS = "1.1,1.2";
    public static final String DEFAULT_ACK = "auto";

    private final ConnectionProvider connectionProvider;
    private ConcurrentHashMap<String, String> topics;
    private boolean legacyWhitespace;

    private PublishSubject<StompMessage> messageStream;
    private BehaviorSubject<Boolean> connectionStream;
    private ConcurrentHashMap<String, Flowable<StompMessage>> streamMap;
    private PathMatcher pathMatcher;
    private Disposable lifecycleDisposable;
    private Disposable messagesDisposable;
    private PublishSubject<LifecycleEvent> lifecyclePublishSubject;
    private List<StompHeader> headers;
    private HeartBeatTask heartBeatTask;

    public StompClient(ConnectionProvider connectionProvider) {
        DLog.write();
        this.connectionProvider = connectionProvider;
        streamMap = new ConcurrentHashMap<>();
        lifecyclePublishSubject = PublishSubject.create();
        pathMatcher = new SimplePathMatcher();
        heartBeatTask = new HeartBeatTask(this::sendHeartBeat, () -> {
            lifecyclePublishSubject.onNext(new LifecycleEvent(LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT));
        });

    }

    /**
     * Sets the heartbeat interval to request from the server.
     * <p>
     * Not very useful yet, because we don't have any heartbeat logic on our side.
     *
     * @param ms heartbeat time in milliseconds
     */
    public StompClient withServerHeartbeat(int ms) {
        heartBeatTask.setServerHeartbeat(ms);
        return this;
    }

    /**
     * Sets the heartbeat interval that client propose to send.
     * <p>
     * Not very useful yet, because we don't have any heartbeat logic on our side.
     *
     * @param ms heartbeat time in milliseconds
     */
    public StompClient withClientHeartbeat(int ms) {
        heartBeatTask.setClientHeartbeat(ms);
        return this;
    }

    /**
     * Connect without reconnect if connected
     */
    public void connect() {
        connect(null);
    }

    /**
     * Connect to websocket. If already connected, this will silently fail.
     *
     * @param _headers HTTP headers to send in the INITIAL REQUEST, i.e. during the protocol upgrade
     */
    public void connect(@Nullable List<StompHeader> _headers) {

        DLog.write("Connect");

        this.headers = _headers;

        if (isConnected()) {
            DLog.write("Already connected, ignore");
            return;
        }
        lifecycleDisposable = connectionProvider.lifecycle()
                .subscribe(lifecycleEvent -> {
                    DLog.write("connect subscribeOn : " + Thread.currentThread().getName());
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            DLog.write("Socket Opened");

                            List<StompHeader> headers = new ArrayList<>();
                            headers.add(new StompHeader(StompHeader.VERSION, SUPPORTED_VERSIONS));
                            headers.add(new StompHeader(StompHeader.HEART_BEAT,
                                    heartBeatTask.getClientHeartbeat() + "," + heartBeatTask.getServerHeartbeat()));

                            if (_headers != null) headers.addAll(_headers);

                            connectionProvider.send(new StompMessage(StompCommand.CONNECT, headers, null).compile(legacyWhitespace))
                                    .subscribe(() -> {
                                        DLog.write("Publish open");
                                        lifecyclePublishSubject.onNext(lifecycleEvent);
                                    });
                            break;
                        case CLOSED:
                            DLog.write("Socket closed");
                            disconnect();
                            break;

                        case ERROR:
                            DLog.write("Socket closed with error");
                            lifecyclePublishSubject.onNext(lifecycleEvent);
                            break;
                    }
                });

        messagesDisposable = connectionProvider.messages()
                .subscribeOn(Schedulers.io())
                .map(StompMessage::from)
                .filter(heartBeatTask::consumeHeartBeat)
                .doOnNext(getMessageStream()::onNext)
                .filter(msg -> msg.getStompCommand().equals(StompCommand.CONNECTED))
                .subscribe(stompMessage -> {
                    getConnectionStream().onNext(true);
                });

    }

    synchronized private BehaviorSubject<Boolean> getConnectionStream() {
        if (connectionStream == null || connectionStream.hasComplete()) {
            connectionStream = BehaviorSubject.createDefault(false);
        }
        return connectionStream;
    }

    synchronized private PublishSubject<StompMessage> getMessageStream() {
        if (messageStream == null || messageStream.hasComplete()) {
            messageStream = PublishSubject.create();
        }
        return messageStream;
    }

    public Completable send(String destination) {
        return send(destination, null);
    }

    public Completable send(String destination, String data) {
        return send(new StompMessage(
                StompCommand.SEND,
                Collections.singletonList(new StompHeader(StompHeader.DESTINATION, destination)),
                data));
    }

    public Completable send(@NonNull StompMessage stompMessage) {
        Completable completable = connectionProvider.send(stompMessage.compile(legacyWhitespace));
        CompletableSource connectionComplete = getConnectionStream()
                .filter(isConnected -> isConnected)
                .firstElement().ignoreElement();
        return completable
                .startWith(connectionComplete);
    }

    @SuppressLint("CheckResult")
    private void sendHeartBeat(@NonNull String pingMessage) {
        Completable completable = connectionProvider.send(pingMessage);
        CompletableSource connectionComplete = getConnectionStream()
                .filter(isConnected -> isConnected)
                .firstElement().ignoreElement();
        completable.startWith(connectionComplete)
                .onErrorComplete()
                .subscribe();
    }

    public Flowable<LifecycleEvent> lifecycle() {
        return lifecyclePublishSubject.toFlowable(BackpressureStrategy.BUFFER);
    }

    /**
     * Disconnect from server, and then reconnect with the last-used headers
     */
    @SuppressLint("CheckResult")
    public void reconnect() {
        disconnectCompletable()
                .subscribe(() -> connect(headers),
                        e -> DLog.write("Disconnect error", e));
    }

    @SuppressLint("CheckResult")
    public void disconnect() {
        disconnectCompletable().subscribe(() -> {
        }, e -> DLog.write("Disconnect error", e));
    }

    private Completable disconnectCompletable() {
        heartBeatTask.shutdown();
        if (lifecycleDisposable != null) {
            lifecycleDisposable.dispose();
        }
        if (messagesDisposable != null) {
            messagesDisposable.dispose();
        }

        return connectionProvider.disconnect()
                .doFinally(() -> {
                    DLog.write("Stomp disconnected");
                    getConnectionStream().onComplete();
                    getMessageStream().onComplete();
                    lifecyclePublishSubject.onNext(new LifecycleEvent(LifecycleEvent.Type.CLOSED));
                });
    }

    public Flowable<StompMessage> topic(String destinationPath) {
        DLog.write();
        return topic(destinationPath, null);
    }

    public Flowable<StompMessage> topic(@NonNull String destPath, List<StompHeader> headerList) {
        DLog.write();
        if (destPath == null)
            return Flowable.error(new IllegalArgumentException("Topic path cannot be null"));
        else if (!streamMap.containsKey(destPath))
            streamMap.put(destPath,
                    subscribePath(destPath, headerList).andThen(
                            getMessageStream()
                                    .filter(msg -> pathMatcher.matches(destPath, msg))
                                    .toFlowable(BackpressureStrategy.BUFFER)
                                    .share()).doFinally(() -> unsubscribePath(destPath).subscribe())
            );
        return streamMap.get(destPath);
    }

    private Completable subscribePath(String destinationPath, @Nullable List<StompHeader> headerList) {
        String topicId = UUID.randomUUID().toString();

        if (topics == null) topics = new ConcurrentHashMap<>();

        // Only continue if we don't already have a subscription to the topic
        if (topics.containsKey(destinationPath)) {
            DLog.write("Attempted to subscribe to already-subscribed path!");
            return Completable.complete();
        }

        topics.put(destinationPath, topicId);
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(StompHeader.ID, topicId));
        headers.add(new StompHeader(StompHeader.DESTINATION, destinationPath));
        headers.add(new StompHeader(StompHeader.ACK, DEFAULT_ACK));
        if (headerList != null) headers.addAll(headerList);
        return send(new StompMessage(StompCommand.SUBSCRIBE,
                headers, null));
    }


    private Completable unsubscribePath(String dest) {
        streamMap.remove(dest);

        String topicId = topics.get(dest);
        topics.remove(dest);

        DLog.write("Unsubscribe path: " + dest + " id: " + topicId);

        return send(new StompMessage(StompCommand.UNSUBSCRIBE,
                Collections.singletonList(new StompHeader(StompHeader.ID, topicId)), null)).onErrorComplete();
    }

    /**
     * Set the wildcard or other matcher for Topic subscription.
     * <p>
     * Right now, the only options are simple, rmq supported.
     * But you can write you own matcher by implementing {@link PathMatcher}
     * <p>
     * When set to {@link com.example.socket.pathmatcher.RabbitPathMatcher, topic subscription allows for RMQ-style wildcards.
     * <p>
     *
     * @param pathMatcher Set to {@link SimplePathMatcher} by default
     */
    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public boolean isConnected() {
        return getConnectionStream().getValue();
    }

    /**
     * Reverts to the old frame formatting, which included two newlines between the message body
     * and the end-of-frame marker.
     * <p>
     * Legacy: Body\n\n^@
     * <p>
     * Default: Body^@
     *
     * @param legacyWhitespace whether to append an extra two newlines
     * @see <a href="http://stomp.github.io/stomp-specification-1.2.html#STOMP_Frames">The STOMP spec</a>
     */
    public void setLegacyWhitespace(boolean legacyWhitespace) {
        this.legacyWhitespace = legacyWhitespace;
    }
}

