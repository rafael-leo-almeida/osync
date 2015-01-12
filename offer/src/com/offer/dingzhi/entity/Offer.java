package com.offer.dingzhi.entity;

public class Offer {

	
	private int id;
	private String title;
	private String location;
	//startTime=0,endTime=0表示该字段没有数值
	private long startTime;
	private long endTime;
	private String position;
	private String speaker;
	private String pay;
	private String baseRequire;
	private String process;
	private String des;
	private String tip;
	private int type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	public String getPay() {
		return pay;
	}
	public void setPay(String pay) {
		this.pay = pay;
	}
	public String getBaseRequire() {
		return baseRequire;
	}
	public void setBaseRequire(String baseRequire) {
		this.baseRequire = baseRequire;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
		
}
