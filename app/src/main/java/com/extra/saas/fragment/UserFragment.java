package com.extra.saas.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.extra.retrofit.HttpBuilder;
import com.extra.retrofit.HttpUtil;
import com.extra.saas.App;
import com.extra.saas.AppUrl;
import com.extra.saas.LoginActivity;
import com.extra.saas.R;
import com.extra.saas.VideoActivity;
import com.extra.saas.VideoShowOnItemClickListener;
import com.extra.saas.adapter.FavoriteAdapter;
import com.extra.saas.adapter.VideoShowRecycleAdapter;
import com.extra.saas.model.Member;
import com.extra.saas.model.VideoShowBean;
import com.extra.saas.result.Result;
import com.extra.saas.result.ResultMember;
import com.extra.saas.result.ResultVideoInfo;
import com.extra.saas.result.ResultVideoShow;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.AppManager;
import com.extra.utils.DataUtils;
import com.extra.utils.SPUtils;
import com.extra.utils.ToastUtils;
import com.extra.view.dialog.DialogSureCancel;
import com.player.util.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.extra.utils.Utils.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment implements VideoShowOnItemClickListener {


    @BindView(R.id.ib_logo)
    ImageView ibLogo;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.expire_time)
    TextView expireTime;
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.to_login)
    TextView toLogin;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    Unbinder unbinder;

    private Member member;
    public  String voucher;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        voucher = SPUtils.getString(getContext(), AppUrl.Voucher);
        if (DataUtils.isNullString(voucher))
        {
            llUser.setVisibility(View.GONE);
            toLogin.setVisibility(View.VISIBLE);
        }else {

            showProgressDialog(R.string.loading);
            new HttpBuilder(AppUrl.member)
                    .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                    .success( s ->{
                        L.d(s);
                        cancleProgressDialog();
                        try {
                            ResultMember result = (ResultMember) JsonUtil.stringToObject(s,ResultMember.class);
                            if (result.getCode()==1){
                                toLogin.setVisibility(View.GONE);
                                llUser.setVisibility(View.VISIBLE);
                                member = result.getData();
                                name.setText(member.getUser_nicename());
                                expireTime.setText(member.getExpire_time());


                            }else {
                                showDialogToast(result.getMsg());
                            }

                        }catch (Exception e){
                            Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                            showDialogToast(result.getMsg());
                        }


                    })
                    .error( e ->{
                        cancleProgressDialog();
                        showDialogToast(getResources().getString(R.string.check_net));
                    })
                    .get();

            new HttpBuilder(AppUrl.freeZone)
                    .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                    .tag(this)
                    .success( s ->{
                        try {
                            ResultVideoShow result = (ResultVideoShow) JsonUtil.stringToObject(s,ResultVideoShow.class);
                            if (result.getCode()==1){
                                FavoriteAdapter adapter = new FavoriteAdapter(getContext(),result.getData(),this);
                                lvList.setAdapter(adapter);

                            }else {
                                showDialogToast(result.getMsg());
                            }
                        }catch (Exception e){
                            Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                            showDialogToast(result.getMsg());
                        }

                    })
                    .error( e ->{
                        showDialogToast(getResources().getString(R.string.check_net));
                    })
                    .get();



        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_exit, R.id.to_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exit:
                SPUtils.putString(getContext(),AppUrl.Voucher,"");
                AppManager.getAppManager().finishAllActivityAndExit(getContext());
                break;
            case R.id.to_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }

    @Override
    public void OnItemClick(VideoShowBean videoShowBean) {
        showProgressDialog(R.string.loading);
        new HttpBuilder(AppUrl.movies)
                .header("VOUCHER", SPUtils.getContent(getContext(),AppUrl.Voucher))
                .params("id", videoShowBean.getId())
                .success( s ->{
                    L.d(s);
                    cancleProgressDialog();
                    try {
                        ResultVideoInfo result = (ResultVideoInfo) JsonUtil.stringToObject(s,ResultVideoInfo.class);
                        if (result.getCode()==1){
                            Intent intent = new Intent(getActivity(), VideoActivity.class);
                            intent.putExtra("VIDEO",result.getData());
                            startActivity(intent);

                        }else {
                            showDialogToast(result.getMsg());
                        }
                    }catch (Exception e){
                        Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                        showDialogToast(result.getMsg());
                    }


                })
                .error( e ->{
                    cancleProgressDialog();
                    showDialogToast(getResources().getString(R.string.check_net));
                })
                .post();
    }

    @Override
    public void OnClickLike(VideoShowBean videoShowBean) {

    }

    private DialogSureCancel dialogSureCancel;
    @Override
    public void OnLongClick(VideoShowBean videoShowBean) {
        if (dialogSureCancel==null)
            dialogSureCancel = new DialogSureCancel(getContext());

        dialogSureCancel.setTitle("提示");
        dialogSureCancel.getTvContent().setText("是否删除此视频！！");


        dialogSureCancel.setSureListener( v ->{
            dialogSureCancel.cancel();
            showProgressDialog(R.string.loading);
                new HttpBuilder(AppUrl.delFavorites)
                        .header("VOUCHER", SPUtils.getContent(getContext(), AppUrl.Voucher))
                        .params("object_id", videoShowBean.getId())
                        .success(s -> {
                            L.d(s);
                            cancleProgressDialog();
                            Result result = (Result) JsonUtil.stringToObject(s, Result.class);
                            ToastUtils.success(result.getMsg());
                            if (result.getCode() == 1) {
                                showDialogToast(result.getMsg());
                            }


                        })
                        .error(e -> {
                            cancleProgressDialog();
                            showDialogToast(getResources().getString(R.string.check_net));
                        })
                        .post();
        });

        dialogSureCancel.setCancelListener( v ->{
            dialogSureCancel.cancel();

        });

        dialogSureCancel.show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(this);
    }
}
