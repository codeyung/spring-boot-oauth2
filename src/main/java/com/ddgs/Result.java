package com.ddgs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/15.下午5:23
 */
public class Result extends HashMap<Object, Object> {

    public Result() {
        super();
        this.setSuccess(false);
    }

    public Result(boolean success) {
        super();
        this.setSuccess(success);
    }

    public void setSuccess(boolean success) {
        this.put("success", success);
    }

    public void setError(ErrorCode code) {
        Map error = new HashMap();
        error.put("code", code.getCode());
        error.put("msg", code);
        error.put("message", code.getMessage());
        this.put("error", error);
    }

    public void setMsg(String msg) {
        this.put("msg", msg);
    }


}
