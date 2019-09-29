package com.chzu.ice.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaSession;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.chzu.ice.chat.pojo.objectBox.Message;
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
import java.util.UUID;

import io.objectbox.Box;

public class MQTTModel implements IMQTTModel {
    private final String TAG = MQTTModel.class.getSimpleName();
    private final String broker = "ws://47.106.132.194:8083";
    private final String clientId = UUID.randomUUID().toString().replaceAll("-","");
    private MqttAsyncClient mClient;
    private MqttConnectOptions opt;
    private int qos = 2;
    private IMQTTPresenter imqttPresenter;
    private MqttCallback callback;
    private IMqttActionListener connectListener;
    private IMqttActionListener subscribeListener;
    private Box<Message> messageBox;
    private Box<FriendRelation> friendRelationBox;
    private String usrname;

    MQTTModel(IMQTTPresenter imqttPresenter) throws MqttException {
        this.imqttPresenter = imqttPresenter;
        messageBox = ObjectBoxHelper.get().boxFor(Message.class);
        friendRelationBox = ObjectBoxHelper.get().boxFor(FriendRelation.class);
        mClient = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
        opt = new MqttConnectOptions();
        opt.setCleanSession(false);
        opt.setAutomaticReconnect(true);
        callback = new MMQTTCallback();
        connectListener = new ConnectListener();
        subscribeListener = new SubscribeListener();
        registerSendMSGBroadcastReceiver();
        registerSubscribeBroadcastReceiver();
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

        String topic = ((App) App.getApplication()).getMyTopic();

        Log.d(TAG, "subscribe: " + topic);

        if (topic != null && !"".equals(topic)) {
            mClient.subscribe(topic, qos, this, subscribeListener);
            Log.d(TAG, "subscribe: 订阅成功:" + topic);
        }

    }

    @Override
    public void unsubscribe() throws MqttException {
//        mClient.unsubscribe(topic);
    }

    @Override
    public void publish(final String topic, String msg) {
        if (mClient.isConnected()) {
            try {
                mClient.publish(topic, msg.getBytes(), qos, false, this, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG, "onSuccess: 发布成功 -> " + topic);
                        imqttPresenter.publishSucceed();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(TAG, "onFailure: 发布失败");
                        imqttPresenter.publishFailed();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    //接收信息
    private void broadcastMessage(String topic, String msg) {
        Intent intent = new Intent("newMessage");
        intent.putExtra("topic", topic);
        intent.putExtra("msg", msg);
        LocalBroadcastManager.getInstance(App.getApplication()).sendBroadcast(intent);
    }

    private void registerSubscribeBroadcastReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SubscribeSignal");
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MQTTModel.this.usrname = intent.getStringExtra("username");
                try {
                    subscribe();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    //发送信息
    private void registerSendMSGBroadcastReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendMessage");
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String t = intent.getStringExtra("topic");
                String m = intent.getStringExtra("msg");
                MQTTModel.this.publish(t, m);
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
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            imqttPresenter.connectFailed();
            timer.schedule(task, 0, 5000);
        }
    }

    //订阅主题
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

    //MQTT回调
    private class MMQTTCallback implements MqttCallback {
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
