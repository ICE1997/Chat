package com.chzu.ice.chat.activity.main;

import org.eclipse.paho.client.mqttv3.MqttException;

public class ChatPresenter implements IChatPresenter {
    private IChatModel mainModel;
    private IChatView mainView;

    public ChatPresenter(IChatView mainView) throws MqttException {
        this.mainView = mainView;
        this.mainModel = new ChatModel(this);

    }

    @Override
    public void publish(String s) throws MqttException {
        mainModel.publish(s);
    }

    @Override
    public void connect() throws MqttException {
        mainModel.connect();
    }

    @Override
    public void publishSucceed() {
        mainView.showPublishSucceed();
    }

    @Override
    public void publishFailed() {
        mainView.showPublishFailed();
    }

    @Override
    public void connectSucceed() {
        mainView.showConnectSucceed();
    }

    @Override
    public void connectFailed() {
        mainView.showPublishFailed();
    }
}
