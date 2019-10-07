package com.chzu.ice.chat.activity.splash;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.utils.SPHelper;

class SplashPresenter implements ISplashContract.Presenter {
    private ISplashContract.View view;

    SplashPresenter(ISplashContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public boolean hasSignedIn() {
        SPHelper loginInfo = new SPHelper(App.getApplication(), AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO);
        return loginInfo.getBoolean(AppConfig.SP_CONFIG_KEY_HAS_SIGNED_IN,false);
    }

    @Override
    public boolean isFirstOpen() {
        SPHelper loginInfo = new SPHelper(App.getApplication(),AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO);
        return loginInfo.getBoolean(AppConfig.SP_CONFIG_KEY_IS_FIRST_OPEN,true);
    }

    @Override
    public String getSignedUserName() {
        SPHelper loginInfo = new SPHelper(App.getApplication(),AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO);
        return loginInfo.getString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER,"");
    }
}
