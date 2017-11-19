package com.extra.saas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class WelcomeAty extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    Disposable dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_welcome_aty);
        ButterKnife.bind(this);

        Glide.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511102993009&di=453775d011b9612599b9e7a7c98c5724&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201504%2F23%2F20150423H4547_NSyJn.thumb.700_0.jpeg").into(imageView);

        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        dis=d;
                    }

                    @Override
                    public void onNext(Long value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //完成时调用
                        toLogin();

                    }
                });
    }

    private void toLogin() {
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    /**
     * 取消订阅
     */
    public  void cancel(){
        if(dis!=null&&!dis.isDisposed()){
            dis.dispose();
        }
    }
}
