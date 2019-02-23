package com.doug.kplate.dao.sys;

import com.doug.kplate.dao.BaseDao;
import com.doug.kplate.entity.sys.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 */
@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    SysUserEntity queryByUserName(String username);

    /**
     * 修改密码
     */
    int updatePassword(Map<String, Object> map);

    public List<SysUserEntity> getUserListOfRole(Map<String, Object> map);

    /**
     * 修改用户状态
     * @param params
     */
    public void updateUserStatus(Map<String, Object> params);
}
