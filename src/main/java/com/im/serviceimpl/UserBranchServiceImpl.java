package com.im.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.User_branchMapper;
import com.im.dbmodel.User;
import com.im.dbmodel.User_branch;
import com.im.service.UserBranchService;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class UserBranchServiceImpl implements UserBranchService {

	@Autowired
	private User_branchMapper userbranchMapper;
	
	//创建分组
	@Override
	public void addIntoUserBranch(User_branch userbranch) {

		userbranchMapper.insertIntoUserBranch(userbranch);
	}

  	//查询分组成员
	@Override
	public User_branch getBranchByUserNameAndBranchName(String username, String branchname, String friendname) {

		return userbranchMapper.selectBranchByUserNameAndBranchName(username, branchname, friendname);
	}

	//查询分组成员列表
	@Override
	public List<User> getUserListByBranchNameAndAdminName(String username, String branchname) {

		return userbranchMapper.selectUserListByBranchNameAndAdminName(username, branchname);
	}

	//移除分组成员
	@Override
	public void deleteUserBranchByUserNameAndBranchName(String friendname, String branchname, String username) {

		userbranchMapper.deleteUserBranchByUserNameAndBranchName(friendname, branchname, username);
	}

	//转移分组成员
	@Override
	public void updateBranchByNewOldUserName(String friendname, Integer oldbranchid, Integer newbranchid) {

		userbranchMapper.updateBranchByNewOldUserName(friendname, oldbranchid, newbranchid);
	}

	@Override
	public User_branch getBranchByUserNameAndFriendId(String username, Integer friendid) {

		return userbranchMapper.selectBranchByUserNameAndFriendId(username, friendid);
	}
	
}
