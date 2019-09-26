package com.chzu.ice.chat.activity.friends;

import android.graphics.Bitmap;

import com.chzu.ice.chat.activity.BasePresenter;
import com.chzu.ice.chat.activity.BaseView;
import com.chzu.ice.chat.db.Friend;
import com.google.zxing.WriterException;

import java.util.List;

public interface IFriendsContract {
    interface View extends BaseView<Presenter> {
        void showNoSuchUser();
        void showHasAlreadyAdded();
        void showAddFriendSucceed();
    }

    interface Presenter extends BasePresenter {
        Bitmap generateQRCode(String s) throws WriterException;
        void addFriend(String mName,String fName);
        List<Friend> getAllFriends(String usr);
    }
}
