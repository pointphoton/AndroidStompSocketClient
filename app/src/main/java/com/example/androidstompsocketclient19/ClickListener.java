package com.example.androidstompsocketclient19;

import android.view.View;

public interface ClickListener {

    void onConnect(View view);

    void onDisconnect(View view);

    void onSendMessage(View view);
}
