/*
 * 文件名：MenuDialog.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.PersistData;
import com.miri.launcher.R;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.msg.model.AppNode;
import com.miri.launcher.msg.model.doc.AppRecommend;
import com.miri.launcher.receiver.NetWorkStateChangeReceiver;
import com.miri.launcher.utils.AppManager;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.SimpleClock;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-10
 */
public class MenuActivity extends Activity {

    public enum ListSort {

        //        LOCALMEDIA, LOCALFILE, BROWER, SETTING, UPGRADE, NETWORKSETTING, DOWNLOAD, ABOUTUS
        LOCALMEDIA, LOCALFILE, BROWER, MARKET, DOWNLOAD, ABOUTUS

    }

    protected static final int TAB_MAX_NUMS = 6;

    private SimpleClock mClock;

    private ImageView mWifiImg;

    private TextView mWifiText;

    private ImageView mLogoView;

    private ListView mListView;

    private ImageView mUpView;

    private ImageView mDownView;

    private NetWorkStateChangeReceiver mWifiStateChangeReceiver;

    private DownloadService downloadService;

    private ServiceConnection dlSvcConnection;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Logger.getLogger().v("msg:" + msg.what + ", isFinish:" + isFinishing());
            if (isFinishing()) {
                return;
            }

            switch (msg.what) {
            case Constants.WIFI_STATE_DISCONNECT:
                int wifiState = msg.arg1;
                //TODO 需解决wifi输错密码后，会一直显示为问号的问题? 暂不用WIFI_STATE_UNKNOWN这个状态
                mWifiImg.setVisibility(View.VISIBLE);
                if (wifiState == WifiManager.WIFI_STATE_UNKNOWN) {
                    mWifiImg.setImageLevel(4);
                } else {
                    mWifiImg.setImageLevel(5);
                }
                mWifiText.setText("");
                mWifiText.setVisibility(View.GONE);
                break;
            case Constants.WIFI_STATE_CONNECTED:
                int level = msg.arg1;
                mWifiText.setText((String) msg.obj);
                Logger.getLogger().d("msg.obj-->" + msg.obj);
                mWifiText.setVisibility(View.VISIBLE);
                mWifiImg.setVisibility(View.VISIBLE);
                mWifiImg.setImageLevel(level);
                break;
            case Constants.WIFI_RSSI_CHANGED:
                mWifiImg.setVisibility(View.VISIBLE);
                mWifiImg.setImageLevel(msg.arg1);
                break;
            case Constants.WIFI_AP_CONNECTED:
                Logger.getLogger().d("===========WIFI_AP_CONNECTED=========");
                break;
            case Constants.WIFI_AP_ERROR:
                Logger.getLogger().e("Wide Area Network error");
                break;
            case Constants.WAN_ERROR:
                Logger.getLogger().e("Wide Area Network error");
                break;
            case Constants.WIFI_AP_NEEDLOGIN:
                Logger.getLogger().e("Wifi ap need login!");
                break;
            case Constants.WIFI_AP_CHECK:
                //TODO 检查AP
                break;
            default:
                break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_menu);
        mClock = (SimpleClock) findViewById(R.id.clock);
        mWifiImg = (ImageView) findViewById(R.id.wifi_icon);
        mWifiImg.setImageResource(R.drawable.wifi_signal_state);
        mWifiText = (TextView) findViewById(R.id.wifi_name);
        mLogoView = (ImageView) findViewById(R.id.left_logo);
        if (PersistData.VERSION_TYPE == Constants.KONGGE) {
            mLogoView.setImageResource(R.drawable.kongge);
            mLogoView.setVisibility(View.VISIBLE);
        } else {
            mLogoView.setImageResource(R.drawable.miri_logo_2);
            mLogoView.setVisibility(View.VISIBLE);
            //            TextView companyText = (TextView) findViewById(R.id.company);
            //            companyText.setText(R.string.company_name);
            //            companyText.setVisibility(View.VISIBLE);
        }
        mUpView = (ImageView) findViewById(R.id.attrows_up);
        mDownView = (ImageView) findViewById(R.id.attrows_down);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setItemsCanFocus(true);
        Toolkit.disableOverScrollMode(mListView);
        mListView.setAdapter(new LeftAdapter(this, getData()));
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) mListView.getSelectedItem();
                if (map == null) {
                    Logger.getLogger().e("adapter data is NULL!");
                    return;
                }
                int index = (Integer) map.get("index");
                Logger.getLogger().d("click index :" + index);
                if (index == ListSort.LOCALMEDIA.ordinal()) {
                    Intent intent = new Intent(MenuActivity.this, MediaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (index == ListSort.LOCALFILE.ordinal()) {
                    start(ListSort.LOCALFILE);
                } else if (index == ListSort.BROWER.ordinal()) {
                    start(ListSort.BROWER);
                }
                //                else if (index == ListSort.SETTING.ordinal()) {
                //                    Toolkit.startNetworkSettings(MenuActivity.this);
                //                    finish();
                //                }
                //                else if (index == ListSort.UPGRADE.ordinal()) {
                //                    Intent intent = new Intent(MenuActivity.this, CheckUpgradeActivity.class);
                //                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                    startActivity(intent);
                //                    finish();
                //                }
                //                else if (index == ListSort.NETWORKSETTING.ordinal()) {
                //                    Intent intent = new Intent(MenuActivity.this, NetworkManagerActivity.class);
                //                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                    startActivity(intent);
                //                    finish();
                //                }
                else if (index == ListSort.MARKET.ordinal()) {
                    start(ListSort.MARKET);
                } else if (index == ListSort.DOWNLOAD.ordinal()) {
                    Intent intent = new Intent(MenuActivity.this, DownloadManagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (index == ListSort.ABOUTUS.ordinal()) {
                    Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
        mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int count = mListView.getCount();
                int pos = mListView.getSelectedItemPosition();
                if (count <= TAB_MAX_NUMS) {
                    mUpView.setVisibility(View.INVISIBLE);
                    mDownView.setVisibility(View.INVISIBLE);
                } else {
                    if ((pos < (count - 1)) && (pos > 0)) {
                        mUpView.setVisibility(View.VISIBLE);
                        mDownView.setVisibility(View.VISIBLE);
                    } else {
                        if (pos == (count - 1)) {
                            mUpView.setVisibility(View.VISIBLE);
                            mDownView.setVisibility(View.INVISIBLE);
                        }
                        if (pos == 0) {
                            mUpView.setVisibility(View.INVISIBLE);
                            mDownView.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        registerReceiver();
        dlSvcConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadService = ((DownloadService.MyBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // Bind the DownloadService.
        bindService(new Intent(this, DownloadService.class), dlSvcConnection,
                Context.BIND_AUTO_CREATE);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_MENU)) {
            Logger.getLogger().e("onKeyDown menu ,KEYCODE_MENU:" + KeyEvent.KEYCODE_MENU);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        if (mClock != null) {
            mClock.destoryClock();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mWifiStateChangeReceiver != null) {
            unregisterReceiver(mWifiStateChangeReceiver);
        }
        unbindService(dlSvcConnection);
        super.onDestroy();
    }

    /**
     * 注册各广播监听器
     */
    private void registerReceiver() {
        //Wifi连接状态广播接收器
        mWifiStateChangeReceiver = new NetWorkStateChangeReceiver(this, mHandler);
        IntentFilter wifiFilter = new IntentFilter();
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //        wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        wifiFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mWifiStateChangeReceiver, wifiFilter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", ListSort.LOCALMEDIA.ordinal());
        map.put("name", R.string.local_media);
        map.put("image", R.drawable.localmedia_icon);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("index", ListSort.LOCALFILE.ordinal());
        map.put("name", R.string.local_file);
        map.put("image", R.drawable.localfile_icon);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("index", ListSort.BROWER.ordinal());
        map.put("name", R.string.broswer);
        map.put("image", R.drawable.brower_icon);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("index", ListSort.MARKET.ordinal());
        map.put("name", R.string.market);
        map.put("image", R.drawable.market_icon);
        list.add(map);

        //        map = new HashMap<String, Object>();
        //        map.put("index", ListSort.SETTING.ordinal());
        //        map.put("name", R.string.adv_settings);
        //        map.put("image", R.drawable.settings_icon);
        //        list.add(map);
        //
        //        map = new HashMap<String, Object>();
        //        map.put("index", ListSort.UPGRADE.ordinal());
        //        map.put("name", R.string.netwrok_upgrade);
        //        map.put("image", R.drawable.upgrade_icon);
        //        list.add(map);
        //
        //        map = new HashMap<String, Object>();
        //        map.put("index", ListSort.NETWORKSETTING.ordinal());
        //        map.put("name", R.string.network_settings);
        //        map.put("image", R.drawable.netwrok_settings_icon);
        //        list.add(map);

        map = new HashMap<String, Object>();
        map.put("index", ListSort.DOWNLOAD.ordinal());
        map.put("name", R.string.download_manager);
        map.put("image", R.drawable.down_manager_con);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("index", ListSort.ABOUTUS.ordinal());
        map.put("name", R.string.about_title);
        map.put("image", R.drawable.aboutus_icon);
        list.add(map);

        return list;
    }

    class LeftAdapter extends ArrayAdapter<Map<String, Object>> {

        private LayoutInflater mInflater = null;

        public LeftAdapter(Context context, List<Map<String, Object>> objects) {
            super(context, R.layout.left_list_item, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View rowView = convertView;
            if (rowView == null) {
                holder = new ViewHolder();
                rowView = this.mInflater.inflate(R.layout.left_list_item, null);
                holder.name = (TextView) rowView.findViewById(R.id.title);
                holder.iconView = (ImageView) rowView.findViewById(R.id.image);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }
            Map<String, Object> map = getItem(position);
            holder.name.setText((Integer) map.get("name"));
            holder.iconView.setImageResource((Integer) map.get("image"));
            return rowView;
        }

        class ViewHolder {

            public TextView name;

            public ImageView iconView;

        }

    }

    private void start(ListSort ls) {
        AppNode appRecommend = getRecommendData(ls);
        if (appRecommend != null) {
            String iconPath = null;
            if (ls.ordinal() != ListSort.MARKET.ordinal()) {
                iconPath = appRecommend.getPoster();
            }
            startApp(appRecommend.getPkgName(), appRecommend.getName(), appRecommend.getUrl(),
                    iconPath);
        }
    }

    /**
     * 点击推荐应用
     * @param pkgName
     * @param url
     */
    private void startApp(String pkgName, String appName, String url, String iconPath) {
        boolean isDownload = false;
        if (!Toolkit.isEmpty(pkgName)) {
            // 判断应用是否已安装
            boolean isExist = Toolkit.isExistApp(this, pkgName);
            Logger.getLogger().d("package [" + pkgName + "] application is exist ? " + isExist);
            if (isExist) {
                // 启动app
                AppManager.runApp(this, pkgName);
            } else {
                isDownload = true;
            }
        } else {
            isDownload = true;
        }

        if (isDownload) {
            // TODO 去下载安装
            if (downloadService != null) {
                downloadService.createDownload(url, null, appName, iconPath, true, true);
            }
        }

    }

    /**
     * 取推荐电子市场apk数据
     * @return
     */
    private AppNode getRecommendData(ListSort ls) {
        AppNode result = null;
        AppRecommend mainRec = PersistData.mAppRecommend;
        if (mainRec == null) {
            return null;
        }
        List<AppNode> appRecommend = null;
        switch (ls) {
        case LOCALFILE:
            appRecommend = mainRec.getLocalFileRecommends();
            break;
        case BROWER:
            appRecommend = mainRec.getBroswerRecommends();
            break;
        case MARKET:
            appRecommend = mainRec.getMarketRecommends();
            break;
        default:
            break;
        }
        if (!Toolkit.isEmpty(appRecommend)) {
            for (AppNode app: appRecommend) {
                if (!Toolkit.isEmpty(app.getPkgName()) && !Toolkit.isEmpty(app.getUrl())) {
                    result = app;
                    break;
                }
            }
            return result;
        }
        return null;

    }

}
