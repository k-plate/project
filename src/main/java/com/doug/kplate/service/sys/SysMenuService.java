package com.doug.kplate.service.sys;


import com.doug.kplate.entity.sys.SysMenuEntity;

import java.util.List;
import java.util.Map;


/**
 * 菜单管理
 *
 */
public interface SysMenuService {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);
	
	/**
	 * 查询菜单
	 */
	SysMenuEntity queryObject(Long menuId);
	
	/**
	 * 查询菜单列表
	 */
	List<SysMenuEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存菜单
	 */
	void save(SysMenuEntity menu);
	
	/**
	 * 修改
	 */
	void update(SysMenuEntity menu);
	
	/**
	 * 删除
	 */
	void deleteBatch(Long[] menuIds);
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);

	/**
	 * 冻结资源
	 */

	void updateUserStatus(Map<String, Object> params);
	/**
	 * 获得不同类型菜单列表（用于下拉框）
	 */
	List<SysMenuEntity> getMenuList(Map<String, Object> params);
}
