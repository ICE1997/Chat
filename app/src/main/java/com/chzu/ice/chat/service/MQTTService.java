package com.chzu.ice.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.chzu.ice.chat.utils.ToastHelper;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTService extends Service implements IMQTTView {
    private static final String TAG = MQTTService.class.getSimpleName();
    private MQTTPresenter mqttPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            this.mqttPresenter = new MQTTPresenter(this);
            mqttPresenter.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mqttPresenter.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void showConnectFailed() {
        ToastHelper.showToast(getApplicationContext(), "连接失败!", Toast.LENGTH_SHORT);
    }


    @Override
    public void showConnectSucceed() {
        ToastHelper.showToast(getApplicationContext(), "连接成功!", Toast.LENGTH_SHORT);
    }

    @Override
    public void showSubscribeSucceed() {
        ToastHelper.showToast(getApplicationContext(), "订阅成功!", Toast.LENGTH_SHORT);
    }

    @Override
    public void showSubscribeFailed() {
        ToastHelper.showToast(getApplicationContext(), "订阅失败!", Toast.LENGTH_SHORT);
    }

    @Override
    public void showPublishSucceed() {
        ToastHelper.showToast(getApplicationContext(), "发送成功!", Toast.LENGTH_SHORT);
    }

    @Override
    public void showPublishFailed() {
        ToastHelper.showToast(getApplicationContext(), "发送失败!", Toast.LENGTH_SHORT);
    }
}
