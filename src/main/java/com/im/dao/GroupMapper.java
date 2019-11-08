package com.im.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.Group;

public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);
    
    //插入新群组
    @Insert("insert into `group`(group_id, groupName, groupDesc, personTotal, groupPhoto,createtime) values(#{groupId}, #{groupname}, #{groupdesc}, #{persontotal}, #{groupphoto}, #{createtime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void InsertIntoGroup(Group group);
    
//    //有选择性地更新群组信息(如果原表中有此项记录而已传入参数为null,则会覆盖掉原来的数据，即将数据置为空)
//    @Update("update `group` set groupName = #{groupname}, groupDesc = #{groupdesc}, groupPhoto = #{groupphoto} where id = #{id}")
//    public int updateByIdSelective(Group group);
    
    //根据群号查群组
    @Select("select id, group_id groupId, groupName groupname, groupDesc groupdesc, personTotal persontotal, groupPhoto groupphoto, createtime from `group` where group_id = #{groupId}")
    public Group getGroupByGroup_id(@Param("groupId") String groupId);
    
    //更新群组人数
    @Update("update `group` set personTotal = personTotal + #{count} where group_id = #{groupId}")
    public void updatePerTotalByGroup_id(@Param("groupId") String groupId, @Param("count") Integer count); 
    
    //更新群组groupName和groupDesc
//    @Update("update group set groupName = #{groupname}, groupDesc = #{groupDesc} where group_id=#{groupId}")
//    public void updateGroupByGroup_id(Group group);
    
//    @Delete("delete from `group` where group_id = #{groupId}")
//    public void deleteGroupByGroup_id(@Param("groupId") String groupId);
}