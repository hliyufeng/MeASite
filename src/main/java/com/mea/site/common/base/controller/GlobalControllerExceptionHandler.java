package com.mea.site.common.base.controller;

import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.status.HttpCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Created by Michael Jou on 2018/3/4. 2:12
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {


    /**
     * 服务器异常
     * @param ex
     * @return
     */
    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity unknownException(Exception ex) {
        return ResponseEntity.build().codeMsg(HttpCode.SERVER_ERROR.value(),HttpCode.SERVER_ERROR.msg());
    }
}
