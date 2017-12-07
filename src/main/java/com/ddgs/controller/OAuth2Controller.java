package com.ddgs.controller;

import com.ddgs.ErrorCode;
import com.ddgs.Result;
import com.ddgs.service.OAuth2Service;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/23.下午1:44
 */

/**
 * 相关文档
 * http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html
 * https://cwiki.apache.org/confluence/display/OLTU/OAuth+2.0+Authorization+Server
 */


@RestController
public class OAuth2Controller {

    @Autowired
    OAuth2Service oAuth2Service;

    @GetMapping(value = "/oauth/authorize")
    public Result authorize(HttpServletRequest request,
                            @RequestParam(value = "response_type", defaultValue = "") String response_type,
                            @RequestParam(value = "client_id", defaultValue = "") String client_id,
                            @RequestParam(value = "redirect_uri", defaultValue = "") String redirect_uri,
                            @RequestParam(value = "scope", defaultValue = "basic") String scope,
                            @RequestParam(value = "state", defaultValue = "") String state) {
        Result result = new Result();
        try {
            //构建OAuth 授权请求 检测错误
            new OAuthAuthzRequest(request);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            result.setError(ErrorCode.ERROR);
            return result;
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            result.setError(ErrorCode.ERROR);
            result.setMsg(e.getDescription());
            return result;
        }

        if (OAuthUtils.isEmpty(redirect_uri)) {
            //告诉客户端没有传入redirectUri直接报错
            result.setError(ErrorCode.INVALID_REDIRECT_URI);
            return result;
        }

        //检查提交的客户端id是否正确
        result = checkClientId(client_id);
        if ((Boolean) result.get("success") == false) {
            return result;
        }

        //当 scope 不等于默认值时检查认证权限
        if (!"basic".equals(scope)) {
            result = checkScope(client_id, scope);
            if ((Boolean) result.get("success") == false) {
                return result;
            }
        }

        //查看是否已经已有 authorizationCode
        result = checkExistCode(client_id, redirect_uri, state);
        if ((Boolean) result.get("success") == true) {
            return result;
        }

        String authorizationCode = null;
        //responseType目前仅支持CODE，不支持TOKEN模式
        if (!response_type.equals(ResponseType.CODE.toString())) {
            result.setSuccess(false);
            result.setError(ErrorCode.RESPONSETYPE_ERROR);
            return result;
        }

        //生成授权码
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            authorizationCode = oauthIssuerImpl.authorizationCode();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setError(ErrorCode.OAUT_CODE_FAIL);
            return result;
        }

        oAuth2Service.addAuthCode(authorizationCode, client_id);
        //得到到客户端重定向地址
        result.setSuccess(true);
        result.put("redirectURI", redirect_uri + "?code=" + authorizationCode + "&state=" + state);
        return result;

    }


    @PostMapping(value = {"/oauth/token"})
    public Result token(HttpServletRequest request,
                        @RequestParam(value = "grant_type", defaultValue = "") String grant_type,
                        @RequestParam(value = "scope", defaultValue = "basic") String scope,
                        @RequestParam(value = "client_id", defaultValue = "") String client_id,
                        @RequestParam(value = "client_secret", defaultValue = "") String client_secret,
                        @RequestParam(value = "username", defaultValue = "") String username,
                        @RequestParam(value = "password", defaultValue = "") String password,
                        @RequestParam(value = "code", defaultValue = "") String code,
                        @RequestParam(value = "redirect_uri", defaultValue = "") String redirect_uri,
                        @RequestParam(value = "refresh_token", defaultValue = "") String refresh_token) {
        Result result = new Result();

        try {
            //构建OAuth 授权请求 检测错误
            new OAuthTokenRequest(request);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            result.setError(ErrorCode.ERROR);
            return result;
        } catch (OAuthProblemException e) {
            result.setError(ErrorCode.ERROR);
            result.setMsg(e.getDescription());
            return result;
        }

        if (grant_type.equals(GrantType.AUTHORIZATION_CODE.toString())) {
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型
            result = checkAuthCode(code, client_id);
            if ((Boolean) result.get("success") == false) {
                return result;
            }
        } else if (grant_type.equals(GrantType.CLIENT_CREDENTIALS.toString())) {
            //检查验证类型,此处只检查 CLIENT_CREDENTIALS类型
            result = checkClient(client_id, client_secret);
            if ((Boolean) result.get("success") == false) {
                return result;
            }
        } else if (grant_type.equals(GrantType.PASSWORD.toString())) {
            // 检查验证类型，此处只检查PASSWORDE类型
            result = checkAccount(username, password);
            if ((Boolean) result.get("success") == false) {
                return result;
            }
            //获取 client_id
            client_id = oAuth2Service.getClientIdByName(username);
        }
//        else if (grant_type.equals(GrantType.REFRESH_TOKEN.toString())) {
//            // 检查验证类型，此处只检查REFRESH_TOKEN类型
//            result = checkRefreshToken(client_id, refresh_token);
//            if ((Boolean) result.get("success") == false) {
//                return result;
//            }
//
//        }
        else {
            //不支持的类型
            result.setSuccess(false);
            result.setError(ErrorCode.RESPONSETYPE_ERROR);
            return result;
        }

        //当 scope 不等于默认值时检查认证权限
        if (!"basic".equals(scope)) {
            result = checkScope(client_id, scope);
            if ((Boolean) result.get("success") == false) {
                return result;
            }
        }

        //查看是否已经已有 Access Token
        result = checkExistAccessToekn(client_id);
        if ((Boolean) result.get("success") == true) {
            return result;
        }

        //生成Access Token
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        String accessToken = null;
        try {
            accessToken = oauthIssuerImpl.accessToken();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setError(ErrorCode.ACCESS_TOKEN_FAIL);
            return result;
        }

        //绑定Access Token
        oAuth2Service.addAccessToken(accessToken, client_id);
        result.setSuccess(true);
        result.put("access_token", accessToken);
        result.put("token_type", "bearer");


//        //生成RefreshToken
//        String refreshToken = null;
//        try {
//            refreshToken = oauthIssuerImpl.accessToken();
//        } catch (OAuthSystemException e) {
//            e.printStackTrace();
//            result.setSuccess(false);
//            result.setError(ErrorCode.ACCESS_TOKEN_FAIL);
//            return result;
//        }
//        //绑定RefreshToken 后期支持
//        oAuth2Service.addRefreshToken(refreshToken, client_id);
//        result.put("refresh_token", refresh_token);
        result.put("expires_in", oAuth2Service.getExpireIn(accessToken));
        return result;

    }


    @PostMapping(value = "/oauth/check")
    public Result check(@RequestParam("access_token") String accessToken) {
        Result result = new Result();
        if (!oAuth2Service.checkAccessToken(accessToken)) {
            result.setError(ErrorCode.INVALID_ACCESS_TOKEN);
            return result;
        }
        result.put("expires_in", oAuth2Service.getExpireIn(accessToken));
        result.setSuccess(true);
        return result;
    }

    /**
     * 检查提交的客户端id是否正确
     *
     * @param client_id
     * @return
     */
    Result checkClientId(String client_id) {
        Result result = new Result();
        if (!oAuth2Service.checkClientId(client_id)) {
            result.setError(ErrorCode.INVALID_CLIENT_ID);
            return result;
        }
        result.setSuccess(true);
        return result;

    }


    /**
     * 检查客户端安全KEY是否正确
     *
     * @param client_secret
     * @return
     */
    Result checkClientSecret(String client_secret) {
        Result result = new Result();
        if (!oAuth2Service.checkClientSecret(client_secret)) {
            result.setError(ErrorCode.INVALID_CLIENT_ID);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 检查验证类型，此处只检查AUTHORIZATION_CODE类型
     *
     * @param authCode
     * @param client_id
     * @return
     */
    Result checkAuthCode(String authCode, String client_id) {
        Result result = new Result();
        if (!oAuth2Service.checkAuthCode(authCode, client_id)) {
            result.setError(ErrorCode.INVALID_AUTH_CODE);
            return result;
        }
        result.setSuccess(true);
        return result;
    }


    /**
     * 检查验证类型,此处只检查 CLIENT_CREDENTIALS类型
     *
     * @param client_id
     * @param client_secret
     * @return
     */
    Result checkClient(String client_id, String client_secret) {
        Result result = new Result();
        if (!oAuth2Service.checkClient(client_id, client_secret)) {
            result.setError(ErrorCode.INVALID_CLIENT_ID);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 检查验证类型，此处只检查PASSWORD类型
     *
     * @param username
     * @param password
     * @return
     */
    Result checkAccount(String username, String password) {
        Result result = new Result();
        if (!oAuth2Service.checkAccount(username, password)) {
            result.setError(ErrorCode.INVALID_AUTH_USER);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 检查验证类型，此处只检查REFRESH_TOKEN类型
     *
     * @param client_id
     * @param refresh_token
     * @return
     */
    Result checkRefreshToken(String client_id, String refresh_token) {
        Result result = new Result();
        if (!oAuth2Service.checkRefreshToken(client_id, refresh_token)) {
            result.setError(ErrorCode.INVALID_ACCESS_TOKEN);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 查看是否已经已有 Access Token
     *
     * @param client_id
     * @return
     */
    Result checkExistAccessToekn(String client_id) {
        Result result = new Result();
        String accessToken = oAuth2Service.getAccessTokenByClientId(client_id);
        if (!StringUtils.isEmpty(accessToken)) {
            result.setSuccess(true);
            result.put("access_token", accessToken);
            result.put("expires_in", oAuth2Service.getExpireIn(accessToken));
            return result;
        }
        return result;
    }

    /**
     * 查看是否已经已有 authorizationCode
     *
     * @param client_id
     * @param redirect_uri
     * @param state
     * @return
     */
    Result checkExistCode(String client_id, String redirect_uri, String state) {
        Result result = new Result();
        String authorizationCode = oAuth2Service.getAuthCodeByClientId(client_id);
        if (!StringUtils.isEmpty(authorizationCode)) {
            result.setSuccess(true);
            result.put("redirectURI", redirect_uri + "?code=" + authorizationCode + "&state=" + state);
            return result;
        }
        return result;
    }

    /**
     * 当 scope 不等于默认值时检查认证权限
     *
     * @param client_id
     * @param scope
     * @return
     */
    Result checkScope(String client_id, String scope) {
        Result result = new Result();
        if (!oAuth2Service.checkScope(client_id, scope)) {
            result.setError(ErrorCode.APP_PERMISSIONS_FAIL);
            return result;
        }
        result.setSuccess(true);
        return result;
    }

}
