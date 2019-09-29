package com.chzu.ice.chat.utils;

public class AppConfig {
    public static final String serverTest = "http://192.168.50.71:8080/api";
    public static final String registerAPI = serverTest + "/userAccount/register";
    public static final String loginAPI = serverTest + "/userAccount/login";
    public static final String addFriendAPI = serverTest + "/friend/addFriend";
    public static final String loadFriendsAPI = serverTest + "/friend/loadFriends";
}
