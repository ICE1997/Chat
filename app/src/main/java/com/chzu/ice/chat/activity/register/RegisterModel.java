package com.chzu.ice.chat.activity.register;

import android.util.Log;

import com.chzu.ice.chat.db.UserAccount;
import com.chzu.ice.chat.db.UserAccount_;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import io.objectbox.Box;

class RegisterModel {
    private static final String TAG = RegisterModel.class.getSimpleName();
    private Box<UserAccount> accountBox;

    RegisterModel() {
        this.accountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
    }

    void register(String usr, String psw, RegisterCallback callback) {
        if(this.accountBox!=null) {
            if(accountBox.query().equal(UserAccount_.usr,usr).build().find().isEmpty()) {
                UserAccount userAccount = new UserAccount(usr,psw);
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
