package com.chzu.ice.chat.config;

public class AppConfig {
    public static final String SERVER_DEV = "http://10.200.16.124:18080/api";
    public static final String REGISTER_API = SERVER_DEV + "/userAccount/register";
    public static final String LOGIN_API = SERVER_DEV + "/userAccount/login";
    public static final String ADD_FRIEND_API = SERVER_DEV + "/friend/addFriend";
    public static final String LOAD_FRIENDS_API = SERVER_DEV + "/friend/loadFriends";


    //用户登录配置文件
    public static final String SP_CONFIG_ADDRESS_LOGIN_INFO = "login_info";
    public static final String SP_CONFIG_KEY_SIGNED_IN_USER = "signed_in_user";
    public static final String SP_CONFIG_KEY_HAS_SIGNED_IN = "has_signed_in";
    public static final String SP_CONFIG_KEY_IS_FIRST_OPEN = "is_first_open";
    public static final String SP_CONFIG_KEY_SIGNED_IN_USER_TOPIC = "signed_in_user_topic";

}
