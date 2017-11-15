package com.extra.saas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.extra.utils.AndroidBug5497Workaround;
import com.extra.utils.AppManager;
import com.extra.view.dialog.DialogSure;
import com.player.util.L;

/**
 * Created by 戴尔 on 2017/11/15.
 */

public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        if (isFullScreen(this))
            AndroidBug5497Workaround.assistActivity(this);
        L.d("onCreate");

    }

    private boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  android.R.id.home:
                onBackPressed();
                // 处理返回逻辑
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected DialogSure dialogSure;
    protected void showDialogToast(String toast) {
        if (dialogSure==null)dialogSure = new DialogSure(this);
        dialogSure.setTitle("ERROR");
        dialogSure.getTvContent().setText(toast);
        dialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogSure.cancel();
            }
        });
        dialogSure.show();
    }

    private ProgressDialog progressDialog;
    protected void showProgressDialog(int resId) {
        if (progressDialog==null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(resId));
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    protected void cancleProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }
}
