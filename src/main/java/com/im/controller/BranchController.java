package com.im.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.im.dbmodel.Branch;
import com.im.dbmodel.User;
import com.im.entity.ConstantBean;
import com.im.entity.ReMap;
import com.im.exception.CloudServerDatabaseException;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.im.log4j.Logger;
import com.im.service.BranchService;
import com.im.service.UserBranchService;
import com.im.service.UserService;
import com.im.util.ObjectUtils;

@RequestMapping("/branch")
@RestController
public class BranchController {

	@Autowired
	private UserService userService;

	@Autowired
	private BranchService branchService;
	
	@Autowired
	private UserBranchService userBranchService;

	public static Logger logger = Logger.getLogger(BranchController.class);

	/**
	 * 	创建分组
	 * @param username
	 * @param branchname
	 * @return
	 */
	@RequestMapping(value = "/createBranch", method = RequestMethod.POST)
	public ReMap createBranch(@RequestParam("username") String username,
			@RequestParam("branchname") String branchname) {

		ReMap reMap = new ReMap();
		// 判断用户是否存在
//		User user = userService.getFromUserByUsername(username);
//		if (ObjectUtils.isNull(user)) {
//			logger.error("404, user not found");
//			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
//		}
		// 判断该用户是否已创建名为branchname
		Branch record = branchService.getBranchByBranchNameAndUserName(branchname, username);
		if (ObjectUtils.isNotNull(record)) {
			logger.error("401, already branchname");
			throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_BRANCHNAME);
		}
		Branch branch = new Branch();
		branch.setAdminname(username);
		branch.setBranchname(branchname);
		branch.setCreatetime(new Date());
		int row = branchService.addToBranch(branch);
		if (row == 0) {
			logger.error("402, database error!");
			throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
		}
		reMap.setMsg("create successfully!");
		return reMap;
	}

	/**
	 * 	修改分组名
	 * @param username
	 * @param oldbranchname
	 * @param newbranchname
	 * @return
	 */
	@RequestMapping(value = "/repairBranchName", method = RequestMethod.POST)
	public ReMap repairBranchName(@RequestParam("username") String username,
			@RequestParam("oldbranchname") String oldbranchname, 
			@RequestParam("newbranchname") String newbranchname) {

		ReMap reMap = new ReMap();
		
		Branch oldbranch = branchService.getBranchByBranchNameAndUserName(oldbranchname, username);
		if(ObjectUtils.isNull(oldbranch)) {
			logger.error("404, not owner in this branchname");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_ADMIN_BRANCH);
		}
		
		Branch newbranch = branchService.getBranchByBranchNameAndUserName(newbranchname, username);
		if(ObjectUtils.isNotNull(newbranch)) {
			logger.error("401, already branchname");
			throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_BRANCHNAME);
		}
		
		branchService.updateBranchName(oldbranchname, newbranchname, username);	
		reMap.setMsg("repair successfully!");
		return reMap;
	}
	
	/**
	 * 	加载分组名列表
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/selectBranchName",method = RequestMethod.GET)
	public Map<String, Object> selectBranchName(@RequestParam("username") String username) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userService.getFromUserByUsername(username);
		if(ObjectUtils.isNull(user)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}
		
		List<String> branchnamelist = branchService.getBranchNameByUserName(username);
		
		map.put("status", 200);
		map.put("msg", "request successfully!");
		map.put("branchnamelist", branchnamelist);
		return map;
	}
	
	/**
	 * 	删除空分组
	 * @param username
	 * @param branchname
	 * @return
	 */
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.GET)
	public ReMap deleteBranch(@RequestParam("username") String username, @RequestParam("branchname") String branchname) {
		
		ReMap reMap = new ReMap();
		
		Branch record = branchService.getBranchByBranchNameAndUserName(branchname, username);
		if(ObjectUtils.isNull(record)) {
			logger.error("404, not owner in this branchname");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_ADMIN_BRANCH);
		}
		
		List<User> user = userBranchService.getUserListByBranchNameAndAdminName(username, branchname);
		if(ObjectUtils.isNotNull(user) && user.size() != 0) {
			logger.error("401, already branch member");
			throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_BRANCH_MEMBER);
		}
		
		branchService.deleteBranchFromName(branchname, username);
		return reMap;
		
	}
	
	

}
