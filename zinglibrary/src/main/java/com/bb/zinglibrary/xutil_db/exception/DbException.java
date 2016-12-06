package com.bb.zinglibrary.xutil_db.exception;

/**
 * Created by zing on 2016/12/3.
 */

public class DbException extends BaseException {
    private static final long serialVersionUID = 1L;

    public DbException() {
    }

    public DbException(String detailMessage) {
        super(detailMessage);
    }

    public DbException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DbException(Throwable throwable) {
        super(throwable);
    }
}
