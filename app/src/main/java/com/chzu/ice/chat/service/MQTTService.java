package com.chzu.ice.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

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
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "连接失败!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void showConnectSucceed() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "连接成功!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void showSubscribeSucceed() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "订阅成功!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showSubscribeFailed() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "订阅失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
