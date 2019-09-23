package com.chzu.ice.chat.activity.main;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainModel implements IMainModel {
    private static final String TAG = MainModel.class.getSimpleName();
    private static final String broker = "ws://47.106.132.194:8083";
    private static final String clientId = "device2";
    private IMainPresenter mainPresenter;
    private MqttAsyncClient mClient;
    private String topic = "test1";
    private MqttConnectOptions opts;

    public MainModel(IMainPresenter mainPresenter) throws MqttException {
        this.mainPresenter = mainPresenter;
        mClient = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
        opts = new MqttConnectOptions();
        opts.setCleanSession(false);
        opts.setAutomaticReconnect(true);
    }

    @Override
    public void connect() throws MqttException {

    }

    @Override
    public void publish(final String s) throws MqttException {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        Intent intent = new Intent("sendMessage");
        intent.putExtra("msg", s);
        intent.putExtra("topic", this.topic);
        localBroadcastManager.sendBroadcast(intent);

    }
}
