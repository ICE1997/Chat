package com.chzu.ice.chat.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class UserAccount {
    @Id
    public long id;
    public String usr;
    public String pwd;

    public UserAccount(String usr, String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
