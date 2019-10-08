package com.chzu.ice.chat.activity.register;

import com.chzu.ice.chat.pojo.gson.resp.BaseResponse;
import com.chzu.ice.chat.pojo.gson.GUserAccount;
import com.chzu.ice.chat.config.AppConfig;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class RegisterModel {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String TAG = RegisterModel.class.getSimpleName();
    private OkHttpClient okHttpClient;

    RegisterModel() {
        okHttpClient = new OkHttpClient();
    }

    void register(final String usr, final String psw, final RegisterCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                GUserAccount gUserAccount = new GUserAccount(usr, psw);
                String act = gson.toJson(gUserAccount);
                RequestBody requestBody = RequestBody.create(JSON, act);
                Request request = new Request.Builder().url(AppConfig.REGISTER_API).post(requestBody).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String respS = Objects.requireNonNull(response.body()).string();
                    BaseResponse respJ = gson.fromJson(respS, BaseResponse.class);
                    switch (respJ.code) {
                        case "10101":
                            callback.registerSucceed();
                            break;
                        case "10102":
                            callback.hasAlreadyRegistered();
                            break;
                        case "10103":
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    interface RegisterCallback {
        void registerSucceed();

        void hasAlreadyRegistered();
    }
}
