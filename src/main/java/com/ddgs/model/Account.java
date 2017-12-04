package com.ddgs.model;

import java.util.List;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/23.下午3:16
 */
public class Account {


    private int id;
    private String phone;
    private String password;


    private String clientId;
    private String secret;

    private String scope;
    private String authorized_grant_types;
    private String redirect_uri;


    private List<Role> roles;


}
