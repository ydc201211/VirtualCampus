package com.vc.entity;

import java.io.Serializable;

/**
 * Created by ydc on 2016/3/25.
 */
public class Action implements Serializable{

    private static final long serialVersionUID = 1L;

    private int action_id;
    private String action_theme;
    private String action_host;
    private String action_time;
    private String action_site;
    private String action_userName;
    private String action_picPath;
    private String action_ip;
    private String action_postTime;
    private String action_detail;
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
	public String getAction_userName() {
		return action_userName;
	}
	public void setAction_userName(String action_userName) {
		this.action_userName = action_userName;
	}
	public String getAction_picPath() {
		return action_picPath;
	}
	public void setAction_picPath(String action_picPath) {
		this.action_picPath = action_picPath;
	}
	public String getAction_ip() {
		return action_ip;
	}
	public void setAction_ip(String action_ip) {
		this.action_ip = action_ip;
	}
	public String getAction_postTime() {
		return action_postTime;
	}
	public void setAction_postTime(String action_postTime) {
		this.action_postTime = action_postTime;
	}
	public String getAction_detail() {
		return action_detail;
	}
	public void setAction_detail(String action_detail) {
		this.action_detail = action_detail;
	}
   
}
