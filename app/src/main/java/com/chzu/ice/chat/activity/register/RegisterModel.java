package com.chzu.ice.chat.activity.register;

import android.util.Log;

import com.chzu.ice.chat.db.UserAccount;
import com.chzu.ice.chat.db.UserAccount_;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import java.util.Random;
import java.util.UUID;

import io.objectbox.Box;

class RegisterModel {
    private static final String TAG = RegisterModel.class.getSimpleName();
    private Box<UserAccount> accountBox;

    RegisterModel() {
        this.accountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
    }

    void register(String usr, String psw, RegisterCallback callback) {
        if(this.accountBox!=null) {
            if(accountBox.query().equal(UserAccount_.UName,usr).build().find().isEmpty()) {
                UserAccount userAccount = new UserAccount(usr,psw);
                userAccount.setTopic("usr"+ UUID.randomUUID().toString().replaceAll("-",""));
                accountBox.put(userAccount);
                callback.registerSucceed();
            }else {
                callback.hasAlreadyRegistered();
            }
        }else {
            Log.d(TAG, "register: 发现异常，accountBox为Null");
        }

    }

    interface RegisterCallback {
        void registerSucceed();
        void hasAlreadyRegistered();
    }
}
