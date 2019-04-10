package com.example.stompclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.reactivex.disposables.Disposable;

import android.os.Bundle;
import android.view.View;

import com.example.dlog.DLog;
import com.example.socket.StompClient;
import com.example.stompclient.databinding.ClientTwoBinding;

public class ClientTwoActivity extends AppCompatActivity {

    private StompClient mStompClient;
    private Disposable mRestPingDisposable;
    private ClientTwoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.client_two);
    }

    public void onConnect(View view) {
        DLog.write();


    }

    public void onDisconnect(View view) {
        DLog.write();
    }


}
