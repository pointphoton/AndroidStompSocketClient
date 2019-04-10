package com.example.androidstompsocketclient19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.View;

import com.example.androidstompsocketclient19.databinding.ActivityMainBinding;
import com.example.dlog.DLog;
import com.example.service.util.Constants;
import com.example.socket.Stomp;
import com.example.socket.StompClient;


public class MainActivity extends AppCompatActivity {

    private StompClient mStompClient;
    private ActivityMainBinding mBinding;
    private static String mUri = Constants.HOST_LOCAL + ":" + Constants.SERVER_PORT + Constants.ENDPOINT + Constants.TOKEN_USER1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);
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


}
