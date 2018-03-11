package com.mea.site.module.sys.service;

import com.feilong.core.Validator;
import com.mea.site.common.base.service.CrudService;
import com.mea.site.common.config.constants.Constant;
import com.mea.site.common.exception.ServiceException;
import com.mea.site.common.utils.DictUtils;
import com.mea.site.common.utils.Reflections;
import com.mea.site.common.utils.UserUtils;
import com.mea.site.module.sys.mapper.MenuMapper;
import com.mea.site.module.sys.mapper.OfficeMapper;
import com.mea.site.module.sys.model.Menu;
import com.mea.site.module.sys.model.Office;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Jou on 2018/3/5. 14:54
 */
@Service
public class OfficeService extends CrudService<Office, OfficeMapper> {

    public List<Office> findAll() {
        return UserUtils.getOfficeList();
    }

    public List<Office> findList(Boolean isAll) {
        if (isAll != null && isAll) {
            return UserUtils.getOfficeAllList();
        } else {
            return UserUtils.getOfficeList();
        }
    }


    public List<Office> findList(Office office) {
        if (office != null) {
            if(Validator.isNullOrEmpty(office.getParentIds())){
                office.setParentIds("");
            }
            office.setParentIds(office.getParentIds() + "%");
            return mapper.findByParentIdsLike(office);
        }
        return new ArrayList<Office>();
    }


    @Transactional(readOnly = false)
    public boolean delete(Office office) {
        int i = super.deleteLogic(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
        return i > 0 ? true : false;
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(Office office) {
        this.saveOffice(office);
        if (office.getChildDeptList() != null) {
            Office childOffice = null;
            for (String id : office.getChildDeptList()) {
                childOffice = new Office();
                childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
                childOffice.setParent(office);
                childOffice.setArea(office.getArea());
                childOffice.setType("2");
                childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1));
                childOffice.setUseable(Constant.YES);
                this.saveOffice(childOffice);
            }
        }
    }


    @Transactional(readOnly = false)
    public void saveOffice(Office entity) {
        // 如果没有设置父节点，则代表为跟节点，有则获取父节点实体
        if (entity.getParent() == null || StringUtils.isBlank(entity.getParentId())
                || "0".equals(entity.getParentId())) {
            entity.setParent(null);
        } else {
            entity.setParent(super.get(entity.getParentId()));
        }
        if (entity.getParent() == null) {
            Office parentEntity = null;
            parentEntity = new Office("0");
            entity.setParent(parentEntity);
            entity.getParent().setParentIds(StringUtils.EMPTY);
        }

        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = entity.getParentIds();

        // 设置新的父节点串
        entity.setParentIds(entity.getParent().getParentIds() + entity.getParent().getId() + ",");

        // 保存或更新实体
        super.saveCustom(entity);

        // 更新子节点 parentIds
        Office o = null;
        o = new Office();
        o.setParentIds("%," + entity.getId() + ",%");
        List<Office> list = mapper.findByParentIdsLike(o);
        for (Office e : list) {
            if (e.getParentIds() != null && oldParentIds != null) {
                e.setParentIds(e.getParentIds().replace(oldParentIds, entity.getParentIds()));
                mapper.updateParentIds(e);
            }
        }

    }
}


