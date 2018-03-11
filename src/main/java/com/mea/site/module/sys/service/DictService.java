package com.mea.site.module.sys.service;

import com.mea.site.common.base.service.CrudService;
import com.mea.site.common.utils.CacheUtils;
import com.mea.site.common.utils.DictUtils;
import com.mea.site.module.sys.mapper.DictMapper;
import com.mea.site.module.sys.model.Dict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Michael Jou on 2018/3/2. 15:59
 * 字典管理
 */
@Service
public class DictService extends CrudService<Dict, DictMapper> {
    @Transactional(readOnly = false)
    public int saveCustom(Dict dict) {
        int i = super.saveCustom(dict);
        if (i > 0) {
            CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
        }
        return i;
    }

    @Transactional(readOnly = false)
    public int deleteLogic(Dict dict) {
        int i = super.deleteLogic(dict);
        if (i > 0) {
            CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
        }
        return i;
    }

    /**
     * 查询字段类型列表
     *
     * @return
     */
    public List<String> findTypeList() {
        return mapper.findTypeList(new Dict());
    }

}
