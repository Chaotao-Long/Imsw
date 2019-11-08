package com.im.dbmodel;

import java.util.Date;

public class Group {
	private Integer id;

	private String groupId;

	private String groupname;

	private String groupdesc;

	private Integer persontotal;

	private String groupphoto;

	private Date createtime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId == null ? null : groupId.trim();
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname == null ? null : groupname.trim();
	}

	public String getGroupdesc() {
		return groupdesc;
	}

	public void setGroupdesc(String groupdesc) {
		this.groupdesc = groupdesc == null ? null : groupdesc.trim();
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
		this.groupphoto = groupphoto == null ? null : groupphoto.trim();
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", groupId=" + groupId + ", groupname=" + groupname + ", groupdesc=" + groupdesc
				+ ", persontotal=" + persontotal + ", groupphoto=" + groupphoto + ", createtime=" + createtime + "]";
	}

}