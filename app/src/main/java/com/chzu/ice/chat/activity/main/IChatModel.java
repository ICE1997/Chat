package com.chzu.ice.chat.activity.main;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IChatModel {
    void connect() throws MqttException;
    void publish(String s) throws MqttException;
}
