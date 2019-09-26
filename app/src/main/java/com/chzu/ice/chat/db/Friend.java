package com.chzu.ice.chat.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Friend {
    @Id
    public long id;
    public String FName;
    public String MName;
    public String FTopic;

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
