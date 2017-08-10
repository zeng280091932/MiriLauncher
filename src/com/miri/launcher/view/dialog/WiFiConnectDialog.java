/* 
 * 文件名：WiFiConnectDialog.java
 * 版权：Copyright
 */
package com.miri.launcher.view.dialog;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Summary;
import com.miri.launcher.utils.WifiConnect;
import com.miri.launcher.utils.WifiConnect.WifiCipherType;
import com.miri.launcher.view.CustomBtnView;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-8
 */
public class WiFiConnectDialog extends CommonDialog implements OnClickListener {

    private Context mContext;

    private CustomBtnView mOkBtn;

    private CustomBtnView mDisableBtn;

    private CustomBtnView mCancelBtn;

    private TextView mTitleTextView;

    private EditText mPassWordText;

    private ScanResult mScanResult;

    private WifiConfiguration mWifiConfig;

    private OnDialogCloseListener mOnDialogCloseListener;

    private OnOkBtnClickListener mOnOkBtnClickListener;

    private OnCancelBtnClickListener mOnCancelBtnClickListener;

    private WifiManager mWifiManager;

    private WifiConnect mWifiConnect;

    public WiFiConnectDialog(Context context) {
        this(context, R.style.Theme_CustomDialog);
    }

    public WiFiConnectDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wifi_connection_dialog);
        //        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mPassWordText = (EditText) findViewById(R.id.password);
        mOkBtn = (CustomBtnView) findViewById(R.id.okBtn);
        mDisableBtn = (CustomBtnView) findViewById(R.id.disableBtn);
        mCancelBtn = (CustomBtnView) findViewById(R.id.cancelBtn);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiConnect = new WifiConnect(mWifiManager);
        mOkBtn.setOnClickListener(this);
        mDisableBtn.setOnClickListener(this);
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

        if (mScanResult != null) {
            mTitleTextView.setText(mScanResult.SSID);
            mWifiConfig = mWifiConnect.IsExsits(mScanResult.SSID);
            if (mWifiConfig != null) {
                Logger.getLogger().d("WifiConfig :" + mWifiConfig);
                //                if (mWifiConfig.preSharedKey != null) {
                mPassWordText.setText("********");
                mPassWordText.setSelection(mPassWordText.getText().length());
                //                }
                mDisableBtn.setVisibility(View.VISIBLE);
            } else {
                mDisableBtn.setVisibility(View.GONE);
            }
            mPassWordText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (mWifiConfig != null) {
                        mWifiConfig = null;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

    }

    public String getPassWord() {
        if (mPassWordText != null) {
            return mPassWordText.getText().toString();
        }
        return null;
    }

    public void setScanResult(ScanResult scanResult) {
        this.mScanResult = scanResult;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okBtn) {
            if (mWifiConfig != null) {
                mWifiConnect.connectWifiByConfig(mWifiConfig);
            } else {
                String password = getPassWord();
                WifiCipherType type = WifiConnect.getSecurity(mScanResult);
                String ssid = Summary.removeDoubleQuotes(mScanResult.SSID);
                mWifiConnect.connect(ssid, password, type);
                mWifiManager.saveConfiguration();
            }
            if (mOnOkBtnClickListener != null) {
                mOnOkBtnClickListener.onClick();
            }
            dismiss();
        } else if (v.getId() == R.id.disableBtn) {
            if (mWifiConfig != null) {
                mWifiConnect.removeNetwork(mWifiConfig.networkId);
            }
            mWifiManager.saveConfiguration();
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
