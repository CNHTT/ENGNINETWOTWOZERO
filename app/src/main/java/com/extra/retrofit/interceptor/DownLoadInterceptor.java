package com.extra.retrofit.interceptor;


import com.extra.retrofit.HttpUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class DownLoadInterceptor implements Interceptor {
    String mBaseIp;

    public DownLoadInterceptor(String ip) {
        mBaseIp = ip;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (HttpUtil.checkNULL(request.header("DOWNLOAD"))) {//非文件下载
            return chain.proceed(request);
        }
        String url = request.url().toString();
        url.replace(mBaseIp, "");
        Request newRequest = request.newBuilder().url(url).build();
        return chain.proceed(newRequest);
    }
}