/* 
 * 文件名：MsgInfoException.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.exception;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class MsgInfoException extends Exception {

    private static final long serialVersionUID = 4866971671056859818L;

    private int errorCode;

    public MsgInfoException() {
        super();
    }

    public MsgInfoException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public MsgInfoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MsgInfoException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MsgInfoException(String detailMessage) {
        super(detailMessage);
    }

    public MsgInfoException(Throwable throwable) {
        super(throwable);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
