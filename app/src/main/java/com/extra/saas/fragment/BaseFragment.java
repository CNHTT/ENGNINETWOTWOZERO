package com.extra.saas.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.view.View;

import com.extra.view.dialog.DialogSure;

/**
 * Created by Extra on 2017/11/15.
 */

public class BaseFragment extends Fragment {

    protected DialogSure dialogSure;
    protected void showDialogToast(String toast) {
        if (dialogSure==null)dialogSure = new DialogSure(getContext());
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
            progressDialog = new ProgressDialog(getContext());
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
