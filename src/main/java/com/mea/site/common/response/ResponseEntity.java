package com.mea.site.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageInfo;
import com.mea.site.common.support.status.HttpCode;

import java.io.Serializable;

/**
 * @author Michael Jou
 * @PACKAGE com.gladtrust.measite.common.response
 * @program: measite-manage
 * @description: 封装返回结构体
 * @create: 2018-02-06 13:59
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity<T> implements Serializable {

    /**
     * 编码
     */
    private String code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 二级编码
     */
    private String sub_code;
    /**
     * 二级消息体
     */

    private long count;
    private String sub_msg;
    private T data;
    private long timestamp = System.currentTimeMillis();

    private long pageSize;


    public static ResponseEntity build() {
        return new ResponseEntity();
    }

    public ResponseEntity OK() {
        this.code = HttpCode.OK.value();
        this.msg = HttpCode.OK.msg();
        return this;
    }

    /**
     * 成功状态吗
     *
     * @return
     */
    public ResponseEntity OK(T t) {
        this.code = HttpCode.OK.value();
        this.msg = HttpCode.OK.msg();
        this.put(t);
        return this;
    }

    public ResponseEntity calPage(PageInfo<T> pageInfo){
        this.count = pageInfo.getTotal();
        this.pageSize = pageInfo.getPageSize();
        this.data = (T) pageInfo.getList();
        return this;
    }
    /**
     * 成功状态吗
     *
     * @return
     */
    public ResponseEntity OK(String msg) {
        this.code = HttpCode.OK.value();
        this.msg = msg;
        return this;
    }

    public ResponseEntity put(T data) {
        this.data = data;
        return this;
    }

    public ResponseEntity error() {
        this.code = HttpCode.ERROR.value();
        this.msg = HttpCode.ERROR.msg();
        return this;
    }


    public ResponseEntity error(String msg) {
        this.code = HttpCode.ERROR.value();
        this.msg = msg;
        return this;
    }

    public ResponseEntity fail() {
        this.code = HttpCode.FAIL.value();
        this.msg = HttpCode.FAIL.msg();
        return this;
    }

    public ResponseEntity errorParam() {
        this.code = HttpCode.ERROR_PARAM.value();
        this.msg = HttpCode.ERROR_PARAM.msg();
        return this;
    }

    public ResponseEntity errorParam(String msg) {
        this.code = HttpCode.ERROR_PARAM.value();
        this.msg = msg;
        return this;
    }
    public ResponseEntity errorParam(String code,String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResponseEntity fail(String msg) {
        this.code = HttpCode.FAIL.value();
        this.msg = msg;
        return this;
    }

    public ResponseEntity fail(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }


    /**
     * 自己转入请求码
     *
     * @param code
     * @param msg
     * @return
     */
    public ResponseEntity codeMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }



    /**
     * 业务状态码
     *
     * @param sub_code
     * @param sub_msg
     * @return
     */
    public ResponseEntity subCodeMsg(String sub_code, String sub_msg) {
        this.sub_code = sub_code;
        this.sub_msg = sub_msg;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_msg() {
        return sub_msg;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


}
