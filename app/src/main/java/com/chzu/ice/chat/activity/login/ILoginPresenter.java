package com.chzu.ice.chat.activity.login;

public interface ILoginPresenter {
    void login(String usr,String pwd);
    void registerComponents();
    void wrongPasswordOrNoUser();
    void showWrongPasswordOrNoUser();
}
