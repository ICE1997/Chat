package com.chzu.ice.chat.activity.main;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MainPresenter implements IMainPresenter {
    private IMainModel mainModel;
    private IMainView mainView;

    public MainPresenter(IMainView mainView) throws MqttException {
        this.mainView = mainView;
        this.mainModel = new MainModel(this);

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
