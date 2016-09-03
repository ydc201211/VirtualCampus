package com.vc.entity;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private String userName;
	private String password;
	private String nickName;
	private String user_picPath;
	private	String user_sex;
	private String user_address;
	private String user_college;
	private	String user_birthday;
	private String user_entrance;
	private String user_hobby;
	private String user_autograph;
	private String user_loginTime;
	private String user_loginIp;
	
	private String token;
	
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	public String getUser_college() {
		return user_college;
	}
	public void setUser_college(String user_college) {
		this.user_college = user_college;
	}
	public String getUser_birthday() {
		return user_birthday;
	}
	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}
	public String getUser_entrance() {
		return user_entrance;
	}
	public void setUser_entrance(String user_entrance) {
		this.user_entrance = user_entrance;
	}
	public String getUser_hobby() {
		return user_hobby;
	}
	public void setUser_hobby(String user_hobby) {
		this.user_hobby = user_hobby;
	}
	public String getUser_autograph() {
		return user_autograph;
	}
	public void setUser_autograph(String user_autograph) {
		this.user_autograph = user_autograph;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUser_picPath() {
		return user_picPath;
	}
	public void setUser_picPath(String user_picPath) {
		this.user_picPath = user_picPath;
	}
	public String getUser_loginTime() {
		return user_loginTime;
	}
	public void setUser_loginTime(String user_loginTime) {
		this.user_loginTime = user_loginTime;
	}
	public String getUser_loginIp() {
		return user_loginIp;
	}
	public void setUser_loginIp(String user_loginIp) {
		this.user_loginIp = user_loginIp;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
