package com.im.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.im.entity.GroupInfo;
import com.im.entity.ReMap;
import com.im.entity.UserIf;
import com.im.exception.CloudServerDatabaseException;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.im.log4j.Logger;
import com.im.service.GroupService;
import com.im.service.UserGroupService;
import com.im.service.UserService;
import com.im.util.FileUtil;
import com.im.util.InfoUtil;
import com.im.util.ObjectUtils;

@RestController
@RequestMapping("/group")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserGroupService userGroupService;

	public static Logger logger = Logger.getLogger(GroupController.class);

	/**
	 * 根据用户身份查看单个群组及其管理员和成员信息
	 * 
	 * @param username
	 * @param groupId
	 * @return 群组相关信息
	 */
	@RequestMapping(value = "/selectGroup", method = RequestMethod.GET)
	public Map<String, Object> selectGroupAdmin(@RequestParam("username") String username,
			@RequestParam("groupId") String groupId) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", 200);
		map.put("msg", "request successfully!");
		GroupInfo groupInfo = new GroupInfo(); // 定义改写实例
		UserIf adminif = new UserIf();
		List<UserIf> userIfList = new ArrayList<UserIf>(); // 定义隐藏对象列表实例

		User user = userService.getFromUserByUsername(username); // 查用户
		Group group = groupService.getGroupByGroup_id(groupId); // 查群组
		if (ObjectUtils.isNull(user) || ObjectUtils.isNull(group)) {
			logger.error("404, user or group not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_OR_GROUP_NOTFOUND);
		}

		User admin = userGroupService.getAdminByGoupId(groupId, true); // 查管理员详细信息
		List<User> memberList = userGroupService.getUserListByGroupId(groupId); // 根据群号查用户详细信息列表
		// 只须重组,无须隐藏群组信息
		InfoUtil.GrouptransInfo(group, groupInfo, "all");

		// 以下根据用户身份来限定查看成员信息，不限群组信息
		User_group usergroup = userGroupService.getUserGroupByUserNameAndGroupId(username, groupId);
		// 1,不是组员,只能查看管理员和成员简略信息
		if (ObjectUtils.isNull(usergroup)) {
			InfoUtil.UsertransIf(admin, adminif, null); // 隐藏管理员大部分信息
			for (User member : memberList) {
				UserIf userIf = new UserIf(); // 定义隐藏对象实例
				InfoUtil.UsertransIf(member, userIf, null); // 隐藏对象大部分信息
				userIfList.add(userIf); // 隐藏对象列表信息
			}
		} else {
			InfoUtil.UsertransIf(admin, adminif, "all"); // 隐藏管理员敏感信息
			for (User member : memberList) {
				UserIf userIf = new UserIf(); // 定义隐藏对象实例
				InfoUtil.UsertransIf(member, userIf, "all"); // 隐藏对象敏感信息
				userIfList.add(userIf); // 隐藏对象列表信息
			}
		}
		groupInfo.setAdminif(adminif); // 隐藏管理员信息
		groupInfo.setUseriflist(userIfList);
		map.put("groupInfo", groupInfo);
		return map;
	}

	/**
	 * 获取群组列表
	 * 
	 * @param username
	 * @return 创建和加入的群组列表
	 */
	@RequestMapping(value = "/selectGroupList", method = RequestMethod.GET)
	public Map<String, Object> selectGroupList(@RequestParam("username") String username) {

		Map<String, Object> retultMap = new HashMap<String, Object>();
		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNull(user)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}
		// 查创建的群组
		List<Group> adminGroups = userGroupService.getGroupListByUsernameAndIsAdmin(username, true);
		// 查加入的群组
		List<Group> userGroups = userGroupService.getGroupListByUsernameAndIsAdmin(username, false);

		List<GroupInfo> adminGroupList = new ArrayList<GroupInfo>();
		List<GroupInfo> userGroupList = new ArrayList<GroupInfo>();
//		GroupInfo adminGroupInfo = new GroupInfo(); 
//		GroupInfo userGroupInfo = new GroupInfo();

		// 隐藏群组部分信息
		for (Group adminGroup : adminGroups) {
			GroupInfo adminGroupInfo = new GroupInfo();
			InfoUtil.GrouptransInfo(adminGroup, adminGroupInfo, null);
			adminGroupList.add(adminGroupInfo);
		}
		for (Group userGroup : userGroups) {
			GroupInfo userGroupInfo = new GroupInfo();
			InfoUtil.GrouptransInfo(userGroup, userGroupInfo, null);
			userGroupList.add(userGroupInfo);
		}

		retultMap.put("status", 200);
		retultMap.put("msg", "request successfully!");
		retultMap.put("adminGroupList", adminGroupList);
		retultMap.put("userGroupList", userGroupList);
		return retultMap;
	}

	/**
	 * 管理员修改群名或群简介或群头像
	 * 
	 * @param username(admin)
	 * @param groupId
	 * @param groupname
	 * @param groupdesc
	 * @return
	 */
	@RequestMapping(value = "/repairGroup", method = RequestMethod.POST)
	public ReMap repairGroup(HttpServletRequest request, @RequestParam("groupId") String groupId,
			@RequestParam(value = "groupname", required = false) String groupname,
			@RequestParam(value = "groupdesc", required = false) String groupdesc,
			@RequestParam(value = "groupphoto", required = false) String groupphoto,
			@RequestParam(value = "groupphotoFile", required = false) MultipartFile groupphotoFile,
			@RequestParam("username") String username) { // 群号id,用户名为必选，群组名,群简介,管理员为可选

//		User user = userService.getFromUserByUsername(username);
//		Group record = groupService.getGroupByGroup_id(groupId);
//		if (ObjectUtils.isNull(record) || ObjectUtils.isNull(user)) { // 如果不存在则报错
//			logger.error("404, user or group not found");
//			throw new CloudServerNotFoundException(ConstantBean.USER_OR_GROUP_NOTFOUND);
//		}

//		User_group usergroup = userGroupService.getGroupByUserNameAndGroupId(username, groupId);
//		if (ObjectUtils.isNull(usergroup)) {
//			logger.error("404, user_group not found");
//			throw new CloudServerNotFoundException(ConstantBean.USER_GROUP_NOTFOUND);
//		}

//		if(groupname == null && groupdesc == null && groupphoto == null) {
//			reMap.setStatus(404);
//			reMap.setMsg("not all is empty!");
//			return reMap;
//		}
		ReMap reMap = new ReMap();
		String groupphotoUrl = null;
		
		// 如果同时传入文件名groupphoto和原文件groupphotoFile,则上传并获取文件路径,否则路径为空
		if (ObjectUtils.isNotNull(groupphoto) && ObjectUtils.isNotNull(groupphotoFile)) {
			// 如果文件名不是以图片格式命名则报错
			if (!groupphoto.endsWith(".jpg") && !groupphoto.endsWith(".png") && !groupphoto.endsWith(".gif")) {
				reMap.setStatus(404);
				reMap.setMsg("upload error, file must endswith jpg or png or gif");
				return reMap;
			}
			groupphotoUrl = FileUtil.fileToUrl(request, groupphotoFile, groupphoto, groupId, "groupphoto");
		}

		// 判断用户是否是成员
		User_group user_group = userGroupService.getUserGroupByUserNameAndGroupId(username, groupId);
		if (ObjectUtils.isNull(user_group)) {
			logger.error("404, user is not member");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_MEMBER);
		}
		// 判断该成员是否是管理员
		if (user_group.getIsadmin() == true) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println(user_group);

			Group group = new Group();
			group.setId(user_group.getGroupid());
			group.setGroupId(groupId);
			group.setGroupname(groupname);
			group.setGroupdesc(groupdesc);
			group.setGroupphoto(groupphotoUrl);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println(group);
			int rows = groupService.updateByIdSelective(group);
			System.out.println("rows：" + rows);
			if (rows == 0) {
				logger.error("402, database error!");
				throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
			}

			reMap.setMsg("repair successfully!");
			return reMap;
		} else {
			logger.error("401, user is not admin");
			throw new CloudServerPermissionDenyException(ConstantBean.OPERATION_NOT_ADMIN);
		}
	}

	/**
	 * 删除群组/全部人退出群组 验证用户是否是群组的管理员，而后删除用户群组关系表该groupId的所有记录
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "/deleteGroup", method = RequestMethod.GET)
	public ReMap deleteGroup(@RequestParam("groupId") String groupId, @RequestParam("username") String username) {

		ReMap reMap = new ReMap();
//		Group record = groupService.getGroupByGroup_id(groupId);
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//		System.out.println(record);
//		if (ObjectUtils.isNull(record)) { // 如果不存在则报错
//			logger.error("404, group not found");
//			throw new CloudServerNotFoundException(ConstantBean.GROUP_NOTFOUND);
//		}

		// 判断用户是否是成员
		User_group user_group = userGroupService.getUserGroupByUserNameAndGroupId(username, groupId);
		if (ObjectUtils.isNull(user_group)) {
			logger.error("404, user is not member");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_NOT_MEMBER);
		}
		// 判断该成员是否是管理员
		if (user_group.getIsadmin() == true) {
			int rows = groupService.deleteGroupById(user_group.getGroupid());
			if (rows == 0) {
				logger.error("402, database error!");
				throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
			}

			reMap.setMsg("delete successfully!");
			return reMap;
		} else {
			logger.error("401, user is not admin");
			throw new CloudServerPermissionDenyException(ConstantBean.OPERATION_NOT_ADMIN);
		}
	}

}