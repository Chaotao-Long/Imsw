package com.im.service;

import java.util.List;

import com.im.dbmodel.Group;
import com.im.dbmodel.User;
import com.im.dbmodel.User_group;

public interface UserGroupService {

	public int joinInGroup(User_group userGroup, Group group, int isCreate);
	
	public User_group getUserGroupByUserNameAndGroupId(String username, String groupId);
	
	public void leaveGroupByUserNameAndGroupId(String groupId, String username);
	
	public User getAdminByGoupId(String groupId, Boolean isadmin);
	
	public List<Group> getGroupListByUsernameAndIsAdmin(String username, Boolean isadmin);
	
	public List<User> getUserListByGroupId(String groupId);
}
