package com.chzu.ice.chat.activity.chat;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IChatPresenter {
    void publish(String s) throws MqttException;
    void publishSucceed();
    void publishFailed();
}
