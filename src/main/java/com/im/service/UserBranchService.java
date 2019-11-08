package com.im.service;

import java.util.List;

import com.im.dbmodel.User;
import com.im.dbmodel.User_branch;

public interface UserBranchService {

	public void addIntoUserBranch(User_branch userbranch);
	
	public User_branch getBranchByUserNameAndBranchName(String username, String branchname, String friendname);

	public List<User> getUserListByBranchNameAndAdminName(String username, String branchname);

 	public void deleteUserBranchByUserNameAndBranchName(String friendname, String branchname, String username);

 	public void updateBranchByNewOldUserName(String friendname, Integer oldbranchid, Integer newbranchid);

 	public User_branch getBranchByUserNameAndFriendId(String username, Integer friendid);
}
