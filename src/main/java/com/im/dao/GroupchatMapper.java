package com.im.dao;

import com.im.dbmodel.Groupchat;

public interface GroupchatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Groupchat record);

    int insertSelective(Groupchat record);

    Groupchat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Groupchat record);

    int updateByPrimaryKey(Groupchat record);
}