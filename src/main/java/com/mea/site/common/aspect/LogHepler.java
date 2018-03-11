package com.mea.site.common.aspect;

import com.mea.site.module.sys.mapper.LogMapper;
import com.mea.site.module.sys.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2018/2/27.
 */
@Component
public class LogHepler {
    @Autowired
    LogMapper logMapper;

    @Async
    public void save(Log log){
        logMapper.insertCustom(log);
    }
}
