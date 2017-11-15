package com.extra.saas.result;

import com.extra.saas.model.VideoShowBean;

import java.util.List;

/**
 * Created by Extra on 2017/11/15.
 */

public class ResultVideoShow extends Result {
    List<VideoShowBean>  data;

    public List<VideoShowBean> getData() {
        return data;
    }

    public void setData(List<VideoShowBean> data) {
        this.data = data;
    }
}
