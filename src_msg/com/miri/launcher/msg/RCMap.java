/* 
 * 文件名：RCMap.java
 * 版权：Copyright
 */
package com.miri.launcher.msg;

import android.util.SparseArray;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class RCMap {

    //本地消息错误提示信息
    private static SparseArray<String> errors = new SparseArray<String>();

    /**
     * 获取错误提示信息
     * @param resultCode
     */
    public static String getMsg(int resultCode) {
        //消息码集合中不存在该消息码时，返回未知错误
        return errors.get(resultCode) == null ? errors.get(9999) : errors.get(resultCode);
    }

    static {
        //本地错误提示信息
        errors.put(0, "成功 ");
        errors.put(1001, "1001：MAC地址未注册");
        errors.put(1002, "1002：登陆失败");
        errors.put(1003, "1003：MAC地址信息不一致");
        errors.put(9999, "9999：未知错误");
    }
}
