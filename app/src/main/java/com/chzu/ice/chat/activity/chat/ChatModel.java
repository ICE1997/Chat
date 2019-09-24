package com.chzu.ice.chat.activity.chat;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class ChatModel implements IChatModel {
    private static final String TAG = ChatModel.class.getSimpleName();
    private IChatPresenter mainPresenter;
    private MqttAsyncClient mClient;
    private String topic = "test1";
    private MqttConnectOptions opts;

    ChatModel(IChatPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void publish(final String s) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        Intent intent = new Intent("sendMessage");
        intent.putExtra("msg", s);
        intent.putExtra("topic", this.topic);
        localBroadcastManager.sendBroadcast(intent);
    }
}
