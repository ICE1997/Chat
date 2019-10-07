package com.chzu.ice.chat.activity.chat;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.config.MQTTConfig;

class ChatModel {
    private static final String TAG = ChatModel.class.getSimpleName();

    void publish(String msg, String topic, PublishCallback callback) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        Intent intent = new Intent(MQTTConfig.SIGNAL_SEND_MESSAGE);
        intent.putExtra(MQTTConfig.EXTRA_SEND_MESSAGE_MESSAGE, msg);
        intent.putExtra(MQTTConfig.EXTRA_SEND_MESSAGE_TOPIC, topic);
        localBroadcastManager.sendBroadcast(intent);
        callback.publishSucceed();
    }

    interface PublishCallback {
        void publishSucceed();

        void publishFailed();
    }
}
