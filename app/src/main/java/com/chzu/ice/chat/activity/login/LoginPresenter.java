package com.chzu.ice.chat.activity.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.pojo.objectBox.UserAccount;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import io.objectbox.Box;

public class LoginPresenter implements ILoginContract.Presenter {
    private ILoginContract.View loginView;
    private LoginModel loginModel;

    LoginPresenter(ILoginContract.View loginView, LoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        loginView.setPresenter(this);
    }

    @Override
    public void login(final String usr, String pwd) {

        loginModel.login(usr, pwd, new LoginModel.LoginCallback() {
            SharedPreferences.Editor spEditor = App.getApplication().getApplicationContext().getSharedPreferences(AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO, Context.MODE_PRIVATE).edit();

            @Override
            public void noSuchUser() {
                spEditor.remove(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER);
                spEditor.remove(AppConfig.SP_CONFIG_KEY_HAS_SIGNED_IN);
                spEditor.apply();
                loginView.showNoSuchUser();
            }

            @Override
            public void wrongPassword() {
                spEditor.remove(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER);
                spEditor.remove(AppConfig.SP_CONFIG_KEY_HAS_SIGNED_IN);
                spEditor.apply();
                loginView.showWrongPassword();
            }

            @Override
            public void loginSucceed(String accessToken, String refreshToken, String topic) {
                spEditor.putBoolean(AppConfig.SP_CONFIG_KEY_IS_FIRST_OPEN, false);
                spEditor.putBoolean(AppConfig.SP_CONFIG_KEY_HAS_SIGNED_IN, true);
                spEditor.putString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER, usr);
                spEditor.putString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC, topic);
                spEditor.putString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER_ACCESS_TOKEN, accessToken);
                spEditor.putString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER_REFRESH_TOKEN, refreshToken);
                spEditor.apply();

                Box<UserAccount> userAccountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
                UserAccount userAccount = new UserAccount(usr);
                userAccountBox.put(userAccount);

                loginView.showLoginSucceed();
            }
        });
    }
}
