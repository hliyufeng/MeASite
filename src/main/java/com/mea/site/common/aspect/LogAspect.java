package com.mea.site.common.aspect;

import com.alibaba.fastjson.JSON;
import com.mea.site.common.utils.HttpContextUtils;
import com.mea.site.common.utils.IPUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.module.sys.mapper.LogMapper;
import com.mea.site.module.sys.model.Log;
import com.mea.site.module.sys.model.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by lenovo on 2018/2/27.
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogHepler logHepler;

    @Pointcut("@annotation(com.mea.site.common.annotation.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        //结束时间
        long endTime = System.currentTimeMillis();
        // 执行时长(毫秒)
        long time = endTime - beginTime;
        //异步保存日志
        saveLog(point, time, beginTime, endTime);

        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time, long beginTime, long endTime) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log sysLog = new Log();
        com.mea.site.common.annotation.Log logA = method.getAnnotation(com.mea.site.common.annotation.Log.class);
        if (logA != null) {
            // 注解上的描述
            sysLog.setOperation(logA.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethodName(className + "." + methodName + "()");
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 设置IP地址
        sysLog.setRemoteAddr(IPUtils.getIpAddr(request));
        sysLog.setUserAgent(request.getHeader("user-agent"));
        sysLog.setParams(JSON.toJSONString(request.getParameterMap()));
        sysLog.setRequestUri(request.getRequestURI());
        sysLog.setMethod(request.getMethod());
        sysLog.setTime((int) time);
        sysLog.setRequestTime(beginTime);
        sysLog.setResponseTime(endTime);
        sysLog.setIsNewRecord(true);
        sysLog.preInsert();
        // 保存系统日志
        logHepler.save(sysLog);
    }
}
