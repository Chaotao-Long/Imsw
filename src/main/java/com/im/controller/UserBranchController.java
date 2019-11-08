package com.im.controller;

import java.util.ArrayList;
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
import com.im.dbmodel.Relation;
import com.im.dbmodel.User;
import com.im.dbmodel.User_branch;
import com.im.entity.ConstantBean;
import com.im.entity.ReMap;
import com.im.entity.UserIf;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.im.log4j.Logger;
import com.im.service.BranchService;
import com.im.service.RelationService;
import com.im.service.UserBranchService;
import com.im.service.UserService;
import com.im.util.InfoUtil;
import com.im.util.ObjectUtils;

@RestController
@RequestMapping("/userbranch")
public class UserBranchController {

	@Autowired
	private UserBranchService userBranchService;

	@Autowired
	private UserService userService;

	@Autowired
	private BranchService branchService;

	@Autowired
	private RelationService relationService;

	public static Logger logger = Logger.getLogger(UserBranchController.class);

	/**
	 * 拉人进分组，前提已经是好友关系
	 * 
	 * @param username
	 * @param friendname
	 * @param branchname
	 * @return
	 */
	@RequestMapping(value = "pullInBranch", method = RequestMethod.POST)
	public ReMap pullInBranch(@RequestParam("username") String username, @RequestParam("friendname") String friendname,
			@RequestParam("branchname") String branchname) {

		ReMap reMap = new ReMap();
		User friend = userService.getFromUserByUsername(friendname);
		if (ObjectUtils.isNull(friend)) {
			logger.error("404, friend not found");
			throw new CloudServerNotFoundException(ConstantBean.FRIEND_NOTFOUND);
		}

		// 判断该用户是否已创建名为branchname
		Branch branch = branchService.getBranchByBranchNameAndUserName(branchname, username);
		if (ObjectUtils.isNull(branch)) {
			logger.error("404, not owner in this branchname");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_ADMIN_BRANCH);
		}

		// 判断是否为好友关系
		Relation relation = relationService.getFromRelationByUsernameAndFriendnameAndIsAgree(username, friendname, 1);
		// 若关系不存在则抛错
		if (ObjectUtils.isNull(relation)) {
			logger.error("404, relation not found");
			throw new CloudServerNotFoundException(ConstantBean.RELATION_NOTFOUND);
		}

		// 判断该用户是否存在该分组内
		User_branch user_branch = userBranchService.getBranchByUserNameAndBranchName(username, branchname, friendname);
		if (ObjectUtils.isNotNull(user_branch)) {
			logger.error("401, already branch member");
			throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_BRANCH_MEMBER);
		}

		User_branch userbranch = new User_branch();
		userbranch.setUserid(friend.getId());
		userbranch.setBranchid(branch.getId());
		userbranch.setJointime(new Date());
		userBranchService.addIntoUserBranch(userbranch);

		return reMap;
	}

	/**
	 * 查看分组成员
	 * 
	 * @param username
	 * @param branchname
	 * @return friendInfo
	 */
	@RequestMapping(value = "/selectMember", method = RequestMethod.GET)
	public Map<String, Object> selectMember(@RequestParam("username") String username,
			@RequestParam("branchname") String branchname) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Branch branch = branchService.getBranchByBranchNameAndUserName(branchname, username);
		if (ObjectUtils.isNull(branch)) {
			logger.error("404, not owner in this branchname");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_ADMIN_BRANCH);
		}

		List<User> friendList = userBranchService.getUserListByBranchNameAndAdminName(username, branchname);
		List<UserIf> memberiflist = new ArrayList<UserIf>();

		for (User user : friendList) {
			UserIf userIf = new UserIf();
			InfoUtil.UsertransIf(user, userIf, null);
			memberiflist.add(userIf);
		}

		resultMap.put("status", 200);
		resultMap.put("msg", "request successfully!");
		resultMap.put("memberiflist", memberiflist);
		return resultMap;

	}

	/**
	 * 移除分组成员，回到好友默认列表，而后才可以删除好友
	 * 
	 * @param username
	 * @param branchname
	 * @param friendname
	 * @return
	 */
	@RequestMapping(value = "/removeMember", method = RequestMethod.GET)
	public ReMap removeMember(@RequestParam("username") String username, @RequestParam("branchname") String branchname,
			@RequestParam("friendname") String friendname) {

		ReMap reMap = new ReMap();
		Branch branch = branchService.getBranchByBranchNameAndUserName(branchname, username);
		if (ObjectUtils.isNull(branch)) {
			logger.error("404, not owner in this branchname");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_ADMIN_BRANCH);
		}

		// 判断该用户是否存在该分组内
		User_branch userbranch = userBranchService.getBranchByUserNameAndBranchName(username, branchname, friendname);
		if (ObjectUtils.isNull(userbranch)) {
			logger.error("404, friend is not in the branch");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_FRIEND_BRANCH);
		}

		userBranchService.deleteUserBranchByUserNameAndBranchName(friendname, branchname, username);
		return reMap;
	}

	/**
	 * 转移好友到不同分组
	 * 
	 * @param username
	 * @param oldbranchname
	 * @param newbranchname
	 * @param friendname
	 * @return
	 */
	@RequestMapping(value = "/transforMember", method = RequestMethod.POST)
	public ReMap transforMember(@RequestParam("username") String username,
			@RequestParam("oldbranchname") String oldbranchname, @RequestParam("newbranchname") String newbranchname,
			@RequestParam("friendname") String friendname) {

		ReMap reMap = new ReMap();
		Branch oldbranch = branchService.getBranchByBranchNameAndUserName(oldbranchname, username);
		Branch newbranch = branchService.getBranchByBranchNameAndUserName(newbranchname, username);

		// 判断管理员是否有这两个分组
		if (ObjectUtils.isNull(oldbranch) || ObjectUtils.isNull(newbranch)) {
			logger.error("404, not owner in this branchname");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_ADMIN_BRANCH);
		}

		// 判断该用户是否存在该分组内
		User_branch userbranch = userBranchService.getBranchByUserNameAndBranchName(username, oldbranchname,
				friendname);
		if (ObjectUtils.isNull(userbranch)) {
			logger.error("404, friend is not in the branch");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_FRIEND_BRANCH);
		}

		userBranchService.updateBranchByNewOldUserName(friendname, oldbranch.getId(), newbranch.getId());
		return reMap;
	}

}
