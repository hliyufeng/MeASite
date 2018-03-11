package com.mea.site.module.sys.controller;

import com.feilong.core.Validator;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.utils.*;
import com.mea.site.module.sys.dto.UserDto;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.model.Office;
import com.mea.site.module.sys.model.Role;
import com.mea.site.module.sys.model.User;
import com.mea.site.module.sys.service.OfficeService;
import com.mea.site.module.sys.service.RoleService;
import com.mea.site.module.sys.service.SystemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.commons.collections.CollectionUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Created by MichaelJou on 2018/3/2.
 */
@Controller
@RequestMapping(value = "/sys/role")
public class RoleController extends BaseController {
    private String prefix = "sys/role";
    @Autowired
    private SystemService systemService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute("role")
    public Role get(@RequestParam(required = false) String id) {
        if (Validator.isNotNullOrEmpty(id)) {
            return roleService.getRole(id);
        } else {
            return new Role();
        }
    }

    @Log("角色列表页面")
    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = {"/", ""})
    public String view() {
        return WebUtils.requireView(prefix + "/role");
    }

    /**
     * 列表数据
     *
     * @param role
     * @return
     */
    @Log("角色列表数据")
    @RequiresPermissions("sys:role:list")
    @PostMapping(value = {"list"})
    @ResponseBody
    public ResponseEntity list(Role role) {
        List<Role> list = systemService.findAllRole();
        return ResponseEntity.build().OK(list);
    }

    @Log("角色编辑页面或者新增页面")
    @RequiresPermissions({"sys:role:add","sys:role:edit"})
    @RequestMapping(value = "form")
    public String form(Role role, Model model) {
        if (role.getOffice() == null) {
            role.setOffice(ShiroUtils.getUser().getOffice());
        }
        model.addAttribute("role", role);
        model.addAttribute("sysDataList", DictUtils.getDictList(Constant.SYS_DATA));
        model.addAttribute("sysDataScopeList", DictUtils.getDictList(Constant.SYS_DATA_SCOPE));
        return WebUtils.requireView(prefix + "/form");
    }

    @Log("角色获取菜单树")
    @RequiresPermissions("user")
    @GetMapping(value = "menuList")
    @ResponseBody
    public ResponseEntity menuList() {
        List<Menu> menus = systemService.findAllMenu();
        List<Map<String, Object>> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(menus)) {
            menus.forEach(menu -> {
                Map<String, Object> data = Maps.newHashMap();
                data.put("id", menu.getId());
                data.put("pId", StringUtils.isNotBlank(menu.getParent().getId()) ? menu.getParent().getId() : 0);
                data.put("name", StringUtils.isNotBlank(menu.getParent().getId()) ? menu.getName() : "权限列表");
                list.add(data);
            });
        }
        return ResponseEntity.build().OK(list);
    }

    @Log("角色获取机构树")
    @RequiresPermissions("user")
    @GetMapping(value = "officeList")
    @ResponseBody
    public ResponseEntity officeList() {
        List<Office> offices = officeService.findAll();
        List<Map<String, Object>> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(offices)) {
            offices.forEach(office -> {
                Map<String, Object> data = Maps.newHashMap();
                data.put("id", office.getId());
                data.put("pId", StringUtils.isNotBlank(office.getParent().getId()) ? office.getParent().getId() : 0);
                data.put("name", office.getName());
                list.add(data);
            });
        }
        return ResponseEntity.build().OK(list);
    }


    @Log("角色保存")
    @RequiresPermissions({"sys:role:save","sys:role:update"})
    @PostMapping(value = "save")
    @ResponseBody
    public ResponseEntity save(Role role) {
        if (!ShiroUtils.getUser().isAdmin() && role.getSysData().equals(Constant.YES)) {
            return ResponseEntity.build().fail("越权操作，只有超级管理员才能修改此数据！");
        }

        if (!beanValidator(role)) {
            return ResponseEntity.build().fail(Constant.SAVE_FAIL);
        }
        if (!(checkName(role.getOldName(), role.getName()).getData())) {

            return ResponseEntity.build().fail("保存角色'" + role.getName() + "'失败, 角色名已存在");
        }
        if (!(checkEnname(role.getOldEnname(), role.getEnname()).getData())) {
            return ResponseEntity.build().fail("保存角色'" + role.getName() + "'失败, 英文名已存在");
        }
        systemService.saveRole(role);
        return ResponseEntity.build().OK(Constant.SAVE_SUCCESS);
    }

    /**
     * 验证角色名是否有效
     *
     * @param oldName
     * @param name
     * @return
     */
    @Log("验证角色名是否有效")
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkName")
    public ResponseEntity<Boolean> checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return ResponseEntity.build().OK(true);
        } else if (name != null && systemService.getRoleByName(name) == null) {
            return ResponseEntity.build().OK(true);
        }
        return ResponseEntity.build().OK(false);
    }

    /**
     * 验证角色英文名是否有效
     *
     * @param oldEnname
     * @param enname
     * @return
     */
    @Log("验证角色英文名是否有效")
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkEnname")
    public ResponseEntity<Boolean> checkEnname(String oldEnname, String enname) {
        if (enname != null && enname.equals(oldEnname)) {
            return ResponseEntity.build().OK(true);
        } else if (enname != null && systemService.getRoleByEnname(enname) == null) {
            return ResponseEntity.build().OK(true);
        }
        return ResponseEntity.build().OK(false);
    }

    @Log("角色删除")
    @RequiresPermissions("sys:role:delete")
    @RequestMapping(value = "delete")
    @ResponseBody
    public ResponseEntity delete(Role role, RedirectAttributes redirectAttributes) {
        if (!ShiroUtils.getUser().isAdmin() && role.getSysData().equals(Constant.YES)) {
            return ResponseEntity.build().fail("越权操作，只有超级管理员才能修改此数据！");
        }
        systemService.deleteRole(role);
        return ResponseEntity.build().OK(Constant.DEL_SUCCESS);
    }


    /**
     * 角色分配 -- 打开角色分配对话框
     *
     * @param role
     * @param model
     * @return
     */
    @Log("角色分配 -- 打开角色分配对话框")
    @RequiresPermissions("sys:role:usertorole")
    @RequestMapping(value = "usertorole")
    public String selectUserToRole(Role role, Model model) {
        model.addAttribute("role", role);
        return WebUtils.requireView(prefix + "/selectUserToRole");
    }


    @Log("角色->部门")
    @RequiresPermissions("user")
    @RequestMapping(value = "userOfficeList")
    @ResponseBody
    public ResponseEntity userOfficeList(String id) {
        List<User> userList = systemService.findUser(new User(new Role(id)));
        Map<String, Object> map = Maps.newConcurrentMap();
        List<Office> officeList = officeService.findAll();
        List<Map<String, Object>> offices = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(officeList)) {
            officeList.forEach(office -> {
                Map<String, Object> officeMap = Maps.newConcurrentMap();
                officeMap.put("id", office.getId());
                officeMap.put("pId", Validator.isNotNullOrEmpty(office.getParent()) ? office.getParent().getId() : 0);
                officeMap.put("name", office.getName());
                offices.add(officeMap);
            });
        }
        map.put("userList", userList);
        map.put("selectIds", Collections3.extractToString(userList, "name", ","));
        map.put("officeList", offices);
        return ResponseEntity.build().OK(map);
    }

    /**
     * 角色分配 -- 根据部门编号获取用户列表
     *
     * @param officeId
     * @return
     */
    @Log("角色分配 -- 根据部门编号获取用户列表")
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "users")
    public ResponseEntity users(String officeId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        User user = new User();
        user.setOffice(new Office(officeId));
        List<User> userList = systemService.findUser(user);
        for (User e : userList) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", 0);
            map.put("name", e.getName());
            mapList.add(map);
        }
        return ResponseEntity.build().OK(mapList);
    }

    /**
     * 角色分配页面
     *
     * @param role
     * @param model
     * @return
     */
    @Log("角色分配页面")
    @RequiresPermissions("sys:role:assign")
    @RequestMapping(value = "assign")
    public String assign(Role role, Model model) {
        model.addAttribute("id", role.getId());
        return WebUtils.requireView(prefix + "/roleAssign");
    }

    @Log("角色分配数据")
    @RequiresPermissions("sys:role:assignList")
    @RequestMapping(value = "assignList")
    @ResponseBody
    public ResponseEntity assignList(Role role) {
        List<User> userList = systemService.findUser(new User(new Role(role.getId())));
        List<UserDto> userDtos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(user -> {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(user.getOffice(), userDto.getOffice());
                BeanUtils.copyProperties(user.getCompany(), userDto.getCompany());
                BeanUtils.copyProperties(user, userDto);
                userDtos.add(userDto);
            });
        }
        return ResponseEntity.build().OK(userDtos);
    }

    /**
     * 角色分配
     *
     * @param role
     * @param idsArr
     * @return
     */
    @Log("角色保存")
    @RequiresPermissions("sys:role:assignrole")
    @RequestMapping(value = "assignrole")
    @ResponseBody
    public ResponseEntity assignRole(Role role, String[] idsArr) throws ServiceException {

       if(systemService.isDistributionRole(role.getId())){
           return ResponseEntity.build().fail("此用户无权分配 【" + role.getName() + "】角色！");
       }
        StringBuilder msg = new StringBuilder();
        int newNum = 0;
        for (int i = 0; i < idsArr.length; i++) {
            User user = systemService.assignUserToRole(role, systemService.getUser(idsArr[i]));
            if (null != user) {
                msg.append("新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
                newNum++;
            }
        }
        return ResponseEntity.build().OK("已成功分配 " + newNum + " 个用户" + msg);
    }

    /**
     * 角色分配 -- 从角色中移除用户
     *
     * @param userId
     * @param roleId
     * @param redirectAttributes
     * @return
     */
    @Log("移除角色")
    @RequiresPermissions("sys:role:outrole")
    @RequestMapping(value = "outrole")
    @ResponseBody
    public ResponseEntity outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
        Role role = systemService.getRole(roleId);
        User user = systemService.getUser(userId);
        if (ShiroUtils.getUser().getId().equals(userId)) {
            return ResponseEntity.build().fail("无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
        } else {
            if (user.getRoleList().size() <= 1) {
                return ResponseEntity.build().fail("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
            } else {
                Boolean flag = systemService.outUserInRole(role, user);
                if (!flag) {
                    return ResponseEntity.build().fail("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
                } else {
                    return ResponseEntity.build().OK("用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
                }
            }
        }
    }


}
