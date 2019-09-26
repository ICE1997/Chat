package com.chzu.ice.chat.activity.login;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.db.UserAccount;
import com.chzu.ice.chat.db.UserAccount_;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import io.objectbox.Box;

class LoginModel {
    private Box<UserAccount> accountBox;

    LoginModel() {
        this.accountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
    }

    void login(String usr, String pwd, LoginCallback callback) {
        if(this.accountBox!=null) {
            if(accountBox.query().equal(UserAccount_.UName,usr).equal(UserAccount_.UPwd,pwd).build().find().isEmpty()){
             callback.isWrongPasswordOrNoUser();
            }else {
                callback.loginSucceed();
            }
        }
    }

    interface LoginCallback {
        void isWrongPasswordOrNoUser();

        void loginSucceed();
    }
}
