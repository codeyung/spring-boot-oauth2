package com.ddgs;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/15.下午5:23
 */
public enum ErrorCode {
    ERROR(1, "错误"),
    SERVER_NOTFOUND(2, "服务器错误"),


    //帐号
    ADMIN_NOT_FOUND(3, "用户未找到"),
    PWD_ERROR(4, "密码错误"),

    //OAuth2
    INVALID_CLIENT_ID(5, "客户端验证失败，如错误的client_id/client_secret。"),
    INVALID_ACCESS_TOKEN(6, "accessToken无效或已过期。"),
    INVALID_REDIRECT_URI(7, "缺少授权成功后的回调地址。"),
    INVALID_AUTH_CODE(8, "错误的授权码。"),
    BAD_CONENT_TYPE(9, "Content-Type 错误"),
    INVALID_AUTH_USER(10, "错误的用户名/密码"),
    RESPONSETYPE_ERROR(11, "授权类型错误"),
    ACCESS_TOKEN_FAIL(12, "accessToken创建失败"),
    OAUT_CODE_FAIL(13, "授权码创建失败"),
    APP_PERMISSIONS_FAIL(14, "权限不足，范围错误");


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.valueOf(name());
    }
}
