package com.doug.kplate.dao;

import com.doug.kplate.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 角色管理
 *
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {
    /**
     * 修改角色状态
     * @param params
     */
    public void updateStatus(Map<String, Object> params);
}
