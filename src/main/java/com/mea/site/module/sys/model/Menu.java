
package com.mea.site.module.sys.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mea.site.common.base.model.DataEntity;
import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单Entity
 *
 * @author MichaelJou
 */
@Table(name = "sys_menu")
public class Menu extends DataEntity<Menu> {

    private static final long serialVersionUID = 1L;
    @Transient
    private Menu parent;    // 父级菜单
    @Column(name = "parent_ids")
    private String parentIds; // 所有父级编号
    @Column(name = "name")
    private String name;    // 名称
    @Column(name = "href")
    private String href;    // 链接
    @Column(name = "target")
    private String target;    // 目标（ mainFrame、_blank、_self、_parent、_top）
    @Column(name = "icon")
    private String icon;    // 图标
    @Column(name = "sort")
    private Integer sort;    // 排序

    @Column(name = "is_show")
    private String show;    // 是否在菜单中显示（1：显示；0：不显示）
    @Column(name = "permission")
    private String permission; // 权限标识
    @Transient
    private List<Menu> childrenMenu = Lists.newArrayList();
    @Column(name = "type")
    private String type; //菜单类型 1:目录 2:菜单 3:按钮 4:url

    @Transient
    private String userId;

    public Menu() {
        super();
        this.sort = 30;
        this.show = "1";
    }

    public Menu(String id) {
        super(id);
    }

    @JsonBackReference
    @NotNull
    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    @Length(min = 1, max = 2000)
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Length(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 2000)
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Length(min = 0, max = 20)
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Length(min = 0, max = 100)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @NotNull
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    @Length(min = 0, max = 200)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }

    @JsonIgnore
    public static void sortList(List<Menu> list, List<Menu> sourcelist, String parentId, boolean cascade) {
        for (int i = 0; i < sourcelist.size(); i++) {
            Menu e = sourcelist.get(i);
            if (e.getParent() != null && e.getParent().getId() != null
                    && e.getParent().getId().equals(parentId)) {
                list.add(e);
                if (cascade) {
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (int j = 0; j < sourcelist.size(); j++) {
                        Menu child = sourcelist.get(j);
                        if (child.getParent() != null && child.getParent().getId() != null
                                && child.getParent().getId().equals(e.getId())) {
                            sortList(list, sourcelist, e.getId(), true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @JsonIgnore
    public static String getRootId() {
        return "1";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Menu> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(List<Menu> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }

    @Length(min = 1, max = 1)
    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}