package com.im.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.Relation;
import com.im.dbmodel.User;

public interface RelationMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Relation record);

	int insertSelective(Relation record);

	Relation selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Relation record);

	int updateByPrimaryKey(Relation record);

	// 根据userid和friendid查好友记录
	@Select("select id, userId userid, friendId friendid, isAgree isagree, updatetime from relation where userId = #{userid} and friendId = #{friendid}")
	public Relation getFromRelationByUserIdAndFriendId(@Param("userid") Integer userid,
			@Param("friendid") Integer friendid);

	// 插入好友关系
	@Insert("insert into relation(userId, friendId, isAgree, updatetime) values(#{userid}, #{friendid}, #{isagree}, #{updatetime})")
	public void addToRelation(Relation relation);

	// 根据记录更新好友情况
	@Update("update relation set isAgree = #{isagree} where userId = #{userid} and friendId = #{friendid}")
	public void updateIsAgreeByUserIdAndFriendId(Relation relation);

//	// 根据双方帐号查找好友关系
//	@Select("select r.id, r.userId userid, r.friendId friendid, r.isAgree isagree, r.updatetime from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and u.username = #{username} and a.username = #{friendname} ")
//	public Relation getFromRelationByUsernameAndfriendname(@Param("username") String username,
//			@Param("friendname") String friendname, @Param("isagree") Integer isagree);

	// 1, 根据帐号和好友情况查所有好友id
	@Select("select r.friendId from relation r inner join user u on r.userId = u.id where u.username = #{username} and r.isAgree = #{isagree}")
	public List<Integer> getFriendIdFromUserJoinRelationByUsernameAndIsAgree(@Param("username") String username,
			@Param("isagree") Integer isagree);

	// 2, 根据帐号和好友情况查所有好友详细信息
	@Select("select a.* from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and u.username = #{username} and isAgree = #{isagree}")
	public List<User> getFriendFromUserRelationByUsernameAndIsAgree(@Param("username") String username,
			@Param("isagree") Integer isagree);

	// 2, 根据帐号和好友情况查所有好友详细信息
	@Select("select u.* from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and a.username = #{friendname} and isAgree = #{isagree}")
	public List<User> getFriendFromUserRelationByFriendnameAndIsAgree(@Param("friendname") String friendname,
			@Param("isagree") Integer isagree);

	// 1, 根据双方帐号及同意情况查找好友关系
	@Select("select r.id, r.userId userid, r.friendId friendid, r.isAgree isagree, r.updatetime from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and u.username = #{username} and a.username = #{friendname} and isAgree = #{isagree}")
	public Relation getFromRelationByUsernameAndFriendnameAndIsAgree(@Param("username") String username,
			@Param("friendname") String friendname, @Param("isagree") Integer isagree);

	// 2, 根据双方帐号及同意情况查找好友关系及好友信息
	@Select("select a.* from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and u.username = #{username} and a.username = #{friendname} and isAgree = #{isagree}")
	public User getFromFriendByUsernameAndFriendnameAndIsAgree(@Param("username") String username,
			@Param("friendname") String friendname, @Param("isagree") Integer isagree);

	// 删除好友记录(移除分组成员)
	@Delete("delete r.* from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and u.username = #{username} and a.username =  #{friendname}")
	public void deleteFriend(@Param("username") String username, @Param("friendname") String friendname);

	// 根据id和好友情况查好友id
//	@Select("select r.friendId from relation r inner join user u on r.userId = u.id where u.id = #{id} and r.isAgree = #{isAgree}")
//	List<Integer> getFriendIdFromUserJoinRelationByidAndIsagree(@Param("id") String id,
//			@Param("isAgree") Integer isAgree);

	// 根据userid和friendid更新好友情况
//	@Update("update relation set isAgree = #{isAgree} where userId = #{userId} and friendId = #{friendId}")
//	public void updateIsAgreeByUserIdAndFriendId(@Param("isAgree") Integer isAgree,@Param("userId") Integer userId,
//			@Param("friendId") Integer friendId);
//	
//	//删除好友记录
//	@Delete("delete from relation where userId = #{userId} and friendId = #{friendId}")
//	public void deleteFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

//	// 根据双方帐号及同意情况查找好友关系
//	@Select("select r.userId,r.friendId,r.isAgree,r.updatetime,a.* from relation r,user u,user a where r.userId = u.id and r.friendId = a.id and u.username = #{username} and a.username = #{friendname} and isAgree = #{isAgree}")
//	public User_Relation getFromRelationByUsernameAndFriendnameAndIsAgree(@Param("username") String username,
//			@Param("friendname") String friendname, @Param("isAgree") Integer isAgree);
}