package com.chzu.ice.chat.activity.login;

public class LoginModel implements ILoginModel {
    private ILoginPresenter loginPresenter;

    LoginModel(ILoginPresenter presenter) {
        this.loginPresenter = presenter;
    }

    @Override
    public void login(String usr, String pwd) {

    }

    @Override
    public void wrongPasswordOrNoUser() {

    }
}
