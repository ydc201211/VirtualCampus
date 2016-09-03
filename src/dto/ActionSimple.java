package dto;

import java.io.Serializable;

public class ActionSimple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int action_id;
	private String action_theme;
	private String action_host;
	private String action_time;
	private String action_site;
	private String action_picPath;
	private String action_detail;

	private int attendance_nums;
	private int like_nums;

	public int getAction_id() {
		return action_id;
	}

	public void setAction_id(int action_id) {
		this.action_id = action_id;
	}

	public String getAction_theme() {
		return action_theme;
	}

	public void setAction_theme(String action_theme) {
		this.action_theme = action_theme;
	}

	public String getAction_host() {
		return action_host;
	}

	public void setAction_host(String action_host) {
		this.action_host = action_host;
	}

	public String getAction_time() {
		return action_time;
	}

	public void setAction_time(String action_time) {
		this.action_time = action_time;
	}

	public String getAction_site() {
		return action_site;
	}

	public void setAction_site(String action_site) {
		this.action_site = action_site;
	}

	public String getAction_picPath() {
		return action_picPath;
	}

	public void setAction_picPath(String action_picPath) {
		this.action_picPath = action_picPath;
	}

	public String getAction_detail() {
		return action_detail;
	}

	public void setAction_detail(String action_detail) {
		this.action_detail = action_detail;
	}

	public int getAttendance_nums() {
		return attendance_nums;
	}

	public void setAttendance_nums(int attendance_nums) {
		this.attendance_nums = attendance_nums;
	}

	public int getLike_nums() {
		return like_nums;
	}

	public void setLike_nums(int like_nums) {
		this.like_nums = like_nums;
	}
}
