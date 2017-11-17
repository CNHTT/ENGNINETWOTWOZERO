package com.extra.saas;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.extra.saas.fragment.FreeZoneFragment;
import com.extra.saas.fragment.UserFragment;
import com.extra.saas.fragment.VipZoneFragment;
import com.extra.utils.DataUtils;
import com.extra.utils.SPUtils;
import com.extra.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.txt_Free_Zone)
    TextView txtFreeZone;
    @BindView(R.id.txt_Vip_Zone)
    TextView txtVipZone;
    @BindView(R.id.txt_user)
    TextView txtUser;
    @BindView(R.id.tab_menu)
    LinearLayout tabMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FreeZoneFragment freeZoneFragment;
    private VipZoneFragment vipZoneFragment;
    private UserFragment userFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private int login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        initView();

    }

    private void initView() {
         transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        try {
           login=  getIntent().getExtras().getInt("login");
           if (login==1){
               toolbarTitle.setText(getResources().getString(R.string.personal));
               txtUser.setTextSize(14);
               txtUser.setSelected(true);
               if (userFragment==null){
                   userFragment = new UserFragment();
                   transaction.add(R.id.fragment_container,userFragment);
               }else {
                   transaction.show(userFragment);
               }

           }else {

               toolbarTitle.setText(getResources().getString(R.string.free_zone));
               txtFreeZone.setSelected(true);
               txtFreeZone.setTextSize(14);
               if (freeZoneFragment==null){
                   freeZoneFragment = new FreeZoneFragment();
                   transaction.add(R.id.fragment_container,freeZoneFragment);
               }else {
                   transaction.show(freeZoneFragment);
               }

           }
        }catch (Exception e){

            toolbarTitle.setText(getResources().getString(R.string.free_zone));
            txtFreeZone.setSelected(true);
            txtFreeZone.setTextSize(14);
            if (freeZoneFragment==null){

                freeZoneFragment = new FreeZoneFragment();
                transaction.add(R.id.fragment_container,freeZoneFragment);
            }else {
                transaction.show(freeZoneFragment);
            }
        }
        transaction.commit();

    }

    @OnClick({ R.id.txt_Free_Zone, R.id.txt_Vip_Zone, R.id.txt_user})
    public void onClick(View view) {

        transaction = getSupportFragmentManager().beginTransaction();
        clearSelect();
        hideAllFragment(transaction);
        switch (view.getId()) {
            case R.id.txt_Free_Zone:
                toolbarTitle.setText(getResources().getString(R.string.free_zone));
                txtFreeZone.setTextSize(14);
                txtFreeZone.setSelected(true);
                if (freeZoneFragment==null){
                    freeZoneFragment = new FreeZoneFragment();
                    transaction.add(R.id.fragment_container,freeZoneFragment);
                }else {
                    transaction.show(freeZoneFragment);
                }
                break;
            case R.id.txt_Vip_Zone:

                toolbarTitle.setText(getResources().getString(R.string.vip_zone));
                txtVipZone.setTextSize(14);
                txtVipZone.setSelected(true);
                if (vipZoneFragment==null){
                    vipZoneFragment = new VipZoneFragment();
                    transaction.add(R.id.fragment_container,vipZoneFragment);
                }else {
                    transaction.show(vipZoneFragment);
                }
                break;
            case R.id.txt_user:
                if (DataUtils.isNullString(SPUtils.getString(this,AppUrl.Voucher)))
                {
                    startActivity(new Intent(this,LoginActivity.class));
                    return;
                }
                toolbarTitle.setText(getResources().getString(R.string.personal));
                txtUser.setTextSize(14);
                txtUser.setSelected(true);
                if (userFragment==null){
                    userFragment = new UserFragment();
                    transaction.add(R.id.fragment_container,userFragment);
                }else {
                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void clearSelect(){
        txtFreeZone.setSelected(false);
        txtFreeZone.setTextSize(13);
        txtVipZone.setSelected(false);
        txtVipZone.setTextSize(13);
        txtUser.setSelected(false);
        txtUser.setTextSize(13);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(freeZoneFragment!=null){
            transaction.hide(freeZoneFragment);
        }
        if(vipZoneFragment!=null){
            transaction.hide(vipZoneFragment);
        }
        if(userFragment!=null){
            transaction.hide(userFragment);
        }
    }

}
