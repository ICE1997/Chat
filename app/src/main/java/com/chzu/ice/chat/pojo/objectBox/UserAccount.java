package com.chzu.ice.chat.pojo.objectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class UserAccount {
    @Id
    public long id;
    private String UName;
    private String UPwd;
    private String topic;

    public UserAccount(String UName, String UPwd) {
        this.UName = UName;
        this.UPwd = UPwd;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getUPwd() {
        return UPwd;
    }

    public void setUPwd(String UPwd) {
        this.UPwd = UPwd;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
