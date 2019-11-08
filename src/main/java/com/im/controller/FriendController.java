package com.im.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.im.dbmodel.Relation;
import com.im.dbmodel.User;
import com.im.dbmodel.User_branch;
import com.im.entity.ConstantBean;
import com.im.entity.ReMap;
import com.im.entity.ResultMap;
import com.im.entity.UserIf;
import com.im.entity.UserInfo;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.im.log4j.Logger;
import com.im.service.FriendTransactionService;
import com.im.service.RelationService;
import com.im.service.UserBranchService;
import com.im.service.UserService;
import com.im.util.FriendControllerUtil;
import com.im.util.InfoUtil;
import com.im.util.ObjectUtils;

/**
 * @author itpeach desc:好友关系 以下展现了两组关于好友关系的方法
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

	@Autowired
	private UserService userService;

	@Autowired
	private RelationService relationService;

	@Autowired
	private FriendTransactionService friendTransactionService;

	@Autowired
	private UserBranchService userBranchService;

	public static Logger logger = Logger.getLogger(FriendController.class);

	// 好友方法一：
	/**
	 * @desc 添加好友
	 * @author itpeach
	 * @param username   发起添加请求的用户帐号
	 * @param friendname 被添加好友的用户帐号
	 * @return reMap
	 */
	@RequestMapping(value = "/addFriend", method = RequestMethod.POST)
	public ReMap addFriend(@RequestParam("username") String username, @RequestParam("friendname") String friendname) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		User friend = userService.getFromUserByUsername(friendname);
		// 如果没找到其中一方信息则报错
		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(friend)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		// 查对方是否有添加过用户本身
		Relation oldrelation = relationService.getFromRelationByUserIdAndFriendId(friend.getId(), user.getId());
		if (ObjectUtils.isNotNull(oldrelation)) {
			// 已是好友
			if (oldrelation.getIsagree() == 1) {
				throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_FRIEND);
			} else { // 之前对方已添加过但没成功,现在反过来添加对方即相当于同意加好友
				// 将添加人的好友记录isAgree更新为1
				Relation relation1 = new Relation();
				relation1.setUserid(friend.getId());
				relation1.setFriendid(user.getId());
				relation1.setIsagree(1);
				relation1.setUpdatetime(new Date());

				// 由于相对性，被添加人同意则插入新的关系中
				Relation relation2 = new Relation();
				relation2.setUserid(user.getId());
				relation2.setFriendid(friend.getId());
				relation2.setIsagree(1);
				relation2.setUpdatetime(new Date());

				// 更新并插入好友记录
				friendTransactionService.updateAndInsertFriendRelation(relation1, relation2);
				logger.info("200, request bsuccessfully, add mutually");
				return reMap;
			}
		}

		// 查之前用户本身是否有添加过对方
		Relation relation = relationService.getFromRelationByUserIdAndFriendId(user.getId(), friend.getId());

		Relation newRelation = new Relation();
		newRelation.setUserid(user.getId());
		newRelation.setFriendid(friend.getId());
		newRelation.setIsagree(0);
		newRelation.setUpdatetime(new Date());

		if (ObjectUtils.isNull(relation)) {

			// 没记录则表明此前没添加过，插入新关系
			relationService.addToRelation(newRelation);
			logger.info("no relationship before, add information into relation table");
			logger.info(newRelation.toString());
			// throw new CloudServerNotFoundException(ConstantBean.RELATION_NOTFOUND);
		}
		// 有记录,讨论isAgree情况，0表示待同意，1表示已同意，2表示拒绝
		else {
			if (relation.getIsagree() == 0) { // 0则表明此前已申请过无须重复申请
				throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_APPLY);
			} else if (relation.getIsagree() == 1) { // 1则表明已是好友，无须重复添加
				throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_FRIEND);
			} else { // 2表明此前拒绝过，重新申请

				relationService.updateIsAgreeByUserIdAndFriendId(newRelation);
				logger.info("200, request successfully, reject before");
				logger.info(newRelation.toString());
			}
		}

		return reMap;
	}

	/**
	 * @desc 同意添加好友
	 * @param username   被添加好友的用户帐号
	 * @param friendname 发起添加请求的用户帐号
	 * @return
	 */
	@RequestMapping(value = "/agreeFriend", method = RequestMethod.POST)
	public ReMap agreeFriend(@RequestParam("username") String username, @RequestParam("friendname") String friendname) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		User friend = userService.getFromUserByUsername(friendname);
		// 如果没找到其中一方信息则报错
		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(friend)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}
		// 查找是否已发起添加,且为待同意状态
		Relation relation = relationService.getFromRelationByUsernameAndFriendnameAndIsAgree(friendname, username, 0);
		if (ObjectUtils.isNull(relation)) {
			logger.error("404, relation not found");
			throw new CloudServerNotFoundException(ConstantBean.RELATION_NOTFOUND);
		}

		// 将添加人的好友记录isAgree更新为1
		Relation relation1 = new Relation();
		relation1.setUserid(friend.getId());
		relation1.setFriendid(user.getId());
		relation1.setIsagree(1);
		relation1.setUpdatetime(new Date());

		// 由于相对性，被添加人同意则插入新的关系中
		Relation relation2 = new Relation();
		relation2.setUserid(user.getId());
		relation2.setFriendid(friend.getId());
		relation2.setIsagree(1);
		relation2.setUpdatetime(new Date());

		// 更新并插入好友记录
		friendTransactionService.updateAndInsertFriendRelation(relation1, relation2);
		logger.info("200, request bsuccessfully, add mutually");
		return reMap;
	}

	/**
	 * @desc 拒绝添加好友
	 * @param username   被添加好友的用户帐号
	 * @param friendname 发起添加请求的用户帐号
	 * @return
	 */
	@RequestMapping(value = "/rejectFriend", method = RequestMethod.POST)
	public ReMap rejectFriend(@RequestParam("username") String username,
			@RequestParam("friendname") String friendname) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		User friend = userService.getFromUserByUsername(friendname);
		// 如果没找到其中一方信息则报错
		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(friend)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		// 待获取同意好友记录
		Relation oldRelation = relationService.getFromRelationByUsernameAndFriendnameAndIsAgree(friendname, username,
				0);

		// 若关系不存在则抛错
		if (ObjectUtils.isNull(oldRelation)) {
			logger.error("404, relation not found");
			throw new CloudServerNotFoundException(ConstantBean.RELATION_NOTFOUND);
		}

		// 更新发起添加时插入的isAgree的值为2
		Relation relation = new Relation();
		relation.setUserid(friend.getId());
		relation.setFriendid(user.getId());
		relation.setIsagree(2);
		relation.setUpdatetime(new Date());

		relationService.updateIsAgreeByUserIdAndFriendId(relation);
		logger.info(relation.toString());

		logger.info("200, request successfully, add reject");
		reMap.setMsg("reject successfully");
		return reMap;
	}

	// 查询单个指定好友信息
	@RequestMapping(value = "/selectFriendInfo", method = RequestMethod.GET)
	public ResultMap selectFriendInfo(@RequestParam("username") String username, @RequestParam String friendname) {

		ResultMap resultMap = new ResultMap();

		// ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		User friend = userService.getFromUserByUsername(friendname);
		// 如果没找到其中一方信息则报错
		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(friend)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		// 1.判断待查的是否是好友关系（已同意的好友）
		List<Integer> friendIdList = relationService.getFriendIdFromUserJoinRelationByUsernameAndIsAgree(username, 1);
		System.out.println(friendIdList);

		Integer thisFriendId = friend.getId();
		// 判断好友关系
		if (!friendIdList.contains(thisFriendId)) {
			// 如果不是好友关系,抛出权限异常
			logger.error("401, get friend information denied, no friendship");
			throw new CloudServerPermissionDenyException(ConstantBean.OPERATION_GET_FRIEND_INFO);
		}

		UserInfo friendInfo = new UserInfo();
		InfoUtil.UsertransInfo(friend, friendInfo);

		resultMap.setUserInfo(friendInfo);
		return resultMap;
	}

	// 删除好友(如果用户在分组内,前提要先移除分组成员,再删除好友)
	@RequestMapping(value = "/deleteFriend", method = RequestMethod.GET)
	public Map<String, Object> deleteFriend(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("friendname") String friendname,
			@RequestParam(value = "branchname", required = false) String branchname) {

		Map<String, Object> returnMap = new HashMap<String, Object>();

		User user = userService.getFromUserByUsername(username);
		User friend = userService.getFromUserByUsername(friendname);

		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(friend)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		// 获取同意好友记录
		Relation relation = relationService.getFromRelationByUsernameAndFriendnameAndIsAgree(username, friendname, 1);

		// 若关系不存在则抛错
		if (ObjectUtils.isNull(relation)) {
			logger.error("404, relation not found");
			throw new CloudServerNotFoundException(ConstantBean.RELATION_NOTFOUND);
		}

		// 判断有没有移除分组成员
		User_branch userbranch = userBranchService.getBranchByUserNameAndFriendId(username, friend.getId());
		if (ObjectUtils.isNotNull(userbranch)) {
			logger.error("401, already branch member");
			throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_BRANCH_MEMBER);
		}

		// 若关系存在，则把两条记录都删除
		relationService.deleteFriend(username, friendname);
		relationService.deleteFriend(friendname, username);

		returnMap.put("status", 200);
		returnMap.put("msg", "request successfully.");
		return returnMap;
	}

	/**
	 * 获取好友列表,包括待同意, 已同意，已拒绝的好友列表
	 * 
	 * @param request
	 * @param UserOpenid 用户本人openid
	 * @return
	 */
	@RequestMapping(value = "/getFrilist", method = RequestMethod.POST)
	public Map<String, Object> getFrilist(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("type") Integer type) { // 合并下面的请求，通过设置多一个参数来判断查看的是哪类型的好友列表
		// type传值为0时表示查看待同意好友列表，为1时查看同意好友列表，为2时查看拒绝好友列表
		Map<String, Object> returnMap = new HashMap<String, Object>();
		System.out.println("进来接口");
		if (type == 0 || type == 1 || type == 2) {
			System.out.println("进来判断");
			// 设置并记录
			returnMap.put("status", 200);
			returnMap.put("msg", "request successfully.");
			User user = userService.getFromUserByUsername(username);
			if (ObjectUtils.isNull(user)) {
				logger.error("404, user not found");
				throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
			}

			// 若type为空即说明只查待同意的好友列表，直接返回
			if (type == 0) {
				// 1.1,根据用户id在relation表中获取isagree=0好友详细信息列表(自己加对方，对方待同意)
				List<User> UserNonFriendList = relationService.getFriendFromUserRelationByUsernameAndIsAgree(username,
						0);
				System.out.println("对方待同意" + UserNonFriendList);
				// 1.2,根据用户id在relation表中获取isagree=0好友详细信息列表(对方加自己，自己待同意)
				List<User> FriendNonUserList = relationService.getFriendFromUserRelationByFriendnameAndIsAgree(username,
						0);
				System.out.println("自己待同意" + FriendNonUserList);

				// 2.根据用户id获取用户的具体信息（隐藏敏感信息）
				// 处理待同意的好友列表
				List<UserIf> UserNonFriendinfoList = new ArrayList<UserIf>();
				List<UserIf> FriendNonUserinfoList = new ArrayList<UserIf>();

				for (User user1 : UserNonFriendList) {
					UserIf userIf1 = new UserIf();
					InfoUtil.UsertransIf(user1, userIf1, null);
					UserNonFriendinfoList.add(userIf1);
				}

				for (User user2 : FriendNonUserList) {
					UserIf userIf2 = new UserIf();
					InfoUtil.UsertransIf(user2, userIf2, null);
					FriendNonUserinfoList.add(userIf2);
				}

				// 4.构造正确返回
				logger.info("200, request successfully");
				returnMap.put("UserNonFriendinfoList", UserNonFriendinfoList);
				logger.info("UserNonFriendinfoList: " + UserNonFriendinfoList.toString());
				returnMap.put("FriendNonUserinfoList", FriendNonUserinfoList);
				logger.info("FriendNonUserinfoList: " + FriendNonUserinfoList.toString());
				return returnMap;

			} else if (type == 1) {
				System.out.println("type为1");
				// 查已同意
				// 根据用户id在relation表中获取isagree=1好友详细信息列表(同意)
				List<User> agreUserList = relationService.getFriendFromUserRelationByUsernameAndIsAgree(username, 1);
				List<User> movedUserList = new ArrayList<User>();

				// 遍历好友列表,找出不在分组内的好友以显示在好友大列表中
				for (User friend : agreUserList) {
					User_branch userbranch = userBranchService.getBranchByUserNameAndFriendId(username, friend.getId());
					// 如果好友不在分组内,则添加进新列表以返回,好友在分组就不添加
					if (ObjectUtils.isNull(userbranch)) {
						movedUserList.add(friend);
						continue;
					}
				}

				// 2.根据用户id获取用户的具体信息（隐藏敏感信息）
				// 处理已同意的好友列表
				List<UserInfo> agreUserinfoList = FriendControllerUtil.addInfoList(movedUserList);

				returnMap.put("agreUserinfoList", agreUserinfoList);
				logger.info("200, request successfully");
				logger.info("agreUserinfoList: " + agreUserinfoList.toString());
				return returnMap;
			} else {
				// 查拒绝列表
				// 1.1,根据用户id在relation表中获取isagree=2好友详细信息列表(自己拒绝对方)
				List<User> UserRejeFriendList = relationService
						.getFriendFromUserRelationByFriendnameAndIsAgree(username, 2);

				// 1.2,根据用户id在relation表中获取isagree=2好友详细信息列表(对方拒绝自己)
				List<User> FriendRejeUserList = relationService.getFriendFromUserRelationByUsernameAndIsAgree(username,
						2);

				// 2.根据用户id获取用户的具体信息（隐藏敏感信息）
				// 处理拒绝的好友列表
				List<UserIf> UserRejeFriendinfoList = new ArrayList<UserIf>();
				List<UserIf> FriendRejeUserinfoList = new ArrayList<UserIf>();

				for (User user1 : UserRejeFriendList) {
					UserIf userIf1 = new UserIf();
					InfoUtil.UsertransIf(user1, userIf1, null);
					UserRejeFriendinfoList.add(userIf1);
				}

				for (User user2 : FriendRejeUserList) {
					UserIf userIf2 = new UserIf();
					InfoUtil.UsertransIf(user2, userIf2, null);
					FriendRejeUserinfoList.add(userIf2);
				}

				// 4.构造正确返回
				logger.info("200, request successfully");
				returnMap.put("UserRejeFriendinfoList", UserRejeFriendinfoList);
				logger.info("UserRejeFriendinfoList: " + UserRejeFriendinfoList.toString());
				returnMap.put("FriendRejeUserinfoList", FriendRejeUserinfoList);
				logger.info("FriendRejeUserinfoList: " + FriendRejeUserinfoList.toString());
				return returnMap;
			}
		} else {
			logger.error("404, type error");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_TRANSFER_TYPE);
		}
	}

}
