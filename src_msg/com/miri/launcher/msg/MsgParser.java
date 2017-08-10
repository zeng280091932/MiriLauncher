/* 
 * 文件名：MsgParser.java
 * 版权：Copyright
 */
package com.miri.launcher.msg;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.miri.launcher.PersistData;
import com.miri.launcher.LauncherApplication;
import com.miri.launcher.MsgConstants;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParser;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.msg.exception.MsgInfoException;
import com.miri.launcher.msg.model.Operate;
import com.miri.launcher.msg.model.Servers;
import com.miri.launcher.msg.model.doc.MessageRevicer;
import com.miri.launcher.msg.model.doc.MessageSender;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
public class MsgParser extends MsgConstants {

    static final String url = PersistData.msg_url;

    public static void auth() throws NetWorkInfoException, JsonParserException, MsgInfoException {
        MessageRevicer revicer = request(url, MSGTYPE_AUTH);
        String mac = revicer.getMac();
        String localMac = NetworkUtil.getMac(LauncherApplication.getInstance());
        if (Toolkit.isEmpty(mac) || !mac.equalsIgnoreCase(localMac)) {
            throw new MsgInfoException(1003);
        }
        String authCode = revicer.getAuthCode();
        if (Toolkit.isEmpty(authCode)) {
            throw new MsgInfoException(9999);
        }
        saveAuthCode(authCode);
    }

    public static void login() throws NetWorkInfoException, JsonParserException, MsgInfoException {
        request(url, MSGTYPE_LOGIN);
    }

    public static List<Operate> heart() throws NetWorkInfoException, JsonParserException,
            MsgInfoException {
        MessageRevicer revicer = request(url, MSGTYPE_HEART);
        return revicer.getOperateInfo();
    }

    public static Servers server() throws NetWorkInfoException, JsonParserException,
            MsgInfoException {
        MessageRevicer revicer = request(url, MSGTYPE_SERVER);
        return revicer.getServerInfo();
    }

    /**
     * 请求消息
     * @param msgType 请求消息类型
     * @return
     * @throws NetWorkInfoException
     * @throws JsonParserException
     */
    private static MessageRevicer request(String urlString, String msgType)
            throws NetWorkInfoException, JsonParserException, MsgInfoException {
        //封装请求参数
        Map<String, String> parameters = new HashMap<String, String>();
        String paramValue = buildReqMsg(msgType);
        Logger.getLogger().i("request param value:" + paramValue);
        parameters.put("TXNINFO", paramValue);
        //封装URL地址
        urlString = wrapGetParameter(urlString, parameters);
        Logger.getLogger().i("request url: " + urlString);

        int readTimeOut = -1;
        if (MSGTYPE_HEART.equals(msgType)) {
            readTimeOut = 10 * 1000;
        }
        MessageRevicer revicer = (MessageRevicer) JsonParser.parse(urlString, MessageRevicer.class,
                readTimeOut);
        if (revicer != null) {
            Integer resultCode = revicer.getResultCode();
            if (resultCode != null) {
                if (resultCode == 0) {
                    return revicer;
                } else {
                    throw new MsgInfoException("Message revicer's resultCode is " + resultCode
                            + "!", resultCode);
                }
            } else {
                throw new MsgInfoException("Message revicer's resultCode is NULL!", 9999);
            }

        } else {
            throw new MsgInfoException("Message revicer is NULL!", 9999);
        }
    }

    /**
     * 构建当前发送的请求信息
     * @param msgType 请求消息类型
     * @return
     */
    public static String buildReqMsg(String msgType) {
        MessageSender msg = new MessageSender();
        msg.setMessageType(msgType);
        String mac = NetworkUtil.getMac(LauncherApplication.getInstance());
        msg.setMac(mac);
        if (MSGTYPE_AUTH.equals(msgType)) {
            msg.setChipModel(Build.PRODUCT);
            msg.setVersionCode(Toolkit.getLocalVersion(LauncherApplication.getInstance()));
        } else if (MSGTYPE_LOGIN.equals(msgType)) {
            msg.setAuthCode(getAuthCode());
            msg.setVersionCode(Toolkit.getLocalVersion(LauncherApplication.getInstance()));
        } else if (MSGTYPE_HEART.equals(msgType)) {
            msg.setAuthCode(getAuthCode());
        } else if (MSGTYPE_SERVER.equals(msgType)) {
            msg.setAuthCode(getAuthCode());
        }
        return new Gson().toJson(msg);
    }

    /**
     * 封装get参数的URL
     * @param urlString
     * @param parameters
     * @return
     */
    public static String wrapGetParameter(String urlString, Map<String, String> parameters) {
        if (parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key: parameters.keySet()) {
                if (i == 0) {
                    param.append("?");
                } else {
                    param.append("&");
                }
                String paramValue = "";
                try {
                    paramValue = URLEncoder.encode(parameters.get(key), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                param.append(key).append("=").append(paramValue);
                i++;
            }

            urlString += param;
        }
        return urlString;
    }

    /**
     * 保存认证码到本地
     * @param authCode
     */
    public static void saveAuthCode(String authCode) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LauncherApplication
                .getInstance());
        SharedPreferences.Editor mEditor = sp.edit();
        mEditor.putString(AUTHCODE, authCode);
        mEditor.commit();
    }

    /**
     * 获取本地保存的认证码
     */
    public static String getAuthCode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LauncherApplication
                .getInstance());
        String authCode = sp.getString(AUTHCODE, "");
        return authCode;
    }

}
