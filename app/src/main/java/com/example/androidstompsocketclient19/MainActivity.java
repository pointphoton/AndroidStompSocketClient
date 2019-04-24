package com.example.androidstompsocketclient19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.View;

import com.example.androidstompsocketclient19.databinding.ActivityMainBinding;
import com.example.dlog.DLog;
import com.example.service.model.SendMessageVm;
import com.example.service.util.MixUtil;
import com.example.socket.Stomp;
import com.example.socket.StompClient;
import com.example.socket.dto.StompHeader;

import java.util.Date;

import static com.example.service.util.Constants.DESTINATION_CHAT;
import static com.example.service.util.Constants.ENDPOINT;
import static com.example.service.util.Constants.HOST_LOCAL;
import static com.example.service.util.Constants.NAME_TESTUSER14;
import static com.example.service.util.Constants.NAME_TESTUSER15;
import static com.example.service.util.Constants.SERVER_PORT;
import static com.example.service.util.Constants.TOKEN_USER14;

public class MainActivity extends AppCompatActivity implements ClickListener {

    private StompClient mStompClient;
    private ActivityMainBinding mBinding;
    private CompositeDisposable compositeDisposable;
    private static String mUri = HOST_LOCAL + ":" + SERVER_PORT + ENDPOINT + TOKEN_USER14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);
        mBinding.setHandler(this);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, mUri);
        resetSubscriptions();
        Disposable dispLifecycle = mStompClient.lifecycle()
               // .subscribeOn(Schedulers.io()) //todo check
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(ex->{
                    DLog.write(ex.getMessage());
                })
                .subscribe(lifecycleEvent -> {
                    DLog.write("subscribeOn : " + Thread.currentThread().getName());
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            DLog.write("Stomp connection opened");
                            mBinding.txtConnectionStatus.setText("Connected");
                            /*
                            TreeMap<String, String> treeMap = lifecycleEvent.getHandshakeResponseHeaders();
                            for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                DLog.write(key + " => " + value);
                            }*/
                            break;
                        case UNAUTHORIZED:
                            DLog.write("Stomp an authorization error", String.valueOf(lifecycleEvent.getMessage()));
                            mBinding.txtConnectionStatus.setText("Unauthorized");
                            break;
                        case ERROR:
                            DLog.write("Stomp a connection error", String.valueOf(lifecycleEvent.getMessage()));
                            mBinding.txtConnectionStatus.setText("Error");
                            break;
                        case CLOSED:
                            DLog.write("Stomp connection closed");
                            mBinding.txtConnectionStatus.setText("Closed");
                    }

                });

        compositeDisposable.add(dispLifecycle);
        /*
        DLog.write("Before observable on: ", Thread.currentThread().getName());
        Observable.just(1, 2, 3, 4, 5)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        DLog.write("Emitting item on: " + Thread.currentThread().getName() + " - " + String.valueOf(integer));
                    }
                })
                .subscribeOn(Schedulers.trampoline())
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        integer *= 2;
                        DLog.write("Processing item on: " + Thread.currentThread().getName() + " - " + String.valueOf(integer));
                        return integer;
                    }
                })
                .filter( integer -> {
                    DLog.write("Filter item on: " + Thread.currentThread().getName() + " - " + String.valueOf(integer));
                    return integer % 2==0;
                })
                .map(new Function<Integer, Integer>() {
                @Override
                 public Integer apply(@NonNull Integer l) throws Exception {
                l *= 2;
                DLog.write("Processing item on: " + Thread.currentThread().getName() + " - " + String.valueOf(l));
                return Integer.parseInt(String.valueOf(l));
                }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribeWith(new DisposableObserver<Integer>() {

                    @Override
                    public void onNext(Integer integer) {
                        DLog.write("Consuming item on: " + Thread.currentThread().getName() + " - " + String.valueOf(integer));
                    }

                    @Override
                    public void onComplete() {
                        DLog.write("onComplete on: " + Thread.currentThread().getName());
                    }



                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onConnect(View view) {
        DLog.write();
        Disposable dispTopic = mStompClient.topic("/user/exchange/amq.direct/chat.message")
             //   .subscribeOn(Schedulers.io()) //todo check
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    DLog.write("throwable message= " + throwable.getMessage());
                })
                .subscribe(topicMessage -> {
                    DLog.write("StompCommand= " + topicMessage.getStompCommand());
                    DLog.write("Received= " + topicMessage.getPayload());
                    for (StompHeader sh : topicMessage.getStompHeaders()) {
                        DLog.write("HEADERS= ", sh.getKey() + " - " + sh.getValue());
                    }
                });
        compositeDisposable.add(dispTopic);
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        mStompClient.connect();


    }

    @Override
    public void onDisconnect(View view) {
        DLog.write();
        mStompClient.disconnect();
        mBinding.txtConnectionStatus.setText("Disconnect");
    }

    @Override
    public void onSendMessage(View view) {
        DLog.write();
        SendMessageVm messageVm = new SendMessageVm(MixUtil.getTimeFormat().format(new Date()) + " FROM " + NAME_TESTUSER14, NAME_TESTUSER15);
        String jsonModel = MixUtil.getGson().toJson(messageVm, SendMessageVm.class);
        compositeDisposable.add(mStompClient.send(DESTINATION_CHAT + NAME_TESTUSER15, jsonModel)
                .compose(applySchedulers())
                .subscribe(() -> {
                    DLog.write("STOMP message send successfully");
                    mBinding.txtMessage.setText("STOMP message send successfully");
                }, throwable -> {
                    DLog.write("Error send STOMP message", throwable.getMessage());
                    mBinding.txtMessage.setText("Error send STOMP message");
                }));
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    private CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
