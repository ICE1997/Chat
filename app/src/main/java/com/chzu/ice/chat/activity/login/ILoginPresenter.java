package com.chzu.ice.chat.activity.login;

public interface ILoginPresenter {
    void login(String usr,String pwd);
    void loginSucceed();
    void isWrongPasswordOrNoUser();
}
