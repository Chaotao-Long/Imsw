package com.im.dbmodel;

import java.util.Date;

public class Relation {
    private Integer id;

    private Integer userid;

    private Integer friendid;

    private Integer isagree;

    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getFriendid() {
        return friendid;
    }

    public void setFriendid(Integer friendid) {
        this.friendid = friendid;
    }

    public Integer getIsagree() {
        return isagree;
    }

    public void setIsagree(Integer isagree) {
        this.isagree = isagree;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}