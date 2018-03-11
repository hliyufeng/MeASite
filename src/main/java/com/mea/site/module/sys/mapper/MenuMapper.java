
package com.mea.site.module.sys.mapper;

import com.mea.site.common.base.mapper.BaseMapper;
import com.mea.site.module.sys.model.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单Mapper接口
 * @author MichaelJou
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> findByUserId(Menu menu);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
}
