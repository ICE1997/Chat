package com.chzu.ice.chat.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IMQTTPresenter {
    void connect() throws MqttException;
    void disconnect() throws MqttException;
    void subscribe() throws MqttException;
    void unsubscribe() throws MqttException;
    void subscribeSucceed();
    void subscribeFailed();
    void connectSucceed();
    void connectFailed();
}
