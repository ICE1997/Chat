package com.chzu.ice.chat.activity.chat;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;

class ChatModel {
    private static final String TAG = ChatModel.class.getSimpleName();

    void publish(final String s,String topic,PublishCallback callback) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        Intent intent = new Intent("sendMessage");
        intent.putExtra("msg", s);
        intent.putExtra("topic", topic);
        localBroadcastManager.sendBroadcast(intent);
        callback.publishSucceed();
    }

    interface PublishCallback {
        void publishSucceed();

        void publishFailed();
    }
}
