package com.ktkj.util;

import com.ktkj.utils.ResourceUtil;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:58<br>
 * 描述: ApiUserUtils <br>
 */
public class ApiUserUtils {

    // 获取微信授权code
    public static String getCode(String state, String redirectUri, String scope) {
        return String.format(ResourceUtil.getConfigByName("wx.getCode"), ResourceUtil.getConfigByName("wx.appId"), redirectUri, scope, state);
    }

    // 获取微信授权access_token
    public static String getOAuthAccessToken(String code) {
        return String.format(ResourceUtil.getConfigByName("wx.getOAuthAccessToken"),
                ResourceUtil.getConfigByName("wx.appId"),
                ResourceUtil.getConfigByName("wx.secret"),
                code);
    }

    // 通过openid获取微信用户信息
    public static String getUserInfoByOpenId(String openId, String accessToken) {
        return String.format(ResourceUtil.getConfigByName("wx.getUserInfoByOpenId"),
                openId, accessToken);
    }

    //替换字符串
    public static String getWebAccess(String code) {
        return String.format(ResourceUtil.getConfigByName("wx.webAccessTokenhttps"),
                ResourceUtil.getConfigByName("wx.appId"),
                ResourceUtil.getConfigByName("wx.secret"),
                code);
    }

    //替换字符串
    public static String getUserMessage(String access_token, String openid) {
        return String.format(ResourceUtil.getConfigByName("wx.userMessage"), access_token, openid);
    }
}