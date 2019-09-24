package com.chzu.ice.chat.activity.login;

public class LoginPresenter implements ILoginPresenter {
    private ILoginView loginView;
    private ILoginModel loginModel;

    LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel(this);
    }

    @Override
    public void login(String usr, String pwd) {
        loginModel.login(usr,pwd);
    }

    @Override
    public void loginSucceed() {
        loginView.showLoginSucceed();
    }


    @Override
    public void isWrongPasswordOrNoUser() {
        loginView.showWrongPasswordOrNoUser();
    }
}
