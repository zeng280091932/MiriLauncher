package com.miri.launcher.market;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.URLUtil;

final public class NetUtil {

    // The maximum number of connections allowed overall.
    public final static int MAX_TOTAL_CONNECTIONS = 1000;

    // The maximum number of connections allowed per route.
    public final static int MAX_ROUTE_CONNECTIONS = 500;

    // The waiting timeout in milliseconds.
    public final static int WAIT_TIMEOUT = 5 * 1000;

    // The connection timeout in milliseconds.
    public final static int CONNECT_TIMEOUT = 10 * 1000;

    // The reading timeout in milliseconds.
    public final static int READ_TIMEOUT = 8 * 1000;

    private static HttpParams httpParams;

    private static ClientConnectionManager connectionManager;

    // Use only one HTTP client to reduce memory & CPU usage.
    private static DefaultHttpClient httpClient;

    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount > 3) {
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                return true;
            }
            return false;
        }

    };

    static {
        httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        connectionManager = new ThreadSafeClientConnManager(httpParams, registry);
        httpClient = new DefaultHttpClient(connectionManager, httpParams);
        httpClient.setHttpRequestRetryHandler(requestRetryHandler);
    }

    public static DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    public static Bitmap getBitMap(String urlStr) {
        Bitmap bitmap = null;
        HttpPost request = new HttpPost(urlStr);
        try {
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            request.abort();
        }
        return bitmap;
    }

    public static String getResponseStr(String urlStr, List<NameValuePair> params) {
        String responseStr = null;
        HttpPost request = new HttpPost(urlStr);
        try {
            if (params != null) {
                request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            request.abort();
        }
        return responseStr;
    }

    public static JSONObject getJSONObject(String urlStr, List<NameValuePair> params) {
        HttpPost request = new HttpPost(urlStr);
        try {
            if (params != null) {
                request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            return new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            request.abort();
            return null;
        }
    }

    public static JSONArray getJSONArray(String urlStr, List<NameValuePair> params) {
        HttpPost request = new HttpPost(urlStr);
        try {
            if (params != null) {
                request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            return new JSONArray(EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            request.abort();
            return null;
        }
    }

    /**
     * Put all URL parameters into a map.
     */
    public static Map<String, String> getURLParameters(String urlStr) {
        if (!URLUtil.isValidUrl(urlStr)) {
            return null;
        }
        if (urlStr.indexOf("?") == -1) {
            return null;
        }
        final String infoStr[] = urlStr.substring(urlStr.indexOf("?") + 1).split("&");
        Map<String, String> parameters = new HashMap<String, String>();
        String paramStr[];
        for (String subStr: infoStr) {
            paramStr = subStr.split("=", 2);
            try {
                paramStr[1] = URLDecoder.decode(paramStr[1], "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                paramStr[1] = null;
            }
            parameters.put(paramStr[0], paramStr[1]);
        }
        return parameters;
    }

    /**
     * Get a specified URL parameter.
     */
    public static String getURLParameter(String urlStr, String paramName) {
        if (urlStr == null || paramName == null) {
            return null;
        }

        int start = urlStr.indexOf(paramName + "=", urlStr.indexOf("?") + 1);
        if (start == -1) {
            return null;
        }

        start += paramName.length() + 1;
        int end = urlStr.indexOf("&", start);
        if (end == -1) {
            end = urlStr.length();
        }
        if (start >= end) {
            return null;
        }

        try {
            return URLDecoder.decode(urlStr.substring(start, end), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
