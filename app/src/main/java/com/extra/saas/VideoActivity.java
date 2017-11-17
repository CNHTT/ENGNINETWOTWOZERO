package com.extra.saas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devlin_n.floatWindowPermission.FloatWindowManager;
import com.devlin_n.videoplayer.controller.StandardVideoController;
import com.devlin_n.videoplayer.player.IjkVideoView;
import com.extra.retrofit.HttpBuilder;
import com.extra.saas.adapter.PhotoAdapter;
import com.extra.saas.model.SmetaBean;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.result.Result;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.DataUtils;
import com.extra.utils.EncodeUtils;
import com.extra.utils.SPUtils;
import com.extra.utils.StatusBarUtil;
import com.extra.utils.ToastUtils;
import com.extra.view.listview.InnerScrollerListView;
import com.player.util.L;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.extra.utils.Utils.getContext;

public class VideoActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_play)
    IjkVideoView ivPlay;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recommended)
    ImageView recommended;
    @BindView(R.id.tv_hits)
    TextView tvHits;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.favorites)
    ImageView favorites;
    @BindView(R.id.tv_content)
    WebView tvContent;
    @BindView(R.id.lv_photo)
    InnerScrollerListView lvPhoto;
    private String url;


    private boolean isFavorite = false;


    private VideoShowBean videoShowBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparent(this);
        videoShowBean = (VideoShowBean) getIntent().getSerializableExtra("VIDEO");
        toolbar.setTitle(videoShowBean.getPost_title());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

    }

    private void initView() {

        if (videoShowBean.getFavorites().equals("0")) {
            isFavorite = false;
            favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites_n));
        } else {
            isFavorite = true;
            favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites));
        }

        if (videoShowBean.getRecommended().equals("1")) {
            recommended.setImageDrawable(getResources().getDrawable(R.drawable.ic_recommended_y));
        }
        url = videoShowBean.getPost_source();
        url = url.substring(url.length() - 5, url.length()) + url.substring(5, url.length() - 5) + url.substring(0, 5);
        try {
            L.d(url);
            url = new String(EncodeUtils.base64Decode(url), "UTF-8");
            StandardVideoController controller = new StandardVideoController(this);
            SmetaBean smetaBean = (SmetaBean) JsonUtil.stringToObject(videoShowBean.getSmeta(), SmetaBean.class);
            PhotoAdapter photoAdapter = new PhotoAdapter(this,smetaBean.getPhoto());
            lvPhoto.setAdapter(photoAdapter);
            Glide.with(this)
                    .load(smetaBean.getThumb())
                    .into(controller.getThumb());
            ivPlay
                    .autoRotate() //启用重力感应自动进入/推出全屏功能
                    .setUrl(url) //设置视频地址
                    .setTitle(videoShowBean.getPost_title()) //设置视频标题
                    .setVideoController(controller)
                    .start(); //开始播放，不调用则不自动播放
            tvContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩放


            String p ="<p><img src=\"http://c.hiphotos.baidu.com/image/pic/item/a50f4bfbfbedab643102c011fd36afc378311eea.jpg\" title=\"timg.jpeg\" alt=\"timg.jpeg\"/></p>";
            p = p.replace("<img", "<img  width=\"100%\"");

            if (videoShowBean.getPost_content() != "")
              tvContent.loadDataWithBaseURL(null,p,"text/html" , "utf-8", null);
            tvLike.setText(videoShowBean.getPost_like());
            tvTitle.setText(videoShowBean.getPost_title());
            tvHits.setText(videoShowBean.getPost_hits());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        ivPlay.pause();
    }
    Html.ImageGetter imageGetter = new Html.ImageGetter() {

        public Drawable getDrawable(String source) {
            Drawable drawable=null;
            URL url;
            try {
                url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;};
    };
    @Override
    protected void onResume() {
        super.onResume();
        ivPlay.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ivPlay.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FloatWindowManager.PERMISSION_REQUEST_CODE) {
            if (FloatWindowManager.getInstance().checkPermission(this)) {
                ivPlay.startFloatWindow();
            } else {
                Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!ivPlay.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.tv_hits, R.id.tv_like, R.id.recommended, R.id.favorites})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hits:
                break;
            case R.id.tv_like:
                if (DataUtils.isNullString(SPUtils.getString(this,AppUrl.Voucher)))
                {
                    startActivity(new Intent(this,LoginActivity.class));
                    return;
                }
                showProgressDialog(R.string.loading);
                new HttpBuilder(AppUrl.post_like)
                        .header("VOUCHER", SPUtils.getContent(getContext(), AppUrl.Voucher))
                        .params("id", videoShowBean.getId())
                        .success(s -> {
                            L.d(s);
                            cancleProgressDialog();
                            Result result = (Result) JsonUtil.stringToObject(s, Result.class);
                            ToastUtils.success(result.getMsg());

                        })
                        .error(e -> {
                            cancleProgressDialog();
                            showDialogToast(getResources().getString(R.string.check_net));
                        })
                        .post();
                break;
            case R.id.recommended:


                break;
            case R.id.favorites:

                if (DataUtils.isNullString(SPUtils.getString(this,AppUrl.Voucher)))
                {
                    startActivity(new Intent(this,LoginActivity.class));
                    return;
                }
                showProgressDialog(R.string.loading);
                if (isFavorite)
                    new HttpBuilder(AppUrl.delFavorites)
                            .header("VOUCHER", SPUtils.getContent(getContext(), AppUrl.Voucher))
                            .params("object_id", videoShowBean.getId())
                            .success(s -> {
                                L.d(s);
                                cancleProgressDialog();
                                Result result = (Result) JsonUtil.stringToObject(s, Result.class);
                                ToastUtils.success(result.getMsg());
                                if (result.getCode() == 1) {
                                    isFavorite = false;
                                    favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites_n));
                                }


                            })
                            .error(e -> {
                                cancleProgressDialog();
                                showDialogToast(getResources().getString(R.string.check_net));
                            })
                            .post();
                else new HttpBuilder(AppUrl.favorites)
                        .header("VOUCHER", SPUtils.getContent(getContext(), AppUrl.Voucher))
                        .params("object_id", videoShowBean.getId())
                        .success(s -> {
                            L.d(s);
                            cancleProgressDialog();
                            Result result = (Result) JsonUtil.stringToObject(s, Result.class);
                            ToastUtils.success(result.getMsg());
                            if (result.getCode() == 1) {favorites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites));
                                isFavorite = true;
                            }


                        })
                        .error(e -> {
                            cancleProgressDialog();
                            showDialogToast(getResources().getString(R.string.check_net));
                        })
                        .post();


                break;
        }
    }
}
