/* 
 * 文件名：JsonUtils.java
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.json.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.miri.launcher.http.HttpRequestHelper;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.utils.FileUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.MD5Encrypt;

/**
 * json常用工具类
 * @author penglin
 */
public class JsonUtils {

    /**
     * 读取Json文件，转换为字符串
     * <p>
     * 默认尝试加载磁盘缓存中的文件，转换成字符串，并对缓存的文件进行json格式校验
     * @param dir 缓存目录
     * @param urlString
     * @return
     */
    public static String readJSon2StrWithCache(String dir, String fileName, String urlString)
            throws NetWorkInfoException {
        fileName = MD5Encrypt.encryptStr(fileName);
        File file = null;

        //验证本地是否存在
        file = new File(dir, fileName);
        //本地存在
        if (file.exists() && file.length() > 0) {
            Logger.getLogger().d("File:" + fileName + " is exist! path:" + file.getAbsolutePath());
            //本地文件存在，验证Json格式是否正确。
            //用于验证标准的json格式，若需解析的json不标准，请注释以下代码
            JsonValidator jsonValid = new JsonValidator();
            boolean ret = jsonValid.validate(file);
            if (ret) {
                Logger.getLogger().d("Read Cached file:" + fileName + " is a valid json file。");
                return FileUtil.readFile2Str(file);
            } else {
                Logger.getLogger().e(
                        "Cached file [" + dir + File.separator + fileName
                                + "] json format is error!");
                //格式不正确，删除本地文件
                file.delete();
            }
        }

        //先下载到本地文件系统，出现异常，向上抛出
        downloadJson(dir, fileName, urlString);

        file = new File(dir, fileName);
        return FileUtil.readFile2Str(file);
    }

    /**
     * 读取Json文件，转换为Reader
     * <p>
     * 默认尝试加载磁盘缓存中的文件，转换成Reader流，并对缓存的文件进行json格式校验
     * @param dir 缓存目录
     * @param urlString epg文件url
     * @return
     */
    public static Reader readJSon2ReaderWithCache(String dir, String urlString)
            throws NetWorkInfoException {

        String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
        fileName = MD5Encrypt.encryptStr(fileName);
        File file = null;

        //验证本地是否存在
        file = new File(dir, fileName);
        //本地存在
        if (file.exists() && file.length() > 0) {
            Logger.getLogger().d("File:" + fileName + " is exist! path:" + file.getAbsolutePath());
            //本地文件存在，验证Json格式是否正确。
            //用于验证标准的json格式，若需解析的json不标准，请注释以下代码
            JsonValidator jsonValid = new JsonValidator();
            boolean ret = jsonValid.validate(file);
            if (ret) {
                Logger.getLogger().d("Reader Cached file:" + fileName + " is a valid json file。");
                return FileUtil.readFile2Reader(file);
            } else {
                Logger.getLogger().e(
                        "Cached file [" + dir + File.separator + fileName
                                + "] json format is error!");
                //格式不正确，删除本地文件
                file.delete();
            }
        }

        //先下载到本地文件系统，出现异常，向上抛出
        downloadJson(dir, fileName, urlString);

        file = new File(dir, fileName);
        return FileUtil.readFile2Reader(file);
    }

    /**
     * 将json下载到文件系统,下载json文件默认30s超时
     * @param dir
     * @param urlString
     * @return
     */
    public static void downloadJson(String dir, String fileName, String urlString)
            throws NetWorkInfoException {
        fileName = MD5Encrypt.encryptStr(fileName);
        File file = new File(dir, fileName);
        BufferedReader buffer = null;
        FileWriter fw = null;
        try {
            buffer = (BufferedReader) HttpRequestHelper.sendGetBeReader(urlString);
            if (buffer == null) {
                if (file.exists()) {
                    file.delete();
                }
                throw new NetWorkInfoException("the BufferedReader is null!");
            }

            fw = new FileWriter(file);
            char buf[] = new char[1024];
            int n;
            while ((n = buffer.read(buf)) != -1) {
                fw.write(buf, 0, n);
            }
            fw.flush();

        } catch (NetWorkInfoException e) {
            e.printStackTrace();
            if (file.exists()) {
                file.delete();
            }
            throw new NetWorkInfoException(e);
        } catch (IOException e) {
            e.printStackTrace();
            if (file.exists()) {
                file.delete();
            }
            throw new NetWorkInfoException(e);
        } finally {
            try {
                close(buffer);
                close(fw);
            } catch (IOException e) {
                e.printStackTrace();
                if (file.exists()) {
                    file.delete();
                }
                //                throw new NetWorkInfoException(e);
            }
        }
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
