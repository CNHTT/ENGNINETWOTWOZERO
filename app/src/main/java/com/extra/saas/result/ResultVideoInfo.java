package com.extra.saas.result;

import com.extra.saas.model.VideoShowBean;

/**
 * Created by 戴尔 on 2017/11/16.
 */

public class ResultVideoInfo extends Result {
    VideoShowBean data;


    public VideoShowBean getData() {
        return data;
    }

    public void setData(VideoShowBean data) {
        this.data = data;
    }
}
