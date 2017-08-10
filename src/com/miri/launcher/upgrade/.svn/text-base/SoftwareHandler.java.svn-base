/* 
 * 文件名：UpdateContentHandler.java
 */
package com.miri.launcher.upgrade;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 解析版本更新的xml文件的ContentHandler
 * @author penglin
 */
public class SoftwareHandler extends DefaultHandler {

    Software software;

    String tagName;

    public SoftwareHandler(Software softWare) {
        software = softWare;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
    }

    /**
     * @param namespaceURI xml命名空间
     * @param localName 标签本地名称 ，如<name></name>，本地名为name
     * @param qName 标签全限定名 ，如<abc:name></abc:name>,全限定名为abc:name
     * @param attr 属性
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attr)
            throws SAXException {
        tagName = qName;
        if (tagName.equals("Software")) {
            software = new Software();
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        tagName = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String temp = new String(ch, start, length);
        if (tagName.equals("Version")) {
            software.setVersion(temp);
        } else if (tagName.equals("Url")) {
            software.setUrl(temp);
        } else if (tagName.equals("Force")) {
            software.setForce(temp);
        } else if (tagName.equals("Log")) {
            software.setLog(temp);
        } else if (tagName.equals("PublicDate")) {
            software.setPublicDate(temp);
        } else if (tagName.equals("LastBuildDate")) {
            software.setLastBuildDate(temp);
        } else if (tagName.equals("VersionCode")) {
            if (temp != null) {
                software.setVersionCode(Integer.parseInt(temp));
            }
        }
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

}
