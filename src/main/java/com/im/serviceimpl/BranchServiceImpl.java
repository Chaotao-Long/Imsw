package com.im.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.BranchMapper;
import com.im.dbmodel.Branch;
import com.im.service.BranchService;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchMapper branchMapper;
	
	@Override
	public int addToBranch(Branch branch) {

		return branchMapper.InsertIntoBranch(branch);
		
	}

	@Override
	public void updateBranchName(String oldbranchname, String newbranchname, String username) {

		 branchMapper.updateBranchName(oldbranchname, newbranchname, username);
	}

	@Override
	public void deleteBranchFromName(String branchname,String username) {

		branchMapper.deleteBranchFromName(branchname, username);
	}

	@Override
	public Branch getBranchByBranchNameAndUserName(String branchname, String username) {

		return branchMapper.selectBranchByBranchNameAndUserName(branchname, username);
	}

	@Override
	public List<String> getBranchNameByUserName(String username) {

		return branchMapper.selectBranchNameByUserName(username);
	}

}
