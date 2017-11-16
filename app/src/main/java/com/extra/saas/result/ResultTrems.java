package com.extra.saas.result;

import com.extra.saas.model.TermsBean;

import java.util.List;

/**
 * Created by 戴尔 on 2017/11/16.
 */

public class ResultTrems extends Result {
    List<TermsBean> data;


    public List<TermsBean> getData() {
        return data;
    }

    public void setData(List<TermsBean> data) {
        this.data = data;
    }
}
