package com.mea.site.module.sys.service;

import com.mea.site.common.base.service.BaseService;
import com.mea.site.common.base.service.CrudService;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.utils.DictUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.module.sys.mapper.RoleMapper;
import com.mea.site.module.sys.model.Role;
import com.mea.site.module.sys.model.User;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael Jou on 2018/3/2. 14:02
 */
@Service
public class RoleService extends CrudService<Role, RoleMapper> {

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public List<Role> getRoleList() {
        List<Role> roleList = Lists.newArrayList();
        User user = ShiroUtils.getUser();
        if (user.isAdmin()) {
            roleList = mapper.findAllList(new Role());
        } else {
            Role role = new Role();
            role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
            roleList = mapper.findList(role);
        }
        if (CollectionUtils.isNotEmpty(roleList)) {
            roleList.forEach(role -> {
                role.setDataScopeLable(DictUtils.getDictLabel(role.getDataScope(), Constant.SYS_DATA_SCOPE, ""));
            });
        }
        return roleList;
    }


    public Role getRole(String id) {
        Role role = mapper.get(id);
        role.setDataScopeLable(DictUtils.getDictLabel(role.getDataScope(), Constant.SYS_DATA_SCOPE, ""));
        return role;
    }

    public Role getRoleByName(String name) {
        Role r = new Role();
        r.setName(name);
        return mapper.getByName(r);
    }
}
