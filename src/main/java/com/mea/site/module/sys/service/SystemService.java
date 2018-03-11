package com.mea.site.module.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mea.site.common.exception.ServiceException;
import com.mea.site.common.support.Page;
import com.mea.site.common.utils.*;
import com.mea.site.module.sys.mapper.RoleMapper;
import com.mea.site.module.sys.mapper.UserMapper;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.model.Role;
import com.mea.site.module.sys.model.User;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

import static com.mea.site.common.base.service.BaseService.dataScopeFilter;

/**
 * Created by lenovo on 2018/2/26.
 */
@Service
@Transactional(readOnly = false)
public class SystemService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return
     */
    public User getUserByLoginName(String loginName) {
        return UserUtils.getByLoginName(loginName);
    }

    @Transactional(readOnly = false)
    public void updateUserLoginInfo(User user) {
        // 保存上次登录信息
        user.setOldLoginIp(user.getLoginIp());
        user.setOldLoginDate(user.getLoginDate());
        // 更新本次登录信息
        user.setLoginIp(IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
        user.setLoginDate(new Date());
        userMapper.updateLoginInfo(user);
    }


    public List<Role> findAllRole() {
        return UserUtils.getRoleList();
    }

    /**
     * 过来授权
     *
     * @return
     */
    public List<Role> findUserAllRole() {
        List<Role> roles = Lists.newArrayList();
        List<Role> roleList = UserUtils.getRoleList();
        if (ShiroUtils.getUser().isAdmin()) {
            return roleList;
        } else {
            if (CollectionUtils.isNotEmpty(roleList)) {
                roleList.forEach(role -> {
                    if (Role.IS_AUTH_OK.equals(role.getIsAuth())) {
                        roles.add(role);
                    }
                });
            }
            roleList = null;
            return roles;
        }

    }

    public List<Menu> findAllMenu() {
        return UserUtils.getMenuList();
    }

    public Role getRoleByName(String name) {
        Role r = new Role();
        r.setName(name);
        return roleMapper.getByName(r);
    }


    @Transactional(readOnly = false)
    public void saveRole(Role role) {
        if (StringUtils.isBlank(role.getId())) {
            role.preInsert();
            roleMapper.insertCustom(role);
        } else {
            role.preUpdate();
            roleMapper.update(role);
        }
        // 更新角色与菜单关联
        roleMapper.deleteRoleMenu(role);
        if (role.getMenuList().size() > 0) {
            roleMapper.insertRoleMenu(role);
        }
        // 更新角色与部门关联
        roleMapper.deleteRoleOffice(role);
        if (role.getOfficeList().size() > 0) {
            roleMapper.insertRoleOffice(role);
        }

        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
    }

    /**
     * @param enname
     * @return
     */
    public Role getRoleByEnname(String enname) {
        Role r = new Role();
        r.setEnname(enname);
        return roleMapper.getByEnname(r);
    }

    @Transactional(readOnly = false)
    public void deleteRole(Role role) {
        roleMapper.deleteLogic(role);
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
    }


    @Transactional(readOnly = false)
    public void saveUser(User user) {
        if (StringUtils.isBlank(user.getId())) {
            user.preInsert();
            userMapper.insertCustom(user);
        } else {
            // 清除原用户机构用户缓存
            User oldUser = userMapper.get(user.getId());
            if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
                CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
            }
            // 更新用户数据
            user.preUpdate();
            userMapper.update(user);
        }
        if (StringUtils.isNotBlank(user.getId())) {
            // 更新用户与角色关联
            userMapper.deleteUserRole(user);
            if (user.getRoleList() != null && user.getRoleList().size() > 0) {
                userMapper.insertUserRole(user);
            } else {
                throw new ServiceException(user.getLoginName() + "没有设置角色！");
            }
            // 清除用户缓存
            UserUtils.clearCache(user);
//			// 清除权限缓存
//			systemRealm.clearAllCachedAuthorizationInfo();
        }
    }

    /**
     * 无分页查询人员列表
     *
     * @param user
     * @return
     */
    public List<User> findUser(User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        List<User> list = userMapper.findList(user);
        return list;
    }


    public PageInfo<User> findUser(Page<User> page) {
        User user = page.getData();
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<User> users = userMapper.findList(user);
        PageInfo<User> pageInfos = new PageInfo<User>(users);
        return pageInfos;
    }

    @Transactional(readOnly = false)
    public User assignUserToRole(Role role, User user) {
        if (user == null) {
            return null;
        }
        List<String> roleIds = user.getRoleIdList();
        if (roleIds.contains(role.getId())) {
            return null;
        }
        user.getRoleList().add(role);
        saveUser(user);
        return user;
    }

    @Transactional(readOnly = false)
    public Boolean outUserInRole(Role role, User user) {
        List<Role> roles = user.getRoleList();
        for (Role e : roles) {
            if (e.getId().equals(role.getId())) {
                roles.remove(e);
                saveUser(user);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    public User getUser(String id) {
        return UserUtils.get(id);
    }


    public Role getRole(String id) {
        return roleMapper.get(id);
    }


    /**
     * 检查是否可分配
     *
     * @param id
     * @return
     */
    public boolean isDistributionRole(String id) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("idI", id)
                .andEqualTo("isDistribution", "0")
                .andEqualTo("delFlag", Role.DEL_FLAG_NORMAL);
        return this.roleMapper.selectCountByExample(example) > 0 ? true : false;
    }

    @Transactional(readOnly = false)
    public void updatePasswordById(String id, String loginName, String newPassword,String credentialsSalt) {
        User user = new User(id);
        user.setPassword(newPassword);
        user.setCredentialsSalt(credentialsSalt);
        userMapper.updatePasswordById(user);
        // 清除用户缓存
        user.setLoginName(loginName);
        UserUtils.clearCache(user);
    }

    /**
     * 删除用户
     *
     * @param user
     */
    @Transactional(readOnly = false)
    public void deleteUser(User user) {
        userMapper.deleteLogic(user);
        // 清除用户缓存
        UserUtils.clearCache(user);

    }
}
