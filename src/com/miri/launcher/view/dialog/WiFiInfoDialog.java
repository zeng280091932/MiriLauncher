/* 
 * 文件名：WiFiConnectDialog.java
 * 版权：Copyright
 */
package com.miri.launcher.view.dialog;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Summary;
import com.miri.launcher.utils.WifiConnect;
import com.miri.launcher.utils.WifiConnect.WifiCipherType;
import com.miri.launcher.view.CustomBtnView;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-8
 */
public class WiFiInfoDialog extends CommonDialog implements OnClickListener {

    private Context mContext;

    private CustomBtnView mOkBtn;

    private CustomBtnView mCancelBtn;

    private TextView mTitleTextView;

    private TextView mWifiStateText;

    private TextView mWifiLevelText;

    private TextView mWifiMacText;

    private TextView mWifiIpText;

    private TextView mWifiLinkText;

    private TextView mWifiFreText;

    private ScanResult mScanResult;

    private WifiManager mWifiManager;

    private WifiConnect mWifiConnect;

    private WifiInfo mWifiInfo;

    private OnDialogCloseListener mOnDialogCloseListener;

    private OnOkBtnClickListener mOnOkBtnClickListener;

    private OnCancelBtnClickListener mOnCancelBtnClickListener;

    public WiFiInfoDialog(Context context) {
        this(context, R.style.Theme_CustomDialog);
    }

    public WiFiInfoDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wifi_info_dialog);
        //        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mTitleTextView = (TextView) findViewById(R.id.title);
        mWifiStateText = (TextView) findViewById(R.id.wifi_state);
        mWifiMacText = (TextView) findViewById(R.id.wifi_mac);
        mWifiIpText = (TextView) findViewById(R.id.wifi_ip);
        mWifiLevelText = (TextView) findViewById(R.id.wifi_level);
        mWifiLinkText = (TextView) findViewById(R.id.wifi_linkspeed);
        mWifiFreText = (TextView) findViewById(R.id.wifi_frequency);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiConnect = new WifiConnect(mWifiManager);
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

        if (mWifiInfo != null) {
            mTitleTextView.setText(Summary.removeDoubleQuotes(mWifiInfo.getSSID()));
            SupplicantState state = mWifiInfo.getSupplicantState();
            //按钮
            if (state == SupplicantState.COMPLETED) {
                mOkBtn.setText("断开");
            } else if (state == SupplicantState.DISCONNECTED) {
                mOkBtn.setText("连接");
            } else {
                mOkBtn.setText("断开");
            }
            setStateInfo(state);
            setWifiSignal(mWifiInfo);
            setWifiMac(mWifiInfo);
            setWifiIP(mWifiInfo);
            setWifiLink(mWifiInfo);
        }

        if (mScanResult != null) {
            //            setWifiSignal(mScanResult);
            setWifiSecurity(mScanResult);
        }

    }

    private void setStateInfo(SupplicantState state) {
        //状态消息
        String wifiState = Summary.getWifiState(mContext, state);
        String format = mContext.getString(R.string.wifi_status_info);
        mWifiStateText.setText(String.format(format, wifiState));
    }

    private void setWifiSignal(WifiInfo wifiInfo) {
        int level = NetworkUtil.getRssiLevel(wifiInfo.getRssi());
        String wifiLevel = Summary.getWifiLevel(mContext, level);
        String format = mContext.getString(R.string.wifi_signal_level);
        mWifiLevelText.setText(String.format(format, wifiLevel));
    }

    private void setWifiSignal(ScanResult scanResult) {
        int level = NetworkUtil.getRssiLevel(scanResult.level);
        String wifiLevel = Summary.getWifiLevel(mContext, level);
        String format = mContext.getString(R.string.wifi_signal_level);
        mWifiLevelText.setText(String.format(format, wifiLevel));
    }

    private void setWifiMac(WifiInfo wifiInfo) {
        String mac = wifiInfo.getMacAddress().toUpperCase();
        String format = mContext.getString(R.string.wifi_mac);
        mWifiMacText.setText(String.format(format, mac));
    }

    private void setWifiIP(WifiInfo wifiInfo) {
        String ip = NetworkUtil.int2Ip(wifiInfo.getIpAddress());
        String format = mContext.getString(R.string.wifi_ip);
        mWifiIpText.setText(String.format(format, ip));
    }

    private void setWifiLink(WifiInfo wifiInfo) {
        String linkSpeed = wifiInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS;
        String format = mContext.getString(R.string.wifi_link_speed);
        mWifiLinkText.setText(String.format(format, linkSpeed));
    }

    private void setWifiSecurity(ScanResult scanResult) {
        WifiCipherType type = WifiConnect.getSecurity(scanResult);
        String security = Summary.getWifiSecurity(mContext, type);
        String format = mContext.getString(R.string.wifi_security);
        mWifiFreText.setText(String.format(format, security));
    }

    public void setScanResult(ScanResult scanResult) {
        this.mScanResult = scanResult;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        mWifiInfo = wifiInfo;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okBtn) {
            if (mWifiInfo != null) {
                SupplicantState state = mWifiInfo.getSupplicantState();
                if (state == SupplicantState.DISCONNECTED) {
                    String ssid = Summary.removeDoubleQuotes(mWifiInfo.getSSID());
                    WifiConfiguration config = mWifiConnect.IsExsits(ssid);
                    if (config != null) {
                        mWifiConnect.connectWifiByConfig(config);
                    }
                } else {
                    mWifiConnect.disableNetwork(mWifiInfo.getNetworkId());
                }
                mWifiManager.saveConfiguration();
            }
            if (mOnOkBtnClickListener != null) {
                mOnOkBtnClickListener.onClick();
            }
            dismiss();
        } else if (v.getId() == R.id.cancelBtn) {
            if (mWifiInfo != null) {
                String ssid = Summary.removeDoubleQuotes(mWifiInfo.getSSID());
                WifiConfiguration config = mWifiConnect.IsExsits(ssid);
                if (config != null) {
                    mWifiConnect.removeNetwork(config.networkId);
                }
                mWifiManager.saveConfiguration();
            }
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
