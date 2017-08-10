/* 
 * 文件名：SoftwareParser.java
 */
package com.miri.launcher.upgrade;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.utils.Logger;

/**
 * @author penglin
 * @version 2013-6-18
 */
public class SoftwareParser {

    /**
     * 解析软件XML升级文件
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws NetWorkInfoException
     * @throws ParserConfigurationException
     */
    public static Software parseXmlSoftware(String urlString) throws NetWorkInfoException,
            ParserFileException {
        Software software = null;
        Long start = System.currentTimeMillis();
        String xmlStr = downUpgradeFile(urlString);
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            SoftwareHandler updateHandler = new SoftwareHandler(software);
            xmlReader.setContentHandler(updateHandler);
            xmlReader.parse(new InputSource(new StringReader(xmlStr)));
            software = updateHandler.getSoftware();
            Long end = System.currentTimeMillis();
            Logger.getLogger().d("parse upgrade file cost time : " + (end - start) + "ms");
            return software;
        } catch (SAXException e) {
            throw new ParserFileException(e);
        } catch (ParserConfigurationException e) {
            throw new ParserFileException(e);
        } catch (IOException e) {
            throw new ParserFileException(e);
        } catch (Exception e) {
            throw new ParserFileException(e);
        }

    }

    /**
     * 解析软件json升级文件
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws SAXException
     * @throws NetWorkInfoException
     * @throws ParserConfigurationException
     */
    public static Software parseJsonSoftware(String urlString) throws NetWorkInfoException,
            ParserFileException {
        Long start = System.currentTimeMillis();
        String jsonStr = downUpgradeFile(urlString);
        try {
            JSONObject jsonObject = new JSONObject(jsonStr.toString()).getJSONObject("Software");
            String version = jsonObject.getString("Version");
            int versionCode = jsonObject.getInt("VersionCode");
            String publicDate = jsonObject.getString("PublicDate");
            String lastBuildDate = jsonObject.getString("LastBuildDate");
            String url = jsonObject.getString("Url");
            String force = jsonObject.getString("Force");
            String log = jsonObject.getString("Log");
            Software software = new Software();
            software.setVersion(version);
            software.setVersionCode(versionCode);
            software.setPublicDate(publicDate);
            software.setLastBuildDate(lastBuildDate);
            software.setUrl(url);
            software.setForce(force);
            software.setLog(log);
            Long end = System.currentTimeMillis();
            Logger.getLogger().d("parse upgrade file cost time : " + (end - start) + "ms");
            return software;
        } catch (JSONException e) {
            throw new ParserFileException(e);
        } catch (Exception e) {
            throw new ParserFileException(e);
        }

    }

    /**
     * @param urlString
     * @return
     */
    public static String downUpgradeFile(String urlString) throws NetWorkInfoException {
        HttpClient httpclient = new DefaultHttpClient();
        //        HttpPost httppost = new HttpPost(urlString);
        HttpGet httpGet = new HttpGet(urlString);
        try {
            //POST请求参数
            //            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //            httppost.setHeader("Charset", "UTF-8");
            //            httppost.setHeader("Content-type", "application/x-www-form-urlencoded");

            HttpResponse response = httpclient.execute(httpGet); //发起GET请求
            int code = response.getStatusLine().getStatusCode();//返回响应码
            Logger.getLogger().d("http responseCode-->" + code);
            /* 若状态码为200 ok */
            if (code == 200) {
                String strResult = EntityUtils.toString(response.getEntity(), "UTF-8");
                Logger.getLogger().d("http strResult-->" + strResult);
                return strResult;
            } else {
                throw new NetWorkInfoException(code);
            }
        } catch (IOException e) {
            throw new NetWorkInfoException(e);
        }
    }

}
