package com.chzu.ice.chat.activity.friends;

public class FriendsPresenter implements IFriendsPresenter {
    private IFriendsView iFriendsView;
    private IFriendsModel iFriendsModel;

    public FriendsPresenter(IFriendsView iFriendsView) {
        this.iFriendsView = iFriendsView;
        this.iFriendsModel = new FriendsModel(this);
    }
}
