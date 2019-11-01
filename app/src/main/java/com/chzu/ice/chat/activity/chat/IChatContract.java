package com.chzu.ice.chat.activity.chat;

import com.chzu.ice.chat.activity.BasePresenter;
import com.chzu.ice.chat.activity.BaseView;
import com.chzu.ice.chat.pojo.mqtt.MTQQMessage;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IChatContract {
    interface View extends BaseView<Presenter> {
        void showPublishSucceed();
        void showPublishFailed();
    }

    interface Presenter extends BasePresenter {
        void publish(MTQQMessage message) throws MqttException;
    }
}
