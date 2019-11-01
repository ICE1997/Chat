package com.chzu.ice.chat;

import android.app.Application;
import android.content.Intent;

import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.service.MQTTService;
import com.chzu.ice.chat.utils.ObjectBoxHelper;
import com.chzu.ice.chat.utils.SPHelper;

import static com.chzu.ice.chat.config.AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC;

public class App extends Application {
    private static Application application;
    private String signedInUsername;
    private String signedInUserTopic;

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBoxHelper.init(this);
        ObjectBoxHelper.startDebug(true, this);

        application = this;

        Intent intent = new Intent(this, MQTTService.class);
        startService(intent);
    }

    public String getSignedInUsername() {
        if (signedInUsername == null) {
            signedInUsername = new SPHelper(getApplication(), AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO).getString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER, "");
        }
        return signedInUsername;
    }

    public void setSignedInUsername(String signedInUsername) {
        this.signedInUsername = signedInUsername;
    }

    public String getSignedInUserTopic() {
        if (signedInUserTopic == null) {
            signedInUserTopic = new SPHelper(getApplication(), AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO).getString(SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC, "");
        }
        return signedInUserTopic;
    }

    public void setSignedInUserTopic(String signedInUserTopic) {
        this.signedInUserTopic = signedInUserTopic;
    }

    public static Application getApplication() {
        return application;
    }
}
