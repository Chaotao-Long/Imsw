package com.im.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.im.dbmodel.Group;
import com.im.dbmodel.User;
import com.im.dbmodel.User_group;
import com.im.entity.ConstantBean;
import com.im.entity.ReMap;
import com.im.exception.CloudServerDatabaseException;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.im.log4j.Logger;
import com.im.service.GroupService;
import com.im.service.UserGroupService;
import com.im.service.UserService;
import com.im.util.FileUtil;
import com.im.util.ObjectUtils;
import com.im.util.RandomUtil;

@RestController
@RequestMapping("/userGroup")
public class UserGroupController {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserGroupService userGroupService;

	public static Logger logger = Logger.getLogger(UserGroupController.class);

	/**
	 * 创建群组/管理员加入群组
	 * 
	 * @param groupname
	 * @param groupdesc
	 * @param username
	 * @return groupId
	 */
	@RequestMapping(value = "/createGroup", method = RequestMethod.POST)
	public Map<String, Object> createGroup(HttpServletRequest request,
			@RequestParam(value = "groupname", required = false) String groupname,
			@RequestParam(value = "groupdesc", required = false) String groupdesc,
			@RequestParam(value = "groupphoto", required = false) String groupphoto,
			@RequestParam(value = "groupphotoFile", required = false) MultipartFile groupphotoFile,
			@RequestParam(value = "username") String username) { // group主要传群组名，群简介，群头像

		Map<String, Object> returnMap = new HashMap<String, Object>();
		String groupphotoUrl = null;
		String groupId = null;
		while (true) {
			groupId = RandomUtil.randomStr();
			Group record = groupService.getGroupByGroup_id(groupId); // 将生成的id查数据库有没有相同id，有则重新生成

			if (ObjectUtils.isNull(record)) { // 直到id唯一为止
				break;
			}
		}
		
		//如果同时传入文件名groupphoto和原文件groupphotoFile,则上传并获取文件路径,否则路径为空
		if (ObjectUtils.isNotNull(groupphoto) && ObjectUtils.isNotNull(groupphotoFile)) {
			// 如果文件名不是以图片格式命名则报错
			if (!groupphoto.endsWith(".jpg") && !groupphoto.endsWith(".png") && !groupphoto.endsWith(".gif")) {
				returnMap.put("status", 404);
				returnMap.put("msg", "upload error, file must endswith jpg or png or gif");
				return returnMap;
			}
			groupphotoUrl = FileUtil.fileToUrl(request, groupphotoFile, groupphoto, groupId, "groupphoto");
		}

		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNull(user)) {
			logger.error("user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		Group group = new Group();
		User_group user_group = new User_group();

		group.setGroupname(groupname);
		group.setGroupdesc(groupdesc);
		group.setGroupId(groupId);
		group.setPersontotal(0); // 先将群组初始化，此时刚创建，人数为零
		group.setGroupphoto(groupphotoUrl);
		group.setCreatetime(new Date());
		// groupService.InsertIntoGroup(group);

		user_group.setUserid(user.getId());
		// user_group.setGroupid(group.getId());
		user_group.setIsadmin(true);
		user_group.setJointime(new Date());

		int row = userGroupService.joinInGroup(user_group, group, 1); // 管理员也要加入群组,此时群组表创建并且人数加1，关系表插入记录
		if (row == 0) {
			logger.error("402, database error");
			throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
		}
		returnMap.put("status", 200);
		returnMap.put("msg", "create successfully!");
		returnMap.put("The groupId：", groupId);
		return returnMap;
	}

	/**
	 * 加入群组
	 * 
	 * @param username
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/joinGroup", method = RequestMethod.POST)
	public ReMap joinGroup(@RequestParam("username") String username, @RequestParam("groupId") String groupId) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		Group group = groupService.getGroupByGroup_id(groupId);

		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(group)) {
			logger.error("404, user or group not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_OR_GROUP_NOTFOUND);
		}

		// 判断是否是组员
		User_group user_group = userGroupService.getUserGroupByUserNameAndGroupId(username, groupId);
		if (ObjectUtils.isNotNull(user_group)) {
			// 是管理员
			if (user_group.getIsadmin() == true) {
				throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_ADMIN);
			} else { // 普通用户
				throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_JOINGROUP);
			}
		}

		User_group userGroup = new User_group();
		userGroup.setUserid(user.getId());
		userGroup.setGroupid(group.getId());
		userGroup.setIsadmin(false);
		userGroup.setJointime(new Date());

		int row = userGroupService.joinInGroup(userGroup, group, 0);
		if (row == 0) {
			logger.error("402, database error");
			throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
		}
		reMap.setMsg("join successfully！");
		return reMap;
	}

	/**
	 * 主动离开群组
	 * 
	 * @param username
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/leaveGroup", method = RequestMethod.GET)
	public ReMap leaveGroup(@RequestParam("username") String username, @RequestParam("groupId") String groupId) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		Group group = groupService.getGroupByGroup_id(groupId);

		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(group)) {
			logger.error("404, user or group not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_OR_GROUP_NOTFOUND);
		}

		// 判断是否是组员
		User_group user_group = userGroupService.getUserGroupByUserNameAndGroupId(username, groupId);
		if (ObjectUtils.isNull(user_group)) {
			throw new CloudServerNotFoundException(ConstantBean.ALREADY_LEAVEGROUP);
		}

		// 判断用户角色，管理员则直接删除群组
		if (user_group.getIsadmin() == true) {
			reMap.setStatus(401);
			reMap.setMsg("you are admin, please delete group directly!");
			return reMap;
		}

		userGroupService.leaveGroupByUserNameAndGroupId(groupId, username);

		reMap.setMsg("leave successfully!");
		return reMap;
	}

	/**
	 * 群主踢人
	 * 
	 * @param adminName 管理员
	 * @param groupId   群号
	 * @param username  被踢者
	 * @return
	 */
	@RequestMapping(value = "/beAway", method = RequestMethod.GET)
	public ReMap beAwayGroup(@RequestParam("adminName") String adminName, @RequestParam("groupId") String groupId,
			@RequestParam("username") String username) {

		ReMap reMap = new ReMap();
		User_group adminGroup = userGroupService.getUserGroupByUserNameAndGroupId(adminName, groupId);
		User_group userGroup = userGroupService.getUserGroupByUserNameAndGroupId(username, groupId);

		if (ObjectUtils.isNull(userGroup) || ObjectUtils.isNull(adminGroup)) {
			logger.error("404, adminname or username is not member");
			throw new CloudServerNotFoundException("adminname or username is " + ConstantBean.OPERATION_NOT_MEMBER);
		}

		if (adminGroup.getIsadmin() == true) {

			userGroupService.leaveGroupByUserNameAndGroupId(groupId, username);
			return reMap;
		} else {
			logger.error("401, adminName is not admin");
			throw new CloudServerPermissionDenyException(ConstantBean.OPERATION_NOT_ADMIN);
		}

	}

}
