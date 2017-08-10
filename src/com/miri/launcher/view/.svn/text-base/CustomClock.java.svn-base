package com.miri.launcher.view;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.utils.Logger;

/**
 * 自定义数字时钟
 */
public class CustomClock extends LinearLayout {

    private TextView timeText;

    private TextView fmsText;

    Calendar mCalendar;

    private final static String m12 = "hh:mm";// h:mm:ss aa

    private final static String m24 = "k:mm";// k:mm:ss

    private final static String mDate = "yyyy.MM.dd EEEE"; // yyyy-MM-dd k:mm:ss EEEE

    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;

    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;

    private int updateInterval;

    private TimeZoneChangeRevicer mTimeZoneRevicer;

    public CustomClock(Context context) {
        super(context);
        initView(context);
        initClock(context);
    }

    public CustomClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initClock(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_clock, this, true);
        timeText = (TextView) view.findViewById(R.id.time);
        fmsText = (TextView) view.findViewById(R.id.fms);
    }

    private void initClock(Context context) {
        Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI,
                true, mFormatChangeObserver);

        setFormat();
    }

    public void destoryClock(Context context) {
        mTickerStopped = true;
        if (mFormatChangeObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(mFormatChangeObserver);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;

        mTimeZoneRevicer = new TimeZoneChangeRevicer();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        getContext().registerReceiver(mTimeZoneRevicer, filter);
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {

            @Override
            public void run() {
                if (mTickerStopped) {
                    return;
                }
                updateInterval = 1000;
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                //设置为东八区
                //                mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                CharSequence time = DateFormat.format(mFormat, mCalendar);
                timeText.setText(time);
                if (get24HourMode()) {
                    fmsText.setVisibility(View.GONE);
                } else {
                    //TODO 处理多语言环境问题
                    fmsText.setVisibility(View.VISIBLE);
                    CharSequence fms = DateFormat.format("aa", mCalendar);
                    fmsText.setText(fms.toString().toUpperCase());
                }
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (updateInterval - (now % 1000));
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();

    }

    @Override
    protected void onDetachedFromWindow() {
        destoryClock(getContext());
        if (mTimeZoneRevicer != null) {
            getContext().unregisterReceiver(mTimeZoneRevicer);
        }
        super.onDetachedFromWindow();
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        //强制使用12小时制
        //        mFormat = m12;
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
        //        ContentResolver cv = getContext().getContentResolver();
        //        String strTimeFormat = android.provider.Settings.System.getString(cv,
        //                android.provider.Settings.System.TIME_12_24);
        //        Logger.getLogger().d("time format-->" + strTimeFormat);
    }

    private class FormatChangeObserver extends ContentObserver {

        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            Logger.getLogger().d("Format change!");
            setFormat();
        }
    }

    private class TimeZoneChangeRevicer extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.getLogger().d("Time Zone change!");
            mCalendar = Calendar.getInstance();

        }

    }
}
