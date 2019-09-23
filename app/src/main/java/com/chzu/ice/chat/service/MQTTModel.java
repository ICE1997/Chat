package com.chzu.ice.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.db.Message;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;

import io.objectbox.Box;

public class MQTTModel implements IMQTTModel {
    private final String TAG = MQTTModel.class.getSimpleName();
    private final String broker = "ws://47.106.132.194:8083";
    private final String clientId = "device1";
    private MqttAsyncClient mClient;
    private MqttConnectOptions opt;
    private String topic = "test1";
    private int qos = 2;
    private IMQTTPresenter imqttPresenter;
    private MqttCallback callback;
    private IMqttActionListener connectListener;
    private IMqttActionListener subscribeListener;
    private Box<Message> messageBox;

    MQTTModel(IMQTTPresenter imqttPresenter) throws MqttException {
        this.imqttPresenter = imqttPresenter;

        messageBox = ObjectBoxHelper.get().boxFor(Message.class);

        mClient = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
        opt = new MqttConnectOptions();
        opt.setCleanSession(false);
        opt.setAutomaticReconnect(true);
        callback = new MMqttCallback();
        connectListener = new ConnectListener();
        subscribeListener = new SubscribeListener();

        registerBroadcastReceiver();
//        opt.setKeepAliveInterval(20);
    }

    @Override
    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mClient.isConnected()) {
                        Log.d(TAG, "run: 已经连接");
                    } else {
                        Log.d(TAG, "run: 正在连接...");
                        mClient.connect(opt, connectListener);
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void disconnect() throws MqttException {
        mClient.close();
    }

    @Override
    public void subscribe() throws MqttException {
        mClient.subscribe(topic, qos, this, subscribeListener);
    }

    @Override
    public void unsubscribe() throws MqttException {
        mClient.unsubscribe(topic);
    }

    private void publish(final String topic, String msg) {
        if (mClient.isConnected()) {
            try {
                mClient.publish(topic, msg.getBytes(), 2, false, this, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG, "onSuccess: 发布成功 -> " + topic);
//                                    mainPresenter.publishSucceed();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(TAG, "onFailure: 发布失败");
//                                    mainPresenter.publishFailed();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(String topic, String msg) {
        Intent intent = new Intent("newMessage");
        intent.putExtra("topic", topic);
        intent.putExtra("msg", msg);
        LocalBroadcastManager.getInstance(App.getApplication()).sendBroadcast(intent);
        Log.d(TAG, "broadcastMessage: 已发送广播");
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendMessage");
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String t = intent.getStringExtra("topic");
                String m = intent.getStringExtra("msg");
                publish(t, m);
            }
        }, intentFilter);
    }

    private class ConnectListener implements IMqttActionListener {
        private Timer timer = new Timer();
        private TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "onFailure: 连接失败，5s后重试");
                connect();
                if (mClient.isConnected()) {
                    Log.d(TAG, "run: 重新连接成功");
                    this.cancel();
                }
            }
        };

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(TAG, "run: 连接成功");
            imqttPresenter.connectSucceed();
            try {
                subscribe();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            imqttPresenter.connectFailed();
            timer.schedule(task, 0, 5000);
        }
    }

    private class SubscribeListener implements IMqttActionListener {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(TAG, "onSuccess: 订阅成功");
            imqttPresenter.subscribeSucceed();
            mClient.setCallback(callback);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(TAG, "onSuccess: 订阅失败");
            imqttPresenter.subscribeFailed();
        }
    }

    private class MMqttCallback implements MqttCallback {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "connectionLost: 连接丢失,重新连线");
                if (mClient.isConnected()) {
                    Log.d(TAG, "run: 已重连接.");
                    try {
                        subscribe();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    this.cancel();
                } else {
                    connect();
                }
            }
        };

        @Override
        public void connectionLost(Throwable cause) {
            timer.schedule(task, 0, 1000);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.d(TAG, "messageArrived: topic: from " + topic);
            Log.d(TAG, "messageArrived: msg;" + message.toString());
            broadcastMessage(topic, message.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }

}
