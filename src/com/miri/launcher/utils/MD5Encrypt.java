package com.miri.launcher.utils;

import java.security.MessageDigest;

/**
 * MD5加密算法
 */
public class MD5Encrypt {

    /**
     * 十六进制下数字到字符的映射数组
     */
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"};

    /**
     * 加密一个字符串
     * @param inputString 需要加密的字符
     * @return 加密结果
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static String encryptStr(String inputString) {
        return encodeByMD5(inputString);
    }

    /**
     * 生成加密密码
     * @param encryptChar 需要加密的字符
     * @param encryptKey 加密标识
     * @return 加密结果
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static String generatePassword(String encryptChar, String encryptKey) {
        StringBuffer tempStr = new StringBuffer();
        String tempStr1 = "";

        if (encryptKey.length() < encryptChar.length()) {
            tempStr1 = encryptChar.substring(encryptKey.length());
        }

        char[] encryptCharArr = encryptChar.toCharArray();
        char[] encryptKeyArr = encryptKey.toCharArray();

        for (int i = 0; i < encryptKeyArr.length; ++i) {
            if (encryptCharArr.length > i) {
                tempStr.append(encryptCharArr[i]);
            }
            tempStr.append(encryptKeyArr[i]);
        }
        tempStr.append(tempStr1);

        return encodeByMD5(tempStr.toString());
    }

    /**
     * 对字符串进行MD5加密
     * @param originString 加密原字符
     * @return 加密后字符
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 十六进制字符串
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
