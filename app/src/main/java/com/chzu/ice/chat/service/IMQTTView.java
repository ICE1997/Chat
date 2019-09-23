package com.chzu.ice.chat.service;

public interface IMQTTView {
    void showConnectFailed();
    void showConnectSucceed();
    void showSubscribeSucceed();
    void showSubscribeFailed();
}
