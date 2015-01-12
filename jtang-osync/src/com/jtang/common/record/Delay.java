package com.jtang.common.record;

import java.util.Date;

public class Delay {

	long id;
	long recordId;
	long synDelay;
	long netDelay;
	Date dateTime;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	public long getSynDelay() {
		return synDelay;
	}
	public void setSynDelay(long synDelay) {
		this.synDelay = synDelay;
	}
	public long getNetDelay() {
		return netDelay;
	}
	public void setNetDelay(long netDelay) {
		this.netDelay = netDelay;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
