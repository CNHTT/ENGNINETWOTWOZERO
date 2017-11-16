package com.extra.saas.model;

/**
 * Created by 戴尔 on 2017/11/16.
 */

public class Member {
    private String user_nicename;
    private String expire_time;

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }
}
