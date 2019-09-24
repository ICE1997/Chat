package com.chzu.ice.chat.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IMQTTModel {
    void connect() throws MqttException;
    void disconnect() throws MqttException;
    void subscribe() throws MqttException;
    void publish(final String topic, String msg);
    void unsubscribe() throws MqttException;
}
