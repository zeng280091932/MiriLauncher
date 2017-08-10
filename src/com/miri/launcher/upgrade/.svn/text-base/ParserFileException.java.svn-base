package com.miri.launcher.upgrade;

/**
 * @author penglin
 */
public class ParserFileException extends Exception {

    private static final long serialVersionUID = -3115198209347016675L;

    private int responseCode = -1;

    public ParserFileException() {
        super();
    }

    public ParserFileException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParserFileException(String detailMessage) {
        super(detailMessage);
    }

    public ParserFileException(Throwable throwable) {
        super(throwable);
    }

    public ParserFileException(String msg, int responseCode) {
        super(msg);
        this.responseCode = responseCode;
    }

    public ParserFileException(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
