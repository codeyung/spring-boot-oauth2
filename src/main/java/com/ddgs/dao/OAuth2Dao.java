package com.ddgs.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/30.上午11:50
 */
public interface OAuth2Dao {

    boolean checkClientId(@Param("client_id") String client_id);

    boolean checkClientSecret(@Param("client_secret") String client_secret);

    boolean checkClient(@Param("client_id") String client_id, @Param("client_secret") String client_secret);

    boolean checkAccount(@Param("username") String username, @Param("password") String password);

    String getClientIdByName(@Param("username") String username);


    boolean checkScope(@Param("num") int num, @Param("client_id") String client_id, @Param("scopes") String[] scopes);
}
