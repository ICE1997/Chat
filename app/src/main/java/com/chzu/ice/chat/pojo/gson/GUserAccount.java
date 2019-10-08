package com.chzu.ice.chat.pojo.gson;

public class GUserAccount {
    public String username;
    public String password;
    public String topic;


    public GUserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public GUserAccount(String username, String password, String topic) {
        this.username = username;
        this.password = password;
        this.topic = topic;
    }
}
