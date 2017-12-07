package com.ddgs.service;

import com.ddgs.dao.OAuth2Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/29.下午5:22
 */
@Repository
public class OAuth2Service {

    @Autowired
    OAuth2Dao oAuth2Dao;

    // 检查code 是否存在
    public boolean checkAuthCode(String code, String client_id) {
        return true;
    }

    //查看 clientId 是否存在
    public boolean checkClientId(String client_id) {
        return oAuth2Dao.checkClientId(client_id);
    }

    //当 scope 不等于默认值时检查认证权限
    public boolean checkScope(String client_id, String scope) {
        return false;
    }

    // 检查secret 是否正确
    public boolean checkClientSecret(String client_secret) {
        return oAuth2Dao.checkClientSecret(client_secret);
    }


    //验证用户帐号密码是否登录
    public boolean checkUser(String username, String password) {
        return true;
    }

    //根据用户名 获取 clientId
    public String getClientIdByName(String username) {
        return oAuth2Dao.getClientIdByName(username);
    }

    //验证授权账户是否存在
    public boolean checkAccount(String client_id, String client_secret) {
        return true;
    }

    //验证refresh_token是否存在
    public boolean checkRefreshToken(String client_id, String refresh_token) {
        return false;
    }

    //验证 token 是否存在
    public boolean checkAccessToken(String accessToken) {
        return true;
    }

    //根据 client_id 获取 accessToken
    public String getAccessTokenByClientId(String client_id) {
        return "";
    }

    //根据 client_id 获取 AuthCode
    public String getAuthCodeByClientId(String client_id) {
        return "";
    }

    //code 授权码跟用户绑定
    public void addAuthCode(String code, String client_id) {
    }

    //给响应用户添加 accessToken
    public void addAccessToken(String accessToken, String client_id) {

    }

    //auth code / access token 过期时间
    public long getExpireIn() {
        return 3600;
    }

}
