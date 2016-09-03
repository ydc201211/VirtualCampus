package com.vc.entity;

import java.io.Serializable;

public class Like implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int like_id;
	private int like_userId;
	private int like_activityId;
	private String like_time;
	public int getLike_id() {
		return like_id;
	}
	public void setLike_id(int like_id) {
		this.like_id = like_id;
	}
	
	public String getLike_time() {
		return like_time;
	}
	public void setLike_time(String like_time) {
		this.like_time = like_time;
	}
	public int getLike_userId() {
		return like_userId;
	}
	public void setLike_userId(int like_userId) {
		this.like_userId = like_userId;
	}
	public int getLike_activityId() {
		return like_activityId;
	}
	public void setLike_activityId(int like_activityId) {
		this.like_activityId = like_activityId;
	}
}
