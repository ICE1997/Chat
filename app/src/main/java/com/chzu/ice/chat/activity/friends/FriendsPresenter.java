package com.chzu.ice.chat.activity.friends;

class FriendsPresenter implements IFriendsContract.Presenter {
    private IFriendsContract.View view;
    private FriendsModel friendsModel;

    FriendsPresenter(IFriendsContract.View view, FriendsModel friendsModel) {
        this.view = view;
        this.friendsModel = friendsModel;
        view.setPresenter(this);
    }
}
