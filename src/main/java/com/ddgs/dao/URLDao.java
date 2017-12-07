package com.ddgs.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/30.上午11:50
 */
public interface URLDao {

    List<String> getURLs(@Param("client_id") String client_id);

}
