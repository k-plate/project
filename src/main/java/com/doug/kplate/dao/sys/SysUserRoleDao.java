package com.doug.kplate.dao.sys;

import com.doug.kplate.dao.BaseDao;
import com.doug.kplate.entity.sys.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户与角色对应关系
 *
 */
@Mapper
public interface SysUserRoleDao extends BaseDao<SysRoleEntity> {
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * 查询用户角色
	 * @param param
	 * @return
	 */
	public List<SysRoleEntity> queryRoleByUserId(Map<String, Object> param);
}
