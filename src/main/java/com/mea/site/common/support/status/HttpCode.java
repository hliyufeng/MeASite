package com.mea.site.common.support.status;

/**
 * 系统状态码
 *
 * @author MichaelJou
 * @version 2016年5月20日 下午3:19:19
 */
public enum HttpCode {
    /**
     * 200请求成功
     */
    OK("200", "SUCCESS"),
    ERROR("-1", "System error"),
    FAIL("-2","FAIL"),
    SESSION_FAILURE("401","Session failure"),
    SERVER_ERROR("500", "System internal error"),
    ERROR_PARAM("-3","Parameter error");

    private final String value;
    private final String msg;

    HttpCode(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    /**
     * Return the integer value of this status code.
     */
    public String value() {
        return this.value;
    }

    public String msg() {
        return msg;
    }
}
