package com.chzu.ice.chat.activity.register;

import com.chzu.ice.chat.activity.BasePresenter;
import com.chzu.ice.chat.activity.BaseView;

public interface IRegisterContract {
    interface View extends BaseView<Presenter> {
        void showRegisterSucceed();
        void showAlreadyRegistered();
    }

    interface Presenter extends BasePresenter {
        void register(String usr,String pwd);
    }
}
