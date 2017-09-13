package com.ssc.entity.system;

import java.util.Date;
public class User{
	private String user_id;
	private String phone;
	private String email;
	private String nickname;
	private String name;
	private String unit;
	private int type;
	private int state;
	private Date join_time;
	private Date update_time;
	private Date readnews_time;
	private int has_msg;
	private String verification_code;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getJoin_time() {
		return join_time;
	}
	public void setJoin_time(Date joinTime) {
		join_time = joinTime;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date updateTime) {
		update_time = updateTime;
	}
	public Date getReadnews_time() {
		return readnews_time;
	}
	public void setReadnews_time(Date readnewsTime) {
		readnews_time = readnewsTime;
	}
	public int getHas_msg() {
		return has_msg;
	}
	public void setHas_msg(int hasMsg) {
		has_msg = hasMsg;
	}
	public String getVerification_code() {
		return verification_code;
	}
	public void setVerification_code(String verificationCode) {
		verification_code = verificationCode;
	}
}
