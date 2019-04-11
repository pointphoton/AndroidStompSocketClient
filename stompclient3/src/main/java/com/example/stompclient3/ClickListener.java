package com.example.stompclient3;

import android.view.View;

public interface ClickListener {

    void onConnect(View view);

    void onDisconnect(View view);

    void onSendMessage(View view);
}
