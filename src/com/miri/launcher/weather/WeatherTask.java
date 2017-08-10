package com.miri.launcher.weather;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParser;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.utils.Logger;

/**
 * 获取天气预报任务
 */
public class WeatherTask {

    /** 天气预报地址 */
    public static final String WEATHER_URL = "http://m.imgo.tv:11199/weather.html";

    //3小时
    private static final long FETCH_INTERVAL = 3 * 60 * 60 * 1000;

    public static final int FETCH_WEATHER = 100;

    public static final int FETCH_WEATHER_SUCCESS = 101;

    public static final int FETCH_WEATHER_ERROR = 102;

    private Context mContext;

    private Handler mHandler;

    private Timer timer;

    private TimerTask fetchTask;

    private boolean mIsStart = false;

    public WeatherTask(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    /**
     * 初始化
     */
    private void init() {
        timer = new Timer();
        fetchTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    WeatherDocument doc = parseWeather();
                    mHandler.removeMessages(WeatherTask.FETCH_WEATHER_SUCCESS);
                    mHandler.removeMessages(WeatherTask.FETCH_WEATHER_ERROR);
                    Message msg = mHandler.obtainMessage(FETCH_WEATHER_SUCCESS);
                    msg.obj = doc;
                    mHandler.sendMessage(msg);
                } catch (NetWorkInfoException e) {
                    Logger.getLogger().e("读取网络数据错误");
                    Logger.getLogger().e(e);
                    mHandler.sendEmptyMessage(FETCH_WEATHER_ERROR);
                } catch (JsonParserException e) {
                    Logger.getLogger().e("解析天气数据错误");
                    Logger.getLogger().e(e);
                    mHandler.sendEmptyMessage(FETCH_WEATHER_ERROR);
                }
            }
        };
    }

    public void retryReqTask() {
        mHandler.removeMessages(WeatherTask.FETCH_WEATHER_SUCCESS);
        mHandler.removeMessages(WeatherTask.FETCH_WEATHER_ERROR);
        mHandler.removeMessages(WeatherTask.FETCH_WEATHER);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Logger.getLogger().d("retry reuqest weather!");
                if (fetchTask != null) {
                    fetchTask.run();
                }

            }
        }).start();

    }

    /**
     * 开始更新任务
     */
    public void start() {
        if (!mIsStart) {
            init();
            timer.schedule(fetchTask, 0, FETCH_INTERVAL);
            mIsStart = true;
        }
    }

    /**
     * 停止更新任务
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            fetchTask = null;
            timer = null;
            mIsStart = false;
        }
    }

    /**
     * 天气预报解析
     * @return
     * @throws JsonParserException
     * @throws NetWorkInfoException
     */
    private WeatherDocument parseWeather() throws JsonParserException, NetWorkInfoException {
        String urlString = WEATHER_URL;
        WeatherDocument weatherDoc = (WeatherDocument) JsonParser.parse(urlString,
                WeatherDocument.class, -1);
        return weatherDoc;
    }

}
