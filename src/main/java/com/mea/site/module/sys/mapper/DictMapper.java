
package com.mea.site.module.sys.mapper;

import com.mea.site.common.base.mapper.BaseMapper;
import com.mea.site.module.sys.model.Dict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典Mapper接口
 * @author Michael
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

	public List<String> findTypeList(Dict dict);
	
}
