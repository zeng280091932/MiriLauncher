package com.miri.launcher.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.view.CustomBtnView;

public class AlertDialog extends CommonDialog implements OnClickListener {

    public static final boolean OPT_YES = true;

    public static final boolean OPT_NO = false;

    private CustomBtnView mOkBtn;

    private CustomBtnView mCancelBtn;

    private TextView mTitleTextView;

    private TextView mMsgTextView;

    private String mTitle;

    private String mMessage;

    private String mOkText;

    private String mCancelText;

    private boolean mShowOkBtn = OPT_YES;

    private boolean mShowCancelBtn = OPT_YES;

    private Context mContext;

    private OnDialogCloseListener mOnDialogCloseListener;

    private OnOkBtnClickListener mOnOkBtnClickListener;

    private OnCancelBtnClickListener mOnCancelBtnClickListener;

    public AlertDialog(Context context) {
        this(context, R.style.Theme_CustomDialog);
    }

    public AlertDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.alert_dialog);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mMsgTextView = (TextView) findViewById(R.id.msg);
        mOkBtn = (CustomBtnView) findViewById(R.id.okBtn);
        mCancelBtn = (CustomBtnView) findViewById(R.id.cancelBtn);
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Dialog#show()
     */
    @Override
    public void show() {
        //        if (mContext == null || !(mContext instanceof Activity)) {
        //            Logger.getLogger().e("This context is not a activity!");
        //            return;
        //        }
        //        if ((mContext instanceof Activity) && ((Activity) mContext).isFinishing()) {
        //            Logger.getLogger().e("This activity is finishing!");
        //            return;
        //        }
        super.show();
        initView();
    }

    private void initView() {
        if (mTitle != null) {
            mTitleTextView.setText(mTitle);
        }
        if (mMessage != null) {
            mMsgTextView.setText(mMessage);
        }
        if (mShowOkBtn) {
            mOkBtn.setVisibility(View.VISIBLE);
            mOkBtn.requestFocus();
        } else {
            mOkBtn.setVisibility(View.GONE);
        }
        if (mOkText != null) {
            mOkBtn.setText(mOkText);
        }
        if (mShowCancelBtn) {
            mCancelBtn.setVisibility(View.VISIBLE);
        } else {
            mCancelBtn.setVisibility(View.GONE);
        }
        if (mCancelText != null) {
            mCancelBtn.setText(mCancelText);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okBtn) {
            if (mOnOkBtnClickListener != null) {
                mOnOkBtnClickListener.onClick();
            }
            dismiss();
        } else if (v.getId() == R.id.cancelBtn) {
            if (mOnCancelBtnClickListener != null) {
                mOnCancelBtnClickListener.onClick();
            }
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnDialogCloseListener != null) {
            mOnDialogCloseListener.onClose();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mOnDialogCloseListener != null) {
            mOnDialogCloseListener.onClose();
        }
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setOnDialogCloseListener(OnDialogCloseListener onDialogCloseListener) {
        if (onDialogCloseListener != null) {
            mOnDialogCloseListener = onDialogCloseListener;
        }
    }

    public void setOnOkBtnClickListener(OnOkBtnClickListener onOkBtnClickListener) {
        if (onOkBtnClickListener != null) {
            mOnOkBtnClickListener = onOkBtnClickListener;
        }
    }

    public void setOnCancelBtnClickListener(OnCancelBtnClickListener onCancelBtnClickListener) {
        if (onCancelBtnClickListener != null) {
            mOnCancelBtnClickListener = onCancelBtnClickListener;
        }
    }

    public void showCancelBtn(boolean isShow) {
        mShowCancelBtn = isShow;
    }

    public void addCancelBtn(String text) {
        showCancelBtn(OPT_YES);
        mCancelText = text;
    }

    public void addCancelBtn(String text, OnCancelBtnClickListener onCancelBtnClickListener) {
        this.addCancelBtn(text);
        setOnCancelBtnClickListener(onCancelBtnClickListener);
    }

    public void showOkBtn(boolean isShow) {
        mShowOkBtn = isShow;
    }

    public void addOkBtn(String text) {
        showOkBtn(OPT_YES);
        mOkText = text;
    }

    public void addOkBtn(String text, OnOkBtnClickListener onOkBtnClickListener) {
        addOkBtn(text);
        setOnOkBtnClickListener(onOkBtnClickListener);
    }

    public interface OnOkBtnClickListener {

        public void onClick();
    }

    public interface OnCancelBtnClickListener {

        public void onClick();
    }

    public interface OnDialogCloseListener {

        public void onClose();
    }

}
