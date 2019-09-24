package com.chzu.ice.chat.activity.friends;

public class FriendsModel implements IFriendsModel {
    private IFriendsPresenter iFriendsPresenter;
    public FriendsModel(IFriendsPresenter iFriendsPresenter) {
        this.iFriendsPresenter = iFriendsPresenter;
    }
}
