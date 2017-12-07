package com.ddgs.service;

import com.ddgs.dao.OAuth2InterceptorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/29.下午5:22
 */
@Repository
public class OAuth2InterceptorService {

    @Autowired
    OAuth2InterceptorDao oAuth2InterceptorDao;

    //验证权限
    public boolean checkURL(String accessToken, String url) {
        return false;
    }
}
