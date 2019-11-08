package com.im.entity;

import java.util.List;

public class GroupInfo {

	private String groupId;

	private String groupname;

	private String groupdesc;

	private Integer persontotal;

	private String groupphoto;

	private String createtime;

	private UserIf adminif;

	private List<UserIf> useriflist;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGroupdesc() {
		return groupdesc;
	}

	public void setGroupdesc(String groupdesc) {
		this.groupdesc = groupdesc;
	}

	public Integer getPersontotal() {
		return persontotal;
	}

	public void setPersontotal(Integer persontotal) {
		this.persontotal = persontotal;
	}

	public String getGroupphoto() {
		return groupphoto;
	}

	public void setGroupphoto(String groupphoto) {
		this.groupphoto = groupphoto;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public UserIf getAdminif() {
		return adminif;
	}

	public void setAdminif(UserIf adminif) {
		this.adminif = adminif;
	}

	public List<UserIf> getUseriflist() {
		return useriflist;
	}

	public void setUseriflist(List<UserIf> useriflist) {
		this.useriflist = useriflist;
	}

}
