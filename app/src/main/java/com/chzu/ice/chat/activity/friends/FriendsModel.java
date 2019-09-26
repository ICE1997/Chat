package com.chzu.ice.chat.activity.friends;

import android.util.Log;

import com.chzu.ice.chat.db.Friend;
import com.chzu.ice.chat.db.Friend_;
import com.chzu.ice.chat.db.UserAccount;
import com.chzu.ice.chat.db.UserAccount_;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

public class FriendsModel {
    private static final String TAG = FriendsModel.class.getSimpleName();
    private Box<Friend> friendBox;
    private Box<UserAccount> userAccountBox;

    public FriendsModel() {
        friendBox = ObjectBoxHelper.get().boxFor(Friend.class);
        userAccountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
    }

    public List<Friend> getAllFriends(String usr) {
        List<Friend> friends = friendBox.query().equal(Friend_.MName, usr).build().find();
        if (friendBox == null || friends.isEmpty()) {
            return new ArrayList<>();
        }
        return friends;
    }

    void addFriend(String mName, String fName, AddFriendCallback callback) {
        if (userAccountBox != null && friendBox != null) {
            List<UserAccount> userAccounts = userAccountBox.query().equal(UserAccount_.UName, fName).build().find();
            List<Friend> friends = friendBox.query().equal(Friend_.MName, mName).equal(Friend_.FName, fName).build().find();
            if (friends.isEmpty()) {
                if (!userAccounts.isEmpty()) {
                    Friend friend = new Friend();
                    friend.setMName(mName);
                    friend.setFName(fName);
                    friend.setFTopic(userAccounts.get(0).getTopic());
                    friendBox.put(friend);
                    callback.addSucceed();
                    Log.d(TAG, "addFriend: 添加好友成功");
                } else {
                    callback.noSuchUser();
                }
            } else {
                callback.hasAlreadyAdded();
            }

        }
    }

    interface AddFriendCallback {
        void noSuchUser();

        void hasAlreadyAdded();

        void addSucceed();
    }
}
