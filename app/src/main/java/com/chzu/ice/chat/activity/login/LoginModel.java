package com.chzu.ice.chat.activity.login;

import android.util.Log;

import com.chzu.ice.chat.pojo.gson.GLoginRep;
import com.chzu.ice.chat.pojo.gson.resp.BaseResponse;
import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.pojo.gson.req.LoginReq;
import com.chzu.ice.chat.pojo.gson.resp.data.LoginData;
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
    private static final String TAG = "LoginModel";

    LoginModel() {
        okHttpClient = new OkHttpClient();
    }

    void login(final String usr, final String pwd, final LoginCallback callback) {
        new Thread(() -> {
            Gson gson = new Gson();
            String loginReqJson = gson.toJson(new LoginReq(usr, pwd));
            RequestBody requestBody = RequestBody.create(JSON, loginReqJson);
            Request request = new Request.Builder().url(AppConfig.LOGIN_API).post(requestBody).build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String respS = Objects.requireNonNull(response.body()).string();
                BaseResponse<LoginData> respJ = gson.fromJson(respS, new TypeToken<BaseResponse<LoginData>>() {
                }.getType());

                switch (respJ.code) {
                    case "10201":
                        Log.d(TAG, "run: 用户topic" + respJ.data.topic);
                        callback.loginSucceed(respJ.data.accessToken,respJ.data.refreshToken,respJ.data.topic);
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
        }).start();
    }

    interface LoginCallback {
        void noSuchUser();

        void wrongPassword();

        void loginSucceed(String accessToken,String refreshToken,String topic);
    }
}
