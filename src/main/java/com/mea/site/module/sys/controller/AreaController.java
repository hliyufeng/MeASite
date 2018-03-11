package com.mea.site.module.sys.controller;

import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.sys.model.Area;
import com.mea.site.module.sys.model.User;
import com.mea.site.module.sys.service.AreaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jou on 2018/3/8. 17:40
 */
@Controller
@RequestMapping(value = "/sys/area")
public class AreaController extends BaseController {


    @Autowired
    private AreaService areaService;




    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Area> list = areaService.findAll();
        for (int i = 0; i < list.size(); i++) {
            Area e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()))) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                map.put("open",false);
                mapList.add(map);
            }
        }
        return mapList;
    }

}
