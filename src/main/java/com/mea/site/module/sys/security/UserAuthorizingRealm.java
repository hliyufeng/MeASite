package com.mea.site.module.sys.security;

import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.UserUtils;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.model.Role;
import com.mea.site.module.sys.model.User;
import com.mea.site.module.sys.service.MenuService;
import com.mea.site.module.sys.service.SystemService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Michael Jou  on 2018/02/06.
 * @Date
 */
public class UserAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private SystemService systemService;

    /**
     * 加载权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = systemService.getUserByLoginName(ShiroUtils.getUser().getLoginName());
        if (user != null) {
            List<Menu> list = UserUtils.getMenuList();
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(menu -> {
                    if (StringUtils.isNotBlank(menu.getPermission())) {
                        for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                            info.addStringPermission(permission);
                        }
                    }
                });
            }
        }

        info.addStringPermission("user");

        for (Role role : user.getRoleList()) {
            info.addRole(role.getEnname());
        }
        //// 更新登录IP和时间
        systemService.updateUserLoginInfo(ShiroUtils.getUser());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {


        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        User user = systemService.getUserByLoginName(userName);
        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

//        // 账号锁定
        if (Constant.NO.equals(user.getLoginFlag())) {
            throw new AuthenticationException("msg:该已帐号禁止登录.");
        }

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得shiro自带的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, // 用户对象
                user.getPassword(), // 密码
                ByteSource.Util.bytes(userName + user.getCredentialsSalt()),// salt=username+salt
                getName() // realm name
        );
        return authenticationInfo;
    }
}
