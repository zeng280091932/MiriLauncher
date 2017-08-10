package com.miri.launcher.view;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.miri.launcher.utils.Logger;

/**
 * 自定义数字时钟
 */
public class SimpleClock extends TextView {

    private Context mContext;

    private Calendar mCalendar;

    private final static String m12 = "M月dd日  EEEE hh:mm aa";// h:mm:ss aa

    private final static String m24 = "M月dd日 EEEE k:mm";// k:mm:ss

    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;

    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;

    private int updateInterval;

    private TimeZoneChangeRevicer mTimeZoneRevicer;

    public SimpleClock(Context context) {
        super(context);
        mContext = context;
        initClock();
    }

    public SimpleClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initClock();
    }

    private void initClock() {
        //        Resources r = mContext.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true,
                mFormatChangeObserver);

        setFormat();

    }

    public void destoryClock() {
        mTickerStopped = true;
        if (mFormatChangeObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mFormatChangeObserver);
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
        mContext.registerReceiver(mTimeZoneRevicer, filter);

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
                setText(time);
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
        destoryClock();
        if (mTimeZoneRevicer != null) {
            mContext.unregisterReceiver(mTimeZoneRevicer);
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
        //强制使用24小时制
        mFormat = m24;
        //        if (get24HourMode()) {
        //            mFormat = m24;
        //        } else {
        //            mFormat = m12;
        //        }
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
