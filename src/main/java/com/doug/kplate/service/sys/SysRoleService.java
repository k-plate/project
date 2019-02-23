package com.doug.kplate.service.sys;


import com.doug.kplate.entity.sys.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 */
public interface SysRoleService {
	
	SysRoleEntity queryObject(Long roleId);
	
	List<SysRoleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysRoleEntity role);
	
	void update(SysRoleEntity role);
	
	void deleteBatch(Long[] roleIds);
	/**
	 * 修改角色状态
	 */
	void updateStatus(Map<String, Object> params);
}
