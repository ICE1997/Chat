package com.chzu.ice.chat.pojo.objectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class FriendRelation {
    @Id
    public long id;
    private String FName;
    private String MName;
    private String FTopic;

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getFTopic() {
        return FTopic;
    }

    public void setFTopic(String FTopic) {
        this.FTopic = FTopic;
    }

    public String getMName() {
        return MName;
    }

    public void setMName(String MName) {
        this.MName = MName;
    }
}
