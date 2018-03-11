package com.mea.site.common.filter.shiro;

import com.alibaba.fastjson.JSON;
import com.feilong.core.Validator;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.status.HttpCode;
import com.mea.site.common.utils.WebUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Michael Jou on 2018/3/9. 16:12
 */
public class AjaxShiroFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            if(WebUtils.isAjax(request) && Validator.isNullOrEmpty(subject.getPrincipal())){
                WebUtils.writeJson((HttpServletResponse) response, JSON.toJSONString(ResponseEntity.build().OK(HttpCode.SESSION_FAILURE.value())));
                return false;
            }
            return true;
        }
        return true;
    }
}
