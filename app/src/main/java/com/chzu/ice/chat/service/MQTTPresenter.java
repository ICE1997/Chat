package com.chzu.ice.chat.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTPresenter implements IMQTTPresenter {
    private static final String TAG = MQTTPresenter.class.getSimpleName();
    private IMQTTView imqttView;
    private IMQTTModel imqttModel;

    MQTTPresenter(IMQTTView imqttView) throws MqttException {
        this.imqttView = imqttView;
        this.imqttModel = new MQTTModel(this);
    }

    @Override
    public void connect() throws MqttException {
        imqttModel.connect();
    }

    @Override
    public void disconnect() throws MqttException {
        imqttModel.disconnect();
    }

    @Override
    public void subscribe() throws MqttException {
        imqttModel.subscribe();
    }

    @Override
    public void unsubscribe() throws MqttException {
        imqttModel.unsubscribe();
    }

    @Override
    public void publish(String topic, String msg) {
        imqttModel.publish(topic, msg);
    }

    @Override
    public void subscribeSucceed() {
        imqttView.showSubscribeSucceed();
    }

    @Override
    public void subscribeFailed() {
        imqttView.showSubscribeFailed();
    }

    @Override
    public void connectSucceed() {
        imqttView.showConnectSucceed();
    }

    @Override
    public void connectFailed() {
        imqttView.showConnectFailed();
    }

    @Override
    public void publishSucceed() {
        imqttView.showPublishSucceed();
    }

    @Override
    public void publishFailed() {
        imqttView.showPublishFailed();
    }

}
