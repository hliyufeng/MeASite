
package com.mea.site.module.sys.mapper;

import com.mea.site.common.base.mapper.BaseMapper;
import com.mea.site.module.sys.model.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色Mapper接口
 * @author Michael
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);

}
