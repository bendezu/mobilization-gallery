package com.bendezu.yandexphotos.util;

public class AuthUtils {

    public static final String PASSWORD = "f9e3d8ad13624c0985b2da84af91b310";

    //OAuth URL constants
    public static final String OAUTH_URL = "https://oauth.yandex.ru/authorize";
    public static final String QUESTION_MARK = "?";
    public static final String RESPONSE_TYPE = "token";
    public static final String AMPERSAND = "&";
    public static final String CLIENT_ID = "27b1814298ef4a65907b9384adceafc4";
    public static final String REDIRECT_URI = "yandexphotos://token";
    public static final String FORCE_CONFIRM = "true";

    public static String getAuthUrl(){
        return OAUTH_URL + QUESTION_MARK
                + "response_type=" + RESPONSE_TYPE +
                AMPERSAND + "client_id=" + CLIENT_ID +
                AMPERSAND + "redirect_uri=" + REDIRECT_URI;
    }

    //Forces authorization even if user has already authorized
    public static String getForceAuthUrl(){
        return getAuthUrl() + AMPERSAND + "force_confirm=" + FORCE_CONFIRM;
    }
}
