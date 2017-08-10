/* 
 * 文件名：ImageGetFromHttp.java
 * 版权：Copyright
 */
package com.miri.launcher.imageCache.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.miri.launcher.utils.Logger;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-7-29
 */
public class ImageGetFromHttp {

    public static Bitmap downloadBitmap(String url) {
        //        return getBitMapFromUrl(url);
        return getBitMapFromUrl2(url);
    }

    /**
     * 从网络上下载
     * @param url
     * @return
     */
    public static Bitmap getBitMapFromUrl2(String url) {

        final HttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Logger.getLogger()
                        .e("Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = null;
                FilterInputStream fit = null;
                BufferedInputStream bis = null;
                ByteArrayOutputStream out = null;
                Bitmap bitmap = null;
                try {
                    is = entity.getContent();
                    fit = new FlushedInputStream(is);
                    bis = new BufferedInputStream(fit, 1024 * 8);
                    out = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = bis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    byte[] data = out.toByteArray();
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    return bitmap;
                } finally {
                    close(out);
                    close(bis);
                    close(fit);
                    close(is);
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            getRequest.abort();
            Logger.getLogger().e("I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Logger.getLogger().e("Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            Logger.getLogger().e("Error while retrieving bitmap from " + url, e);
        } finally {
            client.getConnectionManager().shutdown();
        }
        return null;

    }

    public static Bitmap getBitMapFromUrl(String url) {
        URL m;
        HttpURLConnection conn = null;
        InputStream i = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream out = null;
        Bitmap bitmap = null;
        try {
            m = new URL(url);
            conn = (HttpURLConnection) m.openConnection();
            i = conn.getInputStream();
            bis = new BufferedInputStream(i, 1024 * 8);
            out = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            byte[] data = out.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(out);
            close(bis);
            close(i);
            conn.disconnect();
        }
        return bitmap;
    }

    /**
     * 关闭流
     * @param stream
     * @throws QueryServerInfoException
     */
    private static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
                stream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
    static class FlushedInputStream extends FilterInputStream {

        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
