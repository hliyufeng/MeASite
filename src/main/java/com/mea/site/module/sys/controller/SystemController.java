package com.mea.site.module.sys.controller;

import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

/**
 * Created by Michael Jou on 2018/3/9. 19:17
 */
@Controller
@RequestMapping(value = "/sys")
public class SystemController extends BaseController {

    @Autowired
    private SessionDAO sessionDAO;

    @RequestMapping(value = "cache")
    public String clear(){
        return WebUtils.requireView("/sys/cache/cache");
    }

    /**
     * 清理缓存
     *
     * @return
     */
    @RequestMapping(value = "clealAll")
    @ResponseBody
    public ResponseEntity clearAllChache(String type) {
        /**
         * 清除字典的缓存
         */
        if(Constant.DICT_KEY_CACHE.equals(type)){
            CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
        }else{
            Collection<Session> sessions = sessionDAO.getActiveSessions();
            UserUtils.clearUserCache(sessions);
            CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
        }
        return ResponseEntity.build().OK();
    }
}
