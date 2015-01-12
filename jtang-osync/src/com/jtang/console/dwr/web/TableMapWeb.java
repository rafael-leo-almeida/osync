package com.jtang.console.dwr.web;

import org.json.JSONException;

import org.json.JSONObject;

public class TableMapWeb {

	int mapId;
    String diZhis;
    String diZhid;
    long total;
    long wrong;
    //表示状态，0表示停止，1表示运行
    int status;
    
	
	public String getDiZhis() {
		return diZhis;
	}
	public void setDiZhis(String diZhis) {
		this.diZhis = diZhis;
	}
	public String getDiZhid() {
		return diZhid;
	}
	public void setDiZhid(String diZhid) {
		this.diZhid = diZhid;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getWrong() {
		return wrong;
	}
	public void setWrong(long wrong) {
		this.wrong = wrong;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public String toString(){
		return new JSONObject(this).toString();
	}
	public TableMapWeb(){}
	public TableMapWeb(String json){
		
		try {
			JSONObject obj = new JSONObject(json);
			this.diZhis = obj.getString("diZhis");
			this.status = obj.getInt("status");
			this.diZhid = obj.getString("diZhid");
			this.total = obj.getLong("total");
			this.wrong = obj.getLong("wrong");
			this.mapId = obj.getInt("mapId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		
		TableMapWeb web = new TableMapWeb();
		web.setDiZhis("sfewfewf");
		web.setDiZhid("nimeisni");
		web.setMapId(23);
		web.setStatus(1);
		web.setTotal(23);
		web.setWrong(21);
		System.out.println(web.toString());
		
		
	}
	
	
}
