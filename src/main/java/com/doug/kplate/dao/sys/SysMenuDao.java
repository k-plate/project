package com.doug.kplate.dao.sys;

import com.doug.kplate.dao.BaseDao;
import com.doug.kplate.entity.sys.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 */
@Mapper
public interface SysMenuDao extends BaseDao<SysMenuEntity> {
	
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
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);
	/**
	 * 修改资源状态
	 */
	public void updateUserStatus(Map<String, Object> params);
	/**
	 * 获得不同类型的菜单列表
	 */
	public List<SysMenuEntity> getMenuList(Map<String, Object> params);
}
