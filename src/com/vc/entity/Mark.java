package com.vc.entity;

import java.io.Serializable;

public class Mark implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int markId;  //标记ID
	private String markName;//标记名称
	private double mark_latitude;//标价纬度
	private double mark_longitude;
	private String mark_detail;
	private int mark_like;
	private int mark_parentId;
	private int mark_isOnMap;
	
	
	public int getMarkId() {
		return markId;
	}
	public void setMarkId(int markId) {
		this.markId = markId;
	}
	public String getMarkName() {
		return markName;
	}
	public void setMarkName(String markName) {
		this.markName = markName;
	}
	public double getMark_latitude() {
		return mark_latitude;
	}
	public void setMark_latitude(double mark_latitude) {
		this.mark_latitude = mark_latitude;
	}
	public double getMark_longitude() {
		return mark_longitude;
	}
	public void setMark_longitude(double mark_longitude) {
		this.mark_longitude = mark_longitude;
	}
	public String getMark_detail() {
		return mark_detail;
	}
	public void setMark_detail(String mark_detail) {
		this.mark_detail = mark_detail;
	}
	public int getMark_like() {
		return mark_like;
	}
	public void setMark_like(int mark_like) {
		this.mark_like = mark_like;
	}
	public int getMark_parentId() {
		return mark_parentId;
	}
	public void setMark_parentId(int mark_parentId) {
		this.mark_parentId = mark_parentId;
	}
	public int getMark_isOnMap() {
		return mark_isOnMap;
	}
	public void setMark_isOnMap(int mark_isOnMap) {
		this.mark_isOnMap = mark_isOnMap;
	}

	
	
}
