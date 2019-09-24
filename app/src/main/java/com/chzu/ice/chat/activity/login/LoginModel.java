package com.chzu.ice.chat.activity.login;

public class LoginModel implements ILoginModel {
    private ILoginPresenter loginPresenter;
    private  final String usr="123";
    private final String pwd = "123";

    LoginModel(ILoginPresenter presenter) {
        this.loginPresenter = presenter;
    }

    @Override
    public void login(String usr, String pwd) {
        if(!this.usr.equals(usr)&&!this.pwd.equals(pwd)) {
            loginPresenter.isWrongPasswordOrNoUser();
        }else {
            loginPresenter.loginSucceed();
        }
    }
}
