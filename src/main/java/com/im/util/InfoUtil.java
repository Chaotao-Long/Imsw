package com.im.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import com.im.dbmodel.Group;
import com.im.dbmodel.User;
import com.im.entity.GroupInfo;
import com.im.entity.ReMap;
import com.im.entity.UserIf;
import com.im.entity.UserInfo;

public class InfoUtil {

	public static void UsertransInfo(User user, UserInfo userInfo) { // 隐藏用户敏感信息

		userInfo.setUsername(user.getUsername());
		userInfo.setPassword("*****");
		userInfo.setNickname(user.getNickname());
		userInfo.setPhoto(user.getPhoto());
		userInfo.setSex(user.getSex());
		userInfo.setAge(user.getAge());
		userInfo.setPhone(user.getPhone());
		userInfo.setEmail(user.getEmail());
		userInfo.setDes(user.getDes());
		userInfo.setAddress(user.getAddress());
		userInfo.setStatus(user.getStatus());
		if (user.getLogintime() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(user.getLogintime());
			userInfo.setLogintime(time);
		}
	}

	// 增加type字段通过传入的参数来判断是需要隐藏
	public static void UsertransIf(User user, UserIf userIf, String type) { // 加载用户部分信息或查看非好友的信息

		userIf.setNickname(user.getNickname());
		userIf.setPhotoUrl(user.getPhoto());
		userIf.setUsername(user.getUsername());
		// 符合隐藏条件
		if (type == null) {
			userIf.setStatus(-1);
			userIf.setLogintime("*****");
			if (user.getDes() != null) {
				userIf.setDes(hideString(user.getDes()));
			}
		} else { // 否则无须隐藏
			userIf.setDes(user.getDes());
			userIf.setStatus(user.getStatus());
			if (user.getLogintime() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = sdf.format(user.getLogintime());
				userIf.setLogintime(time);
			}
		}
	}

	// 增加type字段通过传入的参数来判断是需要隐藏
	public static void GrouptransInfo(Group group, GroupInfo groupInfo, String type) { // 查看群组全部信息

		groupInfo.setGroupname(group.getGroupname());
		groupInfo.setPersontotal(group.getPersontotal());
		groupInfo.setGroupphoto(group.getGroupphoto());
		// 符合隐藏条件
		if (type == null) {
			groupInfo.setGroupId("*****");
			if (group.getGroupdesc() != null) {
				groupInfo.setGroupdesc(hideString(group.getGroupdesc()));
			}
			groupInfo.setCreatetime("*****");
		} else { // 无须隐藏
			groupInfo.setGroupdesc(group.getGroupdesc());
			groupInfo.setGroupId(group.getGroupId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(group.getCreatetime());
			groupInfo.setCreatetime(time);
		}
	}

	/**
	 * @param userinfo return user
	 */
	public static void infotransUser(User user, UserInfo userInfo) {

		user.setUsername(userInfo.getUsername());
		user.setPassword(userInfo.getPassword());
		user.setNickname(userInfo.getNickname());
		user.setSex(userInfo.getSex());
		user.setAge(userInfo.getAge());
		user.setPhone(userInfo.getPhone());
		user.setEmail(userInfo.getEmail());
		user.setDes(userInfo.getDes());
		user.setAddress(userInfo.getAddress());
	}

//	public static void GrouptransInfoList(Group group, List<GroupInfo> groupInfoList) { // 加载群组部分信息
//
//		GroupInfo groupInfo = new GroupInfo();
//		groupInfo.setGroupId("****");
//		groupInfo.setGroupname(group.getGroupname());
//		groupInfo.setGroupdesc(hideString(group.getGroupdesc()));
//		groupInfo.setPersontotal(group.getPersontotal());
//		groupInfo.setGroupphoto(group.getGroupphoto());
//		groupInfo.setCreatetime("****");
//		groupInfoList.add(groupInfo);
//
//	}

	/**
	 * 隐藏部分字符串
	 * 
	 * @param str 原始字符串
	 * @return 隐藏后字符串
	 */
	public static String hideString(String str) {
		// 获取字符串的长度
		int len = str.length();

		// 根据长度实现不同的隐藏策略
		if (len >= 1 && len < 3) {
			// 只出现一个字
			str = str.substring(0, 1) + "****";
		} else if (len >= 3) {
			str = str.substring(0, 2) + "***";
		} else {
			str = "******";
		}

		return str;
	}

	public static void returnMap(HttpServletResponse response, PrintWriter out, ReMap reMap) throws IOException {

		String jsonStr = JsonUtil.transformToJson(reMap);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		out = response.getWriter();
		out.append(jsonStr);
	}

}
