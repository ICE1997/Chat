package com.chzu.ice.chat.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Message {
    @Id
    public long id;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
