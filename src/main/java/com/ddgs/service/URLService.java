package com.ddgs.service;

import com.ddgs.dao.URLDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/29.下午5:22
 */
@Repository
public class URLService {

    @Autowired
    URLDao urlDao;

    @Autowired
    RedisService redisService;

    //验证权限
    public boolean checkURL(String accessToken, String url) {
        String client_id = redisService.get("access_token_" + accessToken);

        if (StringUtils.isEmpty(client_id)) {
            return false;
        }

        url = url.replaceAll("/[0-9][0-9]?", "/#");

        List<String> URLs = urlDao.getURLs(client_id);
        for (String str : URLs) {
            str = str.replaceAll("\\{[^{}]*}", "#");

            if (str.equals(url)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        String url_a = "GET:/product/1";
        String str_a = "GET:/product/{id}";

        String url_b = "GET:/product/1/ads/2";
        String str_b = "GET:/product/{id}/ads/{id}";


        url_a = url_a.replaceAll("/[0-9][0-9]?", "/#");
        str_a = str_a.replaceAll("\\{[^{}]*}", "#");

        url_b = url_b.replaceAll("/[0-9][0-9]?", "/#");
        str_b = str_b.replaceAll("\\{[^{}]*}", "#");
        System.out.println(url_a);
        System.out.println(str_a);
        System.out.println(url_b);
        System.out.println(str_b);

    }


}
