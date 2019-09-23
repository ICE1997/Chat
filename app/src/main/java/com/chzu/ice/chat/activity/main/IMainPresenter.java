package com.chzu.ice.chat.activity.main;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IMainPresenter {
    void publish(String s) throws MqttException;
    void connect() throws MqttException;
    void publishSucceed();
    void publishFailed();
    void connectSucceed();
    void connectFailed();
}
