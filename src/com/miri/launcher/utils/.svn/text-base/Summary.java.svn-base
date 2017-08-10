/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.miri.launcher.utils;

import android.content.Context;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;

import com.miri.launcher.R;
import com.miri.launcher.utils.WifiConnect.WifiCipherType;

public class Summary {

    public enum WifiState {
        ENABLED, DISABLED, ENABLING, SCANNING, DISABLING, UNKNOWN
    }

    /**
     * 获取wifi状态
     * @param context
     * @param state
     * @return
     */
    public static String getWifiState(Context context, WifiState state) {
        String[] formats = context.getResources().getStringArray(R.array.wifi_status);
        int index = state.ordinal();

        if (index >= formats.length || formats[index].length() == 0) {
            return null;
        }
        return String.format(formats[index]);
    }

    /**
     * 获取wifi状态
     * @param context
     * @param state
     * @return
     */
    public static String getWifiState(Context context, DetailedState state) {
        String[] formats = context.getResources().getStringArray(R.array.wifi_detail_status);
        int index = state.ordinal();

        if (index >= formats.length || formats[index].length() == 0) {
            return null;
        }
        return String.format(formats[index]);
    }

    /**
     * 获取wifi状态
     * @param context
     * @param state
     * @return
     */
    public static String getWifiState(Context context, SupplicantState state) {
        String[] formats = context.getResources().getStringArray(R.array.wifi_supplicant_status);
        int index = 0;
        if (state == SupplicantState.DISCONNECTED) {
            index = 1;
        } else if (state == SupplicantState.SCANNING) {
            index = 2;
        } else if (state == SupplicantState.COMPLETED) {
            index = 3;
        }
        return String.format(formats[index]);
    }

    /**
     * 获取wifi加密类型
     * @param context
     * @param security
     * @return
     */
    public static String getWifiSecurityString(Context context, WifiCipherType security) {
        String[] formats = context.getResources().getStringArray(R.array.wifi_security);
        int index = security.ordinal();
        if (index == 0) {
            return String.format(formats[0]);
        }
        if (index >= formats.length || formats[index].length() == 0) {
            return null;
        }
        String format = context.getString(R.string.wifi_secured);
        return String.format(format, formats[index]);
    }

    /**
     * 获取wifi加密类型
     * @param context
     * @param security
     * @return
     */
    public static String getWifiSecurity(Context context, WifiCipherType security) {
        String[] formats = context.getResources().getStringArray(R.array.wifi_security);
        int index = security.ordinal();
        if (index == 0) {
            return String.format(formats[0]);
        }
        if (index >= formats.length || formats[index].length() == 0) {
            return null;
        }
        return String.format(formats[index]);
    }

    /**
     * 获取wifi等级
     * @param context
     * @param state
     * @return
     */
    public static String getWifiLevel(Context context, int level) {
        String[] formats = context.getResources().getStringArray(R.array.wifi_level);

        if (level >= formats.length || formats[level].length() == 0) {
            return null;
        }
        return String.format(formats[level]);
    }

    public static String removeDoubleQuotes(String string) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"') && (string.charAt(length - 1) == '"')) {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public static String convertToQuotedString(String string) {
        if (string == null) {
            return null;
        }
        return "\"" + string + "\"";
    }

}
