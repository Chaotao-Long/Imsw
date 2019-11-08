package com.im.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.im.dbmodel.User;
import com.im.entity.UserInfo;
import com.im.service.UserService;

public class FriendControllerUtil {

	// 根据用户id获取用户的具体信息（隐藏敏感信息）
	public static boolean appendInfoList(UserService userService, List<Integer> idList, List<UserInfo> infoList) {

		for (Integer id : idList) {
			// 根据friendid获取friend的用户所有信息
			User allInfo = userService.getFromUserById(id);
			System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("allInfo：" + allInfo);
			if (allInfo == null) {
				return false;
			}

			// 隐藏部分信息
			UserInfo Info = new UserInfo();
			Info.setUsername(allInfo.getUsername());
			Info.setNickname(allInfo.getNickname());
			Info.setPhoto(allInfo.getPhoto());
			Info.setSex(allInfo.getSex());
			Info.setAge(allInfo.getAge());
			Info.setPhone(allInfo.getPhone());
			Info.setEmail(allInfo.getEmail());
			Info.setDes(allInfo.getDes());
			Info.setStatus(allInfo.getStatus());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(allInfo.getLogintime());
			Info.setLogintime(time);

			infoList.add(Info);
		}

		return true;
	}

	public static List<UserInfo> addInfoList(List<User> userList) {

		List<UserInfo> userinfoList = new ArrayList<UserInfo>();

		for (User user : userList) {
			if (user == null) {
				return null;
			}
			// 隐藏部分信息
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(user.getUsername());
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
			userinfoList.add(userInfo);
		}
		return userinfoList;
	}

}
