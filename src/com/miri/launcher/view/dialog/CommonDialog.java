package com.miri.launcher.view.dialog;

import android.app.Dialog;
import android.content.Context;

import com.miri.launcher.utils.Logger;

public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        super(context);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            Logger.getLogger().e("show dialog error!");
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            Logger.getLogger().e("dismiss dialog error!");
            e.printStackTrace();
        } finally {

        }
    }

}
