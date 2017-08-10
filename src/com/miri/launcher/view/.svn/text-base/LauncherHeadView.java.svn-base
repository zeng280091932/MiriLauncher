/* 
 * 文件名：LauncherHeadView.java
 */
package com.miri.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.imageCache.ImageLoader;
import com.miri.launcher.imageCache.ImageLoader.ImgCallback;
import com.miri.launcher.receiver.NetWorkStateChangeReceiver;
import com.miri.launcher.utils.IconUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.weather.WeatherDocument;
import com.miri.launcher.weather.WeatherInfo;
import com.miri.launcher.weather.WeatherTask;

/**
 * 顶部天气、wifi、有线控件
 * @author penglin
 * @version 2013-6-25
 */
public class LauncherHeadView {

    private Context mContext;

    private CustomClock mClock;

    private LinearLayout mWeatherContainer1;

    private LinearLayout mWeatherContainer2;

    private ImageView mWeatherIcon;

    private TextView mCity;

    private TextView mTemp;

    private TextView mWeatherInfo;

    private ImageView mWifiImg;

    private ImageView mWiredImg;

    private WeatherTask mWeatherTask;

    private NetWorkStateChangeReceiver mWifiStateChangeReceiver;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Logger.getLogger().v(
                    "msg:" + msg.what + ", isFinish:" + ((Activity) mContext).isFinishing());
            if (((Activity) mContext).isFinishing()) {
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
                    break;
                case Constants.WIFI_STATE_CONNECTED:
                    mWifiImg.setVisibility(View.VISIBLE);
                    mWifiImg.setImageLevel(msg.arg1);
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
                //有线网络连接状态信息
                case Constants.WIRED_STATE_INFO:
                    State state = (State) msg.obj;
                    Logger.getLogger().d("state-->" + state);
                    mWiredImg.setVisibility(View.VISIBLE);
                    if (state == State.CONNECTED) {
                        mWiredImg.setImageResource(R.drawable.wired_enable);
                    } else {
                        mWiredImg.setImageResource(R.drawable.wired_disable);
                    }
                    break;
                //有线网络未连接
                case Constants.WIRED_STATE_DISCONNECT:
                    mWiredImg.setVisibility(View.VISIBLE);
                    mWiredImg.setImageResource(R.drawable.wired_disable);
                    break;
                //有线网络连接
                case Constants.WIRED_STATE_CONNECTED:
                    mWiredImg.setVisibility(View.VISIBLE);
                    mWiredImg.setImageResource(R.drawable.wired_enable);
                    break;
                //天气数据
                case WeatherTask.FETCH_WEATHER:
                    mWeatherTask.retryReqTask();
                    break;
                case WeatherTask.FETCH_WEATHER_SUCCESS:
                    mHandler.removeMessages(WeatherTask.FETCH_WEATHER);
                    showWeather((WeatherDocument) msg.obj);
                    break;
                case WeatherTask.FETCH_WEATHER_ERROR:
                    Logger.getLogger().d("Weather info parse error!");
                    //获取天气失败，重试请求
                    mHandler.sendEmptyMessageDelayed(WeatherTask.FETCH_WEATHER, 5000);
                    break;
                default:
                    break;
            }

        }
    };

    public LauncherHeadView(Context context) {
        mContext = context;
        initWidget();
        registerReceiver();
        mWeatherTask = new WeatherTask(mContext, mHandler);
        mWeatherTask.start();
    }

    private void initWidget() {
        mClock = (CustomClock) ((Activity) mContext).findViewById(R.id.clock);
        mWeatherContainer1 = (LinearLayout) ((Activity) mContext)
                .findViewById(R.id.weather_container1);
        mWeatherContainer2 = (LinearLayout) ((Activity) mContext)
                .findViewById(R.id.weather_container2);
        mWeatherIcon = (ImageView) ((Activity) mContext).findViewById(R.id.weather_icon);
        mCity = (TextView) ((Activity) mContext).findViewById(R.id.city);
        mTemp = (TextView) ((Activity) mContext).findViewById(R.id.temperature);
        mWeatherInfo = (TextView) ((Activity) mContext).findViewById(R.id.weatherinfo);
        mWifiImg = (ImageView) ((Activity) mContext).findViewById(R.id.wifi_view);
        mWifiImg.setImageResource(R.drawable.wifi_signal_state);
        mWiredImg = (ImageView) ((Activity) mContext).findViewById(R.id.wired_view);
    }

    /**
     * 注册各广播监听器
     */
    private void registerReceiver() {
        //Wifi连接状态广播接收器
        mWifiStateChangeReceiver = new NetWorkStateChangeReceiver(mContext, mHandler);
        IntentFilter wifiFilter = new IntentFilter();
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //        wifiFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        wifiFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        //        Logger.getLogger().d(
        //                "registerReceiver action:" + WifiManager.WIFI_STATE_CHANGED_ACTION + " & "
        //                        + WifiManager.RSSI_CHANGED_ACTION + " & "
        //                        + WifiManager.NETWORK_STATE_CHANGED_ACTION + " & "
        //                        + ConnectivityManager.CONNECTIVITY_ACTION + ", priority:"
        //                        + wifiFilter.getPriority());
        mContext.registerReceiver(mWifiStateChangeReceiver, wifiFilter);
    }

    /**
     * 显示天气预报
     */
    private void showWeather(WeatherDocument weather) {
        if (weather != null) {
            WeatherInfo weatherInfo = weather.getWeatherInfo();
            if (weatherInfo != null) {
                mWeatherInfo.setText(weatherInfo.getWeather());
                mTemp.setText(weatherInfo.getTemp());
                mCity.setText(weatherInfo.getCity());
                ImageLoader.from(mContext).loadImg(mWeatherIcon, weatherInfo.getImg(), 0,
                        new ImgCallback() {

                            @Override
                            public void refresh(Object view, Bitmap bitmap, boolean isFromNet) {
                                if (bitmap != null) {
                                    ((ImageView) view).setImageBitmap(IconUtil
                                            .changeColorFFFFFF(bitmap));
                                }
                            }
                        });
                mWeatherContainer1.setVisibility(View.VISIBLE);
                mWeatherContainer2.setVisibility(View.VISIBLE);
                mCity.setVisibility(View.VISIBLE);
                mWeatherIcon.setVisibility(View.VISIBLE);
                mWeatherInfo.setVisibility(View.VISIBLE);
                mTemp.setVisibility(View.VISIBLE);
            }
        }
    }

    public void destroy() {
        if (mClock != null) {
            mClock.destoryClock(mContext);
        }
        if (mWeatherTask != null) {
            mWeatherTask.stop();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mWifiStateChangeReceiver != null) {
            mContext.unregisterReceiver(mWifiStateChangeReceiver);
        }
    }

}
