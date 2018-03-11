package com.mea.site.common.exception;

/**
 * Created by Michael Jou on 2018/3/6. 17:15
 */
public class ServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
