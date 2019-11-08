package com.im.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.GroupMapper;
import com.im.dao.User_groupMapper;
import com.im.dbmodel.Group;
import com.im.dbmodel.User;
import com.im.dbmodel.User_group;
import com.im.service.UserGroupService;

@Service //bean的作用域为会话session模式，INTERFACES允许该bean注入到单例的bean中@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	private User_groupMapper usergroupMapper;
	
	@Autowired
	private GroupMapper groupMapper;
	
	//加入群组，isCreate为判断字符，1为管理员创建群组，0为普通用户加入群组
	@Override
	public int joinInGroup(User_group userGroup, Group group, int isCreate) {
		
		if(isCreate == 1) {
			groupMapper.InsertIntoGroup(group);
			userGroup.setGroupid(group.getId());
		}
		
		groupMapper.updatePerTotalByGroup_id(group.getGroupId(), 1);        //群组人数加1,	改为通过主键更新
		return usergroupMapper.insert(userGroup);				  //插入群组用户关联表
		
	}

	//查用户是否在群内，若在，查用户身份
	@Override
	public User_group getUserGroupByUserNameAndGroupId(String username, String groupId) {
		
		return usergroupMapper.getUserGroupByUserNameAndGroupId(username, groupId);
	}

	//退出群组
	@Override
	public void leaveGroupByUserNameAndGroupId(String groupId, String username) {

		groupMapper.updatePerTotalByGroup_id(groupId, -1);
		usergroupMapper.leaveGroupByUserNameAndGroupId(groupId, username);
	}

	//根据群号获取管理员信息
	@Override
	public User getAdminByGoupId(String groupId, Boolean isadmin) {

		return usergroupMapper.getAdminByGoupId(groupId, isadmin);
	}

	@Override
	public List<Group> getGroupListByUsernameAndIsAdmin(String username, Boolean isadmin) {

		return usergroupMapper.getGroupListByUsernameAndIsAdmin(username, isadmin);
	}

	@Override
	public List<User> getUserListByGroupId(String groupId) {

		return usergroupMapper.getUserListByGroupId(groupId);
	}


}
