package com.chzu.ice.chat.pojo.mqtt;

public class MTQQMessage {
    public static final String TYPE_GROUP = "group";
    public static final String TYPE_PERSON = "person";
    private String sender;
    private String senderTopic;
    private String receiver;
    private String receiverTopic;
    private String type;
    private String msg;

    public MTQQMessage(String sender, String receiver, String type, String msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.msg = msg;
    }

    public String getSenderTopic() {
        return senderTopic;
    }

    public void setSenderTopic(String senderTopic) {
        this.senderTopic = senderTopic;
    }

    public String getReceiverTopic() {
        return receiverTopic;
    }

    public void setReceiverTopic(String receiverTopic) {
        this.receiverTopic = receiverTopic;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
