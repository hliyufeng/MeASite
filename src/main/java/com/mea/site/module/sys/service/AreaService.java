package com.mea.site.module.sys.service;

import com.mea.site.common.base.service.BaseService;
import com.mea.site.common.base.service.CrudService;
import com.mea.site.common.utils.UserUtils;
import com.mea.site.module.sys.mapper.AreaMapper;
import com.mea.site.module.sys.model.Area;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Michael Jou on 2018/3/8. 17:42
 */
@Service
public class AreaService extends CrudService<Area, AreaMapper> {


    public List<Area> findAll() {
        return UserUtils.getAreaList();
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(Area area) {
        super.saveCustom(area);
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
    }

    @Transactional(readOnly = false)
    public boolean delete(Area area) {
        int i = super.deleteLogic(area);
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
        return i > 0 ? true : false;
    }
}
