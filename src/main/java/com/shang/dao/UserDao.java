package com.shang.dao;


import com.shang.domain.User;

/**
 * 功能概要：User的DAO类
 * 
 * @author shanghaitao
 * @since 2017年8月22日
 */
public interface UserDao {
	/**
	 * 
	 * @author shanghaitao
	 * @since 2017年8月22日
	 * @param userId
	 * @return
	 */
	public User selectUserById(Integer userId);

}
