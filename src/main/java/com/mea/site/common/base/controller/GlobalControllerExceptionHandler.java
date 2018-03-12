package com.mea.site.common.base.controller;

import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.status.HttpCode;
import com.mea.site.common.utils.WebUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Created by Michael Jou on 2018/3/4. 2:12
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = { UnauthorizedException.class })
    public String UnauthorizedException(){
        return WebUtils.redirect("/403");
    }

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
