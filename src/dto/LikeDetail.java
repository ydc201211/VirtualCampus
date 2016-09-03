package dto;

import java.io.Serializable;

public class LikeDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int like_id;
	private int like_userId;
	private int like_activityId;
	private String like_time;
	private String like_user_nickName;
	private String like_user_picPath;
	
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
	public String getLike_user_nickName() {
		return like_user_nickName;
	}
	public void setLike_user_nickName(String like_user_nickName) {
		this.like_user_nickName = like_user_nickName;
	}
	public String getLike_user_picPath() {
		return like_user_picPath;
	}
	public void setLike_user_picPath(String like_user_picPath) {
		this.like_user_picPath = like_user_picPath;
	}
}
