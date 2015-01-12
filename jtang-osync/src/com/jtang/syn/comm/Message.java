package com.jtang.syn.comm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jtang.common.table.SourceInfo;



public class Message implements Serializable {
    
	long recordId;
	SourceInfo sourceInfo;
	String operation;
	Map<String,Object> datas;
	String deleteSqlWhere;
	long monitorTime;
	
	public Message(){
		super();
	}
	public Message(SourceInfo sourceInfo, String operation,Map<String,Object> datas){
		
		this.sourceInfo=sourceInfo;
		this.operation=operation;
		this.datas=datas;
		
	}
	public Message(String json) throws JSONException{
	     JSONObject obj = new JSONObject(json);
	     String operation = obj.getString("operation");
	     Map<String,Object>datas = new HashMap<String, Object>();
	     SourceInfo sourceInfo = new SourceInfo();    
	     JSONObject objDatas = new JSONObject(obj.get("datas").toString());
	     JSONObject objSource = new JSONObject(obj.get("sourceInfo").toString());
	     Iterator<String> iterator = objDatas.keys();
	     while (iterator.hasNext()) {
			String key = (String) iterator.next();
			datas.put(key, objDatas.getString(key));
		 }
	     sourceInfo.setIp(objSource.getString("ip"));
	     sourceInfo.setPort(objSource.getInt("port"));
	     sourceInfo.setTableName(objSource.getString("tableName"));
	     sourceInfo.setDBName(objSource.getString("DBName"));
	     sourceInfo.setUserName(objSource.getString("userName"));
	     this.sourceInfo=sourceInfo;
		 this.operation=operation;
		 this.datas=datas;
		 this.recordId = obj.getLong("recordId");
			
	}
	public SourceInfo getSourceInfo() {
		return sourceInfo;
	}
	public void setSourceInfo(SourceInfo sourceInfo) {
		this.sourceInfo = sourceInfo;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Map<String, Object> getDatas() {
		return datas;
	}
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}
	
	public String getDeleteSqlWhere() {
		return deleteSqlWhere;
	}
	public void setDeleteSqlWhere(String deleteSqlWhere) {
		this.deleteSqlWhere = deleteSqlWhere;
	}
		
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	
	public long getMonitorTime() {
		return monitorTime;
	}
	public void setMonitorTime(long monitorTime) {
		this.monitorTime = monitorTime;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new JSONObject(this).toString();
	}
	
	
    
}
