package com.im.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.RelationMapper;
import com.im.dbmodel.Relation;
import com.im.dbmodel.User;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class RelationServiceImpl implements com.im.service.RelationService {

	@Autowired
	private RelationMapper relationMapper;
	
	@Override
	public Relation getFromRelationByUserIdAndFriendId(Integer userid, Integer friendid) {

		return relationMapper.getFromRelationByUserIdAndFriendId(userid, friendid);
	}

	@Override
	public void addToRelation(Relation relation) {

		relationMapper.addToRelation(relation);
	}

	@Override
	public void updateIsAgreeByUserIdAndFriendId(Relation relation) {

		relationMapper.updateIsAgreeByUserIdAndFriendId(relation);
	}

//	@Override
//	public Relation getFromRelationByUsernameAndfriendname(String username, String friendname, Integer isagree) {
//		
//		return relationMapper.getFromRelationByUsernameAndfriendname(username, friendname, isagree);
//	}

	@Override
	public List<Integer> getFriendIdFromUserJoinRelationByUsernameAndIsAgree(String username, Integer isagree) {

		return relationMapper.getFriendIdFromUserJoinRelationByUsernameAndIsAgree(username, isagree);
	}

	@Override
	public void deleteFriend(String username, String friendname) {

		relationMapper.deleteFriend(username, friendname);
		
	}

	@Override
	public Relation getFromRelationByUsernameAndFriendnameAndIsAgree(String username, String friendname,
			Integer isagree) {

		return relationMapper.getFromRelationByUsernameAndFriendnameAndIsAgree(username, friendname, isagree);
	}

	@Override
	public User getFromFriendByUsernameAndFriendnameAndIsAgree(String username, String friendname, Integer isagree) {

		return relationMapper.getFromFriendByUsernameAndFriendnameAndIsAgree(username, friendname, isagree);
	}

	@Override
	public List<User> getFriendFromUserRelationByUsernameAndIsAgree(String username, Integer isagree) {

		return relationMapper.getFriendFromUserRelationByUsernameAndIsAgree(username, isagree);
	}

	@Override
	public List<User> getFriendFromUserRelationByFriendnameAndIsAgree(String friendname, Integer isagree) {

		return relationMapper.getFriendFromUserRelationByFriendnameAndIsAgree(friendname, isagree);
	}

}
