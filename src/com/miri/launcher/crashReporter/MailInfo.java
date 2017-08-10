/* 
 * 文件名：MailInfo.java
 * 版权：Copyright
 */
package com.miri.launcher.crashReporter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-8-30
 */
public class MailInfo {

    private static Map<String, String> mail = new HashMap<String, String>();

    static {
        mail.put("mail_host", "smtp.126.com");
        mail.put("mail_port", "25");
        mail.put("mail_sport", "25");
        mail.put("mail_from", "miri_message@126.com");
        mail.put("mail_user", "miri_message@126.com");
        mail.put("mail_pass", "50052796");
        mail.put("mail_to", "miri_message@126.com");
        mail.put("mail_subject", "应用错误信息上报，终端：%1$s，终端编号：%2$s，MAC地址：%3$s");
        mail.put("mail_body", "");
    }

    public static String getMsg(String key) {
        return mail.get(key) == null ? "" : mail.get(key);
    }

}
