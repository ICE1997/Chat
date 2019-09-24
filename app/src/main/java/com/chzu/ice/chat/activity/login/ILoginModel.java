package com.chzu.ice.chat.activity.login;

public interface ILoginModel {
    void login(String usr,String pwd);
    void wrongPasswordOrNoUser();
}
