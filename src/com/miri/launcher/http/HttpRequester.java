package com.miri.launcher.http;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.miri.launcher.utils.Logger;

/**
 * HTTP请求对象
 */
public class HttpRequester {

    /** 响应字符集 */
    private String defaultContentEncoding;

    /** 连接主机超时时间 (ms) */
    private Integer connectTimeout;

    /** 主机读取数据超时时间 */
    private Integer readTimeout;

    public HttpRequester() {
        //        defaultContentEncoding = Charset.defaultCharset().name();
        defaultContentEncoding = "UTF-8";
        connectTimeout = 30 * 1000;
        readTimeout = 30 * 1000;
    }

    /**
     * 发送HTTP请求
     * @param urlString
     * @param method
     * @param parameters 参数集合
     * @param propertys 请求属性集合
     * @return 返回读取的字符串
     * @throws NetWorkInfoException
     */
    protected String sendBeStr(String urlString, String method, Map<String, String> parameters,
            Map<String, String> propertys) throws NetWorkInfoException {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        BufferedReader buffer = null;
        StringWriter writer = new StringWriter();
        try {
            if (method.equalsIgnoreCase("GET") && (parameters != null)) {
                StringBuffer param = new StringBuffer();
                int i = 0;
                for (String key: parameters.keySet()) {
                    if (i == 0) {
                        param.append("?");
                    } else {
                        param.append("&");
                    }
                    param.append(key).append("=").append(parameters.get(key));
                    i++;
                }
                urlString += param;
            }

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            // 设置请求发送方式,默认GET.发送POST请求必须设置允许输出.
            urlConnection.setRequestMethod(method);
            //设置是否允许输入,默认true
            urlConnection.setDoInput(true);
            //设置是否允许输出,默认false
            //        urlConnection.setDoOutput(true);
            // 设置不使用缓存
            urlConnection.setUseCaches(false);
            //设置连接主机超时（单位：毫秒）
            urlConnection.setConnectTimeout(connectTimeout);
            //设置从主机读取数据超时（单位：毫秒）
            urlConnection.setReadTimeout(readTimeout);

            if (propertys != null) {
                for (String key: propertys.keySet()) {
                    urlConnection.addRequestProperty(key, propertys.get(key));
                }
            }

            /*
             * <p>To reduce latency, this class may reuse the same underlying {@code Socket} for
             * multiple request/response pairs. As a result, HTTP connections may be held open
             * longer than necessary. Calls to {@link #disconnect()} may return the socket to a pool
             * of connected sockets. This behavior can be disabled by setting the "http.keepAlive"
             * system property to "false" before issuing any HTTP requests. The
             * "http.maxConnections" property may be used to control how many idle connections to
             * each server will be held.
             */

            //            urlConnection.addRequestProperty("http.keepAlive", "false");
            //解决 java.net.SocketException: recvfrom failed: ECONNRESET (Connection reset by peer)
            //解决java.io.EOFException at libcore.io.Streams.readAsciiLine(Streams.java:203)
            /*
             * It appears in some of the newer versions of android, there is a bug with recycled url
             * connections. To fix this (although there may be some performance issues), I needed to
             * add this code :
             */
            //            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            //                urlConnection.setRequestProperty("Connection", "close");
            //            }

            if (method.equalsIgnoreCase("POST") && (parameters != null)) {
                StringBuffer param = new StringBuffer();
                for (String key: parameters.keySet()) {
                    param.append("&");
                    param.append(key).append("=").append(parameters.get(key));
                }
                urlConnection.getOutputStream().write(param.toString().getBytes());
                urlConnection.getOutputStream().flush();
                urlConnection.getOutputStream().close();
            }
            //            Logger.getLogger().e("propertys:" + propertys);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            Logger.getLogger().d("http responseCode-->" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //http内容响应编码格式
                //                String ecod = urlConnection.getContentEncoding();
                //                Logger.getLogger().d("http content encoding-->" + ecod);
                //                if (ecod == null) {
                //                    ecod = this.defaultContentEncoding;
                //                }
                is = urlConnection.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(is, defaultContentEncoding));
                char buf[] = new char[1024];
                int n;
                while ((n = buffer.read(buf)) != -1) {
                    writer.write(buf, 0, n);
                }
                //               String result = new String(writer.toString().getBytes(), this.defaultContentEncoding);
            } else {
                Logger.getLogger().e("[URL][response][failure][code : " + responseCode + " ]");
                throw new NetWorkInfoException("[URL][response][failure][code : " + responseCode
                        + " ]", responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetWorkInfoException(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new NetWorkInfoException(e);
        } finally {
            try {
                close(buffer);
                close(is);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        }

        return writer.append("").toString();
    }

    /**
     * 发送HTTP请求
     * @param urlString
     * @param method
     * @param parameters 参数集合
     * @param propertys 请求属性集合
     * @return Reader流
     * @throws NetWorkInfoException
     */
    protected Reader sendBeReader(String urlString, String method, Map<String, String> parameters,
            Map<String, String> propertys) throws NetWorkInfoException {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        BufferedReader buffer = null;
        try {
            if (method.equalsIgnoreCase("GET") && (parameters != null)) {
                StringBuffer param = new StringBuffer();
                int i = 0;
                for (String key: parameters.keySet()) {
                    if (i == 0) {
                        param.append("?");
                    } else {
                        param.append("&");
                    }
                    param.append(key).append("=").append(parameters.get(key));
                    i++;
                }
                urlString += param;
            }

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            // 设置请求发送方式,默认GET.发送POST请求必须设置允许输出.
            urlConnection.setRequestMethod(method);
            // 设置是否允许输入,默认true
            //            urlConnection.setDoOutput(true);
            // 设置是否允许输出,默认false
            //            urlConnection.setDoInput(true);
            // 设置不使用缓存
            urlConnection.setUseCaches(false);
            //设置连接主机超时（单位：毫秒）
            urlConnection.setConnectTimeout(connectTimeout);
            //设置从主机读取数据超时（单位：毫秒）
            urlConnection.setReadTimeout(readTimeout);

            if (propertys != null) {
                for (String key: propertys.keySet()) {
                    urlConnection.addRequestProperty(key, propertys.get(key));
                }
            }

            //            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            //                urlConnection.setRequestProperty("Connection", "close");
            //            }

            if (method.equalsIgnoreCase("POST") && (parameters != null)) {
                StringBuffer param = new StringBuffer();
                for (String key: parameters.keySet()) {
                    param.append("&");
                    param.append(key).append("=").append(parameters.get(key));
                }
                urlConnection.getOutputStream().write(param.toString().getBytes());
                urlConnection.getOutputStream().flush();
                urlConnection.getOutputStream().close();
            }
            //            Logger.getLogger().e("propertys:" + propertys);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            Logger.getLogger().d("http responseCode-->" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {

                //                String ecod = urlConnection.getContentEncoding();
                //                Logger.getLogger().d("http content encoding-->" + ecod);
                //                if (ecod == null) {
                //                    ecod = this.defaultContentEncoding;
                //                }

                is = urlConnection.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(is, defaultContentEncoding));
            } else {
                Logger.getLogger().e("[URL][response][failure][code : " + responseCode + " ]");
                throw new NetWorkInfoException("[URL][response][failure][code : " + responseCode
                        + " ]", responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                close(buffer);
                close(is);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            }
            throw new NetWorkInfoException(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            try {
                close(buffer);
                close(is);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (RuntimeException e1) {
                e1.printStackTrace();
            }
            throw new NetWorkInfoException(e);
        }

        return buffer;
    }

    /**
     * 正常关闭流
     * @param stream
     * @throws IOException
     */
    private void close(Closeable stream) throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding() {
        return defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     */
    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }

    /**
     * 默认连接主机超时时间
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * 设置连接主机超时时间
     * @return
     */
    public void setConnectTimeout(Integer connectTimeout) {
        if (connectTimeout != null && connectTimeout > 0) {
            this.connectTimeout = connectTimeout;
        }
    }

    /**
     * 默认主机读取数据超时时间
     */
    public Integer getReadTimeout() {
        return readTimeout;
    }

    /**
     * 设置从主机读取数据超时时间
     * @param readTimeout
     */
    public void setReadTimeout(Integer readTimeout) {
        if (readTimeout != null && readTimeout > 0) {
            this.readTimeout = readTimeout;
        }
    }

}
