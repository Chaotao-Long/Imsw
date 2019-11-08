package com.im.service;

import com.im.dbmodel.Group;

public interface GroupService {

	public void InsertIntoGroup(Group group);

	public Group getGroupByGroup_id(String groupId);

	public int deleteGroupById(Integer id);

	public int updateByIdSelective(Group record);
	
}
