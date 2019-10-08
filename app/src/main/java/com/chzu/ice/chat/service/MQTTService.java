package com.chzu.ice.chat.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.config.MQTTConfig;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation_;
import com.chzu.ice.chat.pojo.objectBox.Message;
import com.chzu.ice.chat.utils.ObjectBoxHelper;
import com.chzu.ice.chat.utils.ToastHelper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.objectbox.Box;

public class MQTTService extends Service {
    private final String TAG = MQTTService.class.getSimpleName();
    private MqttAsyncClient mClient;
    private MqttConnectOptions opt;
    private IMqttActionListener connectListener;
    private IMqttActionListener subscribeListener;
    private Box<Message> messageBox;
    private Box<FriendRelation> friendRelationBox;


    public MQTTService() throws MqttException {
        messageBox = ObjectBoxHelper.get().boxFor(Message.class);
        friendRelationBox = ObjectBoxHelper.get().boxFor(FriendRelation.class);
        mClient = new MqttAsyncClient(MQTTConfig.MQTT_SERVER, MQTTConfig.CLIENT_ID, new MemoryPersistence());
        opt = new MqttConnectOptions();
        opt.setCleanSession(false);
        opt.setAutomaticReconnect(true);
        mClient.setCallback(new MMQTTCallback());
        connectListener = new ConnectListener();
        subscribeListener = new SubscribeListener();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerConnectSignalReceiver();
        registerMessageSender();
        registerSubscribeTopicSignalReceiver();
    }

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

    public void disconnect() throws MqttException {
        mClient.close();
    }

    public void subscribe(String topic) throws MqttException {
        mClient.subscribe(topic, MQTTConfig.QOS, this, subscribeListener);
    }

    public void unsubscribe() throws MqttException {
        mClient.unsubscribe("");
    }

    public void publish(final String topic, String msg) {
        if (mClient.isConnected()) {
            try {
                mClient.publish(topic, msg.getBytes(), MQTTConfig.QOS, false, this, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(TAG, "onSuccess: 发布成功 -> " + topic);
                        showPublishSucceed();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(TAG, "onFailure: 发布失败");
                        showPublishFailed();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void showConnectFailed() {
        ToastHelper.showToast(getApplicationContext(), "连接失败!", Toast.LENGTH_SHORT);
    }


    public void showConnectSucceed() {
        ToastHelper.showToast(getApplicationContext(), "连接成功!", Toast.LENGTH_SHORT);
    }


    public void showSubscribeSucceed() {
        ToastHelper.showToast(getApplicationContext(), "订阅成功!", Toast.LENGTH_SHORT);
    }


    public void showSubscribeFailed() {
        ToastHelper.showToast(getApplicationContext(), "订阅失败!", Toast.LENGTH_SHORT);
    }


    public void showPublishSucceed() {
        ToastHelper.showToast(getApplicationContext(), "发送成功!", Toast.LENGTH_SHORT);
    }


    public void showPublishFailed() {
        ToastHelper.showToast(getApplicationContext(), "发送失败!", Toast.LENGTH_SHORT);
    }

    //收到连接信号
    private void registerConnectSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_CONNECT);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!mClient.isConnected()) {
                    connect();
                    Log.d(TAG, "onReceive: 连接...");
                } else {
                    Log.d(TAG, "onReceive: 已经连接");
                }
            }
        }, intentFilter);

    }

    //发送连接连接成功信号（广播）
    private void sendConnectSucceedSignal() {
        Intent intent = new Intent(MQTTConfig.SIGNAL_CONNECT_SUCCEED);
        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);
    }

    //收到信息信号
    private void receiveMessage(String topic, String msg) {
        Intent intent = new Intent(MQTTConfig.SIGNAL_RECEIVE_MESSAGE);
        intent.putExtra(MQTTConfig.EXTRA_RECEIVE_MESSAGE_TOPIC, topic);
        intent.putExtra(MQTTConfig.EXTRA_RECEIVE_MESSAGE_MESSAGE, msg);
        Message message = new Message();
        List<FriendRelation> friendRelations = friendRelationBox.query().equal(FriendRelation_.FTopic, topic).build().find();
        if (friendRelations.size() > 0) {
            Log.d(TAG, "receiveMessage: > 0" );
            FriendRelation f = friendRelations.get(0);
            message.setFromU(f.getFName());
            message.setToU(f.getMName());
            message.setMsg(msg);
            messageBox.put(message);
            LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);
        }else {
            Log.e(TAG, "receiveMessage: NoSuchUser");
        }
    }

    //注册订阅主题的信号
    private void registerSubscribeTopicSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_SUBSCRIBE);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String topic = intent.getStringExtra(MQTTConfig.EXTRA_SUBSCRIBE_TOPIC);
                    subscribe(topic);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, intentFilter);
    }

    //发送信息的广播
    private void registerMessageSender() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_SEND_MESSAGE);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String t = intent.getStringExtra(MQTTConfig.EXTRA_SEND_MESSAGE_TOPIC);
                String m = intent.getStringExtra(MQTTConfig.EXTRA_SEND_MESSAGE_MESSAGE);
                MQTTService.this.publish(t, m);
            }
        }, intentFilter);
    }

    //连接回调
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
            sendConnectSucceedSignal();
            showConnectSucceed();
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            showConnectFailed();
            timer.schedule(task, 0, 5000);
        }
    }

    //订阅主题监听器
    private class SubscribeListener implements IMqttActionListener {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(TAG, "onSuccess: 订阅成功");
            showSubscribeSucceed();
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.d(TAG, "onFailure: 订阅失败,"+exception.getLocalizedMessage());
            showSubscribeFailed();
        }
    }

    //MQTT全程回调/监听器
    private class MMQTTCallback implements MqttCallback {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "connectionLost: 连接丢失,重新连线");
                if (mClient.isConnected()) {
                    Log.d(TAG, "run: 已重连接.");
                    sendConnectSucceedSignal();
                    this.cancel();
                } else {
                    connect();
                }
            }
        };

        @Override
        public void connectionLost(Throwable cause) {
            timer.schedule(task, 0, 5000);
        }

        //这个topic是本机的topic
        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.d(TAG, "messageArrived: topic: to " + topic);
            Log.d(TAG, "messageArrived: msg:" + message.toString());
            receiveMessage(topic, message.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(TAG, "deliveryComplete: 消息已发送");
        }
    }

}
