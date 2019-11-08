package com.im.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.im.dbmodel.Branch;

public interface BranchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Branch record);

    int insertSelective(Branch record);

    Branch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Branch record);

    int updateByPrimaryKey(Branch record);
    
    // 插入新分组
 	@Insert("insert into `branch`(branchName,adminName,createtime) values(#{branchname},#{adminname},#{createtime})")
 	public int InsertIntoBranch(Branch branch);

 	// 修改分组名
 	@Update("update branch set branchName = #{newbranchname} where branchName = #{oldbranchname} and adminName = #{username}")
 	public void updateBranchName(@Param("oldbranchname") String oldbranchname,
 			@Param("newbranchname") String newbranchname, @Param("username") String username);

 	// 删除分组(前提要求分组成员为空,可以是移除,也可以是转移)
 	@Delete("delete from `branch` where branchName = #{branchname} and adminName = #{username}")
 	public void deleteBranchFromName(@Param("branchname") String branchname, @Param("username") String username);

 	// 分组拥有者查询分组
 	@Select("select * from `branch` where adminName = #{username} and branchName = #{branchname} ")
 	public Branch selectBranchByBranchNameAndUserName(@Param("branchname") String branchname, @Param("username") String username);
 	
 	// 根据用户username查分组名列表
 	@Select("select branchName from `branch` where adminName = #{username}")
 	public List<String> selectBranchNameByUserName(@Param("username") String username);
 	
}