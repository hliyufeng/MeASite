package com.mea.site.common.utils;

import com.mea.site.common.base.service.BaseService;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.module.sys.mapper.*;
import com.mea.site.module.sys.model.*;
import com.mea.site.module.sys.security.UserAuthorizingRealm;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Michael Jou on 2018/3/5. 14:32
 */
public class UserUtils {

    private static UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private static MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private static AreaMapper areaMapper = SpringContextHolder.getBean(AreaMapper.class);
    private static OfficeMapper officeMapper = SpringContextHolder.getBean(OfficeMapper.class);

    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    public static final String CACHE_AUTH_INFO = "authInfo";
    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static User get(String id) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user == null) {
            user = userMapper.get(id);
            if (user == null) {
                return null;
            }
            user.setRoleList(roleMapper.findList(new Role(user)));
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        Date createDate = user.getCreateDate();
        Date loginDate = user.getLoginDate();
        if (createDate != null) {
            user.setCreateStTime(DateUtils.format(createDate, DateUtils.DATE_TIME_PATTERN));
        }
        if (loginDate != null) {
            user.setLoginStTime(DateUtils.format(loginDate, DateUtils.DATE_TIME_PATTERN));
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return 取不到返回null
     */
    public static User getByLoginName(String loginName) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
        if (user == null) {
            user = userMapper.getByLoginName(new User(null, loginName));
            if (user == null) {
                return null;
            }
            user.setRoleList(roleMapper.findList(new Role(user)));
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        removeCache(CACHE_AUTH_INFO);
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_MENU_LIST);
        removeCache(CACHE_AREA_LIST);
        removeCache(CACHE_OFFICE_LIST);
        removeCache(CACHE_OFFICE_ALL_LIST);
        UserUtils.clearCache(ShiroUtils.getUser());
    }

    /**
     * 清除指定用户缓存
     *
     * @param user
     */
    public static void clearCache(User user) {
        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
        if (user.getOffice() != null && user.getOffice().getId() != null) {
            CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
        }
    }

    /**
     * 清理所有用户的相关权限
     * @param sessions
     */
    public static void clearUserCache(Collection<Session> sessions) {
        if (CollectionUtils.isNotEmpty(sessions)) {
            sessions.forEach(session -> {
                session.removeAttribute(CACHE_AUTH_INFO);
                session.removeAttribute(CACHE_ROLE_LIST);
                session.removeAttribute(CACHE_MENU_LIST);
                session.removeAttribute(CACHE_AREA_LIST);
                session.removeAttribute(CACHE_OFFICE_LIST);
                session.removeAttribute(CACHE_OFFICE_ALL_LIST);
            });
        }
       List<User> userList = ShiroUtils.getSubjct().getPrincipals().asList();
        if(CollectionUtils.isNotEmpty(userList)){
            userList.forEach(user -> {
                UserUtils.clearCache(user);
            });
        }
    }


    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<Role> getRoleList() {
        @SuppressWarnings("unchecked")
        List<Role> roleList = (List<Role>) getCache(CACHE_ROLE_LIST);
        User user = ShiroUtils.getUser();
        if (roleList == null) {
            if (user.isAdmin()) {
                roleList = roleMapper.findAllList(new Role());
            } else {
                Role role = new Role();
                role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
                roleList = roleMapper.findList(role);
            }
            putCache(CACHE_ROLE_LIST, roleList);
        }
        if (CollectionUtils.isNotEmpty(roleList)) {
            roleList.forEach(role -> {
                role.setDataScopeLable(DictUtils.getDictLabel(role.getDataScope(), Constant.SYS_DATA_SCOPE, ""));
                if (user.isAdmin()) {
                    role.setIsAuth(Constant.YES);
                    role.setIsDistribution(Constant.YES);
                }
                role.setChecked(false);
            });
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<Menu> getMenuList() {
        @SuppressWarnings("unchecked")
        List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
        if (menuList == null) {
            User user = ShiroUtils.getUser();
            if (user.isAdmin()) {
                menuList = menuMapper.findAllList(new Menu());
            } else {
                Menu m = new Menu();
                m.setUserId(user.getId());
                menuList = menuMapper.findByUserId(m);
            }
            putCache(CACHE_MENU_LIST, menuList);
        }
        return menuList;
    }

    /**
     * 获取当前用户授权的区域
     *
     * @return
     */
    public static List<Area> getAreaList() {
        @SuppressWarnings("unchecked")
        List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
        if (areaList == null) {
            areaList = areaMapper.findAllList(new Area());
            putCache(CACHE_AREA_LIST, areaList);
        }
        return areaList;
    }

    /**
     * 获取当前用户有权限访问的部门
     *
     * @return
     */
    public static List<Office> getOfficeList() {
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
        if (officeList == null) {
            User user = ShiroUtils.getUser();
            if (user.isAdmin()) {
                officeList = officeMapper.findAllList(new Office());
            } else {
                Office office = new Office();
                office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
                officeList = officeMapper.findList(office);
            }
            putCache(CACHE_OFFICE_LIST, officeList);
        }
        return officeList;
    }

    /**
     * 获取当前用户有权限访问的部门
     *
     * @return
     */
    public static List<Office> getOfficeAllList() {
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_ALL_LIST);
        if (officeList == null) {
            officeList = officeMapper.findAllList(new Office());
        }
        return officeList;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }


    // ============== User Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = ShiroUtils.getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        ShiroUtils.getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        ShiroUtils.getSession().removeAttribute(key);
    }


}
