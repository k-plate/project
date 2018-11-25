package com.doug.kplate.service;


import com.doug.kplate.entity.SysUserTokenEntity;
import com.doug.kplate.utils.R;

/**
 * 用户Token
 *
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	SysUserTokenEntity queryByToken(String token);
	
	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

}
