package com.chzu.ice.chat.activity.login;

public class LoginPresenter implements ILoginPresenter {
    private ILoginView loginView;
    private ILoginModel loginModel;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel(this);
    }

    @Override
    public void login(String usr, String pwd) {

    }

    @Override
    public void registerComponents() {
        this.loginView.registerComponents();
    }

    @Override
    public void wrongPasswordOrNoUser() {

    }

    @Override
    public void showWrongPasswordOrNoUser() {

    }
}
