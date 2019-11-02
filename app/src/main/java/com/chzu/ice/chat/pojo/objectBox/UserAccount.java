package com.chzu.ice.chat.pojo.objectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class UserAccount {
    @Id
    public long id;
    private String UName;
    private String privateKey;
    private String topic;

    public UserAccount(String UName) {
        this.UName = UName;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
