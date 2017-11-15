package com.extra.saas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.extra.retrofit.HttpBuilder;
import com.extra.saas.result.Result;
import com.extra.saas.result.ResultLogin;
import com.extra.saas.util.JsonUtil;
import com.extra.utils.AppManager;
import com.extra.utils.DataUtils;
import com.extra.utils.SPUtils;
import com.extra.utils.StatusBarUtil;
import com.extra.utils.ToastUtils;
import com.player.util.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.extra.utils.DataUtils.isEmpty;
import static com.extra.utils.RegexUtils.isEmail;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView ivCleanPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.clean_password)
    ImageView cleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView ivShowPwd;
    @BindView(R.id.et_password2)
    EditText etPassword2;
    @BindView(R.id.clean_password2)
    ImageView cleanPassword2;
    @BindView(R.id.iv_show_pwd2)
    ImageView ivShowPwd2;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    private String email;
    private String password;
    private String repassword;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);

        toolbar.setTitle(R.string.register);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivCleanPhone.getVisibility() == View.GONE) {
                    ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivCleanPhone.setVisibility(View.GONE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && cleanPassword.getVisibility() == View.GONE) {
                    cleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    cleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    ToastUtils.error("Please enter a number or letter");
                    s.delete(temp.length() - 1, temp.length());
                    etPassword.setSelection(s.length());
                }
            }
        });
        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && cleanPassword2.getVisibility() == View.GONE) {
                    cleanPassword2.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    cleanPassword2.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    ToastUtils.error("Please enter a number or letter");
                    s.delete(temp.length() - 1, temp.length());
                    etPassword2.setSelection(s.length());
                }
            }
        });
    }

    @OnClick({R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd, R.id.clean_password2, R.id.iv_show_pwd2, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                etMobile.setText("");
                break;
            case R.id.clean_password:
                etPassword.setText("");
                break;
            case R.id.iv_show_pwd:
                if (etPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowPwd.setImageResource(R.mipmap.pass_visuable);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowPwd.setImageResource(R.mipmap.pass_gone);
                }
                String pwd = etPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etPassword.setSelection(pwd.length());
                break;
            case R.id.clean_password2:
                etPassword2.setText("");
                break;
            case R.id.iv_show_pwd2:
                if (etPassword2.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowPwd2.setImageResource(R.mipmap.pass_visuable);
                } else {
                    etPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowPwd2.setImageResource(R.mipmap.pass_gone);
                }
                String pwd2 = etPassword2.getText().toString();
                if (!TextUtils.isEmpty(pwd2))
                    etPassword2.setSelection(pwd2.length());
                break;
            case R.id.btn_register:

                email = etMobile.getText().toString();
                password = etPassword.getText().toString();
                repassword = etPassword2.getText().toString();

                if (isEmpty(email)&&isEmpty(email)&&isEmpty(email)){
                    showDialogToast(getResources().getString(R.string.please_input));
                    return;
                }

                if (!isEmail(email)){
                    showDialogToast(getResources().getString(R.string.please_email));
                    return;
                }

                if (!repassword.equals(password)){
                    showDialogToast(getResources().getString(R.string.please_re_pass));
                    return;
                }

                register();

                break;
        }
    }

    private void register() {

        showProgressDialog(R.string.loading);
        new HttpBuilder(AppUrl.register)
                .params("email",email)
                .params("password",password)
                .params("repassword",repassword)
                .tag(this)
                .success(s ->{
                    cancleProgressDialog();
                    L.d(s);
                    try {
                        ResultLogin result = (ResultLogin) JsonUtil.stringToObject(s,ResultLogin.class);
                        if (result.getCode()==1){
                            SPUtils.putString(this,AppUrl.Voucher,result.getData().getVoucher());
                            //跳转主页
                            ToastUtils.showToast(result.getMsg());
                            AppManager.getAppManager().finishAllActivityAndExit(this);
                            Intent intent =new Intent(this,MainActivity.class);
                            intent.putExtra("login",1);
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
}
