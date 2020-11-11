package com.example.finalproject.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.finalproject.R;

public class LoadingDialog {
    private static Dialog loadingDialog;

    public static void showLoadingDialog(Context context) {
        loadingDialog = new Dialog(context, R.style.LoadingDialog);

        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.show();
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.findViewById(R.id.loading_icon).startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate360));
    }

    public static void dismissDialog(){
        loadingDialog.dismiss();
    }
}
