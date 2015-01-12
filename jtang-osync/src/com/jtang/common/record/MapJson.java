package com.jtang.common.record;

import org.json.JSONException;
import org.json.JSONObject;

public class MapJson {

	int id;
	String mapJson;
	//0表示停止状态，1表示正在运行状态。
	int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMapJson() {
		return mapJson;
	}
	public void setMapJson(String mapJson) {
		this.mapJson = mapJson;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String toString(){
		return new JSONObject(this).toString();
	}
	
	public MapJson(){}
	public MapJson(String json){
		try {
			JSONObject obj = new JSONObject(json);
			this.setId(obj.getInt("id"));
			this.setMapJson(obj.getString("mapJson"));
			this.setStatus(obj.getInt("status"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
}
