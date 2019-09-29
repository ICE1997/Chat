package com.chzu.ice.chat.pojo.objectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Message {
    @Id
    public long id;
    private long timestamp;
    private String fromU;
    private String toU;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromU() {
        return fromU;
    }

    public void setFromU(String fromU) {
        this.fromU = fromU;
    }

    public String getToU() {
        return toU;
    }

    public void setToU(String toU) {
        this.toU = toU;
    }
}
