
package com.mea.site.module.sys.model;

import com.mea.site.common.base.model.DataEntity;
import com.mea.site.common.config.constants.Constant;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 角色Entity
 */
@Table(name = "sys_role")
public class Role extends DataEntity<Role> {

    private static final long serialVersionUID = 1L;
    @Transient
    private Office office;    // 归属机构
    @Column(name = "office_id")
    private long officeId;
    @Column(name = "name")
    private String name;    // 角色名称
    @Column(name = "enname")
    private String enname;    // 英文名称
    @Column(name = "role_type")
    private String roleType;// 权限类型
    @Column(name = "data_scope")
    private String dataScope;// 数据范围
    @Transient
    private String oldName;    // 原角色名称
    @Transient
    private String oldEnname;    // 原英文名称
    @Column(name = "is_sys")
    private String sysData;        //是否是系统数据
    @Column(name = "useable")
    private String useable;        //是否是可用
    @Column(name = "is_distribution")
    private String isDistribution = "1";//是否可分配 1:可分配 0 不可分配
    @Column(name = "is_auth")
    private String isAuth = "1";//是否可分配 1:可分配 0 不可分配
    @Transient
    private String dataScopeLable;
    @Transient
    private User user;        // 根据用户ID查询角色列表
    @Transient
    private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
    @Transient
    private List<Office> officeList = Lists.newArrayList(); // 按明细设置数据范围
    @Transient
    private boolean checked = false;

    // 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
    @Transient
    public static final String DATA_SCOPE_ALL = "1";
    @Transient
    public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
    @Transient
    public static final String DATA_SCOPE_COMPANY = "3";
    @Transient
    public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
    @Transient
    public static final String DATA_SCOPE_OFFICE = "5";
    @Transient
    public static final String DATA_SCOPE_SELF = "8";
    @Transient
    public static final String DATA_SCOPE_CUSTOM = "9";
    @Transient
    public static final String IS_AUTH_OK = "1";
    @Transient
    public static final String IS_AUTH_NO = "0";

    public Role() {
        super();
        this.dataScope = DATA_SCOPE_SELF;
        this.useable = Constant.YES;
    }

    public Role(String id) {
        super(id);
    }

    public Role(User user) {
        this();
        this.user = user;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public String getSysData() {
        return sysData;
    }

    public void setSysData(String sysData) {
        this.sysData = sysData;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Length(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 100)
    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    @Length(min = 1, max = 100)
    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getOldEnname() {
        return oldEnname;
    }

    public void setOldEnname(String oldEnname) {
        this.oldEnname = oldEnname;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<String> getMenuIdList() {
        List<String> menuIdList = Lists.newArrayList();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        menuList = Lists.newArrayList();
        for (String menuId : menuIdList) {
            Menu menu = new Menu();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }

    public String getMenuIds() {
        return StringUtils.join(getMenuIdList(), ",");
    }

    public void setMenuIds(String menuIds) {
        menuList = Lists.newArrayList();
        if (menuIds != null) {
            String[] ids = StringUtils.split(menuIds, ",");
            setMenuIdList(Lists.newArrayList(ids));
        }
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public List<String> getOfficeIdList() {
        List<String> officeIdList = Lists.newArrayList();
        for (Office office : officeList) {
            officeIdList.add(office.getId());
        }
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        officeList = Lists.newArrayList();
        for (String officeId : officeIdList) {
            Office office = new Office();
            office.setId(officeId);
            officeList.add(office);
        }
    }

    public String getOfficeIds() {
        return StringUtils.join(getOfficeIdList(), ",");
    }

    public void setOfficeIds(String officeIds) {
        officeList = Lists.newArrayList();
        if (officeIds != null) {
            String[] ids = StringUtils.split(officeIds, ",");
            setOfficeIdList(Lists.newArrayList(ids));
        }
    }

    /**
     * 获取权限字符串列表
     */
    public List<String> getPermissions() {
        List<String> permissions = Lists.newArrayList();
        for (Menu menu : menuList) {
            if (menu.getPermission() != null && !"".equals(menu.getPermission())) {
                permissions.add(menu.getPermission());
            }
        }
        return permissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDataScopeLable() {
        return dataScopeLable;
    }

    public void setDataScopeLable(String dataScopeLable) {
        this.dataScopeLable = dataScopeLable;
    }

    public long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(long officeId) {
        this.officeId = officeId;
    }

    public String getIsDistribution() {
        return isDistribution;
    }

    public void setIsDistribution(String isDistribution) {
        this.isDistribution = isDistribution;
    }

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
