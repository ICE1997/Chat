package com.chzu.ice.chat;

import android.app.Application;
import android.content.Intent;

import com.chzu.ice.chat.service.MQTTService;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

public class App extends Application {
    private static Application application;
    private String userName;
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBoxHelper.init(this);
        ObjectBoxHelper.startDebug(true,this);

        application = this;

        Intent intent = new Intent(getApplicationContext(), MQTTService.class);
        startService(intent);
    }

    public void setCurrentUserName(String user){
        this.userName = user;
    }
    public String getCurrentUserName() {
        return userName;
    }

    public static Application getApplication() {
        return application;
    }
}
