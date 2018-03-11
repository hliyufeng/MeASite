package com.mea.site.module.sys.controller;


import com.github.pagehelper.PageInfo;
import com.mea.site.common.annotation.Log;
import com.mea.site.common.base.controller.BaseController;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.response.ResponseEntity;
import com.mea.site.common.support.Page;
import com.mea.site.common.utils.*;
import com.mea.site.module.sys.dto.UserDto;
import com.mea.site.module.sys.model.Office;
import com.mea.site.module.sys.model.Role;
import com.mea.site.module.sys.model.User;
import com.mea.site.module.sys.service.SystemService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Michael Jou on 2018/3/6. 18:24
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {

    private String prefix = "sys/user";

    @Autowired
    private SystemService systemService;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getUser(id);
        } else {
            return new User();
        }
    }

    @Log("用户管理")
    @RequiresPermissions("sys:user:view")
    @GetMapping(value = {"/", ""})
    public String user(User user, Model model) {
        return WebUtils.requireView(prefix + "/user");
    }


    /**
     * 列表数据
     *
     * @param user
     * @param page
     * @return
     */
    @Log("用户列表数据")
    @RequiresPermissions("sys:user:list")
    @PostMapping(value = {"list"})
    @ResponseBody
    public ResponseEntity list(Page<User> page, User user) {
        PageInfo<User> pageInfo = systemService.findUser(page.put(user));
        List<UserDto> userDtos = Lists.newArrayList();
        List<User> userList = pageInfo.getList();
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(user1 -> {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(user1.getOffice(), userDto.getOffice());
                BeanUtils.copyProperties(user1.getCompany(), userDto.getCompany());
                BeanUtils.copyProperties(user1, userDto);
                userDtos.add(userDto);
            });
        }
        PageInfo<UserDto> pageInfo1 = new PageInfo<UserDto>();
        BeanUtils.copyProperties(pageInfo, pageInfo1);
        pageInfo1.setList(userDtos);
        return ResponseEntity.build().OK().calPage(pageInfo1);
    }


    @Log("用户表单")
    @RequiresPermissions({"sys:user:edit", "sys:user:add"})
    @RequestMapping(value = "form")
    public String form(User user, Model model) {
        if (user.getCompany() == null || user.getCompany().getId() == null) {
            user.setCompany(ShiroUtils.getUser().getCompany());
        }
        if (user.getOffice() == null || user.getOffice().getId() == null) {
            user.setOffice(ShiroUtils.getUser().getOffice());
        }
        List<Role> roles = systemService.findUserAllRole();
        List<Role> roleList = user.getRoleList();
        if (user != null && CollectionUtils.isNotEmpty(roleList) && roles != null && CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(role -> {
                String id = role.getId();
                roleList.forEach(role1 -> {
                    String id1 = role1.getId();
                    if (id.equals(id1)) {
                        role.setChecked(true);
                    }
                });
            });
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        model.addAttribute("sysUserType", DictUtils.getDictList(Constant.SYS_USER_TYPE));
        model.addAttribute("sysData", DictUtils.getDictList(Constant.SYS_DATA));
        return WebUtils.requireView(prefix + "/form");
    }

    @Log("保存用户")
    @RequiresPermissions({"sys:user:update", "sys:user:save"})
    @RequestMapping(value = "save")
    @ResponseBody
    public ResponseEntity save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        user.setCompany(new Office(request.getParameter("company.id")));
        user.setOffice(new Office(request.getParameter("office.id")));
        // 如果新密码为空，则不更换密码
        if (StringUtils.isNotBlank(user.getNewPassword())) {
            User user1 = EndecryptUtils.md5Password(user.getLoginName(), user.getNewPassword(), 2);
            user.setPassword(user1.getPassword());
            user.setCredentialsSalt(user1.getCredentialsSalt());
        }
        if (!beanValidator(user)) {
            return ResponseEntity.build().fail(Constant.SAVE_FAIL);
        }
        if (!checkLoginName(user.getOldLoginName(), user.getLoginName()).getData()) {
            return ResponseEntity.build().fail("保存用户'" + user.getLoginName() + "'失败，登录名已存在");
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (Role r : systemService.findAllRole()) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);
        // 保存用户信息
        systemService.saveUser(user);
        // 清除当前用户缓存
        if (user.getLoginName().equals(ShiroUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
            //UserUtils.getCacheMap().clear();
        }
        return ResponseEntity.build().OK("保存用户'" + user.getLoginName() + "'成功");
    }


    @Log("删除用户")
    @RequiresPermissions("sys:user:delete")
    @RequestMapping(value = "delete")
    @ResponseBody
    public ResponseEntity delete(User user) {
        if (ShiroUtils.getUser().getId().equals(user.getId())) {
            return ResponseEntity.build().fail("删除用户失败, 不允许删除当前用户");
        } else if (User.isAdmin(user.getId())) {
            return ResponseEntity.build().fail("删除用户失败, 不允许删除超级管理员用户");
        } else {
            systemService.deleteUser(user);
            return ResponseEntity.build().OK("删除用户成功");
        }

    }

    /**
     * 验证登录名是否有效
     *
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @Log("验证登录名是否有效")
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkLoginName")
    public ResponseEntity<Boolean> checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return ResponseEntity.build().OK(true);
        } else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
            return ResponseEntity.build().OK(true);
        }
        return ResponseEntity.build().OK(false);
    }


    @Log("修改密码")
    @RequestMapping(value = "/chagepwd")
    public String chagePwd() {
        return WebUtils.requireView(prefix + "/changePwd");
    }

    /**
     * 修改个人用户密码
     *
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @Log("修改密码")
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    @ResponseBody
    public ResponseEntity modifyPwd(String oldPassword, String newPassword, Model model) {
        User user = ShiroUtils.getUser();
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
            String oldPasswordMd5 = EndecryptUtils.md5Password(user.getLoginName(), oldPassword, 2, user.getCredentialsSalt());
            if (oldPasswordMd5.equals(user.getPassword())) {
                User user1 = EndecryptUtils.md5Password(user.getLoginName(), newPassword, 2);
                systemService.updatePasswordById(user.getId(), user.getLoginName(), user1.getPassword(), user1.getCredentialsSalt());
                return ResponseEntity.build().OK("修改密码成功");
            } else {
                return ResponseEntity.build().fail("修改密码失败，旧密码错误");
            }

        }
        return ResponseEntity.build().fail("旧密码和新密码不能为空!");
    }

}
