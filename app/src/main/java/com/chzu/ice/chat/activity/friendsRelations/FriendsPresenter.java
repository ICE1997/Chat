package com.chzu.ice.chat.activity.friendsRelations;

import android.graphics.Bitmap;

import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

class FriendsPresenter implements IFriendsContract.Presenter {
    private IFriendsContract.View view;
    private FriendsModel friendsModel;

    FriendsPresenter(IFriendsContract.View view, FriendsModel friendsModel) {
        this.view = view;
        this.friendsModel = friendsModel;
        view.setPresenter(this);
    }

    @Override
    public Bitmap generateQRCode(String s) throws WriterException {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.encodeBitmap(s, BarcodeFormat.QR_CODE, 600, 600);
        return bitmap;
    }

    @Override
    public void addFriend(String mName, String fName) {
        friendsModel.addFriend(mName, fName, new FriendsModel.AddFriendCallback() {
            @Override
            public void noSuchUser() {
                view.showNoSuchUser();
            }

            @Override
            public void hasAlreadyAdded() {
                view.showHasAlreadyAdded();
            }

            @Override
            public void addSucceed() {
                view.showAddFriendSucceed();
            }
        });
    }

    @Override
    public void loadAllFriends(String usr,FriendsModel.LoadFriendCallback callback) {
        friendsModel.loadAllFriends(usr,callback);
    }
}
