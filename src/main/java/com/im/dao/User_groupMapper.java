package com.im.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.im.dbmodel.Group;
import com.im.dbmodel.User;
import com.im.dbmodel.User_group;

public interface User_groupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User_group record);

    int insertSelective(User_group record);

    User_group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User_group record);

    int updateByPrimaryKey(User_group record);
    
 // 根据用户名和群组id查找用户群组关系
 	@Select("select ug.* from `user_group` ug, `user` u, `group` g where ug.userId = u.id and ug.groupId = g.id and u.username = #{username} and g.group_id = #{groupId}")
 	public User_group getUserGroupByUserNameAndGroupId(@Param("username") String username,
 			@Param("groupId") String groupId);

 	// 根据群号id和用户名删除用户群组关系
 	@Delete("delete ug.* from `user_group` ug, `user` u, `group` g  where ug.userId = u.id and ug.groupId = g.id and u.username = #{username} and g.group_id = #{groupId}")
 	public void leaveGroupByUserNameAndGroupId(@Param("groupId") String groupId, @Param("username") String username);

 	// 根据群号查管理员信息
 	@Select("select u.* from `user_group` ug, `user` u, `group` g where ug.userId = u.id and ug.groupId = g.id and g.group_id = #{groupId} and ug.isAdmin = #{isadmin}")
 	public User getAdminByGoupId(@Param("groupId") String groupId, @Param("isadmin") Boolean isadmin);

 	// 根据群号groupId删除记录
 	@Delete("delete from `user_group` where groupId = #{groupid}")
 	public void deleteFromGroupByGroupid(@Param("groupid") Integer groupid);

 	//根据用户身份分别查其存在的群组
 	@Select("select g.* from `user_group` ug, `user` u, `group` g where ug.userId = u.id and ug.groupId = g.id and u.username = #{username} and ug.isAdmin = #{isadmin}")
 	public List<Group> getGroupListByUsernameAndIsAdmin(@Param("username") String username,
 			@Param("isadmin") Boolean isadmin);
 	
 	// 根据群号查成员列表(不包括管理员)
 	@Select("select u.* from `user_group` ug, `user` u, `group` g where ug.userId = u.id and ug.groupId = g.id and g.group_id = #{groupId} and ug.isAdmin = 0")
 	public List<User> getUserListByGroupId(@Param("groupId") String groupId);
}