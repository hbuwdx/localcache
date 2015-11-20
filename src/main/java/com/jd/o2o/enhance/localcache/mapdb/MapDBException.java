package com.jd.o2o.enhance.localcache.mapdb;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class MapDBException extends RuntimeException {

    private String message;

    private Exception exception;

    public MapDBException(String message) {
        super(message);
        this.message = message;
    }

    public MapDBException(String message, Exception exception){
        super(message,exception);
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
