package com.mea.site.module.sys.service;

import com.mea.site.common.base.service.CrudService;
import com.mea.site.common.config.constants.MenuTypeEnum;
import com.mea.site.common.utils.CacheUtils;
import com.mea.site.common.utils.ShiroUtils;
import com.mea.site.common.utils.UserUtils;
import com.mea.site.module.sys.dto.MenuDto;
import com.mea.site.module.sys.mapper.MenuMapper;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.model.User;
import com.google.common.collect.Lists;
import com.mchange.v2.log.LogUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lenovo on 2018/2/27.
 */
@Service
public class MenuService extends CrudService<Menu, MenuMapper> {


    /**
     * 获取权限菜单
     *
     * @return
     */
    public List<MenuDto> buildMenu() {
        List<Menu> menus = UserUtils.getMenuList();
        List<Menu> rootMenuListResp = Lists.newArrayList();
        List<Menu> menuMenuList = Lists.newArrayList();
        List<MenuDto> menuDtos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(menus)) {
            menus.forEach(menu -> {
                String id = menu.getParentId();
                String type = menu.getType();
                if ("1".equals(id) && MenuTypeEnum.CATALOG.getType().equals(type)) {
                    rootMenuListResp.add(menu);
                } else if (MenuTypeEnum.MENU.getType().equals(type)) {
                    menuMenuList.add(menu);
                }

            });
        }

        if (CollectionUtils.isNotEmpty(rootMenuListResp)) {
            rootMenuListResp.forEach(rootMenu -> {
                String rootId = rootMenu.getId();
                MenuDto menuDto = new MenuDto();
                menuDto.setHref(rootMenu.getHref());
                menuDto.setIcon(rootMenu.getIcon());
                menuDto.setTitle(rootMenu.getName());
                if (CollectionUtils.isNotEmpty(menuMenuList)) {
                    menuMenuList.forEach(menu -> {
                        String parenId = menu.getParentId();
                        if (rootId.equals(parenId)) {
                            MenuDto menusDto = new MenuDto();
                            menusDto.setHref(menu.getHref());
                            menusDto.setIcon(menu.getIcon());
                            menusDto.setTitle(menu.getName());
                            menuDto.getChildren().add(menusDto);
                        }
                    });
                }
                menuDtos.add(menuDto);
            });
        }
        return menuDtos;
    }


    @Transactional(readOnly = false)
    public void saveMenu(Menu menu) {
        // 获取父节点实体
        menu.setParent(this.mapper.get(menu.getParent().getId()));
        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = menu.getParentIds();
        // 设置新的父节点串
        menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");
        // 保存或更新实体
        if (StringUtils.isBlank(menu.getId())) {
            menu.preInsert();
            mapper.insertCustom(menu);
        } else {
            menu.preUpdate();
            mapper.update(menu);
        }
        // 更新子节点 parentIds
        Menu m = new Menu();
        m.setParentIds("%," + menu.getId() + ",%");
        List<Menu> list = mapper.findByParentIdsLike(m);
        for (Menu e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
            mapper.updateParentIds(e);
        }
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
    }

    @Transactional(readOnly = false)
    public boolean deleteMenu(Menu menu) {
        boolean isSuccess = mapper.deleteLogic(menu) > 0 ? true : false;
        if (isSuccess) {
            UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
        }
        return isSuccess;
    }

    public Menu getMenu(String id) {
        return mapper.get(id);
    }
}
