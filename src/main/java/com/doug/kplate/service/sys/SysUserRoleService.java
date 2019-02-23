package com.doug.kplate.service.sys;


import com.doug.kplate.entity.sys.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 用户与角色对应关系
 *
 */
public interface SysUserRoleService {
	
	void saveOrUpdate(Long userId, List<Long> roleIdList);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);
	
	void delete(Long userId);

	/**
	 * 查询用户角色
	 */
	List<SysRoleEntity> queryRoleByUserId(Map<String, Object> param);
}
