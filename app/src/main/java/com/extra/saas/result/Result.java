package com.extra.saas.result;

/**
 * Created by 戴尔 on 2017/11/15.
 */

public class Result {
    private  int status ;
    private  String msg;

    public int getCode() {
        return status;
    }

    public void setCode(int code) {
        this.status = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
