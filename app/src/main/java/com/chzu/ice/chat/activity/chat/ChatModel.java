package com.chzu.ice.chat.activity.chat;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.config.MQTTConfig;
import com.chzu.ice.chat.pojo.mqtt.MTQQMessage;
import com.google.gson.Gson;

class ChatModel {
    private static final String TAG = ChatModel.class.getSimpleName();

    void publish(MTQQMessage message, PublishCallback callback) {
        Gson gson = new Gson();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        Intent intent = new Intent(MQTTConfig.SIGNAL_SEND_MESSAGE);
        intent.putExtra(MQTTConfig.EXTRA_SEND_MESSAGE_MESSAGE, gson.toJson(message));
        intent.putExtra(MQTTConfig.EXTRA_SEND_MESSAGE_TOPIC, message.getReceiverTopic());
        localBroadcastManager.sendBroadcast(intent);
        callback.publishSucceed();
    }

    interface PublishCallback {
        void publishSucceed();

        void publishFailed();
    }
}
