package com.chzu.ice.chat.activity.main;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IMainModel {
    void connect() throws MqttException;
    void publish(String s) throws MqttException;
}
