package com.chzu.ice.chat.activity.login;

import com.chzu.ice.chat.activity.BasePresenter;
import com.chzu.ice.chat.activity.BaseView;

public interface ILoginContract {
    interface View extends BaseView<Presenter> {
        void showWrongPasswordOrNoUser();
        void showLoginSucceed();
    }

    interface Presenter extends BasePresenter {
        void login(String usr,String pwd);
    }
}
