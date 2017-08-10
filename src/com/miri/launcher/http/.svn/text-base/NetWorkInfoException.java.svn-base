package com.miri.launcher.http;

/**
 * HTTP网络请求异常类
 */
public class NetWorkInfoException extends Exception {

    private static final long serialVersionUID = -7424277169772476521L;

    private int responseCode = -1;

    public NetWorkInfoException() {
        super();
    }

    public NetWorkInfoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetWorkInfoException(String detailMessage) {
        super(detailMessage);
    }

    public NetWorkInfoException(Throwable throwable) {
        super(throwable);
    }

    public NetWorkInfoException(String msg, int responseCode) {
        super(msg);
        this.responseCode = responseCode;
    }

    public NetWorkInfoException(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
