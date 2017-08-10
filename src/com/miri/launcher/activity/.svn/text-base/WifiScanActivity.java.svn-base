/* 
 * 文件名：WifiScanActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.miri.launcher.R;
import com.miri.launcher.model.AccessPoint;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Summary;
import com.miri.launcher.utils.Summary.WifiState;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.utils.WifiConnect;
import com.miri.launcher.utils.WifiConnect.WifiCipherType;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.SwitchButton;
import com.miri.launcher.view.dialog.WiFiConnectDialog;
import com.miri.launcher.view.dialog.WiFiInfoDialog;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-30
 */
public class WifiScanActivity extends BaseActivity {

    private final int[] STATE_SECURED = {R.attr.state_encrypted};

    private final int[] STATE_NONE = {};

    private NetworkManagerActivity mParentActivity;

    private View mCurrTabView;

    private CheckBox mWifiSwitchButton;

    private TextView mWifiConnectMsg;

    private ListView mListView;

    private ScanResultListAdapter mListAdapter;

    private WifiManager mWifiManager;

    private WifiConnect mWifiConnect;

    private WifiNetWorkReceiver mWifiReceiver;

    private IntentFilter mWifiFilter;

    private int mCurrPosition = -1;

    private Scanner mScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_scan_result);
        mWifiSwitchButton = (SwitchButton) findViewById(R.id.wifi_switch);
        mWifiConnectMsg = (TextView) findViewById(R.id.wifi_msg);
        mListView = (ListView) findViewById(android.R.id.list);
        Toolkit.disableOverScrollMode(mListView);
        /** 获取当前选中的tab页 */
        mParentActivity = (NetworkManagerActivity) getParent();
        TabHost tabHost = (TabHost) mParentActivity.findViewById(R.id.tab_host);
        mCurrTabView = tabHost.getCurrentTabView();

        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiConnect = new WifiConnect(mWifiManager);
        mWifiReceiver = new WifiNetWorkReceiver();
        mWifiFilter = new IntentFilter();
        mWifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mWifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mWifiFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        mWifiFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        mWifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mWifiFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        mWifiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        //        mWifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mWifiFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        mScanner = new Scanner();

        //初始化wifi开关
        if (mWifiConnect.isWifiEnabled()) {
            mWifiSwitchButton.setChecked(true);
            mScanner.resume();
            mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.SCANNING));
        } else {
            mWifiSwitchButton.setChecked(false);
            mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.DISABLED));
        }
        bindListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiReceiver, mWifiFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiReceiver);
        mScanner.pause();
    }

    private void bindListener() {
        mWifiSwitchButton.setOnKeyListener(mOnKey);
        mCurrTabView.setOnKeyListener(mOnKey);
        mListView.setOnKeyListener(mOnKey);

        mWifiSwitchButton.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((View) mWifiSwitchButton.getParent())
                            .setBackgroundResource(R.drawable.frame_bg);
                } else {
                    ((View) mWifiSwitchButton.getParent()).setBackgroundDrawable(null);
                }
            }
        });

        mWifiSwitchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mWifiConnect.closeWifi();
                } else {
                    mWifiConnect.openWifi();
                }

            }
        });

        mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AccessPoint ap = (AccessPoint) mListAdapter.getItem(position);
                final ScanResult scanRes = ap.getScanResult();
                final WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                boolean isConnectedSSID = false;
                Logger.getLogger().d("ScanResult: " + scanRes);
                Logger.getLogger().d("wifiInfo: " + wifiInfo);
                if (wifiInfo != null) {
                    String ssid = Summary.removeDoubleQuotes(wifiInfo.getSSID());
                    if (scanRes.SSID.equals(ssid)) {
                        isConnectedSSID = true;
                    }
                }

                if (isConnectedSSID) {
                    WiFiInfoDialog dlg = new WiFiInfoDialog(WifiScanActivity.this);
                    dlg.setWifiInfo(wifiInfo);
                    dlg.setScanResult(scanRes);
                    dlg.setOnOkBtnClickListener(new WiFiInfoDialog.OnOkBtnClickListener() {

                        @Override
                        public void onClick() {
                            mScanner.resume();
                            addOrUpdateListView(mWifiConnect.getScanResult());
                        }
                    });
                    dlg.show();
                    return;
                }

                final WifiCipherType type = WifiConnect.getSecurity(scanRes);
                if (type == WifiCipherType.WIFICIPHER_NOPASS) {
                    Logger.getLogger().d("scanRes.capabilities : " + scanRes.capabilities);
                    mWifiConnect.connect(scanRes.SSID, "", type);
                } else {
                    final WiFiConnectDialog dlg = new WiFiConnectDialog(WifiScanActivity.this);
                    dlg.setScanResult(scanRes);
                    dlg.setOnOkBtnClickListener(new WiFiConnectDialog.OnOkBtnClickListener() {

                        @Override
                        public void onClick() {
                            addOrUpdateListView(mWifiConnect.getScanResult());
                        }
                    });
                    dlg.show();
                }
            }
        });

    }

    private void addOrUpdateListView(List<AccessPoint> result) {
        if (mListAdapter == null) {
            mListAdapter = new ScanResultListAdapter(this, result);
            mListView.setAdapter(mListAdapter);
        } else {
            mListAdapter.setData(result);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private void clearListView() {
        if (mListAdapter != null && mListAdapter.getCount() > 0) {
            mListAdapter.setData(Collections.EMPTY_LIST);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private class ScanResultListAdapter extends BaseAdapter {

        private Context mContext;

        private LayoutInflater mInflater;

        private List<AccessPoint> data;

        public ScanResultListAdapter(Context context, List<AccessPoint> list) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            data = list;
        }

        public void setData(List<AccessPoint> list) {
            data = list;
        }

        public class Entity {

            public RelativeLayout root;

            public TextView ssid;

            public TextView connectInfo;

            public ImageView wifiLevel;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Entity entity;
            if (convertView == null) {
                entity = new Entity();
                convertView = mInflater.inflate(R.layout.wifi_scan_result_item, null);
                entity.root = (RelativeLayout) convertView.findViewById(R.id.root);
                entity.wifiLevel = (ImageView) convertView.findViewById(R.id.wifi_level);
                entity.ssid = (TextView) convertView.findViewById(R.id.ssid);
                entity.connectInfo = (TextView) convertView.findViewById(R.id.connect_info);
                convertView.setTag(entity);
            } else {
                entity = (Entity) convertView.getTag();
            }

            AccessPoint ap = (AccessPoint) getItem(position);
            ScanResult result = ap.getScanResult();

            entity.ssid.setText(result.SSID);
            entity.ssid.setTag(result.SSID);
            WifiCipherType security = WifiConnect.getSecurity(result);
            setWifiSignal(entity.wifiLevel, result.level, security);

            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (wifiInfo != null
                    && result.SSID.equals(Summary.removeDoubleQuotes(wifiInfo.getSSID()))) {
                String state = Summary.getWifiState(mContext, wifiInfo.getSupplicantState());
                entity.connectInfo.setText(state);
            } else {
                String wifiSecurity = Summary.getWifiSecurityString(mContext, security);
                entity.connectInfo.setText(wifiSecurity);
            }

            return convertView;
        }

        @Override
        public int getCount() {
            if (Toolkit.isEmpty(data)) {
                return 0;
            }
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            if (Toolkit.isEmpty(data)) {
                return null;
            }
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    private View getConnectPosition(String ssid) {
        View posView = null;
        if (ssid != null && mListView != null) {
            int firstPos = mListView.getFirstVisiblePosition();
            int lastPos = mListView.getLastVisiblePosition();
            for (int i = firstPos; i <= lastPos; i++) {
                View child = mListView.getChildAt(i);
                if (child != null) {
                    String posSsid = (String) ((TextView) child.findViewById(R.id.ssid)).getTag();
                    if (posSsid != null && posSsid.equals(ssid)) {
                        posView = child;
                        break;
                    }
                }
            }
        }
        return posView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * wifi广播监听器
     * @author penglin
     * @version TVLAUNCHER001, 2013-10-7
     */
    class WifiNetWorkReceiver extends BroadcastReceiver {

        private long lastRefreshTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //            Logger.getLogger().d("onReceive:" + action);
            //监听wifi的打开与关闭，与wifi的连接无关 
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                Logger.getLogger().d("WIFI_STATE_CHANGED_ACTION wifi state : " + state);
                updateWifiState(state);
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    DetailedState state = networkInfo.getDetailedState();
                    Logger.getLogger().d("NETWORK_STATE_CHANGED_ACTION wifi state : " + state);
                    updateConnectionState(state);
                }
            } else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                SupplicantState supState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                DetailedState state = WifiInfo.getDetailedStateOf(supState);
                Logger.getLogger().d("SUPPLICANT_STATE_CHANGED_ACTION wifi state : " + state);
                updateConnectionState(state);
            } else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                SupplicantState supState = intent
                        .getParcelableExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
                DetailedState state = WifiInfo.getDetailedStateOf(supState);
                Logger.getLogger().d("SUPPLICANT_CONNECTION_CHANGE_ACTION wifi state : " + state);
                updateConnectionState(state);
            } else if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                mWifiConnectMsg.setText(Summary.getWifiState(context, WifiState.ENABLED));
                long currentTime;
                if ((currentTime = System.currentTimeMillis()) - lastRefreshTime > 10 * 1000) {
                    addOrUpdateListView(mWifiConnect.getScanResult());
                    lastRefreshTime = currentTime;
                }
            } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {

            } else if (action.equals(WifiManager.NETWORK_IDS_CHANGED_ACTION)) {
                mScanner.resume();
                addOrUpdateListView(mWifiConnect.getScanResult());
            }

        }
    }

    /**
     * 更新wifi状态
     * @param state
     */
    private void updateWifiState(int state) {
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLED:
                mWifiSwitchButton.setChecked(true);
                mWifiSwitchButton.setEnabled(true);
                mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.ENABLED));
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                mWifiSwitchButton.setChecked(false);
                mWifiSwitchButton.setEnabled(true);
                mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.DISABLED));
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                mWifiSwitchButton.setEnabled(false);
                mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.DISABLING));
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                mWifiSwitchButton.setEnabled(false);
                mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.ENABLING));
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                mWifiSwitchButton.setChecked(true);
                mWifiSwitchButton.setEnabled(true);
                mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.UNKNOWN));
                break;
            default:
                break;
        }

        if (state == WifiManager.WIFI_STATE_ENABLED) {
            mScanner.resume();
            addOrUpdateListView(mWifiConnect.getScanResult());
        } else {
            mScanner.pause();
            clearListView();
        }
    }

    private void updateConnectionState(DetailedState state) {
        /* sticky broadcasts can call this when wifi is disabled */
        if (!mWifiConnect.isWifiEnabled()) {
            mScanner.pause();
            return;
        }

        if (state == DetailedState.OBTAINING_IPADDR) {
            mScanner.pause();
        } else {
            mScanner.resume();
        }

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        Logger.getLogger().e("connect ssid:" + wifiInfo.getSSID());
        View child = getConnectPosition(Summary.removeDoubleQuotes(wifiInfo.getSSID()));
        refresh(child, wifiInfo, state);

        if (state == DetailedState.CONNECTED) {
            addOrUpdateListView(mWifiConnect.getScanResult());
        } else if (state == DetailedState.DISCONNECTED) {
            addOrUpdateListView(mWifiConnect.getScanResult());
        } else if (state == DetailedState.DISCONNECTING) {
            //            addOrUpdateListView(mWifiConnect.getScanResult());
        } else if (state == DetailedState.SCANNING) {
            mWifiConnectMsg.setText(Summary.getWifiState(this, WifiState.SCANNING));
        } else if (state == DetailedState.FAILED) {
            addOrUpdateListView(mWifiConnect.getScanResult());
        }

    }

    private void refresh(View child, WifiInfo wifiInfo, DetailedState state) {
        if (child != null) {
            TextView info = (TextView) child.findViewById(R.id.connect_info);
            if (info != null) {
                info.setText(Summary.getWifiState(this, state));
            }
        }

    }

    /**
     * 设置wifi信号强度图标
     * @param img
     * @param rssi
     * @param security
     */
    private void setWifiSignal(ImageView img, int rssi, WifiCipherType security) {
        int level = NetworkUtil.getRssiLevel(rssi);
        if (level >= 0 && level < NetworkUtil.WIFI_SIGNAL_LEVEL) {
            img.setImageLevel(level);
            img.setImageResource(R.drawable.wifi_signal);
            img.setImageState((security != WifiCipherType.WIFICIPHER_NOPASS) ? STATE_SECURED
                    : STATE_NONE, true);
        } else {
            img.setBackgroundDrawable(null);
        }
    }

    /**
     * 按键事件
     */
    private OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v == mCurrTabView && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    mWifiSwitchButton.requestFocus();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
            }

            if (v == mWifiSwitchButton && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    mCurrTabView.requestFocus();
                    return true;
                }
            }

            if (v == mListView && event.getAction() == KeyEvent.ACTION_DOWN) {
                int position = mListView.getSelectedItemPosition();
                /** 最左侧的item点击left,选中当前tab页 */
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    mCurrTabView.requestFocus();
                    return true;
                }
                /** 焦点在列表第最后一个，阻止焦点向下 */
                if ((position == mListView.getCount() - 1)
                        && (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                    return true;
                }
            }
            return false;

        }
    };

    private class Scanner extends Handler {

        private int mRetry = 0;

        void resume() {
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }

        void pause() {
            mRetry = 0;
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message message) {
            if (mWifiManager.startScan()) {
                mRetry = 0;
            } else if (++mRetry >= 3) {
                mRetry = 0;
                CustomToast.makeText(WifiScanActivity.this, R.string.wifi_fail_to_scan,
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (mRetry != 0) {
                mWifiConnectMsg.setText(Summary.getWifiState(WifiScanActivity.this,
                        WifiState.SCANNING));
            }
            sendEmptyMessageDelayed(0, 6000);
        }
    }
}
