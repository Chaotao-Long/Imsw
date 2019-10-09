package com.im.entity;

import java.util.Date;

//用户开发资料
public class UserInfo {

	private String username;

	private String nickname;

	private String photo;

	private Boolean sex;

	private Integer age;

	private String phone;

	private String email;

	private String des;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public UserInfo(String username, String nickname, String photo, Boolean sex, Integer age, String phone,
			String email, String des) {
		this.username = username;
		this.nickname = nickname;
		this.photo = photo;
		this.sex = sex;
		this.age = age;
		this.phone = phone;
		this.email = email;
		this.des = des;
	}

}
