package com.chzu.ice.chat.activity.friendsRelations;

import android.util.Log;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.pojo.json.GRepFriendRelation;
import com.chzu.ice.chat.pojo.json.GReqFriendRelation;
import com.chzu.ice.chat.pojo.json.GResponse;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation_;
import com.chzu.ice.chat.pojo.objectBox.UserAccount;
import com.chzu.ice.chat.pojo.objectBox.UserAccount_;
import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.utils.ObjectBoxHelper;
import com.chzu.ice.chat.utils.SPHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.objectbox.Box;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class FriendsModel {
    private static final String TAG = FriendsModel.class.getSimpleName();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;
    private Box<FriendRelation> friendBox;
    private Box<UserAccount> userAccountBox;

    FriendsModel() {
        friendBox = ObjectBoxHelper.get().boxFor(FriendRelation.class);
        userAccountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
        okHttpClient = new OkHttpClient();
    }

    List<FriendRelation> loadAllFriends() {
        List<FriendRelation> relations = new ArrayList<>();
        Gson gson = new Gson();
        SPHelper spHelper = new SPHelper(App.getApplication(), AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO);
        String signedInUsername = spHelper.getString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER, "");
        GReqFriendRelation friendRelation = new GReqFriendRelation();
        friendRelation.userName = signedInUsername;
        String act = gson.toJson(friendRelation);
        RequestBody requestBody = RequestBody.create(JSON, act);
        Request request = new Request.Builder().url(AppConfig.LOAD_FRIENDS_API).post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String respS = Objects.requireNonNull(response.body()).string();
            Log.d(TAG, "run: " + respS);
            GResponse<List<GRepFriendRelation>> gRelations = gson.fromJson(respS, new TypeToken<GResponse<List<GRepFriendRelation>>>() {
            }.getType());
            switch (gRelations.code) {
                case "10401":
                    friendBox.removeAll();
                    for (GRepFriendRelation relation : gRelations.data) {
                        Log.d(TAG, "loadAllFriends: " + relation.userName +"\t" + relation.friendName +"\t" + relation.friendTopic + "\n");
                        FriendRelation temp = new FriendRelation();
                        temp.setFName(relation.friendName);
                        temp.setMName(relation.userName);
                        temp.setFTopic(relation.friendTopic);
                        friendBox.put(temp);
                    }
                    relations = friendBox.query().equal(FriendRelation_.MName, signedInUsername).build().find();
                    break;
                case "10402":
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relations;
    }

    void addFriend(String mName, String fName, AddFriendCallback callback) {
        if (userAccountBox != null && friendBox != null) {
            List<UserAccount> userAccounts = userAccountBox.query().equal(UserAccount_.UName, fName).build().find();
            List<FriendRelation> friendRelations = friendBox.query().equal(FriendRelation_.MName, mName).equal(FriendRelation_.FName, fName).build().find();
            if (friendRelations.isEmpty()) {
                if (!userAccounts.isEmpty()) {
                    FriendRelation friendRelation = new FriendRelation();
                    friendRelation.setMName(mName);
                    friendRelation.setFName(fName);
                    friendRelation.setFTopic(userAccounts.get(0).getTopic());
                    friendBox.put(friendRelation);
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


    public interface AddFriendCallback {
        void noSuchUser();

        void hasAlreadyAdded();

        void addSucceed();
    }
}
