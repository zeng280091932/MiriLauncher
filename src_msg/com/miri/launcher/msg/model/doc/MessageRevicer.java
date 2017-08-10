/* 
 * 文件名：MessageRevicer.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.model.doc;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.miri.launcher.msg.model.Operate;
import com.miri.launcher.msg.model.Servers;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class MessageRevicer implements Serializable {

    private static final long serialVersionUID = 1420187624190004919L;

    Integer resultCode;

    String mac;

    String authCode;

    @SerializedName("operates")
    List<Operate> operateInfo;

    @SerializedName("serverInfo")
    Servers serverInfo;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
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

    public List<Operate> getOperateInfo() {
        return operateInfo;
    }

    public void setOperateInfo(List<Operate> operateInfo) {
        this.operateInfo = operateInfo;
    }

    public Servers getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(Servers serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public String toString() {
        return "MessageRevicer [resultCode=" + resultCode + ", mac=" + mac + ", authCode="
                + authCode + ", operateInfo=" + operateInfo + ", serverInfo=" + serverInfo + "]";
    }

}
