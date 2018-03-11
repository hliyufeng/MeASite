package com.mea.site.module.sys.controller;

import com.github.pagehelper.PageInfo;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.Page;
import com.mea.site.common.utils.CacheUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.sys.model.Dict;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by Michael Jou on 2018/3/2. 15:56
 * 字典管理
 */
@Controller
@RequestMapping(value = "/sys/dict")
public class DictController extends BaseController {

    private String prefix = "sys/dict";

    @Autowired
    private DictService dictService;

    @ModelAttribute
    public Dict get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return dictService.get(id);
        } else {
            return new Dict();
        }
    }

    @Log("访问字典管理")
    @RequiresPermissions("sys:dict:view")
    @RequestMapping(value = {"/", ""})
    public String dict(Dict dict) {
        return WebUtils.requireView(prefix + "/dict");
    }

    /**
     * 列表数据
     *
     * @param
     * @param model
     * @return
     */
    @Log("字典列表数据")
    @RequiresPermissions("sys:dict:list")
    @RequestMapping(value = {"list"})
    @ResponseBody
    public ResponseEntity list(Dict dict, Page<Dict> page, Model model) {
        PageInfo<Dict> list = dictService.findPage(page.put(dict));
        return ResponseEntity.build().OK().calPage(list);
    }


    /**
     * 更新
     *
     * @return
     */
    @Log("新增字典页面")
    @RequiresPermissions("sys:dict:add")
    @RequestMapping(value = "add")
    public String add(String type, String sort, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("sort", sort);
        return WebUtils.requireView(prefix + "/add");
    }

    /**
     * 编辑
     *
     * @return
     */
    @Log("编辑字典页面")
    @RequiresPermissions("sys:dict:edit")
    @RequestMapping(value = "edit")
    public String edit(Dict dict, Model model) {
        model.addAttribute("dict", dict);
        return WebUtils.requireView(prefix + "/edit");
    }

    @Log("字典插入或者更新")
    @RequiresPermissions({"sys:dict:save","sys:dict:update"})
    @PostMapping(value = "save")
    @ResponseBody
    public ResponseEntity save(Dict dict) {
        if (!beanValidator(dict)) {
            return ResponseEntity.build().fail(Constant.SAVE_FAIL);
        }
        dictService.saveCustom(dict);
        return ResponseEntity.build().OK(Constant.SAVE_SUCCESS);
    }

    @Log("删除字典")
    @RequiresPermissions("sys:dict:delete")
    @RequestMapping(value = "delete")
    @ResponseBody
    public ResponseEntity delete(Dict dict) {
        dictService.deleteLogic(dict);
        boolean isSuccess = dictService.deleteLogic(dict) > 0 ? true : false;
        if (isSuccess) {
            return ResponseEntity.build().OK(Constant.DEL_SUCCESS);
        }
        return ResponseEntity.build().OK(Constant.DEL_FAIL);
    }

    @Log("获取字典类型")
    @RequiresPermissions("user")
    @GetMapping(value = "getType")
    @ResponseBody
    public ResponseEntity getType() {
        List<String> list = dictService.findTypeList();
        return ResponseEntity.build().OK(list);
    }

}
