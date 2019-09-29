package com.chzu.ice.chat.activity.friendsRelations;

import android.graphics.Bitmap;

import com.chzu.ice.chat.activity.BasePresenter;
import com.chzu.ice.chat.activity.BaseView;
import com.chzu.ice.chat.adapter.FriendsListAdapter;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.google.zxing.WriterException;

import java.util.List;

public interface IFriendsContract {
    interface View extends BaseView<Presenter> {
        void showNoSuchUser();
        void showHasAlreadyAdded();
        void showAddFriendSucceed();
        void showLoadSucceed(List<FriendRelation> relations);
    }

    interface Presenter extends BasePresenter {
        Bitmap generateQRCode(String s) throws WriterException;
        void addFriend(String mName,String fName);
        void loadAllFriends(String usr,FriendsModel.LoadFriendCallback callback);
    }
}
