package com.ddgs.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/30.上午11:50
 */
public interface OAuth2Dao {
    
    boolean checkClientId(@Param("client_id") String client_id);

    boolean checkClientSecret(@Param("client_secret") String client_secret);

    String getAIdByClientId(@Param("client_id") String client_id);
}
