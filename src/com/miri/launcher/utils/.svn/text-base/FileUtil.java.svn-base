/* 
 * 文件名：FileUtil.java
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 文件操作
 * @author penglin
 */
public class FileUtil {

    /**
     * 读取本地文件,转换为字符串
     */
    public static String readFile2Str(String fileName) {
        File file = new File(fileName);
        return readFile2Str(file);
    }

    /**
     * 读取本地文件,转换为字符串
     */
    public static String readFile2Str(File file) {
        InputStream is = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //            String temp = null;
            //            while ((temp = reader.readLine()) != null) {
            //                sb.append(temp);
            //            }

            int n;
            char b[] = new char[1024];
            while ((n = reader.read(b)) != -1) {
                sb.append(b, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                close(reader);
                close(is);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return sb.toString();
    }

    /**
     * 读取本地文件，转换为Reader
     * @param file
     * @return
     */
    public static Reader readFile2Reader(String fileName) {
        File file = new File(fileName);
        return readFile2Reader(file);
    }

    /**
     * 读取本地文件，转换为Reader
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Reader readFile2Reader(File file) {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                close(reader);
                close(is);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        return reader;
    }

    /**
     * 正常关闭流
     * @param stream
     * @throws IOException
     */
    private static void close(Closeable stream) throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

}
