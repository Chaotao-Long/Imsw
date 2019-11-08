package com.im.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.im.dbmodel.User;
import com.im.entity.ConstantBean;
import com.im.entity.ReMap;
import com.im.entity.ResultMap;
import com.im.entity.UserInfo;
import com.im.exception.CloudServerDatabaseException;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.im.log4j.Logger;
import com.im.service.UserService;
import com.im.util.FileUtil;
import com.im.util.InfoUtil;
import com.im.util.JwtUtil;
import com.im.util.ObjectUtils;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	public static Logger logger = Logger.getLogger(UserController.class);

	// 提交注册按钮
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public ReMap register(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "photo", required = false) String photo,
			@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
			@RequestParam(value = "sex", required = false) Boolean sex,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "des", required = false) String des,
			@RequestParam(value = "address", required = false) String address) { // 新增address字段

		User existUser = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNotNull(existUser)) {
			logger.error("401, already username!");
			throw new CloudServerPermissionDenyException(ConstantBean.ALREADY_USERNAME);
		}

		ReMap reMap = new ReMap();
		String photoUrl = null;

		if (ObjectUtils.isNotNull(photo) && ObjectUtils.isNotNull(photoFile)) {
			// 如果文件名不是以图片格式命名则报错
			if (!photo.endsWith(".jpg") && !photo.endsWith(".png") && !photo.endsWith(".gif")) {
				reMap.setStatus(404);
				reMap.setMsg("upload error, file must endswith jpg or png or gif");
				return reMap;
			}
			photoUrl = FileUtil.fileToUrl(request, photoFile, photo, username, "userphoto");

		}

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setNickname(nickname);
		user.setSex(sex);
		user.setDes(des);
		user.setEmail(email);
		user.setAge(age);
		user.setPhone(phone);
		user.setAddress(address);
		user.setPhoto(photoUrl);
		user.setRegistertime(new Date());
		user.setStatus(0);

		int rows = userService.addEntitytoUser1(user); // 数据库异常,插入失败
		if (rows > 0) {
			reMap.setMsg("register successfully!");
			return reMap;
		} else {
			logger.error("402, database error!");
			throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
		}
	}

	// 用户登录控制
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestParam("username") String username,
			@RequestParam("password") String password) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNull(user)) {
			logger.error("404, user not found!");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND); // 密码错误,权限异常
		}
		if (!user.getPassword().equals(password)) {
			logger.error("401, password error!");
			throw new CloudServerNotFoundException(ConstantBean.OPERATION_TRANSFER_PASSWD); // 密码错误,权限异常
		}

		userService.updateStatusByUsername(username, 1);
		userService.updateLogintimeByUsername(username, new Date());
		String sign = JwtUtil.sign(username);
		long expire = System.currentTimeMillis() + JwtUtil.maxTime;
		String token = username + "/" + sign + "/" + expire;
		System.out.println("登录返回的token值" + token);

		resultMap.put("status", 200);
		resultMap.put("msg", "login successfully!");
		resultMap.put("token", token);
		return resultMap;

	}

	// 查询自身信息
	@RequestMapping(value = "/selectUserInfo", method = RequestMethod.GET)
	public ResultMap selectUserInfo(HttpServletRequest request, @RequestParam("username") String username) {

		ResultMap resultMap = new ResultMap();
		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNull(user)) {
			logger.error("404, user not found!");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}
		UserInfo userInfo = new UserInfo();
		InfoUtil.UsertransInfo(user, userInfo);
		resultMap.setUserInfo(userInfo);
		return resultMap;

	}

	// 修改密码
	@RequestMapping(value = "/repair", method = RequestMethod.POST)
	public ReMap repairPassword(@RequestParam("username") String username,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
			HttpSession session) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNotNull(user)) {
			if (oldPassword.equals(user.getPassword())) {

				userService.updatePasswordByUsername(user.getUsername(), newPassword);
				return reMap;
			} else {
				logger.error("402, oldPassword error!");
				throw new CloudServerNotFoundException(ConstantBean.OLD_PASSWORD);
			}
		} else {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

	}

	// 修改用户信息
	@RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST)
	public ReMap changeUserInfo(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "photo", required = false) String photo,
			@RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
			@RequestParam(value = "sex", required = false) Boolean sex,
			@RequestParam(value = "age", required = false) Integer age,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "des", required = false) String des,
			@RequestParam(value = "address", required = false) String address) { // 新增address字段

		ReMap reMap = new ReMap();
		User user = new User();
		// User oldUser = userService.getFromUserByUsername(userInfo.getUsername());
		User oldUser = userService.getFromUserByUsername(username);

		if (ObjectUtils.isNull(oldUser)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}
		
		String photoUrl = null;
		if (ObjectUtils.isNotNull(photo) && ObjectUtils.isNotNull(photoFile)) {
			// 如果文件名不是以图片格式命名则报错
			if (!photo.endsWith(".jpg") && !photo.endsWith(".png") && !photo.endsWith(".gif")) {
				reMap.setStatus(404);
				reMap.setMsg("upload error, file must endswith jpg or png or gif");
				return reMap;
			}
			photoUrl = FileUtil.fileToUrl(request, photoFile, photo, username, "userphoto");

		}
		
			user.setNickname(nickname);
			user.setSex(sex);
			user.setDes(des);
			user.setEmail(email);
			user.setAge(age);
			user.setPhone(phone);
			user.setAddress(address);
			user.setPhoto(photoUrl);
			 //InfoUtil.infotransUser(user, userInfo); // userinfo转为user
			user.setId(oldUser.getId());

			int row = userService.updateUserInfo(user); // 用了生成器默认的方法(根据id进行选择性地更新)，可以自定义改为根据username来更新
			if (row > 0) {
				reMap.setMsg("alter successfully!");
				return reMap;
			} else { // 数据库异常
				logger.error("402, database error!");
				throw new CloudServerDatabaseException(ConstantBean.OPERATION_DATABASE);
			}
		
	

	}

	// 查看以往头像列表
	@RequestMapping(value = "/getUserPhoto", method = RequestMethod.GET)
	public Map<String, Object> userPhoto(@RequestParam("username") String username, HttpServletRequest request) {

		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNull(user)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<String> fileList = FileUtil.selectFileList(request, username, "userphoto");
		System.out.println("fileList：" + fileList);

		resultMap.put("status", 200);
		resultMap.put("msg", "request successfully!");
		resultMap.put("fileList", fileList);
		return resultMap;

	}

	// 退出
	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public ReMap exitSystem(@RequestParam("username") String username) {

		ReMap reMap = new ReMap();
		User user = userService.getFromUserByUsername(username);
		if (ObjectUtils.isNull(user)) {
			logger.error("404, user not found");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND);
		}

		userService.updateStatusByUsername(username, 0);
		return reMap;
	}

}
