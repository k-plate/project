package com.doug.kplate.dao.sys;

import com.doug.kplate.dao.BaseDao;
import com.doug.kplate.entity.sys.SysRoleEntity;
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
