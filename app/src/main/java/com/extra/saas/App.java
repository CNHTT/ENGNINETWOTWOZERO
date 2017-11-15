package com.extra.saas;

import android.app.Application;

import com.extra.retrofit.HttpUtil;
import com.extra.retrofit.interfaces.HeadersInterceptor;
import com.extra.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by 戴尔 on 2017/11/15.
 */

public class App extends Application {

    public static List<?> images=new ArrayList<>();
    public static List<String> titles=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        new HttpUtil.SingletonBuilder(this,AppUrl.localhost)
                .build();
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles= new ArrayList(list1);
    }
}
