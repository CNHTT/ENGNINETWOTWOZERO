package com.extra.saas.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.extra.retrofit.HttpBuilder;
import com.extra.saas.App;
import com.extra.saas.AppUrl;
import com.extra.saas.LoginActivity;
import com.extra.saas.R;
import com.extra.saas.VideoActivity;
import com.extra.saas.model.Member;
import com.extra.saas.result.Result;
import com.extra.saas.result.ResultMember;
import com.extra.saas.result.ResultVideoInfo;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.AppManager;
import com.extra.utils.DataUtils;
import com.extra.utils.SPUtils;
import com.player.util.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {


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
}
