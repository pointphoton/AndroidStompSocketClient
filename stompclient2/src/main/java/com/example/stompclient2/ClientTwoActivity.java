package com.example.stompclient2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.View;

import com.example.dlog.DLog;
import com.example.service.model.ReadInfo;
import com.example.service.model.ReceivedMessage;
import com.example.service.model.SendMessageVm;
import com.example.service.util.MixUtil;
import com.example.socket.Stomp;
import com.example.socket.StompClient;
import com.example.socket.dto.StompHeader;
import com.example.stompclient.R;
import com.example.stompclient.databinding.ClientTwoBinding;

import java.util.Date;

import static com.example.service.util.Constants.DESTINATION_CHAT;
import static com.example.service.util.Constants.DESTINATION_READ;
import static com.example.service.util.Constants.ENDPOINT;
import static com.example.service.util.Constants.HOST_LOCAL;
import static com.example.service.util.Constants.NAME_TESTUSER14;
import static com.example.service.util.Constants.NAME_TESTUSER15;
import static com.example.service.util.Constants.SERVER_PORT;
import static com.example.service.util.Constants.TOKEN_USER15;

public class ClientTwoActivity extends AppCompatActivity implements ClickListener {

    private StompClient mStompClient;
    private ClientTwoBinding mBinding;
    private CompositeDisposable compositeDisposable;
    private static String mUri = HOST_LOCAL + ":" + SERVER_PORT + ENDPOINT ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.client_two);
        mBinding.setHandler(this);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, mUri);
        resetSubscriptions();
        Disposable dispLifecycle = mStompClient.lifecycle().observeOn(AndroidSchedulers.mainThread())
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
        compositeDisposable.add(dispLifecycle);

    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }

    public void onConnect(View view) {
        DLog.write();
        Disposable dispTopic = mStompClient.topic("/user/exchange/amq.direct/chat.message")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    DLog.write("StompCommand= " + topicMessage.getStompCommand());
                    DLog.write("Received= " + topicMessage.getPayload());
                    for (StompHeader sh : topicMessage.getStompHeaders()) {
                        DLog.write("HEADERS= ", sh.getKey() + " - " + sh.getValue());
                    }
                    String json = topicMessage.getPayload();
                    ReceivedMessage rm = MixUtil.getGson().fromJson(json, ReceivedMessage.class);
                    if (rm.isSuccess() && rm.getErrorCode() == 0 && rm.getSourceusername()!=null) {
                        //todo should add to which user we are speaking
                        ReadInfo ri = new ReadInfo(rm.getMessageUuid(), null);
                        String jsonModel = MixUtil.getGson().toJson(ri, ReadInfo.class);
                        compositeDisposable.add(mStompClient.send(DESTINATION_READ, jsonModel)
                                .compose(applySchedulers())
                                .subscribe(() -> {
                                    DLog.write("STOMP - READ message send successfully");
                                }, throwable -> {
                                    DLog.write("Error send STOMP - READ message", throwable.getMessage());

                                }));

                    }
                });

        compositeDisposable.add(dispTopic);
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        mStompClient.connect(TOKEN_USER15);


    }

    public void onDisconnect(View view) {
        DLog.write();
        mStompClient.disconnect();
        mBinding.txtConnectionStatus.setText("Disconnect");
    }

    @Override
    public void onSendMessage(View view) {
        DLog.write();
        SendMessageVm messageVm = new SendMessageVm(MixUtil.getTimeFormat().format(new Date()) + " FROM " + NAME_TESTUSER15, NAME_TESTUSER14);
        String jsonModel = MixUtil.getGson().toJson(messageVm, SendMessageVm.class);
        compositeDisposable.add(mStompClient.send(DESTINATION_CHAT + NAME_TESTUSER14, jsonModel)
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
