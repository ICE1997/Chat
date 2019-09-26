package com.chzu.ice.chat.activity.chat;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;

class ChatModel {
    private static final String TAG = ChatModel.class.getSimpleName();
    private String topic = "test1";

    void publish(final String s, PublishCallback callback) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        Intent intent = new Intent("sendMessage");
        intent.putExtra("msg", s);
        intent.putExtra("topic", this.topic);
        localBroadcastManager.sendBroadcast(intent);
        callback.publishSucceed();
    }

    interface PublishCallback {
        void publishSucceed();

        void publishFailed();
    }
}
