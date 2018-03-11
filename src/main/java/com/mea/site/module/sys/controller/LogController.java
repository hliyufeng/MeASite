package com.mea.site.module.sys.controller;

import com.feilong.core.Validator;
import com.github.pagehelper.PageInfo;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.Page;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.sys.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by Michael Jou on 2018/3/9. 17:09
 */
@Controller
@RequestMapping(value = "/sys/log")
public class LogController extends BaseController{

    private String prefix = "sys/log";
    @Autowired
    private LogService logService;

    @Log("log日志页面")
    @RequestMapping(value = {"/", ""})
    public String log() {
        return WebUtils.requireView(prefix + "/log");
    }

    /**
     * 列表数据
     *
     * @param log
     * @return
     */
    @Log("log日志")
    @PostMapping(value = {"list"})
    @ResponseBody
    public ResponseEntity list(com.mea.site.module.sys.model.Log log,Page<com.mea.site.module.sys.model.Log> page) {
        if(Validator.isNullOrEmpty(log.getBeginDate())){
            log.setBeginDate(new Date());
        }
        if(Validator.isNullOrEmpty(log.getEndDate())){
            log.setEndDate(new Date());
        }
        PageInfo<Log> pageInfo = logService.findPage(page.put(log));
        return ResponseEntity.build().OK().calPage(pageInfo);
    }


}
