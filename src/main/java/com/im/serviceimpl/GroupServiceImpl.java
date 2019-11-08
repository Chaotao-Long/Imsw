package com.im.serviceimpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.GroupMapper;
import com.im.dao.User_groupMapper;
import com.im.dbmodel.Group;
import com.im.service.GroupService;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupMapper groupMapper;
	
	@Autowired
	private User_groupMapper user_groupMapper;
	
	//插入群组记录
	@Override
	public void InsertIntoGroup(Group group) {
		
		 groupMapper.InsertIntoGroup(group);
		 System.out.println(("~~~~~~~~~~~~~~~~~~serviceImpl层打印group的主键id"));
		 System.out.println(group.getId());
	}

	//根据群号查群组
	@Override
	public Group getGroupByGroup_id(String groupId) {

		return groupMapper.getGroupByGroup_id(groupId);
	}

	//删除群组
	@Override
	public int deleteGroupById(Integer id) {

		user_groupMapper.deleteFromGroupByGroupid(id);
		return groupMapper.deleteByPrimaryKey(id);
	}

	//有选择性修改群组信息
	@Override
	public int updateByIdSelective(Group record) {

		return groupMapper.updateByPrimaryKeySelective(record);
	}


}
