package com.chzu.ice.chat.activity.splash;

import com.chzu.ice.chat.activity.BasePresenter;
import com.chzu.ice.chat.activity.BaseView;

public interface ISplashContract {
    public interface View extends BaseView<Presenter> {

    }

    public interface Presenter extends BasePresenter {
        boolean hasSignedIn();
        boolean isFirstOpen();
        String getSignedUserName();
    }
}
