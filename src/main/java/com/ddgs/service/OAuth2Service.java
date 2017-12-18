package com.ddgs.service;

import com.ddgs.dao.OAuth2Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/29.下午5:22
 */
@Repository
public class OAuth2Service {

    @Autowired
    OAuth2Dao oAuth2Dao;

    @Autowired
    RedisService redisService;

    //查看 clientId 是否存在
    public boolean checkClientId(String client_id) {
        return oAuth2Dao.checkClientId(client_id);
    }

    //当 scope 不等于默认值时检查认证权限
    public boolean checkScope(String client_id, String scope) {
        String[] scopes = scope.split(",");
        return oAuth2Dao.checkScope(scopes.length, client_id, scopes);
    }

    // 检查secret 是否正确
    public boolean checkClientSecret(String client_secret) {
        return oAuth2Dao.checkClientSecret(client_secret);
    }

    //验证授权客户端是否存在
    public boolean checkClient(String client_id, String client_secret) {
        return oAuth2Dao.checkClient(client_id, client_secret);
    }


    //验证用户帐号密码是否登录
    public boolean checkAccount(String username, String password) {
        return oAuth2Dao.checkAccount(username, password);
    }

    //根据用户名 获取 clientId
    public String getClientIdByName(String username) {
        return oAuth2Dao.getClientIdByName(username);
    }

    //给响应用户添加 accessToken
    public void addAccessToken(String accessToken, String client_id) {
        redisService.set("access_token_" + accessToken, client_id, 3600);
        redisService.set("access_token_client_id_" + client_id, accessToken, 3660);
    }

    //给响应用户添加 refreshToken
    public void addRefreshToken(String refreshToken, String client_id) {
        redisService.set("refresh_token_" + refreshToken, client_id, 3600 * 30);
        redisService.set("refresh_client_id_" + client_id, refreshToken, 3660 * 30);
    }

    //验证refresh_token是否存在
    public boolean checkRefreshToken(String refresh_token) {
        String token = redisService.get("refresh_token_" + refresh_token);

        if (StringUtils.isEmpty(token) || !token.equals(refresh_token)) {
            return false;
        }

        return true;
    }

    //根据 refresh_token 获取 clientId
    public String getClientIdByRefreshToken(String refresh_token) {
        return redisService.get("refresh_client_id_" + refresh_token);
    }

    // 更新 access_token
    public String updateAccessToekn(String client_id) {
        String accessToken = this.getAccessTokenByClientId(client_id);
        redisService.set("access_token_" + accessToken, client_id, 3600);
        redisService.set("access_token_client_id_" + client_id, accessToken, 3660);
        return accessToken;
    }

    // 检查code 是否存在
    public boolean checkAuthCode(String code, String client_id) {
        String authCode = redisService.get("authorizationCode_" + client_id);

        if (StringUtils.isEmpty(authCode) || !authCode.equals(code)) {
            return false;
        }
        redisService.del("authorizationCode_" + client_id);
        return true;
    }

    //根据 client_id 获取 accessToken
    public String getAccessTokenByClientId(String client_id) {
        return redisService.get("access_token_client_id_" + client_id);
    }

    //根据 client_id 获取 refresh_token
    public String getRefreshToeknByClientId(String client_id) {
        return redisService.get("refresh_client_id_" + client_id);
    }

    //根据 client_id 获取 AuthCode
    public String getAuthCodeByClientId(String client_id) {
        return redisService.get("authorizationCode_" + client_id);
    }

    //code 授权码跟用户绑定
    public void addAuthCode(String code, String client_id) {
        redisService.set("authorizationCode_" + client_id, code, 3600);
    }

    //验证 token 是否存在
    public boolean checkAccessToken(String accessToken) {
        return redisService.hasKey("access_token_" + accessToken);
    }

    //auth code / access token 过期时间
    public long getExpireIn(String accessToken) {
        return redisService.getExpire("access_token_" + accessToken);
    }

}
