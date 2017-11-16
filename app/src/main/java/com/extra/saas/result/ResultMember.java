package com.extra.saas.result;

import com.extra.saas.model.Member;

/**
 * Created by 戴尔 on 2017/11/16.
 */

public class ResultMember extends Result {
    private Member data;

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }
}
