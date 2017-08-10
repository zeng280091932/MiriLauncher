package com.miri.launcher.json;

/**
 * 解析json异常类
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-20
 */
public class JsonParserException extends Exception {

    private static final long serialVersionUID = -2765601761974750653L;

    protected Throwable detail;

    protected int row = -1;

    protected int column = -1;

    public JsonParserException() {
        super();
    }

    public JsonParserException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException(Throwable throwable) {
        super(throwable);
    }

}
