package com.im.service;

import java.util.List;

import com.im.dbmodel.Branch;

public interface BranchService {

	public int addToBranch(Branch branch);
	
    public void updateBranchName(String oldbranchname, String newbranchname, String username);
    
    public void deleteBranchFromName(String branchname, String username);

    public Branch getBranchByBranchNameAndUserName(String branchname, String username);
    
    public List<String> getBranchNameByUserName(String username);
}
