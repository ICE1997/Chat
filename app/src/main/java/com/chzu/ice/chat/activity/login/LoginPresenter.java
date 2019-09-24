package com.chzu.ice.chat.activity.login;

public class LoginPresenter implements ILoginContract.Presenter {
    private ILoginContract.View loginView;
    private LoginModel loginModel;

    LoginPresenter(ILoginContract.View loginView,LoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        loginView.setPresenter(this);
    }

    @Override
    public void login(String usr, String pwd) {
        loginModel.login(usr, pwd, new LoginModel.LoginCallback() {
            @Override
            public void isWrongPasswordOrNoUser() {
                loginView.showWrongPasswordOrNoUser();
            }

            @Override
            public void loginSucceed() {
                loginView.showLoginSucceed();
            }
        });
    }
}
