package com.jtang.syn.comm;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ReturnMsg implements Serializable {

	public long recordId;
	public long receiveTime;
	//0表示没有成功，1表示成功删除，2表示成功更新，3表示成功插入
	public int status;
	
	
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	
	public long getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public ReturnMsg(){}
	public ReturnMsg(String json){
		try {
			JSONObject obj = new JSONObject(json);
			this.setReceiveTime(obj.getLong("receiveTime"));
			this.setRecordId(obj.getLong("recordId"));
			this.setStatus(obj.getInt("status"));
		} catch (JSONException e) {
			e.printStackTrace();
			this.recordId=-1;
		}
	}
	
	public String toString(){
		return new JSONObject(this).toString();
	}
	public static void main(String args[]){
		
		ReturnMsg rm = new ReturnMsg();
		rm.setReceiveTime(new Date().getTime());
		rm.setRecordId(2);
		rm.setStatus(0);
		String rmJson = new JSONObject(rm).toString();
		System.out.println(new JSONObject(rm).toString());
	    ReturnMsg rm2 = new ReturnMsg(rmJson);
		System.out.println(rm2.toString());
		
	}
	
	
	
}
