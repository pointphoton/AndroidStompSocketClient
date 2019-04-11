package com.example.androidstompsocketclient19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import com.example.androidstompsocketclient19.databinding.ActivityMainBinding;
import com.example.dlog.DLog;
import com.example.service.model.SendMessageVm;
import com.example.service.util.Constants;
import com.example.service.util.MixUtil;
import com.example.socket.Stomp;
import com.example.socket.StompClient;
import com.example.socket.dto.StompHeader;

import java.util.Date;

import static com.example.service.util.Constants.DESTINATION;
import static com.example.service.util.Constants.ENDPOINT;
import static com.example.service.util.Constants.HOST_LOCAL;
import static com.example.service.util.Constants.NAME_TESTUSER1;
import static com.example.service.util.Constants.NAME_TESTUSER2;
import static com.example.service.util.Constants.SERVER_PORT;


public class MainActivity extends AppCompatActivity implements ClickListener {

    private StompClient mStompClient;
    private ActivityMainBinding mBinding;
    private CompositeDisposable compositeDisposable;
    private static String mUri = HOST_LOCAL + ":" + SERVER_PORT + ENDPOINT + "deneme";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);
        mBinding.setHandler(this);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, mUri);
        resetSubscriptions();
        Disposable dispLifecycle = mStompClient.lifecycle().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            DLog.write("Stomp connection opened");
                            mBinding.txtConnectionStatus.setText("Connected");
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

        Disposable dispTopic = mStompClient.topic(Constants.SUBSCRIBE)
                .subscribeOn(Schedulers.io())
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
        mStompClient.connect();
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
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
        SendMessageVm messageVm = new SendMessageVm(MixUtil.getTimeFormat().format(new Date()) + " FROM " + NAME_TESTUSER1, NAME_TESTUSER2);
        String jsonModel = MixUtil.getGson().toJson(messageVm, SendMessageVm.class);
        compositeDisposable.add(mStompClient.send(DESTINATION + NAME_TESTUSER2, jsonModel)
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
