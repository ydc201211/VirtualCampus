package dto;

import java.io.Serializable;

public class AttendanceDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int attendance_id;
	private int attendance_userId;
	private String attendance_time;
	private int attendance_activityId;
	private String attendance_user_nickName;
	private String attendacne_user_picPath;

	public int getAttendance_activityId() {
		return attendance_activityId;
	}

	public void setAttendance_activityId(int attendance_activityId) {
		this.attendance_activityId = attendance_activityId;
	}

	public String getAttendance_time() {
		return attendance_time;
	}

	public void setAttendance_time(String attendance_time) {
		this.attendance_time = attendance_time;
	}

	public int getAttendance_id() {
		return attendance_id;
	}

	public void setAttendance_id(int attendance_id) {
		this.attendance_id = attendance_id;
	}

	public int getAttendance_userId() {
		return attendance_userId;
	}

	public void setAttendance_userId(int attendance_userId) {
		this.attendance_userId = attendance_userId;
	}

	public int getActivity_id() {
		return attendance_activityId;
	}

	public void setActivity_id(int activity_id) {
		this.attendance_activityId = activity_id;
	}

	public String getAttendance_user_nickName() {
		return attendance_user_nickName;
	}

	public void setAttendance_user_nickName(String attendance_user_nickName) {
		this.attendance_user_nickName = attendance_user_nickName;
	}

	public String getAttendacne_user_picPath() {
		return attendacne_user_picPath;
	}

	public void setAttendacne_user_picPath(String attendacne_user_picPath) {
		this.attendacne_user_picPath = attendacne_user_picPath;
	}
}
