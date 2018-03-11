package com.mea.site.module.sys.controller;

import com.feilong.core.Validator;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.StringUtils;
import com.mea.site.common.utils.UserUtils;
import com.mea.site.common.utils.WebUtils;
import com.mea.site.module.sys.dto.MenuDto;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.service.MenuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
 * Created by lenovo on 2018/2/27.
 */
@Controller
@Slf4j
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {

    private String prefix = "sys/menu";

    @Autowired
    private MenuService menuService;

    @ModelAttribute("menu")
    public Menu get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return menuService.getMenu(id);
        } else {
            return new Menu();
        }
    }

    @Log("菜单页面")
    @RequiresPermissions("sys:menu:view")
    @GetMapping(value = {"/", ""})
    String view(Model model) {
        return WebUtils.requireView(prefix + "/menu");
    }


    @Log("查询菜单")
    @RequestMapping(value = "/menuList")
    @RequiresPermissions("user")
    @ResponseBody
    public ResponseEntity menuList() {
        List<MenuDto> menuDtoList = menuService.buildMenu();
        return ResponseEntity.build().OK(menuDtoList);
    }

    @Log("菜单列表")
    @RequiresPermissions("sys:menu:list")
    @RequestMapping("/list")
    @ResponseBody
    List<Menu> list() {
        List<Menu> list = Lists.newArrayList();
        List<Menu> sourcelist = UserUtils.getMenuList();
        Menu.sortList(list, sourcelist, Menu.getRootId(), true);
        return list;
    }

    /**
     * 新增
     *
     * @param model
     * @return
     */
    @Log("新增菜或者编辑页面访问")
    @RequiresPermissions({"sys:menu:add","sys:menu:edit"})
    @RequestMapping("/form")
    String form(Model model, Menu menu, String id) {
        String url = "/add";
        if (Validator.isNotNullOrEmpty(id)) {
            url = "/edit";
        } else {
            url = "/add";
        }
        if (menu.getParent() == null ||
                Validator.isNullOrEmpty(menu.getParent().getId())) {
            menu.setParent(new Menu(Menu.getRootId()));
        }
        menu.setParent(menuService.getMenu(menu.getParent().getId()));
        // 获取排序号，最末节点排序号+30
        if (Validator.isNullOrEmpty(menu.getId())) {
            List<Menu> list = Lists.newArrayList();
            List<Menu> sourcelist =UserUtils.getMenuList();
            Menu.sortList(list, sourcelist, menu.getParentId(), false);
            if (CollectionUtils.isNotEmpty(list)) {
                menu.setSort(list.get(list.size() - 1).getSort() + 30);
            }
        }
        model.addAttribute("menu", menu);
        return WebUtils.requireView(prefix + url);
    }

    @Log("保存或者更新菜单")
    @RequiresPermissions({"sys:menu:save","sys:menu:update"})
    @PostMapping(value = "save")
    @ResponseBody
    public ResponseEntity save(Menu menu, Model model, RedirectAttributes redirectAttributes) {
        if (!ShiroUtils.getUser().isAdmin()) {
            return ResponseEntity.build().fail("越权操作，只有超级管理员才能添加或修改数据！");
        }
        if (!beanValidator(menu)) {
            return ResponseEntity.build().fail(Constant.SAVE_FAIL);
        }
        menuService.saveMenu(menu);
        return ResponseEntity.build().OK(Constant.SAVE_SUCCESS);
    }


    @Log("删除菜单")
    @RequiresPermissions("sys:menu:delete")
    @PostMapping(value = "delete")
    @ResponseBody
    public ResponseEntity delete(Menu menu, RedirectAttributes redirectAttributes) {
        boolean isSuccess = menuService.deleteMenu(menu);
        if (isSuccess) {
            return ResponseEntity.build().OK(Constant.DEL_SUCCESS);
        }
        return ResponseEntity.build().fail(Constant.DEL_FAIL);
    }


    /**
     * isShowHide是否显示隐藏菜单
     *
     * @param extId
     * @param response
     * @return
     */
    @Log("获取菜单树")
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String isShowHide, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Menu> list = UserUtils.getMenuList();
        for (int i = 0; i < list.size(); i++) {
            Menu e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                if (isShowHide != null && isShowHide.equals("0") && e.getShow().equals("0")) {
                    continue;
                }
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }


}
