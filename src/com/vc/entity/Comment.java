package com.vc.entity;

import java.io.Serializable;

public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int comment_id;
	private String comment_content;
	private String comment_time;
	private int comment_userId;
	private String comment_ip;
	private int comment_activityId;
	
	public int getComment_activityId() {
		return comment_activityId;
	}
	public void setComment_activityId(int comment_activityId) {
		this.comment_activityId = comment_activityId;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}
	public int getComment_userId() {
		return comment_userId;
	}
	public void setComment_userId(int comment_userId) {
		this.comment_userId = comment_userId;
	}
	public String getComment_ip() {
		return comment_ip;
	}
	public void setComment_ip(String comment_ip) {
		this.comment_ip = comment_ip;
	}
}
