package com.im.dao;

import com.im.dbmodel.User_group;

public interface User_groupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User_group record);

    int insertSelective(User_group record);

    User_group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User_group record);

    int updateByPrimaryKey(User_group record);
}