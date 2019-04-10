package com.example.localspringclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;

import com.example.dlog.DLog;
import com.example.localspringclient.databinding.ActivityLocalSpringBinding;
import com.example.service.model.SendMessageVm;
import com.example.service.util.Constants;
import com.example.service.util.MixUtil;
import com.example.socket.Stomp;
import com.example.socket.StompClient;
import com.example.socket.dto.StompHeader;
import com.google.gson.Gson;

import java.util.Date;

public class LocalSpringActivity extends AppCompatActivity {
    private StompClient mStompClient;
    private ActivityLocalSpringBinding mBinding;
    private static String mUri = "ws://10.0.2.2:8080/websocket/websocket";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_local_spring);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, mUri);
        mStompClient.lifecycle().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            DLog.write("Stomp connection opened");
                            mBinding.txtConnectionStatus.setText("Connected");
                            break;
                        case ERROR:
                            DLog.write("Stomp connection error", lifecycleEvent.getMessage());
                            mBinding.txtConnectionStatus.setText("Error");
                            break;
                        case CLOSED:
                            DLog.write("Stomp connection closed");
                            mBinding.txtConnectionStatus.setText("Closed");
                    }
                });

        mStompClient.topic("/topic/test")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    DLog.write("StompCommand= " + topicMessage.getStompCommand());
                    DLog.write("Received= " + topicMessage.getPayload());
                    for (StompHeader sh : topicMessage.getStompHeaders()) {
                        DLog.write("HEADERS= ", sh.getKey() + " - " + sh.getValue());
                    }
                });
    }


    public void onConnect(View view) {
        DLog.write();
        mStompClient.connect();
    }

    public void onDisconnect(View view) {
        DLog.write();
        mStompClient.disconnect();
        mBinding.txtConnectionStatus.setText("Disconnect");
    }

    public void onSendEchoViaStomp(View view) {
        SendMessageVm messageVm = new SendMessageVm("FROM USER=" + MixUtil.getTimeFormat().format(new Date()), "admin");
        String jsonModel = MixUtil.getGson().toJson(messageVm, SendMessageVm.class);
        DLog.write("m=" + jsonModel.toString());
        mStompClient.send("/chat.private.admin", jsonModel)
                .compose(applySchedulers())
                .subscribe(() -> {
                    DLog.write("STOMP message send successfully");
                    mBinding.txtMessage.setText("STOMP message send successfully");
                }, throwable -> {
                    DLog.write("Error send STOMP message", throwable.getMessage());
                    mBinding.txtMessage.setText("Error send STOMP message");

                });
    }

    private CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
