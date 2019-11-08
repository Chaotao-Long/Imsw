package com.im.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    //注册插入用户信息
  	@Insert("insert into user (username, password, nickname, photo, sex, age, phone, email, des,address, balance, status, logintime, registertime) "
  			+ "values (#{username}, #{password}, #{nickname}, #{photo}, #{sex}, #{age},#{phone},#{email},#{des},#{address},#{balance},#{status},#{logintime},#{registertime})")
  	public void InsertIntoUser(User user);

  	//登录验证用户名和密码
  	@Select("select * from user where username = #{username} and password = #{password}")
  	public User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

  	
  	//根据username更新用户密码
  	@Update("update user set password = #{newPassword} where username = #{username}")
  	public void updateUserPassword(@Param("username") String username, @Param("newPassword") String newPassword);
  	
//  	//更新用户信息
//  	@Update("update user set username = #{username} , password = #{password} , nickname =　#{nickname} , photo =　#{photo} , sex =　#{sex} , age =　#{age} ,"
//  			+ " phone =　#{phone} , email =　#{email} , des =　#{des} , balance =　#{balance} , registertime =　#{registertime} where username = #{username}")
//  	public void updateUserInfo(User user);

  	//根据username查询用户
  	@Select("select * from user where username = #{username}")
	public User getFromUserByUsername(@Param("username") String username);
  	
  	
  	//根据用户username来更新用户登录时间
  	@Update("update user set logintime = #{logintime} where username = #{username}")
  	public void updateLogintimeByUsername(@Param("username") String username, @Param("logintime") Date logintime);
  	
  	//根据用户username来更新用户状态
  	@Update("update user set status = #{status} where username = #{username}")
  	public void updateStatusByUsername(@Param("username") String username, @Param("status") Integer status);
}