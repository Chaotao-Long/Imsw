package com.im.dao;

import com.im.dbmodel.Tr_record;

public interface Tr_recordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tr_record record);

    int insertSelective(Tr_record record);

    Tr_record selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tr_record record);

    int updateByPrimaryKey(Tr_record record);
}