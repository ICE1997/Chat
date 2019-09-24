package com.chzu.ice.chat.activity.login;

class LoginModel {
    private final String usr = "123";
    private final String pwd = "123";

    void login(String usr, String pwd, LoginCallback callback) {
        if (!this.usr.equals(usr) || !this.pwd.equals(pwd)) {
            callback.isWrongPasswordOrNoUser();
        } else {
            callback.loginSucceed();
        }
    }

    interface LoginCallback {
        void isWrongPasswordOrNoUser();

        void loginSucceed();
    }
}
