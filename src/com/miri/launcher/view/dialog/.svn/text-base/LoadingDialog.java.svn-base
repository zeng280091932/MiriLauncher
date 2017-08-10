/*
 * 文件名：LoadingDialog.java
 * 版权：Copyright 2009-2012 KOOLSEE MediaNet. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.miri.launcher.R;
import com.miri.launcher.utils.ResourceHelper;

/**
 * 加载进度对话框
 * @author zengjiantao
 * @version HDMNV100R001, 2012-3-30
 */
public class LoadingDialog extends Dialog {

    private static final String TAG = "LoadingDialog";

    private final LinearLayout mLinearLayout;

    private final Context mContext;

    public LoadingDialog(Context context) {
        super(context, R.style.Theme_CustomDialog_NoBackground_NoDim);
        mContext = context;
        ResourceHelper.updateConfig(mContext);
        setContentView(R.layout.fullscreen_loading);
        mLinearLayout = (LinearLayout) findViewById(R.id.fullscreen_loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        if (mContext == null || !(mContext instanceof Activity)) {
            Log.e(TAG, "This context is not a activity!");
            return;
        }
        if (((Activity) mContext).isFinishing()) {
            Log.e(TAG, "This activity is finishing!");
            return;
        }
        mLinearLayout.setVisibility(View.VISIBLE);
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLinearLayout.setVisibility(View.GONE);
        mLinearLayout.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        //阻止返回事件
        return;
    }
}
