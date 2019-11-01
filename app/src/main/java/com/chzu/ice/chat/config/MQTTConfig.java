package com.chzu.ice.chat.config;

import java.util.UUID;

public class MQTTConfig {
    public static final String MQTT_SERVER="ws://47.106.132.194:8083";
    public static final String CLIENT_ID=UUID.randomUUID().toString().replaceAll("-","");
    public static final int QOS=2;
    public static final String SIGNAL_RECEIVE_MESSAGE ="RECEIVED_MESSAGE";
    public static final String SIGNAL_SEND_MESSAGE="SEND_MESSAGE";
    public static final String SIGNAL_SUBSCRIBE="SUBSCRIBE";
    public static final String SIGNAL_CONNECT="CONNECT";
    public static final String SIGNAL_CONNECT_SUCCEED="CONNECT_SUCCEED";
    public static final String SIGNAL_CONNECT_FAILED="CONNECT_FAILED";

    public static final String SIGNAL_UNSUBSCRIBE="UNSUBSCRIBE";
    public static final String SIGNAL_DISCONNECT="DISCONNECT";

    public static final String EXTRA_SUBSCRIBE_TOPIC ="SUBSCRIBE_TOPIC";
    public static final String EXTRA_SEND_MESSAGE_TOPIC ="SEND_MESSAGE_TOPIC";
    public static final String EXTRA_SEND_MESSAGE_MESSAGE ="SEND_MESSAGE_MESSAGE";

    public static final String EXTRA_RECEIVE_MESSAGE_TOPIC ="RECEIVE_MESSAGE_TOPIC";
    public static final String EXTRA_RECEIVE_MESSAGE_MESSAGE ="RECEIVE_MESSAGE_MESSAGE";

}
