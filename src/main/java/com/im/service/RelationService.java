package com.im.service;

import java.util.List;

import com.im.dbmodel.Relation;
import com.im.dbmodel.User;

public interface RelationService {

	// 根据userid和friendid查好友记录
	public Relation getFromRelationByUserIdAndFriendId(Integer userid, Integer friendid);

	// 插入好友关系
	public void addToRelation(Relation relation);

	// 根据记录更新好友情况
	public void updateIsAgreeByUserIdAndFriendId(Relation relation);

	// 根据双方帐号查好友记录
//	public Relation getFromRelationByUsernameAndfriendname(String username, String friendname, Integer isagree);

	// 连接user表和relation表，根据useropenid和isagree获取friendid列表
	public List<Integer> getFriendIdFromUserJoinRelationByUsernameAndIsAgree(String username, Integer isagree);

	// 根据双方帐号删除好友记录
	public void deleteFriend(String username, String friendname);

	// 根据双方帐号及同意情况查找好友关系
	public Relation getFromRelationByUsernameAndFriendnameAndIsAgree(String username, String friendname,
			Integer isagree);

	// 根据双方帐号及同意情况查找好友关系及好友信息
	public User getFromFriendByUsernameAndFriendnameAndIsAgree(String username, String friendname, Integer isagree);

	// 2, 根据添加人和好友情况查所有好友详细信息
	public List<User> getFriendFromUserRelationByUsernameAndIsAgree(String username, Integer isagree);
	
	//根据被添加人和好友情况查所有好友详细信息
	public List<User> getFriendFromUserRelationByFriendnameAndIsAgree(String friendname, Integer isagree);

}
