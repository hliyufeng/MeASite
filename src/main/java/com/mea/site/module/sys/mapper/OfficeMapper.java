
package com.mea.site.module.sys.mapper;


import com.mea.site.common.base.mapper.TreeMapper;
import com.mea.site.module.sys.model.Office;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 机构Mapper接口
 * @author Michael
 */
@Mapper
public interface OfficeMapper extends TreeMapper<Office> {

    /**
     * 找到所有子节点
     * @param office
     * @return
     */
    public List<Office> findByParentIdsLike(Office office);

    /**
     * 更新所有父节点字段
     * @param entity
     * @return
     */
    public int updateParentIds(Office entity);
}
