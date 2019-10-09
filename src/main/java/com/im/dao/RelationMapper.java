package com.im.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.Relation;

public interface RelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Relation record);

    int insertSelective(Relation record);

    Relation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Relation record);

    int updateByPrimaryKey(Relation record);
    
    

//	//插入好友关系
//	@Insert("insert into relation(userId, friendId, isAgree) values(#{userId}, #{friendId}, #{isAgree})")
//	public void addToRelation(Relation relation);
//	
//	//根据id和好友情况查好友id
//	@Select("select friendId from relation where userId = #{userId} and isAgree = #{isAgree}")
//	List<Integer> getFriendIdFromUserByUserIdAndIsAgree(@Param("userId") Integer userId,
//			@Param("isAgree") Integer isAgree);
//	
//	//根据id和好友情况查好友id
//	@Select("select r.friendId from relation r inner join user u on r.userId = u.id where u.id = #{id} and r.isAgree = #{isAgree}")
//	List<Integer> getFriendIdFromUserJoinRelationByidAndIsagree(@Param("id") String id,
//			@Param("isAgree") Integer isAgree);
//	
//	//根据记录更新好友情况
//	@Update("update relation set isAgree = #{isAgree} where userId = #{userId} and friendId = #{friendId}")
//	public void updateIsAgreeByUserIdAndFriendId(Relation relation);
//
//	//根据userid和friendid查好友记录
//	@Select("select * from relation where userId = #{userId} and friendId = #{friendId}")
//	public Relation getFromRelationByUserIdAndFriendId(@Param("userId") Integer userId,
//			@Param("friendId") Integer friendId);
//	
//	//根据userid和friendid更新好友情况
//	@Update("update relation set isAgree = #{isAgree} where userId = #{userId} and friendId = #{friendId}")
//	public void updateIsAgreeByUserIdAndFriendId(@Param("isAgree") Integer isAgree,@Param("userId") Integer userId,
//			@Param("friendId") Integer friendId);
//	
//	//删除好友记录
//	@Delete("delete from relation where userId = #{userId} and friendId = #{friendId}")
//	public void deleteFriend(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
}