package com.chzu.ice.chat.activity.login;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.pojo.json.GLoginRep;
import com.chzu.ice.chat.pojo.json.GResponse;
import com.chzu.ice.chat.pojo.json.GUserAccount;
import com.chzu.ice.chat.utils.AppConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class LoginModel {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;

    LoginModel() {
        okHttpClient = new OkHttpClient();
    }

    void login(final String usr, final String pwd, final LoginCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                GUserAccount gUserAccount = new GUserAccount(usr, pwd);
                String act = gson.toJson(gUserAccount);
                RequestBody requestBody = RequestBody.create(JSON, act);
                Request request = new Request.Builder().url(AppConfig.loginAPI).post(requestBody).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String respS = Objects.requireNonNull(response.body()).string();
                    GResponse<GLoginRep> respJ = gson.fromJson(respS, new TypeToken<GResponse<GLoginRep>>(){}.getType());
                    switch (respJ.code) {
                        case "10201":
                            System.out.println(respJ.data.topic);
                            ((App) App.getApplication()).setMyTopic(respJ.data.topic);
                            callback.loginSucceed();
                            break;
                        case "10202":
                            callback.noSuchUser();
                            break;
                        case "10203":
                            callback.wrongPassword();
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    interface LoginCallback {
        void noSuchUser();

        void wrongPassword();

        void loginSucceed();
    }
}
