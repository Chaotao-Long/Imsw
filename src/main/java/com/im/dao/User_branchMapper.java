package com.im.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.User;
import com.im.dbmodel.User_branch;

public interface User_branchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User_branch record);

    int insertSelective(User_branch record);

    User_branch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User_branch record);

    int updateByPrimaryKey(User_branch record);
    
    //增加用户分组记录
  	@Insert("insert into `user_branch`(userId, branchId, jointime) values(#{userid},#{branchid},#{jointime})")
  	public void insertIntoUserBranch(User_branch userbranch);

  	//根据分组名和用户名查用户是否在该分组内(以此来判断是否能移除或转移用户)
  	@Select("select ub.* from `user_branch` ub, `user` u, `branch` b where ub.userId = u.id and ub.branchId = b.id "
  			+ "and u.username = #{friendname} and b.branchName = #{branchname} and b.adminName = #{username}")
  	public User_branch selectBranchByUserNameAndBranchName(@Param("username") String username,
  			@Param("branchname") String branchname, @Param("friendname") String friendname);
  	
  	//根据管理员名和用户id判断用户是否在该人的所有分组内
  	@Select("select ub.* from `user_branch` ub, `branch` b where ub.branchId = b.id and b.adminName = #{username} and ub.userId = #{friendid}")
  	public User_branch selectBranchByUserNameAndFriendId(@Param("username") String username, @Param("friendid") Integer friendid);
  	
  	//根据拥有者名和分组名查询分组成员信息
  	@Select("select u.* from `user_branch` ub, `user` u, `branch` b where b.id = ub.branchId "
  			+ "and ub.userId = u.id and b.adminName = #{username} and b.branchName = #{branchname}")
  	public List<User> selectUserListByBranchNameAndAdminName(@Param("username") String username, @Param("branchname") String branchname);
  	
  	//查分组信息
//  	@Select("select ub.* from `user_branch` ub, `branch` b where ub.branchId = b.id and b.adminName = #{username} and b.branchName = #{branchname}")
//  	public List<User_branch> selectUserBranchByName(@Param("username") String username, @Param("branchname") String branchname);
  	
  	//根据分组名和拥有者名和成员名移除分组成员(而后才可以删除好友关系)
  	@Delete("delete ub.* from `user_branch` ub, `user` u, `branch` b where ub.userId = u.id and ub.branchId = b.id "
  			+ "and u.username = #{friendname} and b.branchName = #{branchname} and b.adminName = #{username}")
  	public void deleteUserBranchByUserNameAndBranchName(@Param("friendname") String friendname,
  			@Param("branchname") String branchname, @Param("username") String username);
  	
  	//1.根据转移前后分组名和用户名转移分组成员(前提是两个分组均存在且不重名,为避免又增又删,通过直接对同一记录的旧分组id更新为新分组id来实现转移)
//    @Update("update `user_branch` ub, `user` u, `branch` a, `branch` b set ub.branchId = b.id where ub.branchId = a.id and u.id = ub.userId and a.branchName = "
//    		+ "#{oldbranchname} and a.adminName = #{adminname} and b.branchName = #{newbranchname} and b.adminName = #{adminname} and u.username = #{friendname}")
//    public void updateBranchByNewOldName(@Param("friendname") String friendname, @Param("oldbranchname") String oldbranchname,
//  			@Param("newbranchname") String newbranchname, @Param("adminname") String username);
  	
  	//2.根据转移前后分组名和用户名转移分组成员(前提是两个分组均存在且不重名,为避免又增又删,通过直接对同一记录的旧分组id更新为新分组id来实现转移)
  	@Update("update `user_branch` ub, `user` u set ub.branchId = #{newbranchid} where ub.userId = u.id and ub.branchId = #{oldbranchid} and u.username = #{friendname}")
  	public void updateBranchByNewOldUserName(@Param("friendname") String friendname, @Param("oldbranchid") Integer oldbranchid, @Param("newbranchid") Integer newbranchid);
  	
  	
  	
}