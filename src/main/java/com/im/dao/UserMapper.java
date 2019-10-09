package com.im.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.User;

public interface UserMapper {
	//根据id删除用户信息
    int deleteByPrimaryKey(Integer id);

    //插入用户所有信息
    int insert(User record);

    //有选择性地插入用户信息
    int insertSelective(User record);

	//根据用户id查询信息
    User selectByPrimaryKey(Integer id);

    //根据id有选择性地更新用户信息
    int updateByPrimaryKeySelective(User record);

    //根据id更新用户所有信息
    int updateByPrimaryKey(User record);
    
    //注册插入用户信息
  	@Insert("insert into user (username, password, nickname, photo, sex, age, phone, email, des, balance, registertime) "
  			+ "values (#{username}, #{password}, #{nickname}, #{photo}, #{sex}, #{age},#{phone},#{email},#{des},#{balance},#{registertime})")
  	public void InsertIntoUser(User user);

  	//登录验证用户名和密码
  	@Select("select * from user where username = #{username} and password = #{password}")
  	public User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

  	
  	//根据userid更新用户密码
  	@Update("update user set password = #{newPassword} where id = #{id}")
  	public void updateUserPassword(@Param("id") int id, @Param("newPassword") String newPassword);
  	
  	//更新用户信息
  	@Update("update user set nickname =　#{nickname} , photo =　#{photo} , sex =　#{sex} , age =　#{age},"
  			+ " phone =　#{phone} , email =　#{email} , des =　#{des} where id = #{id}")
  	public void updateUserInfo(User user);
}