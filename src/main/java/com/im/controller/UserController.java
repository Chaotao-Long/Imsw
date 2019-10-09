package com.im.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.New;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.im.dbmodel.User;
import com.im.entity.ReMap;
import com.im.entity.ResultMap;
import com.im.entity.UserInfo;
import com.im.service.UserService;
import com.im.util.ObjectUtils;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// 用户注册
	@RequestMapping(value = "/toRegister")
	public String toRegister() {
		return "register";
	}

	// 提交注册按钮
	@RequestMapping("/submit")
	@ResponseBody
	public String register(@RequestBody User user) {

		user.setRegistertime(new Date());
		System.out.println(user.toString());
		int rows = userService.addEntitytoUser1(user);
		System.out.println(rows);
		if (rows > 0) {
			return "200";
		} else {
			return "402";
		}
	}

	// 用户登录控制
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session, Model model) {

		User user = userService.getUserByUsernameAndPassword(username, password);

		if (!ObjectUtils.isNull(user)) {

			session.setAttribute("USER_SESSION", user);
			return "redirect:/index/init";
		}
		model.addAttribute("msg", "登录账户或密码错误，请重新输入！");
		return "login";
	}

//	退出登录控制
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String tologin() {
		return "login";
	}

	// 修改密码
	@RequestMapping("/repair")
	@ResponseBody
	public Map<String, Object> repairPassword(@RequestParam("id") Integer id,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			HttpSession session) {

		System.out.println("oldPassword：" + oldPassword);
		System.out.println("newPassword：" + newPassword);

		Map<String, Object> map = new HashMap<String, Object>();
		// User user = (User) session.getAttribute("USER_SESSION");
		User user = userService.getFromUserById(id);
		if (oldPassword.equals(user.getPassword())) {
			userService.updatePasswordById(user.getId(), newPassword);
			System.out.println("修改成功");
			map.put("status", "200");
			map.put("msg", "request successfully!");
			return map;
		} else {
			map.put("status", "402");
			map.put("msg", "oldPassword is not right!");
			return map;
		}

	}

	// 查询自身信息
	@RequestMapping("/selectUserInfo")
	@ResponseBody
	public ResultMap selectUserInfo(@RequestParam("userId") Integer userId) {

		ResultMap resultMap = new ResultMap();
		User user = userService.getFromUserById(userId);
		if (ObjectUtils.isNotNull(user)) {
			UserInfo userInfo = new UserInfo(user.getUsername(), user.getNickname(), user.getPhoto(), user.getSex(),
					user.getAge(), user.getPhone(), user.getEmail(), user.getDes());
			resultMap.setUserInfo(userInfo);
			return resultMap;
		}
		resultMap.setMsg("402");
		resultMap.setMsg("userId is not found!");
		return resultMap;
	}

	// 修改用户信息
	@RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public ReMap changeUserInfo(@RequestBody User user) {
		System.out.println("1111111111111111111111111111111111111111");
		System.out.println("user："+user);
		ReMap reMap = new ReMap();
		User oldUser = userService.getFromUserById(user.getId());
		if (ObjectUtils.isNotNull(oldUser)) {
			userService.updateUserInfo(user);
			return reMap;
		}
		reMap.setStatus("402");
		reMap.setMsg("user is not found!");
		return reMap;
	}

	// 查询好友信息
	@RequestMapping("/selectFriendInfo")
	@ResponseBody
	public ResultMap selectFriendInfo(@RequestParam("userId") Integer userId,
			@RequestParam("friendId") Integer friendId) {

		ResultMap resultMap = new ResultMap();

		return resultMap;
	}

//	测试拦截器
//	@RequestMapping("/customer.action")
//	public String main() {
//		return "customer";
//	}

//	测试/login.action
//	@RequestMapping("/login1.action")
//	public String tologin(Integer id) {
//		if (id == 1) {
//			return "customer";
//		}
//		return "login";
//	}
}
