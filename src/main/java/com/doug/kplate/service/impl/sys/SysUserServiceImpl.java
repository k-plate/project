package com.doug.kplate.service.impl.sys;

import com.doug.kplate.common.annotation.DataFilter;
import com.doug.kplate.dao.sys.SysUserDao;
import com.doug.kplate.entity.sys.SysUserEntity;
import com.doug.kplate.service.sys.SysUserRoleService;
import com.doug.kplate.service.sys.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 系统用户
 * 
 * @author honghu cloud
 * @technology +QQ： 2170983087
 * @date 2016年9月18日 上午9:46:09
 */
@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return sysUserDao.queryByUserName(username);
	}
	
	@Override
	public SysUserEntity queryObject(Long userId) {
		return sysUserDao.queryObject(userId);
	}

	@Override
	public List<SysUserEntity> queryList(Map<String, Object> map) {
		return sysUserDao.queryList(map);
	}

	@Override
	public List<SysUserEntity> getUserListOfRole(Map<String, Object> map){
		List<SysUserEntity> sysUserEntities = sysUserDao.getUserListOfRole(map);
		return sysUserEntities;
	}
	
	@Override
	@DataFilter(tableAlias = "u", user = false)
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		sysUserDao.save(user);
		List<Long> roleIds = new ArrayList<>();
		roleIds.add(user.getRoleId());
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), roleIds);
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		sysUserDao.update(user);
		List<Long> roleIds = new ArrayList<>();
		roleIds.add(user.getRoleId());
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), roleIds);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	@Override
	public void updateUserStatus(Map<String, Object> params) {
		sysUserDao.updateUserStatus(params);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}

}
