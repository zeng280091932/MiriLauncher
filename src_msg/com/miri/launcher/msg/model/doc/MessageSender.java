/* 
 * 文件名：MessageSender.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.model.doc;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class MessageSender implements Serializable {

    private static final long serialVersionUID = 7255886395896417599L;

    String messageType;

    String mac;

    String authCode;

    String chipModel;

    @SerializedName("version")
    String versionCode;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getChipModel() {
        return chipModel;
    }

    public void setChipModel(String chipModel) {
        this.chipModel = chipModel;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "MessageSender [messageType=" + messageType + ", mac=" + mac + ", authCode="
                + authCode + ", chipModel=" + chipModel + ", versionCode=" + versionCode + "]";
    }

}
